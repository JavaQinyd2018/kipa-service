package com.kipa.http.service.convert;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
public interface Convert<T, R> {

    /**
     * 转化对象
     * @param object
     * @return
     */
    R convert(T object);
}
