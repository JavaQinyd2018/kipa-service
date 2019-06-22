package com.kipa.mock.http.service.bo;

import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MockResponse {

    private Map<String, String> cookies;
    /**
     * 相应的header
     */
    private Map<String, String> headers;
    /**
     * 相应的结果体
     */
    private String body;
    /**
     * 结果原因
     */
    private String reasonPhrase;
    /**
     * 结果状态码
     */
    private Integer statusCode;
    /**
     * 延迟时长
     */
    private Long delay;

    public static MockResponse.MockResponseBuilder builder() {
        return new MockResponse.MockResponseBuilder();
    }

    public static class MockResponseBuilder {
        private Map<String, String> cookies = new ConcurrentHashMap<>();
        private Map<String, String> headers = new ConcurrentHashMap<>();
        private String body;
        private String reasonPhrase;
        private Integer statusCode;
        private Long delay;

        MockResponseBuilder() {
        }

        public MockResponse.MockResponseBuilder addCookie(String name, String value) {
            this.cookies.put(name, value);
            return this;
        }

        public MockResponse.MockResponseBuilder cookies(Map<String, String> cookies) {
            this.cookies = cookies;
            return this;
        }

        public MockResponse.MockResponseBuilder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public MockResponse.MockResponseBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public MockResponse.MockResponseBuilder body(String body) {
            this.body = body;
            return this;
        }

        public MockResponse.MockResponseBuilder reasonPhrase(String reasonPhrase) {
            this.reasonPhrase = reasonPhrase;
            return this;
        }

        public MockResponse.MockResponseBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public MockResponse.MockResponseBuilder delay(Long delay) {
            this.delay = delay;
            return this;
        }

        public MockResponse build() {
            return new MockResponse(this.cookies, this.headers, this.body, this.reasonPhrase, this.statusCode, this.delay);
        }

        @Override
        public String toString() {
            return "MockResponse.MockResponseBuilder(cookies=" + this.cookies + ", headers=" + this.headers + ", body=" + this.body + ", reasonPhrase=" + this.reasonPhrase + ", statusCode=" + this.statusCode + ", delay=" + this.delay + ")";
        }
    }
}
