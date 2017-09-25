package bing.system.exception;

import bing.exception.BusinessExceptionCodes;

public interface MenuExceptionCodes extends BusinessExceptionCodes {

    /**
     * 菜单异常错误编码前缀
     */
    String PREFIX = "MENU_";

    String CONTAINS_SUBMENUS = PREFIX + "001";

    String TOP_MENU_FORBIDDEN_DELETE = PREFIX + "002";

}
