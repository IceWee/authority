package bing.util;

/**
 * <p>
 * Exception Operation Utils
 * </p>
 *
 * @author IceWee
 * @see java.lang.Throwable
 */
public class ExceptionUtils {

	private ExceptionUtils() {
		super();
	}

	/**
	 * <p>
	 * Parse {@code e} to String.
	 * </p>
	 *
	 * @param e the {@code Throwable} to parsing
	 * @return {@code String} parsed String or ""
	 */
	public static String parseStackTrace(Throwable e) {
		StringBuilder builder = new StringBuilder();
		if (e != null) {
			builder.append(e.toString());
			builder.append('\n');
			for (int i = 0; i < e.getStackTrace().length; i++) {
				builder.append(e.getStackTrace()[i]);
				builder.append('\n');
			}
		}
		return builder.toString();
	}

}
