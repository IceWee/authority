package bing.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.constant.EhCacheNames;
import bing.constant.GlobalConstants;
import bing.constant.StatusEnum;
import bing.constant.TreeNodeTypeEnum;
import bing.domain.GenericPage;
import bing.domain.GenericTreeNode;
import bing.exception.BusinessException;
import bing.system.condition.SysMenuCondition;
import bing.system.constant.TreeNodeIdPrefixes;
import bing.system.dao.SysMenuDao;
import bing.system.dao.SysResourceDao;
import bing.system.exception.MenuExceptionCodes;
import bing.system.model.SysMenu;
import bing.system.vo.SysMenuVO;

@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;

	@Autowired
	private SysResourceDao sysResourceDao;

	@Override
	public GenericPage<SysMenuVO> listByPage(SysMenuCondition condition) {
		Long pageNo = condition.getPageNo();
		PageHelper.startPage(pageNo.intValue(), condition.getPageSize().intValue());
		List<SysMenuVO> list = sysMenuDao.listByCondition(condition);
		PageInfo<SysMenuVO> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNo, pageInfo.getTotal(), list);
	}

	@Override
	public void save(SysMenu entity) {
		entity.setId(null);
		Date now = new Date();
		entity.setCreateDate(now);
		entity.setUpdateDate(now);
		sysMenuDao.insert(entity);
	}

	@Override
	public void update(SysMenu entity) {
		entity.setUpdateDate(new Date());
		sysMenuDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public SysMenu getById(Integer id) {
		return sysMenuDao.selectByPrimaryKey(id);
	}

	@Override
	public void deleteById(Integer id, String username) {
		Integer subMenuCount = sysMenuDao.countByParentId(id);
		if (subMenuCount > 0) {
			throw new BusinessException(MenuExceptionCodes.CONTAINS_SUBMENUS);
		}
		SysMenu entity = sysMenuDao.selectByPrimaryKey(id);
		if (Objects.equals(entity.getParentId(), GlobalConstants.TOP_PARENT_ID)) {
			throw new BusinessException(MenuExceptionCodes.TOP_MENU_FORBIDDEN_DELETE);
		}
		entity.setStatus(StatusEnum.DELETED.ordinal());
		entity.setUpdateUser(username);
		entity.setUpdateDate(new Date());
		sysMenuDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	@Cacheable(cacheNames = { EhCacheNames.MENU_TREE_CACHE })
	public List<GenericTreeNode> getMenuTree() {
		List<SysMenuVO> topMenus = sysMenuDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<SysMenuVO> menus = sysMenuDao.listAll();
		List<GenericTreeNode> treeNodes = convertMenu(topMenus);
		GenericTreeNode.buildGenericTree(treeNodes, convertMenu(menus));
		return treeNodes;
	}

	@Override
	public List<GenericTreeNode> getMenuTree(Integer id) {
		List<SysMenuVO> topMenus = sysMenuDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<SysMenuVO> menus = sysMenuDao.listAll();
		List<GenericTreeNode> treeNodes = convertMenu(topMenus);
		List<GenericTreeNode> allTreeNodes = convertMenu(menus);
		List<GenericTreeNode> treeNodesExclude = allTreeNodes.stream().filter(treeNode -> !Objects.equals(treeNode.getAttribute(GlobalConstants.ATTRIBUT_ID), id)).collect(Collectors.toList());
		GenericTreeNode.buildGenericTree(treeNodes, treeNodesExclude);
		return treeNodes;
	}

	@Override
	public List<GenericTreeNode> listMenuByUserId(Integer userId) {
		List<SysMenuVO> topMenus = sysMenuDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<SysMenuVO> menus = sysMenuDao.listAll();
		List<Integer> ownResourceIds = sysResourceDao.listResourceIdByUserId(userId);
		// 迭代全部菜单，未绑定资源的忽略，绑定资源的需要与用户具备的资源比较，不包含则删除菜单
		List<SysMenuVO> ownMenus = menus.stream().filter(menu -> (menu.getResourceId() == null) || (ownResourceIds.contains(menu.getResourceId()))).collect(Collectors.toList());
		List<GenericTreeNode> treeNodes = convertMenu(topMenus);
		GenericTreeNode.buildGenericTree(treeNodes, convertMenu(ownMenus));
		// 移除顶级空菜单，即没子菜单的菜单
		GenericTreeNode topNode;
		GenericTreeNode lv2Node;
		Iterator<GenericTreeNode> topIt = treeNodes.iterator();
		Iterator<GenericTreeNode> lv2It;
		while (topIt.hasNext()) {
			topNode = topIt.next();
			lv2It = topNode.getChildren().iterator();
			while (lv2It.hasNext()) {
				lv2Node = lv2It.next();
				if (lv2Node.getChildren().isEmpty()) {
					lv2It.remove();
				}
			}
		}
		return treeNodes;
	}

	/**
	 * 将资源分类列表转换为节点列表
	 * 
	 * @param sysResourceCategories
	 * @return
	 */
	private List<GenericTreeNode> convertMenu(List<SysMenuVO> sysMenus) {
		List<GenericTreeNode> treeNodes = new ArrayList<>();
		GenericTreeNode treeNode;
		Integer id;
		for (SysMenuVO sysMenu : sysMenus) {
			id = sysMenu.getId();
			treeNode = new GenericTreeNode();
			treeNode.setId(TreeNodeIdPrefixes.MENU + id);
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_ID, id);
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_PARENT_ID, sysMenu.getParentId());
			// 一定要设置type为branch否则禁止添加leaf
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_TYPE, TreeNodeTypeEnum.BRANCH.ordinal());
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_URL, sysMenu.getUrl());
			treeNode.setText(sysMenu.getName());
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

}
