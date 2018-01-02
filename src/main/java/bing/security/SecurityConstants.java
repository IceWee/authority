package bing.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface SecurityConstants {

    String REALM_NAME = "SECURITY_REALM";

    /**
     * Request parameter name - remember-me
     */
    String PARAM_REMEMBER_ME = "rememberme";

    /**
     * Cookie key prefix
     */
    String COOKIE_KEY = "authority_";

    String URI_LOGIN = "/login";

    String URI_LOGIN_SUCCESS = "/index";

    String URI_LOGIN_FAILURE = "/login?error";

    String URI_ACCESS_DENIED = "/accessDenied";

    String URI_LOGOUT_SUCCESS = "/login?logout";

    String URI_SESSION_EXPIRED = "/login?expired";

    int MAXIMUM_SESSIONS = 1;

    /**
     * Session timeout seconds, 10 mins
     */
    int SESSION_TIMEOUT_SECONDS = 10 * 60;

    /**
     * Cookie expire seconds
     */
    int COOKIE_EXPIRE_SECONDS = 60 * 60 * 24 * 7;

    /**
     * Public resource paths
     */
    List<String> PUBLIC_RESOURCE_PATHS = Collections.unmodifiableList(Arrays.asList("/js/**", "/css/**", "/images/**", "/i18n/**"));

}
