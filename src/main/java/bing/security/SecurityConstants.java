package bing.security;

public class SecurityConstants {

	public static final String REALM_NAME = "SECURITY_REALM";

	/**
	 * Request parameter name - remember-me
	 */
	public static final String PARAM_REMEMBER_ME = "rememberme";

	/**
	 * Cookie key prefix
	 */
	public static final String COOKIE_KEY = "authority_";

	public static final String URI_LOGIN = "/login";

	public static final String URI_LOGIN_SUCCESS = "/index";

	public static final String URI_LOGIN_FAILURE = "/login?error";

	public static final String URI_ACCESS_DENIED = "/accessDenied";

	public static final String URI_LOGOUT_SUCCESS = "/login?logout";

	public static final String URI_SESSION_EXPIRED = "/login?expired";

	public static final int MAXIMUM_SESSIONS = 1;

	/**
	 * Cookie expire seconds
	 */
	public static final int COOKIE_EXPIRE_SECONDS = 60 * 60 * 24 * 7;

	/**
	 * Public resource paths
	 */
	public static final String[] PUBLIC_RESOURCE_PATHS = {"/js/**", "/css/**", "/images/**", "/i18n/**"};

	private SecurityConstants() {
		super();
	}

}
