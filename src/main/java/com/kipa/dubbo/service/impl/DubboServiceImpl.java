package com.kipa.dubbo.service.impl;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kipa.dubbo.excute.DubboRequest;
import com.kipa.dubbo.excute.DubboResponse;
import com.kipa.dubbo.service.DubboService;
import com.kipa.dubbo.service.base.BaseDubboService;
import com.kipa.utils.PreCheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/3
 */
@Service("dubboService")
public class DubboServiceImpl implements DubboService {

    @Autowired
    private BaseDubboService baseDubboService;

    @Override
    public String invoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair) {
        return invoke(interfaceName, methodName, typeAndValuePair, null);
    }

    @Override
    public String asyncInvoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair, ResponseCallback responseCallback) {
        return asyncInvoke(interfaceName, methodName, typeAndValuePair, null, responseCallback);
    }

    @Override
    public String directedInvoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair, String directUrl) {
        return directedInvoke(interfaceName, methodName, typeAndValuePair, directUrl,null);
    }

    @Override
    public String invoke(final String interfaceName, final String methodName, final Map<String, Object> typeAndValuePair,String version) {
        DubboRequest dubboRequest = assembleDubboRequest(interfaceName, methodName, typeAndValuePair, version);
        DubboResponse dubboResponse = baseDubboService.invoke(dubboRequest);
        return parseDubboResponse(dubboResponse);
    }

    @Override
    public String asyncInvoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair,String version, ResponseCallback responseCallback) {
        PreCheckUtils.checkEmpty(responseCallback, "异步回调接口不能为空");
        DubboRequest dubboRequest = assembleDubboRequest(interfaceName,methodName, typeAndValuePair, version);
        dubboRequest.setCallback(responseCallback);
        DubboResponse dubboResponse = baseDubboService.asyncInvoke(dubboRequest);
        return parseDubboResponse(dubboResponse);
    }

    @Override
    public String directedInvoke(String interfaceName, String methodName,  Map<String, Object> typeAndValuePair, String directUrl, String version) {
        PreCheckUtils.checkEmpty(directUrl, "直连的URL不能为空");
        DubboRequest dubboRequest = assembleDubboRequest(interfaceName ,methodName, typeAndValuePair,version);
        dubboRequest.setDirectUrl(directUrl);
        DubboResponse dubboResponse = baseDubboService.directInvoke(dubboRequest);
        return parseDubboResponse(dubboResponse);
    }

    private DubboRequest assembleDubboRequest(String interfaceName, String methodName, Map<String, Object> typeAndValuePair, String version) {
        DubboRequest dubboRequest = new DubboRequest();
        PreCheckUtils.checkEmpty(interfaceName, "dubbo接口的全路径名不能为空");
        PreCheckUtils.checkEmpty(methodName, "接口对应的方法不能为空");
        dubboRequest.setInterfaceName(interfaceName);
        dubboRequest.setMethodName(methodName);
        dubboRequest.setClassValuePair(typeAndValuePair);
        dubboRequest.setVersion(version);
        return dubboRequest;
    }

    private String parseDubboResponse(DubboResponse dubboResponse) {
        return JSON.toJSONString(dubboResponse);
    }

    @Override
    public DubboResponse invoke(DubboRequest request) {
        checkRequest(request);
        return baseDubboService.invoke(request);
    }

    @Override
    public DubboResponse asyncInvoke(DubboRequest request,  ResponseCallback responseCallback) {
        checkRequest(request);
        request.setCallback(responseCallback);
        return baseDubboService.asyncInvoke(request);
    }

    @Override
    public DubboResponse directedInvoke(DubboRequest request) {
        checkRequest(request);
        return baseDubboService.directInvoke(request);
    }

    private void checkRequest(DubboRequest dubboRequest) {
        PreCheckUtils.checkEmpty(dubboRequest, "dubbo请求的对象不能为空");
        PreCheckUtils.checkEmpty(dubboRequest.getInterfaceName(), "dubbo接口的全路径名不能为空");
        PreCheckUtils.checkEmpty(dubboRequest.getMethodName(),"接口对应的方法不能为空");
    }
}
