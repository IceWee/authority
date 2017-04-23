package bing.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import bing.constants.SystemConstants;

/**
 * 自定义权限类，为了扩展登陆时验证码属性
 * 
 * @author IceWee
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = -6968148705807281971L;
	
	/**
	 * 登陆验证码
	 */
	private final String captcha;

	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		captcha = request.getParameter(SystemConstants.PARAM_CAPTCHA);
	}

	public String getCaptcha() {
		return captcha;
	}

}
