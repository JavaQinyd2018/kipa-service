package com.kipa.http.service.convert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.kipa.common.KipaProcessException;
import com.kipa.http.core.HeaderConstant;
import com.kipa.http.core.HttpHeadConstant;
import com.kipa.http.core.HttpResponse;
import com.kipa.http.exception.HttpProcessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
@Slf4j
@Component
public class ResponseConvert implements Convert<Response, HttpResponse> {

    private static final String JSON_OBJECT_FLAG = "{";
    private static final String JSON_ARRAY_FLAG = "[";

    @Override
    public HttpResponse convert(Response response) {

        HttpResponse httpResponse = new HttpResponse();
        //1.如果返回的response为空则返回null
        if (response != null) {
            log.debug("okhttp响应的结果是：{}",response.toString());
            if (response.isSuccessful()) {
                httpResponse.setSuccess(true);
                Headers headers = response.headers();
                httpResponse.setHeaderMap(headers.toMultimap());
                Map<String,Object> map = Maps.newLinkedHashMap();
                if (response.body() != null) {
                    String data = "";
                    try {
                        data = response.body().string();
                    } catch (Exception e) {
                        throw new HttpProcessException(e);
                    }
                    String contentType = headers.get(HttpHeadConstant.CONTENT_TYPE);
                    log.debug("请求返回的文件内容类型为：{}",contentType);
                    if (StringUtils.contains(contentType, HeaderConstant.JSON)) {
                        if (data.startsWith(JSON_OBJECT_FLAG)) {
                            map.put("data", JSONObject.parseObject(data));
                            map.put("dataType", "JSONObject");
                        }else if (data.startsWith(JSON_ARRAY_FLAG)) {
                            map.put("data", JSONArray.parseArray(data));
                            map.put("dataType","JSONArray" );
                        }
                        httpResponse.setJsonFormat(true);
                    }else {
                        map.put("data",data);
                        httpResponse.setJsonFormat(false);
                    }
                    map.put("contentLength",response.body().contentLength());
                }
                Map<String, Object> requestMap = Maps.newHashMap();
                requestMap.put("url",response.request().url().url());
                requestMap.put("headers",response.request().headers());
                httpResponse.setRequestInfo(requestMap);
                httpResponse.setBodyMap(map);
                httpResponse.setCode(response.code());
                httpResponse.setMessage(ResponseMessage.SUCCESS.getMessage());
                return httpResponse;
            }else {
                int code = response.code();
                String message = ResponseMessage.getMessage(code);
                String error = "";
                try {
                    if (response.body() != null) {
                        error = response.body().string();
                    }
                } catch (Exception e) {
                    throw new HttpProcessException(e);
                }
                throw new HttpProcessException("http调用失败，错误信息：" + message + "。服务端详细返回信息：\n" +error);
            }
        }
        return httpResponse;
    }

    public HttpResponse convertDownloadFile(Response response, String localTargetDir) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setSuccess(false);
        if (response != null && response.isSuccessful()) {
            Headers headers = response.headers();
            httpResponse.setHeaderMap(headers.toMultimap());
            if (response.body() != null) {
                InputStream inputStream = response.body().byteStream();
                File localDir = new File(localTargetDir);
                if (!localDir.exists() && !localDir.mkdirs()) {
                    throw new KipaProcessException("创建目录："+localTargetDir+"失败了");
                }
                FileWriter writer = null;
                try {
                    writer = new FileWriter(localDir);
                    IOUtils.copy(inputStream, writer, Charset.forName("UTF-8"));
                    httpResponse.setSuccess(true);
                    httpResponse.setJsonFormat(false);
                    return httpResponse;
                } catch (Exception e) {
                    log.error("文件解析失败，错误原因是：{}",e);
                }finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                           log.error("释放资源失败：{}",e);
                        }
                    }
                }
            }
        }
        return httpResponse;
    }

    @Getter
    @AllArgsConstructor
    enum ResponseMessage{

        /**
         * 成功
         */
        SUCCESS(200, "请求响应成功"),

        /**
         * 重定向
         */
        REDIRECT(302, "重定向跳转"),

        /**
         * 请求错误
         */
        REQUEST_ERROR(400, "客户端请求语法错误或者参数错误，服务器不能识别"),

        /**
         * 权限不允许
         */
        PERMISSION_REFUSED(403, "服务器接收到请求，但是认证失败，拒绝提供服务"),

        /**
         * 资源不存在
         */
        SERVICE_NOT_FOUND(404, "请求资源不存在"),

        /**
         * 请求方法错误
         */
        METHOD_NOT_SUPPORTED(405,"当前http请求中的请求方法不支持"),
        /**
         * 系统错误
         */
        SYSTEM_ERROR(500, "服务器系统内部错误");

        private Integer code;
        private String message;

        public static String getMessage(Integer code) {
            ResponseMessage[] values = ResponseMessage.values();
            for (ResponseMessage value : values) {
                if (value.getCode().compareTo(code) == 0) {
                    return value.getMessage();
                }
            }
            throw new IllegalArgumentException("该错误码不存在");
        }
    }
}
