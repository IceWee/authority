package bing.system.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.constant.GlobalConstants;
import bing.constant.StatusEnum;
import bing.domain.GenericPage;
import bing.exception.BusinessException;
import bing.exception.BusinessExceptionCodes;
import bing.system.condition.SysResourceCondition;
import bing.system.dao.SysResourceCategoryDao;
import bing.system.dao.SysResourceDao;
import bing.system.dao.SysRoleDao;
import bing.system.dao.SysRoleResourceDao;
import bing.system.exception.ResourceExceptionCodes;
import bing.system.model.SysResource;
import bing.system.model.SysResourceCategory;
import bing.system.model.SysRole;
import bing.system.model.SysRoleResource;
import bing.system.vo.SysResourceCategoryVO;
import bing.system.vo.SysResourceVO;

@Service("sysResourceService")
public class SysResourceServiceImpl implements SysResourceService {

	@Autowired
	private SysRoleDao sysRoleDao;

	@Autowired
	private SysResourceDao sysResourceDao;

	@Autowired
	private SysRoleResourceDao sysRoleResourceDao;

	@Autowired
	private SysResourceCategoryDao sysResourceCategoryDao;

	@Override
	public GenericPage<SysResourceVO> listByPage(SysResourceCondition condition) {
		Long pageNo = condition.getPageNo();
		PageHelper.startPage(pageNo.intValue(), condition.getPageSize().intValue());
		List<SysResourceVO> list = sysResourceDao.listByCondition(condition);
		PageInfo<SysResourceVO> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNo, pageInfo.getTotal(), list);
	}

	/**
	 * 特别说明： 系统每增加一个资源都需自动为admin授权
	 */
	@Override
	@Transactional
	public void save(SysResource entity) {
		entity.setId(null);
		SysResource persistResource = sysResourceDao.getByURL(entity.getUrl());
		if (persistResource != null) {
			throw new BusinessException(ResourceExceptionCodes.URL_USED);
		}
		Date now = new Date();
		entity.setCreateDate(now);
		entity.setUpdateDate(now);
		// 自动为admin授权
		sysResourceDao.insert(entity);
		Integer resourceId = entity.getId();
		SysRole admin = sysRoleDao.getByCode(GlobalConstants.ADMIN);
		if (admin == null) {
			throw new BusinessException(BusinessExceptionCodes.ROLE_ADMIN_MISSING);
		}
		Integer roleId = admin.getId();
		SysRoleResource sysRoleResource = new SysRoleResource();
		sysRoleResource.setResourceId(resourceId);
		sysRoleResource.setRoleId(roleId);
		sysRoleResource.setCreateDate(now);
		sysRoleResource.setUpdateDate(now);
		sysRoleResource.setCreateUser(GlobalConstants.ADMIN);
		sysRoleResource.setUpdateUser(GlobalConstants.ADMIN);
		sysRoleResourceDao.insert(sysRoleResource);
	}

	@Override
	public void update(SysResource entity) {
		SysResource persistResource = sysResourceDao.selectByPrimaryKey(entity.getId());
		String origionUrl = persistResource.getUrl();
		String newUrl = entity.getUrl();
		if (!StringUtils.equals(origionUrl, newUrl)) {
			throw new BusinessException(ResourceExceptionCodes.URL_FORBIDDEN_MODIFY);
		}
		entity.setCreateDate(new Date());
		entity.setUpdateDate(new Date());
		sysResourceDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public SysResource getById(Integer id) {
		return sysResourceDao.selectByPrimaryKey(id);
	}

	@Override
	public void deleteById(Integer id, String username) {
		SysResource entity = new SysResource();
		entity.setId(id);
		entity.setStatus(StatusEnum.DELETED.ordinal());
		entity.setUpdateUser(username);
		entity.setUpdateDate(new Date());
		sysResourceDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<SysResourceCategoryVO> getCategoryTree() {
		List<SysResourceCategoryVO> topCategories = sysResourceCategoryDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<SysResourceCategoryVO> categories = sysResourceCategoryDao.listAll();
		categories.removeAll(topCategories);
		buildTree(topCategories, categories);
		return topCategories;
	}

	@Override
	public SysResourceCategory getCategoryById(Integer categoryId) {
		return sysResourceCategoryDao.selectByPrimaryKey(categoryId);
	}

	private void buildTree(List<SysResourceCategoryVO> topCategories, List<SysResourceCategoryVO> categories) {
		SysResourceCategoryVO topCategory;
		SysResourceCategoryVO category;
		for (Iterator<SysResourceCategoryVO> topIterator = topCategories.iterator(); topIterator.hasNext();) {
			topCategory = topIterator.next();
			for (Iterator<SysResourceCategoryVO> iterator = categories.iterator(); iterator.hasNext();) {
				category = iterator.next();
				if (category.getParentId() == topCategory.getId()) {
					topCategory.addChild(category);
					iterator.remove();
				}
			}
		}
		for (Iterator<SysResourceCategoryVO> iterator = topCategories.iterator(); iterator.hasNext();) {
			topCategory = iterator.next();
			if (!topCategory.getChildren().isEmpty()) {
				buildTree(topCategory.getChildren(), categories);
			}
		}
	}

}
