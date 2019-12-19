package com.kipa.common.core;


/**
 * @author Qinyadong
 * @date 2019/12/17 10:29
 * @description
 * @since
 */
public interface Executor<C, T, V> {

    /**
     * 调用第三方执行器
     * @param client
     * @param request
     * @return
     */
    V execute(C client, T request) throws Exception;

}
