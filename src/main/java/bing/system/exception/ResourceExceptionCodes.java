package bing.system.exception;

import bing.exception.BusinessExceptionCodes;

public interface ResourceExceptionCodes extends BusinessExceptionCodes {

    /**
     * 资源异常错误编码前缀
     */
    String PREFIX = "RESOURCE_";

    String URL_USED = PREFIX + "001";

    String URL_FORBIDDEN_MODIFY = PREFIX + "002";

    String CATEGORY_CONTAINS_RESOURCE = PREFIX + "003";

    String CATEGORY_CONTAINS_SUBCATEGORY = PREFIX + "004";

    String USED_BY_MENU = PREFIX + "005";

    String AUTHORIZED_TO_ROLE = PREFIX + "006";

}
