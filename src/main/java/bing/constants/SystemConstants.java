package bing.constants;

public class SystemConstants {

	/**
	 * Request parameter name - capthca
	 */
	public static final String PARAM_CAPTCHA = "captcha";

	/**
	 * Request parameter name - remember-me
	 */
	public static final String PARAM_REMEMBER_ME = "rememberme";

	/**
	 * Session timeout seconds
	 */
	public static final int SESSION_TIMEOUT_SECONDS = 10 * 60; // 10 mins

	/**
	 * MyBatis mapper scan packages
	 */
	public static final String MAPPER_SCAN_PACKAGES = "bing.**.dao";

	/**
	 * Super manager username - admin
	 */
	public static final String ADMIN = "admin";

	/**
	 * Current user attribute in session scope
	 */
	public static final String SESSION_ATTRIBUTE_CURRENT_USER = "currentUser";

	private SystemConstants() {
		super();
	}

}
