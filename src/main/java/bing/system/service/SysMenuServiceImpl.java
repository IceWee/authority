package bing.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.constant.EhCacheNames;
import bing.constant.GlobalConstants;
import bing.constant.StatusEnum;
import bing.domain.GenericPage;
import bing.domain.GenericTreeNode;
import bing.exception.BusinessException;
import bing.system.condition.SysMenuCondition;
import bing.system.constant.TreeNodeIdPrefixes;
import bing.system.dao.SysMenuDao;
import bing.system.exception.MenuExceptionCodes;
import bing.system.model.SysMenu;

@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;

	@Override
	public GenericPage<SysMenu> listByPage(SysMenuCondition condition) {
		Long pageNo = condition.getPageNo();
		PageHelper.startPage(pageNo.intValue(), condition.getPageSize().intValue());
		List<SysMenu> list = sysMenuDao.listByCondition(condition);
		PageInfo<SysMenu> pageInfo = new PageInfo<>(list);
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
	@Cacheable(cacheNames = {EhCacheNames.MENU_TREE_CACHE})
	public List<GenericTreeNode> getMenuTree() {
		List<SysMenu> topMenus = sysMenuDao.listByParentId(GlobalConstants.TOP_PARENT_ID);
		List<SysMenu> menus = sysMenuDao.listAll();
		List<GenericTreeNode> treeNodes = convertMenu(topMenus);
		GenericTreeNode.buildGenericTree(treeNodes, convertMenu(menus));
		return treeNodes;
	}

	/**
	 * 将资源分类列表转换为节点列表
	 * 
	 * @param sysResourceCategories
	 * @return
	 */
	private List<GenericTreeNode> convertMenu(List<SysMenu> sysMenus) {
		List<GenericTreeNode> treeNodes = new ArrayList<>();
		GenericTreeNode treeNode;
		Integer id;
		for (SysMenu sysMenu : sysMenus) {
			id = sysMenu.getId();
			treeNode = new GenericTreeNode();
			treeNode.setId(TreeNodeIdPrefixes.MENU + id);
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_ID, id);
			treeNode.setAttribute(GlobalConstants.ATTRIBUT_PARENT_ID, sysMenu.getParentId());
			treeNode.setText(sysMenu.getName());
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

}
