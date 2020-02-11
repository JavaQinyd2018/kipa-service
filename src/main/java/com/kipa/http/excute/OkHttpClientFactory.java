package com.kipa.http.excute;

import com.kipa.core.ClientFactory;
import com.kipa.http.ssl.SSLSocketFactoryManager;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


import java.util.concurrent.TimeUnit;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * OkhttpClient生成器
 */
public class OkHttpClientFactory implements ClientFactory<OkHttpClient, OkHttpClientProperties> {

    @Override
    public OkHttpClient create(OkHttpClientProperties properties) throws Exception {
       return build(properties);
    }

    /**
     * 构建一个OkHttpClient
     * @param properties s属性配置文件
     * @return 创建一个OkClient对象， 用于调用http
     */
    private OkHttpClient build(OkHttpClientProperties properties) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (properties != null) {
            //1. 如果有属性配置文件的配置，怎根据配置文件的信息配置
            builder.connectTimeout(properties.getConnectTimeout() != 0 ? properties.getConnectTimeout(): HttpConstant.CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(properties.getReadTimeout() != 0 ? properties.getReadTimeout() : HttpConstant.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(properties.getWriteTimeout() != 0 ? properties.getWriteTimeout() : HttpConstant.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(properties.getMaxIdleConnections() == 0 ? properties.getMaxIdleConnections() : HttpConstant.MAX_IDLE_CONNECTIONS,
                            properties.getKeepAliveDuration() == 0 ? properties.getKeepAliveDuration() : HttpConstant.KEEPALIVE_DURATION, TimeUnit.MINUTES))
                    .retryOnConnectionFailure(properties.isRetry())
                    .addInterceptor(interceptor)
                    .followRedirects(properties.isFollowRedirects())
                    .sslSocketFactory(SSLSocketFactoryManager.getSSLSocketFactory(properties.isVerifySSLCertificate(),
                            properties.getCertificatePath(), properties.getKeyStorePath(), properties.getKeyStorePass()),
                            new SSLSocketFactoryManager.SSLSocketFactoryHandler());
        }else {
            //2. 如果没有就使用默认的配置
            builder.connectTimeout(HttpConstant.CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(HttpConstant.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(HttpConstant.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(HttpConstant.MAX_IDLE_CONNECTIONS, HttpConstant.KEEPALIVE_DURATION, TimeUnit.MINUTES))
                    .retryOnConnectionFailure(false)
                    .addInterceptor(interceptor)
                    .followRedirects(true);
        }

        return builder.build();
    }

}
