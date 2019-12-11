package com.kipa.http.service.base;

import com.kipa.http.core.HttpConstant;
import com.kipa.http.core.OkHttpClientProperties;
import com.kipa.http.ssl.SSLSocketFactoryManager;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Component;


import java.util.concurrent.TimeUnit;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * OkhttpClient生成器
 */
@Component
public class OkHttpClientGenerator{

    public OkHttpClient build(OkHttpClientProperties config) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (config == null) {
            throw new IllegalArgumentException("http客户端的配置项不能为空");
        }
        builder.connectTimeout(config.getConnectTimeout() != 0 ? config.getConnectTimeout(): HttpConstant.CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout() != 0 ? config.getReadTimeout() : HttpConstant.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(config.getWriteTimeout() != 0 ? config.getWriteTimeout() : HttpConstant.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(config.getMaxIdleConnections() == 0 ? config.getMaxIdleConnections() : HttpConstant.MAX_IDLE_CONNECTIONS,
                        config.getKeepAliveDuration() == 0 ? config.getKeepAliveDuration() : HttpConstant.KEEPALIVE_DURATION, TimeUnit.MINUTES))
                .retryOnConnectionFailure(config.isRetry())
                .addInterceptor(interceptor)
                .followRedirects(config.isFollowRedirects())
                .sslSocketFactory(SSLSocketFactoryManager.getSSLSocketFactory(config.isVerifySSLCertificate(),
                config.getCertificatePath(), config.getKeyStorePath(), config.getKeyStorePass()),
                new SSLSocketFactoryManager.SSLSocketFactoryHandler());
        return builder.build();
    }

    public OkHttpClient build() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(HttpConstant.CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(HttpConstant.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(HttpConstant.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(HttpConstant.MAX_IDLE_CONNECTIONS, HttpConstant.KEEPALIVE_DURATION, TimeUnit.MINUTES))
                .retryOnConnectionFailure(false)
                .addInterceptor(interceptor)
                .followRedirects(true);
        return builder.build();
    }

}
