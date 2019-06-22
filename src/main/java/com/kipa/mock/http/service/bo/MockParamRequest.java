package com.kipa.mock.http.service.bo;

import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/8
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class MockParamRequest {
    private String path;
    private String method;
    private Map<String, String> cookies ;
    private Map<String, String> headers ;
    private boolean secure;
    private Map<String, String> requestParams;

    public static MockParamRequest.MockParamRequestBuilder builder() {
        return new MockParamRequest.MockParamRequestBuilder();
    }

    public static class MockParamRequestBuilder {
        private String path;
        private String method;
        private Map<String, String> cookies = new ConcurrentHashMap<>();
        private Map<String, String> headers = new ConcurrentHashMap<>();
        private boolean secure;
        private Map<String, String> requestParams = new ConcurrentHashMap<>();

        MockParamRequestBuilder() {
        }

        public MockParamRequest.MockParamRequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        public MockParamRequest.MockParamRequestBuilder method(String method) {
            this.method = method;
            return this;
        }

        public MockParamRequest.MockParamRequestBuilder addCookie(String name, String value) {
            this.cookies.put(name, value);
            return this;
        }

        public MockParamRequest.MockParamRequestBuilder cookies(Map<String, String> cookies) {
            this.cookies = cookies;
            return this;
        }

        public MockParamRequest.MockParamRequestBuilder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public MockParamRequest.MockParamRequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public MockParamRequest.MockParamRequestBuilder secure(boolean secure) {
            this.secure = secure;
            return this;
        }
        public MockParamRequest.MockParamRequestBuilder addRequestParam(String paramName, String paramValue) {
            this.requestParams.put(paramName, paramValue);
            return this;
        }

        public MockParamRequest.MockParamRequestBuilder requestParams(Map<String, String> requestParams) {
            this.requestParams = requestParams;
            return this;
        }

        public MockParamRequest build() {
            return new MockParamRequest(this.path, this.method, this.cookies, this.headers, this.secure, this.requestParams);
        }
    }
}
