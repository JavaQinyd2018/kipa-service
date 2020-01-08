package com.kipa.http.excute;

/**
 * Http常量
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
public class HttpConstant {

    public static final Integer CONNECTION_TIMEOUT_SECONDS = 10;

    public static final Integer READ_TIMEOUT_SECONDS = 10 * 60;

    public static final Integer WRITE_TIMEOUT_SECONDS = 10 * 60;

    public static final int MAX_IDLE_CONNECTIONS = 20;

    public static final long KEEPALIVE_DURATION = 5;

    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CONNECTION = "Connection";
    public static final String COOKIE = "Cookie";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String DATE= "Date";
    public static final String EXPECT = "Expect";
    public static final String FROM = "From";
    public static final String HOST = "Host";
    public static final String IF_MATCH = "If-Match ";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String IF_RANGE = "If-Range";
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String KEEP_ALIVE = "Keep-Alive";
    public static final String MAX_FORWARDS = "Max-Forwards";
    public static final String PRAGMA = "Pragma";
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String RANGE = "Range";
    public static final String REFERER = "Referer";
    public static final String TE = "TE";
    public static final String UPGRADE = "Upgrade";
    public static final String USER_AGENT = "User-Agent";
    public static final String VIA = "Via";
    public static final String WARNING = "Warning";

    public static final String APP_FORM_URLENCODED="application/x-www-form-urlencoded";
    public static final String TEXT_PLAIN="text/plain";
    public static final String TEXT_HTML="text/html";
    public static final String TEXT_XML="text/xml";
    public static final String TEXT_JSON="text/json";
    public static final String CONTENT_CHARSET_ISO_8859_1 = "ISO-8859-1";
    public static final String CONTENT_CHARSET_UTF8 = "utf-8";
    public static final String DEF_PROTOCOL_CHARSET = "US-ASCII";
    public static final String CONN_CLOSE = "close";
    public static final String EXPECT_CONTINUE = "100-continue";
    public static final String JSON = "application/json";
}
