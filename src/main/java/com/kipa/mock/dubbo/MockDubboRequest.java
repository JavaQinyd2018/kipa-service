package com.kipa.mock.dubbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Qinyadong
 * @date: 2019/5/29 14:41
 * mock dubbo接口的请求类
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class MockDubboRequest {

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
    private Map<String, Object> classValuePair;

    public static MockDubboRequestBuilder builder() {
        return new MockDubboRequestBuilder();
    }

    public static class MockDubboRequestBuilder{

        private String interfaceName;

        private String methodName;

        private Map<String, Object> classValuePair = new ConcurrentHashMap<>();

        public MockDubboRequestBuilder interfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
            return this;
        }

        public  MockDubboRequestBuilder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public MockDubboRequestBuilder classValuePair(Map<String, Object> classValuePair) {
            this.classValuePair = classValuePair;
            return this;
        }

        public MockDubboRequestBuilder add(String paramTypeName,Object paramValue) {
            this.classValuePair.put(paramTypeName, paramValue);
            return this;
        }

        public MockDubboRequest build() {
            return new MockDubboRequest(this.interfaceName, this.methodName, this.classValuePair);
        }
    }
}
