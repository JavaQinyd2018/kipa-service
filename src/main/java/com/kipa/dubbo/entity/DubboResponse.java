package com.kipa.dubbo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author: Qinyadong
 * @date: 2019/4/2 16:30
 * dubbo响应的包装类
 */
@Data
@NoArgsConstructor
public final class DubboResponse {
    /**
     * dubbo接口响应的数据
     */
    private Object responseData;

    /**
     * 结果是否为json格式
     */
    private boolean jsonFormat;
    /**
     * 响应的状态，是否成功
     */
    private boolean success;
    /**
     * 错误的情况返回的错误信息
     */
    private String message;
}
