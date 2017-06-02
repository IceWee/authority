package bing.util;

import java.util.UUID;

public class UUIDUtils {

	private UUIDUtils() {
		super();
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
