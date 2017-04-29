package bing.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import bing.constants.GlobalConstants;
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

	protected boolean hasErrors(BindingResult bindingResult, Model model) {
		boolean hasErrors = bindingResult.hasErrors() && !bindingResult.getAllErrors().isEmpty();
		if (hasErrors) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			String message = errors.get(0).getDefaultMessage();
			model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_MESSAGE, message);
		}
		return hasErrors;
	}

}
