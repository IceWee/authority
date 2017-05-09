package bing.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.constant.EhCacheNames;
import bing.constant.GlobalConstants;
import bing.constant.StatusEnum;
import bing.constant.TreeNodeTypeEnum;
import bing.domain.GenericPage;
import bing.domain.GenericTreeNode;
import bing.exception.BusinessException;
import bing.exception.BusinessExceptionCodes;
import bing.system.condition.SysResourceCondition;
import bing.system.constant.TreeNodeIdPrefixes;
import bing.system.dao.SysMenuDao;
import bing.system.dao.SysResourceCategoryDao;
import bing.system.dao.SysResourceDao;
import bing.system.dao.SysRoleDao;
import bing.system.dao.SysRoleResourceDao;
import bing.system.exception.ResourceExceptionCodes;
import bing.system.model.SysResource;
import bing.system.model.SysResourceCategory;
import bing.system.model.SysRole;
import bing.system.model.SysRoleResource;
import bing.system.vo.SysResourceVO;

@Service("sysResourceService")
public class SysResourceServiceImpl implements SysResourceService {

	@Autowired
	private SysResourceDao sysResourceDao;

	@Autowired
	private SysRoleDao sysRoleDao;

	@Autowired
	private SysMenuDao sysMenuDao;

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
		int menuCount = sysMenuDao.countByResourceId(id);
		if (menuCount > 0) {
			throw new BusinessException(ResourceExceptionCodes.USED_BY_MENU);
		}
		int roleCount = sysRoleResourceDao.countByResourceId(id);
		if (roleCount > 0) {
			throw new BusinessException(ResourceExceptionCodes.AUTHORIZED_TO_ROLE);
		}
		SysResource entity = new SysResource();
		entity.setId(id);
		entity.setStatus(StatusEnum.DELETED.ordinal());
		entity.setUpdateUser(username);
		entity.setUpdateDate(new Date());
		sysResourceDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	@Cacheable(cacheNames = {EhCacheNames.CATEGORY_TREE_CACHE})
	public List<GenericTreeNode> getCategoryTree() {
		List<SysResourceCategory> topCategories = sysResourceCategoryDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<SysResourceCategory> categories = sysResourceCategoryDao.listAll();
		List<GenericTreeNode> treeNodes = convertResourceCategory(topCategories);
		GenericTreeNode.buildGenericTree(treeNodes, convertResourceCategory(categories));
		return treeNodes;
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

	@Override
	public List<GenericTreeNode> getResourceTree(Integer roleId) {
		List<SysResource> resources = sysResourceDao.listAll();
		List<GenericTreeNode> resourceTreeNodes = convertResource(resources);
		List<SysResource> ownSysResources = sysResourceDao.listByRoleId(roleId);
		List<GenericTreeNode> checkedResourceNodes = convertResource(ownSysResources);
		// 迭代全部资源并自动勾选已授权的节点
		resourceTreeNodes.forEach(treeNode -> {
			checkedResourceNodes.forEach(checkedNode -> {
				if (Objects.equals(treeNode.getAttribute(GlobalConstants.ATTRIBUT_ID), checkedNode.getAttribute(GlobalConstants.ATTRIBUT_ID))) {
					treeNode.setChecked(true);
				}
			});
			treeNode.setIconCls(GlobalConstants.EASYUI_ICON_CLS_LEAF);
		});
		// 遍历资源分类挂接资源
		List<SysResourceCategory> topCategories = sysResourceCategoryDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<GenericTreeNode> topCategoryTreeNodes = convertResourceCategory(topCategories);
		List<SysResourceCategory> categories = sysResourceCategoryDao.listAll();
		List<GenericTreeNode> categoryTreeNodes = convertResourceCategory(categories);
		categoryTreeNodes.forEach(branch -> branch.setIconCls(GlobalConstants.EASYUI_ICON_CLS_BRANCH));
		// 将资源分类节点与资源节点合并递归构造树形结构
		categoryTreeNodes.addAll(resourceTreeNodes);
		GenericTreeNode.buildGenericTree(topCategoryTreeNodes, categoryTreeNodes);
		return topCategoryTreeNodes;
	}

	@Override
	@Cacheable(cacheNames = {EhCacheNames.RESOURCE_TREE_CACHE})
	public List<GenericTreeNode> getResourceTree() {
		List<SysResource> resources = sysResourceDao.listAll();
		List<GenericTreeNode> resourceTreeNodes = convertResource(resources);
		resourceTreeNodes.forEach(leaf -> leaf.setIconCls(GlobalConstants.EASYUI_ICON_CLS_LEAF));
		// 遍历资源分类挂接资源
		List<SysResourceCategory> topCategories = sysResourceCategoryDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<GenericTreeNode> topCategoryTreeNodes = convertResourceCategory(topCategories);
		List<SysResourceCategory> categories = sysResourceCategoryDao.listAll();
		List<GenericTreeNode> categoryTreeNodes = convertResourceCategory(categories);
		categoryTreeNodes.forEach(branch -> branch.setIconCls(GlobalConstants.EASYUI_ICON_CLS_BRANCH));
		// 挂接资源
		categoryTreeNodes.forEach(branch -> {
			resourceTreeNodes.forEach(leaf -> {
				if (Objects.equals(branch.getAttribute(GlobalConstants.ATTRIBUT_ID), leaf.getAttribute(GlobalConstants.ATTRIBUT_PARENT_ID))) {
					branch.addChild(leaf);
				}
			});
		});
		GenericTreeNode.buildGenericTree(topCategoryTreeNodes, categoryTreeNodes);
		return topCategoryTreeNodes;
	}

	/**
	 * 将资源分类列表转换为节点列表
	 * 
	 * @param sysResourceCategories
	 * @return
	 */
	private List<GenericTreeNode> convertResourceCategory(List<SysResourceCategory> sysResourceCategories) {
		List<GenericTreeNode> treeNodes = new ArrayList<>();
		GenericTreeNode treeNode;
		Integer id;
		for (SysResourceCategory sysResourceCategory : sysResourceCategories) {
			id = sysResourceCategory.getId();
			treeNode = new GenericTreeNode();
			treeNode.setId(TreeNodeIdPrefixes.RESOURCE_CATEGORY + id);
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_ID, sysResourceCategory.getId());
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_PARENT_ID, sysResourceCategory.getParentId());
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_TYPE, TreeNodeTypeEnum.BRANCH.ordinal());
			treeNode.setText(sysResourceCategory.getName());
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

	/**
	 * 将资源列表转换为节点列表
	 * 
	 * @param sysResources
	 * @return
	 */
	private List<GenericTreeNode> convertResource(List<SysResource> sysResources) {
		List<GenericTreeNode> treeNodes = new ArrayList<>();
		GenericTreeNode treeNode;
		Integer id;
		for (SysResource sysResource : sysResources) {
			id = sysResource.getId();
			treeNode = new GenericTreeNode();
			treeNode.setId(TreeNodeIdPrefixes.RESOURCE + id);
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_ID, id);
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_PARENT_ID, sysResource.getCategoryId());
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_TYPE, TreeNodeTypeEnum.LEAF.ordinal());
			treeNode.setText(sysResource.getName());
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

}
