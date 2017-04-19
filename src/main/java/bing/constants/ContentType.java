package bing.constants;

import org.springframework.http.MediaType;

public class ContentType extends MediaType {

	private static final long serialVersionUID = -3464941866629340373L;

	public final static String APPLICATION_JAVASCRIPT_VALUE = "application/javascript";
	public final static String APPLICATION_CSS_VALUE = "text/css";
	public final static String NAME_ANY_ENDWITH_HTML = "*.html";
	public final static String NAME_ANY_ENDWITH_CSS = "*.css";
	public final static String NAME_ANY_ENDWITH_JS = "*.js";

	public ContentType(String type) {
		super(type);
	}

}
