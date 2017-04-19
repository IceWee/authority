package bing.constants;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class Charsets {

	private Charsets() {
		super();
	}

	/**
	 * CharEncodingISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1.
	 */
	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";

	/**
	 * CharEncodingISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1.
	 */
	public static final Charset ISO_8859_1 = Charset.forName(CHARSET_ISO_8859_1);

	/**
	 * Seven-bit ASCII, also known as ISO646-US, also known as the Basic Latin block of the Unicode character set.
	 */
	public static final String CHARSET_US_ASCII = "US-ASCII";

	/**
	 * Seven-bit ASCII, also known as ISO646-US, also known as the Basic Latin block of the Unicode character set.
	 */
	public static final Charset US_ASCII = Charset.forName(CHARSET_US_ASCII);

	/**
	 * Sixteen-bit Unicode Transformation Format, The byte order specified by a mandatory initial byte-order mark (either order accepted on input,
	 * big-endian used on output)
	 */
	public static final String CHARSET_UTF_16 = "UTF-16";

	/**
	 * Sixteen-bit Unicode Transformation Format, The byte order specified by a mandatory initial byte-order mark (either order accepted on input,
	 * big-endian used on output)
	 */
	public static final Charset UTF_16 = Charset.forName(CHARSET_UTF_16);

	/**
	 * Sixteen-bit Unicode Transformation Format, big-endian byte order.
	 */
	public static final String CHARSET_UTF_16BE = "UTF-16BE";

	/**
	 * Sixteen-bit Unicode Transformation Format, big-endian byte order.
	 */
	public static final Charset UTF_16BE = Charset.forName(CHARSET_UTF_16BE);

	/**
	 * Sixteen-bit Unicode Transformation Format, little-endian byte order.
	 */
	public static final String CHARSET_UTF_16LE = "UTF-16LE";

	/**
	 * Sixteen-bit Unicode Transformation Format, little-endian byte order.
	 */
	public static final Charset UTF_16LE = Charset.forName(CHARSET_UTF_16LE);

	/**
	 * Eight-bit Unicode Transformation Format.
	 */
	public static final String CHARSET_UTF_8 = "UTF-8";

	/**
	 * Eight-bit Unicode Transformation Format.
	 */
	public static final Charset UTF_8 = Charset.forName(CHARSET_UTF_8);

	/**
	 * GBK Unicode Transformation Format.
	 */
	public static final String CHARSET_GBK = "GBK";

	/**
	 * GBK Unicode Transformation Format.
	 */
	public static final Charset GBK = Charset.forName(CHARSET_GBK);

	/**
	 * Returns the given Charset or the default Charset if the given Charset is null.
	 * 
	 * @param charset A charset or null.
	 * @return the given Charset or the default Charset if the given Charset is null
	 */
	public static Charset toCharset(Charset charset) {
		return Charsets.toCharset(charset);
	}

	/**
	 * Returns a Charset for the named charset. If the name is null, return the default Charset.
	 * 
	 * @param charset The name of the requested charset, may be null.
	 * @return a Charset for the named charset
	 * @throws UnsupportedCharsetException If the named charset is unavailable
	 */
	public static Charset toCharset(String charset) {
		return Charsets.toCharset(charset);
	}

}
