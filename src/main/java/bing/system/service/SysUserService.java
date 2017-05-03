package bing.system.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bing.domain.GenericService;
import bing.system.condition.SysUserCondition;
import bing.system.model.SysUser;
import bing.system.vo.RoleUserVO;
import bing.system.vo.SysUserVO;

public interface SysUserService extends UserDetailsService, GenericService<SysUser, SysUserVO, SysUserCondition> {

	/**
	 * 获取角色用户对象
	 * 
	 * @param roleId
	 * @return
	 */
	RoleUserVO getRoleUsers(Integer roleId);

	/**
	 * 保存角色用户
	 * 
	 * @param roleId
	 * @param userIds
	 * @param username
	 */
	void saveRoleUsers(Integer roleId, Integer[] userIds, String username);

}
