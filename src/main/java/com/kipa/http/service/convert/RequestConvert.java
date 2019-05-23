package com.kipa.http.service.convert;

import com.kipa.http.core.HttpRequest;
import com.kipa.http.emuns.HttpSendMethod;
import com.kipa.http.emuns.RequestType;
import com.kipa.http.exception.HttpProcessException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
@Component
@Slf4j
public class RequestConvert implements Convert<HttpRequest, Request> {

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
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(mediaType, httpRequest.getJson());
                HttpSendMethod httpSendMethod = HttpSendMethod.valueOf(httpRequest.getMethod());
                builder.method(httpSendMethod.getName(),requestBody);
                break;
            case FILE:
                Map<String, String> fileMap = httpRequest.getFileMap();
                String fileType = fileMap.get("fileType");
                String type = fileMap.get("mediaType");
                String filePath = fileMap.get("filePath");
                String fileName = fileMap.get("fileName");
                //带参数的文件上传
                RequestBody fileBody = RequestBody.create(MediaType.parse(type), new File(filePath));
                RequestBody fileRequestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type",fileType)
                        .addFormDataPart("file",fileName, fileBody)
                        .build();
                builder.method(HttpSendMethod.POST.getName(), fileRequestBody);
                break;
                default:
                    break;
        }
        Request request = builder.build();
        log.debug("okhttp请求的入参为：{}",request.toString());
        return request;
    }

}


