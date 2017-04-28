package bing.web.controller;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import bing.system.model.SysUser;

/**
 * 抽象Controller
 * 
 * @author IceWee
 */
public abstract class AbstractController {

	/**
	 * 获取当前登录用户
	 * 
	 * @return
	 */
	protected Optional<SysUser> getCurrentUser() {
		Optional<SysUser> optional = Optional.ofNullable(null);
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (object instanceof SysUser && object != null) {
			SysUser user = (SysUser) object;
			optional = Optional.of(user);
		}
		return optional;
	}

}
