package com.kipa.mock.http.entity;

import com.kipa.core.Properties;
import lombok.Data;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 14:51
 * mock 服务的配置信息
 */
@Data
public class MockServerProperties implements Properties {

    /**
     * 远程host
     */
    private String remoteHost;
    /**
     * 远程port
     */
    private Integer remotePort;
    /**
     * 本地port
     */
    private Integer localPort;
}
