package com.kipa.http.service.convert;

import com.alibaba.fastjson.JSONObject;
import com.kipa.common.KipaProcessException;
import com.kipa.common.core.Convertor;
import com.kipa.http.core.HeaderConstant;
import com.kipa.http.core.HttpRequest;
import com.kipa.http.emuns.HttpSendMethod;
import com.kipa.http.emuns.RequestType;
import com.kipa.http.exception.HttpProcessException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
@Component
@Slf4j
public class RequestConvert implements Convertor<HttpRequest, Request> {

    @Override
    public Request convert(HttpRequest httpRequest) {
        if (httpRequest == null) {
            throw new HttpProcessException("http的请求不能为空");
        }
        Request.Builder builder = new Request.Builder();
        //+url
        builder.url(httpRequest.getUrl());
        Map<String, String> headerMap = httpRequest.getHeaderMap();
        if (MapUtils.isNotEmpty(headerMap)) {
            //+headers
            headerMap.forEach(builder::addHeader);
        }
       //根据请求类型处理
        RequestType requestType = httpRequest.getType();
        if (requestType == null) {
            throw new HttpProcessException("http的请求类型不能为空");
        }
        switch (requestType) {
            case PARAMETER:
                if (httpRequest.getMethod().equalsIgnoreCase(HttpSendMethod.GET.getName())) {
                    builder.get();
                }else {
                    final Map<String, String> bodyMap = httpRequest.getBodyMap();
                    if (MapUtils.isNotEmpty(bodyMap)) {
                        FormBody.Builder formBodyBuilder = new FormBody.Builder();
                        bodyMap.forEach(formBodyBuilder::add);
                        FormBody formBody = formBodyBuilder.build();
                        builder.method(httpRequest.getMethod(), formBody);
                    }
                }
                break;
            case JSON:
                MediaType mediaType = MediaType.parse(String.format("%s; charset=%s",HeaderConstant.APP_FORM_URLENCODED, HeaderConstant.CONTENT_CHARSET_UTF8));
                String json = httpRequest.getJson();
                RequestBody requestBody = RequestBody.create(mediaType, json);
                if (StringUtils.startsWith(json, "{") && StringUtils.endsWith(json, "}") ||
                        StringUtils.startsWith(json, "[") && StringUtils.endsWith(json, "]")) {
                    MediaType mediaTypeJSON = MediaType.parse(String.format("%s; charset=%s",HeaderConstant.JSON, HeaderConstant.CONTENT_CHARSET_UTF8));
                    requestBody = RequestBody.create(mediaTypeJSON, json);
                }
                HttpSendMethod httpSendMethod = HttpSendMethod.valueOf(httpRequest.getMethod());
                builder.method(httpSendMethod.getName(),requestBody);
                break;
            case FILE:
                Map<String, String> fileMap = httpRequest.getFileMap();
                String method = fileMap.get("method");
                if (StringUtils.equalsIgnoreCase(method, HttpSendMethod.UPLOAD.getName())) {
                    String type = fileMap.get("mediaType");
                    String filePath = fileMap.get("filePath");
                    String fileParamJson = fileMap.get("fileParamJson");
                    //带参数的文件上传
                    RequestBody fileBody = RequestBody.create(MediaType.parse(type), new File(filePath));
                    if (StringUtils.isNotBlank(fileParamJson)) {
                        MultipartBody.Builder fileBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                        Map<String, String> map = null;
                        try {
                            map = JSONObject.parseObject(fileParamJson, Map.class);
                        } catch (Exception e) {
                            throw new KipaProcessException("文件参数Json转化为map失败",e);
                        }
                        map.forEach(fileBuilder::addFormDataPart);
                        fileBuilder.addPart(fileBody);
                        builder.method(HttpSendMethod.POST.getName(), fileBuilder.build());
                    }else {
                        builder.method(HttpSendMethod.POST.getName(), fileBody);
                    }
                }else if (StringUtils.equalsIgnoreCase(method, HttpSendMethod.DOWNLOAD.getName())) {
                    //文件下载为get请求
                    builder.get();
                }
                break;
                default:
                    break;
        }
        Request request = builder.build();
        log.debug("okhttp请求的入参为：{}",request.toString());
        return request;
    }

}


