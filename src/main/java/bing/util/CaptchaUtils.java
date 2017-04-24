package bing.util;

public class CaptchaUtils {

	private static final String WORD = "ACDEFGHJKLMNPQRTUVWXYZ234679";

	private static final String NUMBER = "0123456789";

	private CaptchaUtils() {
		super();
	}

	public static String captcha(int length) {
		StringBuilder builder = new StringBuilder();
		int len = WORD.length();
		for (int i = 0; i < length; i++) {
			builder.append(WORD.charAt((int) Math.round(Math.random() * (len - 1))));
		}
		return builder.toString();
	}

	public static String number(int length) {
		StringBuilder builder = new StringBuilder();
		int len = NUMBER.length();
		for (int i = 0; i < length; i++) {
			builder.append(NUMBER.charAt((int) Math.round(Math.random() * (len - 1))));
		}
		return builder.toString();
	}

}
