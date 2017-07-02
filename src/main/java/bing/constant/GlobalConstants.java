package bing.constant;

public class GlobalConstants {

	/**
	 * Request parameter name - capthca
	 */
	public static final String PARAM_CAPTCHA = "captcha";

	/**
	 * MyBatis mapper scan packages
	 */
	public static final String MAPPER_SCAN_PACKAGES = "bing.**.dao";

	/**
	 * Components of Spring Management scan packages
	 */
	public static final String COMPONENT_SCAN_PACKAGES = "bing";

	/**
	 * Super manager username - admin
	 */
	public static final String ADMIN = "admin";

	/**
	 * Current user attribute in session scope
	 */
	public static final String SESSION_ATTRIBUTE_CURRENT_USER = "currentUser";

	/**
	 * message attribute in request scope
	 */
	public static final String REQUEST_ATTRIBUTE_MESSAGE = "message";

	/**
	 * error attribute in request scope
	 */
	public static final String REQUEST_ATTRIBUTE_ERROR = "error";

	/**
	 * error attribute in request scope
	 */
	public static final String REQUEST_ATTRIBUTE_STACK = "stack";

	/**
	 * bean attribute in request scope
	 */
	public static final String REQUEST_ATTRIBUTE_BEAN = "bean";

	/**
	 * menus attribute in session scope
	 */
	public static final String SESSION_ATTRIBUTE_MENUS = "menus";

	/**
	 * 根节点父ID值-0，无ID为0的节点，即为顶级节点
	 */
	public static final Integer TOP_PARENT_ID = 0;

	/**
	 * 默认无效ID，鉴于有些请求必须包含ID，但参数有没有值的时候使用该无效的ID
	 */
	public static final Integer DEFAULT_INVALID_ID = -1;

	/**
	 * 日期时间格式化
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String ATTRIBUT_ID = "id";
	public static final String ATTRIBUT_PARENT_ID = "parentId";
	public static final String ATTRIBUT_TYPE = "type";
	public static final String ATTRIBUT_URL = "url";

	public static final String ICON_CLS_ROOT = "icon-root";
	public static final String ICON_CLS_CATEGORY = "icon-category";
	public static final String ICON_CLS_RESOURCE = "icon-resource";

	private GlobalConstants() {
		super();
	}

}
