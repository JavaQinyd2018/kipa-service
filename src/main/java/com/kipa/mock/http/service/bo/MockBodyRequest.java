package com.kipa.mock.http.service.bo;

import lombok.*;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/8
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class MockBodyRequest {
    private String path;
    private String method;
    private Map<String, String> cookies;
    private Map<String, String> headers;
    private boolean secure;
    private String body;
    private Charset charset;

    public static MockBodyRequest.MockBodyRequestBuilder builder() {
        return new MockBodyRequest.MockBodyRequestBuilder();
    }

    public static class MockBodyRequestBuilder {
        private String path;
        private String method;
        private Map<String, String> cookies = new ConcurrentHashMap<>();
        private Map<String, String> headers = new ConcurrentHashMap<>();
        private boolean secure;
        private String body;
        private Charset charset;

        MockBodyRequestBuilder() {
        }

        public MockBodyRequest.MockBodyRequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        public MockBodyRequest.MockBodyRequestBuilder method(String method) {
            this.method = method;
            return this;
        }

        public MockBodyRequest.MockBodyRequestBuilder addCookie(String name, String value) {
            this.cookies.put(name, value);
            return this;
        }

        public MockBodyRequest.MockBodyRequestBuilder cookies(Map<String, String> cookies) {
            this.cookies = cookies;
            return this;
        }

        public MockBodyRequest.MockBodyRequestBuilder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public MockBodyRequest.MockBodyRequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public MockBodyRequest.MockBodyRequestBuilder secure(boolean secure) {
            this.secure = secure;
            return this;
        }

        public MockBodyRequest.MockBodyRequestBuilder body(String body) {
            this.body = body;
            return this;
        }

        public MockBodyRequest.MockBodyRequestBuilder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public MockBodyRequest build() {
            return new MockBodyRequest(this.path, this.method, this.cookies, this.headers, this.secure, this.body, this.charset);
        }

    }
}
