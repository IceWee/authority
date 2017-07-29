package bing.system.web.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

import bing.constant.GlobalConstants;
import bing.constant.RedisKeys;
import bing.i18n.MessageSourceService;
import bing.security.CaptchaException;
import bing.util.CaptchaUtils;
import bing.web.controller.GenericController;

@Controller
public class LoginController extends GenericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	private static final String KEY_MESSAGE = "msg";

	@Autowired
	private MessageSourceService messageSourceService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 登录
	 * 
	 * @param error
	 * @param logout
	 * @param expired
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "expired", required = false) String expired, Model model, HttpServletRequest request) {
		addAttribute(model, KEY_MESSAGE, messageSourceService.getMessage("login.label.username"));
		if (error != null) {
			addAttribute(model, KEY_MESSAGE, getErrorMessage(request));
		}
		if (logout != null) {
			addAttribute(model, KEY_MESSAGE, messageSourceService.getMessage("logout.tips.success"));
		}
		if (expired != null) {
			addAttribute(model, KEY_MESSAGE, messageSourceService.getMessage("session.tips.expired"));
		}
		return "login";
	}

	/**
	 * 注销
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	/**
	 * 生验证码图片
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/captchaImage")
	public void captchaImage(HttpServletResponse response, HttpSession session) throws IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 设置验证码
		String captcha = CaptchaUtils.captcha(4);
		LOGGER.debug("生成登录验证码并缓存：{}", captcha);
		session.setAttribute(GlobalConstants.PARAM_CAPTCHA, captcha);
		String currentSessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		stringRedisTemplate.opsForValue().set(RedisKeys.PREFIX_CAPTCHA + currentSessionId, captcha);
		BufferedImage image = CaptchaUtils.captchaImage(captcha);
		ImageIO.write(image, "JPEG", response.getOutputStream());
		response.flushBuffer();
	}

	@ResponseBody
	@RequestMapping(value = "/captchaCode")
	public String captchaCode(HttpSession session) {
		return (String) session.getAttribute(GlobalConstants.PARAM_CAPTCHA);
	}

	/**
	 * 登录错误Spring security会将最后一个Exception放到session中，可以利用这一点处理自定义异常
	 * 
	 * @param request
	 * @return
	 */
	private String getErrorMessage(HttpServletRequest request) {
		Exception exception = (Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		String error = StringUtils.EMPTY;
		if (exception instanceof CaptchaException) {
			error = messageSourceService.getMessage("login.tips.captcha");
		} else if (exception instanceof BadCredentialsException) {
			error = messageSourceService.getMessage("login.tips.invalid");
		} else if (exception instanceof LockedException) {
			error = exception.getMessage();
		}
		return error;
	}

}
