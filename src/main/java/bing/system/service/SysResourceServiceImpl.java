package bing.system.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
		buildCategoryTree(topCategories, categories);
		return topCategories;
	}

	@Override
	public SysResourceCategory getCategoryById(Integer categoryId) {
		return sysResourceCategoryDao.selectByPrimaryKey(categoryId);
	}

	@Override
	public void saveCategory(SysResourceCategory category) {
		category.setCreateDate(new Date());
		category.setUpdateDate(new Date());
		sysResourceCategoryDao.insert(category);
	}

	@Override
	public void updateCategory(SysResourceCategory category) {
		category.setUpdateDate(new Date());
		sysResourceCategoryDao.updateByPrimaryKey(category);
	}

	@Override
	public void deleteCategoryById(Integer categoryId, String username) {
		Integer resourceCount = sysResourceDao.countByCategoryId(categoryId);
		if (resourceCount > 0) {
			throw new BusinessException(ResourceExceptionCodes.CATEGORY_CONTAINS_RESOURCE);
		}
		Integer subCategoryCount = sysResourceCategoryDao.countByParentId(categoryId);
		if (subCategoryCount > 0) {
			throw new BusinessException(ResourceExceptionCodes.CATEGORY_CONTAINS_SUBCATEGORY);
		}
		SysResourceCategory entity = new SysResourceCategory();
		entity.setId(categoryId);
		entity.setStatus(StatusEnum.DELETED.ordinal());
		entity.setUpdateUser(username);
		entity.setUpdateDate(new Date());
		sysResourceCategoryDao.updateByPrimaryKeySelective(entity);
	}

	/**
	 * 递归构造资源分类树
	 * 
	 * @param parentCategories 上级资源分类
	 * @param categories 全部资源分类
	 */
	private void buildCategoryTree(List<SysResourceCategoryVO> parentCategories, List<SysResourceCategoryVO> categories) {
		parentCategories.forEach(parentCategory -> {
			categories.forEach(category -> {
				if (Objects.equals(parentCategory.getId(), category.getParentId())) {
					parentCategory.addChild(category);
				}
			});
			if (!parentCategory.getChildren().isEmpty()) {
				buildCategoryTree(parentCategory.getChildren(), categories);
			}
		});
	}

}
