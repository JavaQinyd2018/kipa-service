package com.kipa.dubbo.entity;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 16:30
 * dubbo请求的包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class DubboRequest {

    /**
     * dubbo接口名称
     */
    private String interfaceName;
    /**
     * dubbo方法名称
     */
    private String methodName;
    /**
     * dubbo参数类型名称和值键值对：
     * 例如:String say(String word);
     * map.put("java.lang.String","hello world")
     */
    private Map<String, Object> classValuePair = new ConcurrentHashMap<>();

    /**
     * 异步回调的接口
     */
    private ResponseCallback responseCallback;
    /**
     * 构建器
     * @return
     */
    public static DubboRequestBuilder builder() {
        return new DubboRequestBuilder();
    }

    public static class DubboRequestBuilder{

        private String interfaceName;
        private String methodName;
        private Map<String, Object> classValuePair = new ConcurrentHashMap<>();
        private ResponseCallback responseCallback;

        public DubboRequestBuilder interfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
            return this;
        }

        public  DubboRequestBuilder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public DubboRequestBuilder classValuePair(Map<String, Object> classValuePair) {
            this.classValuePair = classValuePair;
            return this;
        }

        public DubboRequestBuilder add(String paramTypeName,Object paramValue) {
            this.classValuePair.put(paramTypeName, paramValue);
            return this;
        }

        public DubboRequestBuilder responseCallback(ResponseCallback responseCallback) {
            this.responseCallback = responseCallback;
            return this;
        }

        public DubboRequest build() {
            return new DubboRequest(this.interfaceName, this.methodName, this.classValuePair,this.responseCallback);
        }
    }
}
