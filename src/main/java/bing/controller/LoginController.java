package bing.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import org.springframework.web.context.request.RequestContextHolder;

import bing.constants.RedisKeys;
import bing.constants.SystemConstants;
import bing.service.MessageSourceService;
import bing.util.CaptchaUtils;

@Controller
public class LoginController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private MessageSourceService messageSourceService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "expired", required = false) String expired, ModelMap model, HttpSession session) {
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
		LOGGER.info("生成登录验证码并缓存：{}", captcha);
		session.setAttribute(SystemConstants.PARAM_CAPTCHA, captcha);
		String currentSessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		stringRedisTemplate.opsForValue().set(RedisKeys.PREFIX_CAPTCHA + currentSessionId, captcha);
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
