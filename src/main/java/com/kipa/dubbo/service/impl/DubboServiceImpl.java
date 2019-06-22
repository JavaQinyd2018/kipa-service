package com.kipa.dubbo.service.impl;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kipa.dubbo.entity.DubboRequest;
import com.kipa.dubbo.entity.DubboResponse;
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
    public List<Map<String, Object>> invoke(final String interfaceName, final String methodName, final Map<String, Object> typeAndValuePair) {
        DubboRequest dubboRequest = assembleDubboRequest(interfaceName, methodName, typeAndValuePair);
        DubboResponse dubboResponse = baseDubboService.invoke(dubboRequest);
        return parseDubboResponse(dubboResponse);
    }

    @Override
    public List<Map<String, Object>> asyncInvoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair, ResponseCallback responseCallback) {
        PreCheckUtils.checkEmpty(responseCallback, "异步回调接口不能为空");
        DubboRequest dubboRequest = assembleDubboRequest(interfaceName,methodName, typeAndValuePair);
        dubboRequest.setResponseCallback(responseCallback);
        DubboResponse dubboResponse = baseDubboService.asyncInvoke(dubboRequest);
        return parseDubboResponse(dubboResponse);
    }

    @Override
    public List<Map<String, Object>> directedInvoke(String interfaceName, String methodName,  Map<String, Object> typeAndValuePair) {
        DubboRequest dubboRequest = assembleDubboRequest(interfaceName ,methodName, typeAndValuePair);
        DubboResponse dubboResponse = baseDubboService.directInvoke(dubboRequest);
        return parseDubboResponse(dubboResponse);
    }

    private DubboRequest assembleDubboRequest(String interfaceName, String methodName, Map<String, Object> typeAndValuePair) {
        DubboRequest dubboRequest = new DubboRequest();
        PreCheckUtils.checkEmpty(interfaceName, "dubbo接口的全路径名不能为空");
        PreCheckUtils.checkEmpty(methodName, "接口对应的方法不能为空");
        dubboRequest.setInterfaceName(interfaceName);
        dubboRequest.setMethodName(methodName);
        dubboRequest.setClassValuePair(typeAndValuePair);
        return dubboRequest;
    }

    private List<Map<String, Object>> parseDubboResponse(DubboResponse dubboResponse) {
        List<Map<String, Object>> mapList = Lists.newArrayList();

        if (dubboResponse == null) {
            return mapList;
        }

        Object responseData = dubboResponse.getResponseData();

        if (responseData == null) {
            return mapList;
        }

        String json = JSON.toJSONString(responseData);
        if (StringUtils.startsWith(json, "{")) {
            Map map = JSONObject.parseObject(json, Map.class);
            mapList.add(map);
        }else if (StringUtils.startsWith(json, "[")) {
            List<Map> maps = JSONArray.parseArray(json, Map.class);
            maps.forEach(mapList::add);
        }
        return mapList;
    }

    @Override
    public DubboResponse invoke(DubboRequest request) {
        checkRequest(request);
        return baseDubboService.invoke(request);
    }

    @Override
    public DubboResponse asyncInvoke(DubboRequest request,  ResponseCallback responseCallback) {
        checkRequest(request);
        request.setResponseCallback(responseCallback);
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
