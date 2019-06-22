package com.kipa.mock.http.entity;

import com.kipa.http.emuns.HttpSendMethod;
import com.kipa.mock.http.annotation.MockMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 14:44
 * mock请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMockRequest {

    private Map<String, String> cookies;
    private Map<String, String> headers;
    private String body;
    private Charset charset;
    private String path;
    private Map<String, String> requestParams;
    private boolean secure;
    private MockMethod mockMethod;
    private HttpSendMethod httpSendMethod;
}
