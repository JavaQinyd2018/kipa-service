package com.kipa.common;

/**
 * @author Qinyadong
 * @date 2019/7/5 15:35
 * @desciption 框架运行时异常包装类
 */
public class KipaProcessException extends RuntimeException {

    public KipaProcessException(String errorMessage) {
        super(errorMessage);
    }

    public KipaProcessException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public KipaProcessException(Throwable throwable) {
        super(throwable);
    }
}
