package bing.constant;

public class GlobalConstants {

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
	 * Components of Spring Management scan packages
	 */
	public static final String COMPONENT_SCAN_PACKAGES = "bing";

	/**
	 * Super manager username - admin
	 */
	public static final String ADMIN = "admin";

	/**
	 * Current user attribute in session scope
	 */
	public static final String SESSION_ATTRIBUTE_CURRENT_USER = "currentUser";

	/**
	 * message attribute in request scope
	 */
	public static final String REQUEST_ATTRIBUTE_MESSAGE = "message";

	/**
	 * error attribute in request scope
	 */
	public static final String REQUEST_ATTRIBUTE_ERROR = "error";

	/**
	 * bean attribute in request scope
	 */
	public static final String REQUEST_ATTRIBUTE_BEAN = "bean";

	/**
	 * Cookie key prefix
	 */
	public static final String COOKIE_KEY = "authority_";

	/**
	 * Cookie expire seconds
	 */
	public static final int COOKIE_EXPIRE_SECONDS = 60 * 60 * 24 * 7;

	/**
	 * Public resource paths
	 */
	public static final String[] PUBLIC_RESOURCE_PATHS = {"/js/**", "/css/**", "/images/**", "/i18n/**"};

	private GlobalConstants() {
		super();
	}

}
