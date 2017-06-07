package bing.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.constant.EhCacheNames;
import bing.constant.GlobalConstants;
import bing.constant.StatusEnum;
import bing.domain.GenericPage;
import bing.exception.BusinessException;
import bing.system.condition.SysMenuCondition;
import bing.system.dao.SysMenuDao;
import bing.system.dao.SysResourceDao;
import bing.system.dao.SysUserDao;
import bing.system.domain.MenuTreeNode;
import bing.system.exception.MenuExceptionCodes;
import bing.system.model.SysMenu;
import bing.system.model.SysUser;
import bing.system.vo.SysMenuVO;

@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private SysMenuDao sysMenuDao;

	@Autowired
	private SysResourceDao sysResourceDao;

	@Override
	public GenericPage<SysMenuVO> listByPage(SysMenuCondition condition) {
		int pageNumber = condition.getPageNumber();
		int pageSize = condition.getPageSize();
		PageHelper.startPage(pageNumber, condition.getPageSize());
		List<SysMenuVO> list = sysMenuDao.listByCondition(condition);
		PageInfo<SysMenuVO> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNumber, pageSize, pageInfo.getTotal(), list);
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
			throw new BusinessException(MenuExceptionCodes.singleton().CONTAINS_SUBMENUS);
		}
		SysMenu entity = sysMenuDao.selectByPrimaryKey(id);
		if (StringUtils.equals(Objects.toString(entity.getParentId()), Objects.toString(GlobalConstants.TOP_PARENT_ID))) {
			throw new BusinessException(MenuExceptionCodes.singleton().TOP_MENU_FORBIDDEN_DELETE);
		}
		entity.setStatus(StatusEnum.DELETED.ordinal());
		entity.setUpdateUser(username);
		entity.setUpdateDate(new Date());
		sysMenuDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	@Cacheable(cacheNames = { EhCacheNames.MENU_TREE_CACHE })
	public List<MenuTreeNode> getMenuTree() {
		List<SysMenuVO> topMenus = sysMenuDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<SysMenuVO> menus = sysMenuDao.listAll();
		List<MenuTreeNode> treeNodes = convertMenu(topMenus);
		treeNodes.forEach(menu -> menu.setIconSkin(GlobalConstants.ICON_CLS_ROOT));
		MenuTreeNode.buildMenuTree(treeNodes, convertMenu(menus));
		return treeNodes;
	}

	@Override
	public List<MenuTreeNode> getMenuTree(Integer id) {
		List<SysMenuVO> topMenus = sysMenuDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<SysMenuVO> menus = sysMenuDao.listAll();
		List<MenuTreeNode> treeNodes = convertMenu(topMenus);
		treeNodes.forEach(menu -> menu.setIconSkin(GlobalConstants.ICON_CLS_ROOT));
		List<MenuTreeNode> allTreeNodes = convertMenu(menus);
		List<MenuTreeNode> treeNodesExclude = allTreeNodes.stream().filter(treeNode -> !StringUtils.equals(treeNode.getId(), Objects.toString(id)))
				.collect(Collectors.toList());
		MenuTreeNode.buildMenuTree(treeNodes, treeNodesExclude);
		return treeNodes;
	}

	@Override
	public List<MenuTreeNode> listMenuByUserId(Integer userId) {
		List<SysMenuVO> topMenus = sysMenuDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<SysMenuVO> menus = sysMenuDao.listAll();
		List<MenuTreeNode> treeNodes = convertMenuWithUrl(topMenus);
		SysUser user = sysUserDao.selectByPrimaryKey(userId);
		List<SysMenuVO> ownMenus = menus;
		if (!GlobalConstants.ADMIN.equals(user.getUsername())) { // 非超级管理员，需要根据用户具备角色过滤菜单
			List<Integer> ownResourceIds = sysResourceDao.listResourceIdByUserId(userId);
			// 迭代全部菜单，未绑定资源的忽略，绑定资源的需要与用户具备的资源比较，不包含则删除菜单
			ownMenus = menus.stream().filter(menu -> (menu.getResourceId() == null) || (ownResourceIds.contains(menu.getResourceId())))
					.collect(Collectors.toList());
		}
		MenuTreeNode.buildMenuTree(treeNodes, convertMenuWithUrl(ownMenus));
		// 移除顶级空菜单，即没子菜单的菜单
		MenuTreeNode topNode;
		MenuTreeNode lv2Node;
		Iterator<MenuTreeNode> topIt = treeNodes.iterator();
		Iterator<MenuTreeNode> lv2It;
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
	 * 生成菜单树节点
	 * 
	 * @param sysMenus
	 * @return
	 */
	private List<MenuTreeNode> convertMenu(List<SysMenuVO> sysMenus) {
		List<MenuTreeNode> treeNodes = new ArrayList<>();
		MenuTreeNode treeNode;
		Integer id;
		for (SysMenuVO sysMenu : sysMenus) {
			id = sysMenu.getId();
			treeNode = new MenuTreeNode();
			treeNode.setId(Objects.toString(id));
			treeNode.setName(sysMenu.getName());
			treeNode.setParentId(Objects.toString(sysMenu.getParentId()));
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

	/**
	 * 生成菜单树节点
	 * 
	 * @param sysMenus
	 * @return
	 */
	private List<MenuTreeNode> convertMenuWithUrl(List<SysMenuVO> sysMenus) {
		List<MenuTreeNode> treeNodes = new ArrayList<>();
		MenuTreeNode treeNode;
		Integer id;
		for (SysMenuVO sysMenu : sysMenus) {
			id = sysMenu.getId();
			treeNode = new MenuTreeNode();
			treeNode.setId(Objects.toString(id));
			treeNode.setName(sysMenu.getName());
			treeNode.setParentId(Objects.toString(sysMenu.getParentId()));
			treeNode.setUrl(sysMenu.getUrl());
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

}
