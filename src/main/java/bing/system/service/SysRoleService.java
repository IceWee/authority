package bing.system.service;

import java.util.List;

import bing.domain.GenericService;
import bing.system.condition.SysRoleCondition;
import bing.system.model.SysRole;
import bing.system.vo.SysRoleVO;
import bing.system.vo.UserRoleVO;

public interface SysRoleService extends GenericService<SysRole, SysRoleVO, SysRoleCondition> {

	/**
	 * 获取用户权角色对象
	 * 
	 * @param userId
	 * @return
	 */
	UserRoleVO getUserRoles(Integer userId);

	/**
	 * 保存用户角色
	 * 
	 * @param userId
	 * @param roleIds
	 * @param username
	 */
	void saveUserRoles(Integer userId, Integer[] roleIds, String username);

	/**
	 * 获取全部角色
	 * 
	 * @return
	 */
	List<SysRole> listAll();

}
