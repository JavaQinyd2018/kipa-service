package com.kipa.core;

import lombok.Data;

/**
 * @author Qinyadong
 * 异步调用请求的抽象类
 * @param <B> 调用的接口泛型
 */
@Data
public abstract class AsyncInvokeRequest<B> implements InvokeRequest {

    /**
     * 异步回调的接口
     */
    protected B callback;
}
