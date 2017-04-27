package bing.controller;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import bing.model.SysUser;

public class BaseController {

	/**
	 * 获取当前登录用户
	 * 
	 * @return
	 */
	protected Optional<SysUser> currentUser() {
		Optional<SysUser> optional = Optional.ofNullable(null);
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (object instanceof SysUser && object != null) {
			SysUser user = (SysUser) object;
			optional = Optional.of(user);
		}
		return optional;
	}

}
