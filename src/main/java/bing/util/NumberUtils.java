package bing.util;

import java.util.Objects;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class NumberUtils {

	private NumberUtils() {
		super();
	}
	
	/**
	 * Generates a randow num-string with specified length.
	 *
	 * @param length
	 *            The length to create
	 * @return Random num-string with specified length.
	 */
	public static String createRandomNumString(int length) {
		Random random = new Random();
		double num = (1 + random.nextDouble()) * Math.pow(10, length);
		String string = Objects.toString(num);
		return StringUtils.substring(string, 1, length + 1);
	}
	
}
