package com.kipa.dubbo.exception;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 16:26
 * dubbo调用包装类
 */
public class DubboInvokeException extends RuntimeException{

    public DubboInvokeException(String errorMessage) {
        super(errorMessage);
    }

    public DubboInvokeException(Exception e) {
        super(e);
    }

    public DubboInvokeException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
