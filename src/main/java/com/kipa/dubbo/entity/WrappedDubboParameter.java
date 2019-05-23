package com.kipa.dubbo.entity;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/3
 * 泛华调用的接口入参包装类
 */
@Data
@NoArgsConstructor
public class WrappedDubboParameter {

    private String methodName;
    /**
     * 参数类型数组
     */
    private String[] paramTypeArray;
    /**
     * 参数值数组
     */
    private Object[] valueArray;

    /**
     * 异步回调接口
     */
    private ResponseCallback responseCallback;
}
