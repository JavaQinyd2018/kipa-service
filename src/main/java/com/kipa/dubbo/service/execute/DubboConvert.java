package com.kipa.dubbo.service.execute;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/3
 */
public interface DubboConvert<T, R> {

    /**
     * 转化接口
     * @param object
     * @return
     */
    R convert(T object);
}
