package com.yff.test.data;

/**
 * @author YFF
 * @date 2020/4/4
 */
public class LoginData {
    /**
     * 登录页的 URL
     */
    public static final String URL[][] = {
            {"****************************************", ""},
            {"****************************************", ""}
    };

    /**
     * token url
     */
    public static final String TOKEN_URL[][] = {
            {"****************************************"},
            {"****************************************"}
    };

    /**
     * board url
     */
    public static final String BOARD_URL[][] = {
            {"****************************************"},
            {"****************************************"}
    };

    /**
     * Authorization 请求头
     */
    public static final String AUTHORIZATION_HEADER[][] = {
            {"****************************************"},
            {"****************************************"}
    };

    /**
     * Accept 请求头
     */
    public static final String ACCEPT_HEADER = "application/json";

    /**
     * scope 请求参数
     */
    public static final String SCOPE_PARAM = "read write";

    /**
     * grant_type 请求参数
     */
    public static final String GRANT_TYPE_PARAM = "password";

    /**
     * status code 返回码
     */
    public static final int SUCCESS_CODE = 200;
}
