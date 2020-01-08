package com.kipa.http.excute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kipa.core.Properties;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * okhttp的属性配置隐射对象
 * 和 application.properties 属性配置一一对应
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OkHttpClientProperties implements Properties{
    /**
     * http连接超时时间
     */
    private long connectTimeout;
    /**
     * 读超时
     */
    private long readTimeout;
    /**
     * 写超时
     */
    private long writeTimeout;
    /**
     * 重定向
     */
    private boolean followRedirects;
    /**
     * 重试
     */
    private boolean retry;
    /**
     * 是否安全证书校验
     */
    private boolean verifySSLCertificate;
    /**
     * 安全证书路径
     */
    private String certificatePath;
    /**
     * 秘钥路径
     */
    private String keyStorePath;
    /**
     * 秘钥验证
     */
    private String keyStorePass;

    /**
     * http连接池最大Idle连接数
     */
    private int maxIdleConnections;
    /**
     * 连接保持activate的时间
     */
    private long keepAliveDuration;
}
