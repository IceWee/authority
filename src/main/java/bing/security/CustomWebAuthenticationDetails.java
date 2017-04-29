package bing.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import bing.constant.GlobalConstants;

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
		captcha = request.getParameter(GlobalConstants.PARAM_CAPTCHA);
	}

	public String getCaptcha() {
		return captcha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((captcha == null) ? 0 : captcha.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomWebAuthenticationDetails other = (CustomWebAuthenticationDetails) obj;
		if (captcha == null) {
			if (other.captcha != null)
				return false;
		} else if (!captcha.equals(other.captcha))
			return false;
		return true;
	}

}
