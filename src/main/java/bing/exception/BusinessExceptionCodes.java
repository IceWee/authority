package bing.exception;

public interface BusinessExceptionCodes {

    /**
     * 系统异常错误编码前缀
     */
    String PREFIX = "SYS_";

    String SERVER_ERROR = PREFIX + "001";

    String UNKNOW_ERROR = PREFIX + "002";

    String USER_ADMIN_MISSING = PREFIX + "003";

    String ROLE_ADMIN_MISSING = PREFIX + "004";

    String UPLOAD_FAILED = PREFIX + "005";

    String SESSION_EXPIRED = PREFIX + "006";

    String ACCESS_DENIED = PREFIX + "403";

}
