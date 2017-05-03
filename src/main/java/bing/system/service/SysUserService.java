package bing.system.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bing.domain.GenericService;
import bing.system.condition.SysUserCondition;
import bing.system.model.SysUser;
import bing.system.vo.SysUserVO;
import bing.system.vo.UserRoleVO;

public interface SysUserService extends UserDetailsService, GenericService<SysUser, SysUserVO, SysUserCondition> {

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
	 */
	void saveUserRoles(Integer userId, Integer[] roleIds);

}
