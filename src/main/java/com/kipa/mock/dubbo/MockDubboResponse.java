package com.kipa.mock.dubbo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Qinyadong
 * @date: 2019/5/29 14:42
 * mock dubbo接口的相应结果
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class MockDubboResponse {

    /**
     * dubbo接口响应的数据
     */
    private Object responseData;

    /**
     * 响应的状态，是否成功
     */
    private boolean success;


    /**
     * 错误的情况返回的错误信息
     */
    private String message;
}
