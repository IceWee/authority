package bing.util;

import java.util.UUID;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static String uuid() {
		return replace(UUID.randomUUID().toString(), "-", "");
	}

}
