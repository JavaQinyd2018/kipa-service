package com.kipa.dubbo.excute;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.kipa.core.AsyncInvokeRequest;
import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 16:30
 * dubbo请求的包装类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public final class DubboRequest extends AsyncInvokeRequest<ResponseCallback> {

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
    private Multimap<String, Object> classValuePair = ArrayListMultimap.create();

    /**
     * 直连URL
     */
    private String directUrl;
    /**
     * 请求的版本号
     */
    private String version;

    /**
     * 构建器
     * @return
     */
    public DubboRequest(String interfaceName, String methodName, Multimap<String, Object> classValuePair, String directUrl, String version, ResponseCallback responseCallback) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.classValuePair = classValuePair;
        this.version = version;
        this.callback = responseCallback;
        this.directUrl = directUrl;
    }
    public static DubboRequestBuilder builder() {
        return new DubboRequestBuilder();
    }

    public static class DubboRequestBuilder{

        private String interfaceName;
        private String methodName;
        private Multimap<String, Object> classValuePair = ArrayListMultimap.create();
        private ResponseCallback responseCallback;
        private String directUrl;
        private String version;

        public DubboRequestBuilder interfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
            return this;
        }

        public  DubboRequestBuilder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public DubboRequestBuilder classValuePair(Multimap<String, Object> classValuePair) {
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

        public DubboRequestBuilder directUrl(String directUrl) {
            this.directUrl = directUrl;
            return this;
        }
        public DubboRequestBuilder version(String  version) {
            this.version = version;
            return this;
        }
        public DubboRequest build() {
            return new DubboRequest(this.interfaceName, this.methodName, this.classValuePair,this.directUrl, this.version, this.responseCallback);
        }
    }
}
