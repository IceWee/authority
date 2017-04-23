package bing.constants;

/**
 * Redis缓存key定义类，防止缓存key重复
 * 
 * @author IceWee
 */
public class RedisKeys {

	/**
	 * 验证码缓存key前缀
	 */
	public static final String PREFIX_CAPTCHA = "CAPTCHA:";

	private RedisKeys() {
		super();
	}

}
