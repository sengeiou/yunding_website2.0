package com.yundingshuyuan.website.constants;

/**
 * @author:leeyf
 * @create: 2019-03-24 17:44
 * @Description:
 */
public class SysConstant {
    public final static String HEADER_TOKEN = "YDSY_HEADER_TOKEN";

    public static final String TOKEN_REQUEST_PARAM = "accessToken";

    public static final String HTTP_HEADER_CONTENT_TYPE = "Content_Type";

    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json;charset=UTF-8";

    /**
     * token过期时间
     */
    public static final Integer TOKEN_TIMEOUT_PARAM = 6;/*hours*/

    /*
     * 加密前缀
     */
    public static final String PZ="YDSY#%s";

    /**
     *es索引
     */
    public static final String ES_INDEX = "yunding_website";
}