package bing.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bing.conditions.SysUserCondition;
import bing.domain.GenericPage;
import bing.model.SysUser;

public interface SysUserService extends UserDetailsService {

	/**
	 * 用户分页查询
	 * 
	 * @author IceWee
	 * @param sysUserCondition
	 * @return
	 */
	GenericPage<SysUser> listByPage(SysUserCondition sysUserCondition);

}
