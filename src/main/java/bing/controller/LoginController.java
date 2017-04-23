package bing.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bing.model.SysUser;
import bing.service.MessageSourceService;
import bing.util.CaptchaUtils;

@Controller
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private MessageSourceService messageSourceService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "expired", required = false) String expired, ModelMap model, HttpSession session) {
		LOGGER.info("visiting login action...");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof SysUser) { // 已登录
			SysUser user = (SysUser) principal;
			LOGGER.info("用户：{} 已登录，重定向到主页面", user.getName());
			return "redirect:/main";
		}
		model.addAttribute("msg", messageSourceService.getMessage("login.label.username"));
		if (error != null) {
			model.addAttribute("msg", messageSourceService.getMessage("login.tips.invalid"));
		}
		if (logout != null) {
			model.addAttribute("msg", messageSourceService.getMessage("logout.tips.success"));
		}
		if (expired != null) {
			model.addAttribute("msg", messageSourceService.getMessage("session.tips.expired"));
		}
		// 设置验证码
		String captcha = CaptchaUtils.captcha(4);
		LOGGER.info("生成登录验证码：{}", captcha);
		session.setAttribute("captcha", captcha);
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("visiting logout action...");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

}
