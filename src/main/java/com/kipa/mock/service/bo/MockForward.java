package com.kipa.mock.service.bo;

import lombok.*;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class MockForward {
    /**
     * 跳转到host
     */
    private String host;
    /**
     * 跳转到port
     */
    private Integer port;

    private String scheme;
    /**
     * 延迟时长
     */
    private Long delay;
}
