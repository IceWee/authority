package bing.system.exception;

import bing.exception.BusinessExceptionCodes;

public interface UserExceptionCodes extends BusinessExceptionCodes {

    /**
     * 用户异常错误编码前缀
     */
    String PREFIX = "USER_";

    String USERNAME_REGISTERED = PREFIX + "001";

    String USERNAME_FORBIDDEN_MODIFY = PREFIX + "002";

    String ORIGION_PASSWORD_WRONG = PREFIX + "003";

    String NAME_IS_NULL = PREFIX + "004";

}
