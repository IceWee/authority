package bing.system.exception;

import bing.exception.BusinessExceptionCodes;

public interface RoleExceptionCodes extends BusinessExceptionCodes {


    /**
     * 角色异常错误编码前缀
     */
    String PREFIX = "ROLE_";

    String CODE_USED = PREFIX + "001";

    String CODE_FORBIDDEN_MODIFY = PREFIX + "002";

    String AUTHORIZED_TO_USER = PREFIX + "003";


}
