package bing.system.service;

import java.util.List;

import bing.domain.GenericService;
import bing.domain.GenericTreeNode;
import bing.system.condition.SysMenuCondition;
import bing.system.model.SysMenu;
import bing.system.vo.SysMenuVO;

public interface SysMenuService extends GenericService<SysMenu, SysMenuVO, SysMenuCondition> {

	/**
	 * 菜单树
	 * 
	 * @return
	 */
	List<GenericTreeNode> getMenuTree();

	/**
	 * 菜单树(排除自己)
	 * 
	 * @param id
	 * @return
	 */
	List<GenericTreeNode> getMenuTree(Integer id);

	/**
	 * 获取用户菜单(根据权限过滤)
	 * 
	 * @param userId
	 * @return
	 */
	List<GenericTreeNode> listMenuByUserId(Integer userId);

}
