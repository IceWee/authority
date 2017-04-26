package bing.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bing.conditions.SysUserCondition;
import bing.domain.GenericPage;
import bing.model.SysUser;

public interface SysUserService extends UserDetailsService {

	/**
	 * 用户分页查询
	 * 
	 * @param sysUserCondition
	 * @return
	 */
	GenericPage<SysUser> listByPage(SysUserCondition sysUserCondition);

	/**
	 * 保存或更新用户
	 * 
	 * @param sysUser
	 */
	void saveOrUpdate(SysUser sysUser);

	/**
	 * 主键获取用户
	 * 
	 * @param id
	 * @return
	 */
	SysUser get(Integer id);

}
