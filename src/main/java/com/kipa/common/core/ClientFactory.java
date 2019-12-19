package com.kipa.common.core;

/**
 * @author Qinyadong
 * @date 2019/12/17 10:39
 * @description
 * @since
 */
public interface ClientFactory<P,C> {

    /**
     * 根据属性创建Client对象
     * @param properties
     * @return
     * @throws Exception
     */
    C create(P properties) throws Exception;
}
