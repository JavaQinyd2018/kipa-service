package com.kipa.dubbo.service;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.kipa.dubbo.excute.DubboRequest;
import com.kipa.dubbo.excute.DubboResponse;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/3
 */
public interface DubboService {

    /**
     *
     * @param interfaceName
     * @param methodName
     * @param typeAndValuePair
     * @return
     */
    String invoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair);

    /**
     * dubbo接口返回结果是一个对象或者List的时候用改方法简便，
     * 可以方便转化为Json统一提前处理结果为list或者map直接遍历，不用再另行处理
     * 同步调用
     * @param interfaceName
     * @param typeAndValuePair
     * @return
     */
    String invoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair,String version);

    /**
     * 如果是一个基础数据类型或者调用该结果失败，用该方法
     * 同步调用
     * @param request
     * @return
     */
    DubboResponse invoke(DubboRequest request);

    /**
     *
     * @param interfaceName
     * @param methodName
     * @param typeAndValuePair
     * @param responseCallback
     * @return
     */
    String asyncInvoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair, ResponseCallback responseCallback);

    /**
     *
     * @param interfaceName 接口名称
     * @param methodName 方法名称
     * @param typeAndValuePair 参数类型和参数值map
     * @param responseCallback 响应回调接口
     * @return
     */
    String asyncInvoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair, String version, ResponseCallback responseCallback);

    /**
     * 异步调用
     * @param request 异步请求参数
     * @param responseCallback 响应回调接口
     * @return
     */
    DubboResponse asyncInvoke(DubboRequest request,  ResponseCallback responseCallback);

    /**
     *
     * @param interfaceName
     * @param methodName
     * @param typeAndValuePair
     * @return
     */
    String directedInvoke(String interfaceName,String methodName, Map<String, Object> typeAndValuePair, String directUrl);
    /**
     * 直连调用
     * @param interfaceName
     * @param typeAndValuePair
     * @return
     */
    String directedInvoke(String interfaceName,String methodName, Map<String, Object> typeAndValuePair, String directUrl, String version);

    /**
     * 直连
     * @param request
     * @return
     */
    DubboResponse directedInvoke(DubboRequest request);

}
