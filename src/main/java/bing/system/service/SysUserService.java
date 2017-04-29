package bing.system.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bing.domain.GenericPage;
import bing.system.condition.SysUserCondition;
import bing.system.model.SysUser;

public interface SysUserService extends UserDetailsService {

	/**
	 * 用户分页查询
	 * 
	 * @param sysUserCondition
	 * @return
	 */
	GenericPage<SysUser> listUserByPage(SysUserCondition sysUserCondition);

	/**
	 * 保存用户
	 * 
	 * @param sysUser
	 */
	void saveUser(SysUser sysUser);

	/**
	 * 更新用户
	 * 
	 * @param sysUser
	 */
	void updateUser(SysUser sysUser);

	/**
	 * 主键获取用户
	 * 
	 * @param id
	 * @return
	 */
	SysUser getUserById(Integer id);

	/**
	 * 逻辑删除
	 * 
	 * @param id
	 * @param username 操作人
	 */
	void deleteUserById(Integer id, String username);

}
