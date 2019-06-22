package com.kipa.mock.http.service.bo;

import lombok.*;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public final class MockError {
    /**
     * 关闭连接
     */
    private boolean dropConnection;
    /**
     * 信息
     */
    private String responseMessage;
    /**
     * 延迟时长
     */
    private Long delay;
}
