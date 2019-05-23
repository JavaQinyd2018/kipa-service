package com.kipa.http.exception;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * http包装的Exception
 */
public class HttpProcessException extends RuntimeException {

    public HttpProcessException(String errorMessage) {
        super(errorMessage);
    }

    public HttpProcessException(Exception e) {
        super(e);
    }

    public HttpProcessException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
