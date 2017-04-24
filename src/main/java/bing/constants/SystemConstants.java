package bing.constants;

public class SystemConstants {

	/**
	 * Request parameter name - capthca
	 */
	public static final String PARAM_CAPTCHA = "captcha";

	/**
	 * Session timeout seconds
	 */
	public static final int SESSION_TIMEOUT_SECONDS = 10 * 60;

	/**
	 * MyBatis mapper scan packages
	 */
	public static final String MAPPER_SCAN_PACKAGES = "bing.**.dao";

	/**
	 * Spring session redis key prefix
	 */
	public static final String SPRING_SESSION_REDIS_PREFIX = "spring:session:sessions:";

	private SystemConstants() {
		super();
	}

}
