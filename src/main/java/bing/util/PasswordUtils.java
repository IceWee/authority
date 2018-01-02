package bing.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * 密码加密与验证工具类，使用SHA-256加密，产生长度为80的加密串，随机盐，每次产生的加密串不同
 * 
 * @author IceWee
 */
public class PasswordUtils {

	private static final String ENCODE_SECRET = "SHA-256";
	private static final PasswordEncoder ENCODER = new StandardPasswordEncoder(ENCODE_SECRET);

	/**
	 * 密码加密
	 * 
	 * @param rawPassword 未加密密码
	 * @return
	 */
	public static String encrypt(String rawPassword) {
		return ENCODER.encode(rawPassword);
	}

	/**
	 * 密码验证
	 * 
	 * @param rawPassword 未加密密码
	 * @param encodedPassword 已加密密码
	 * @return
	 */
	public static boolean match(String rawPassword, String encodedPassword) {
		return ENCODER.matches(rawPassword, encodedPassword);
	}

}
