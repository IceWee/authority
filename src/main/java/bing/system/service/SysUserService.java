package bing.system.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bing.domain.GenericPage;
import bing.exception.BusinessException;
import bing.system.condition.SysUserCondition;
import bing.system.model.SysUser;

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
	void saveOrUpdate(SysUser sysUser) throws BusinessException;

	/**
	 * 主键获取用户
	 * 
	 * @param id
	 * @return
	 */
	SysUser get(Integer id);

}
