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
     * dubbo接口返回结果是一个对象或者List的时候用改方法简便，
     * 可以方便转化为Json统一提前处理结果为list或者map直接遍历，不用再另行处理
     * 同步调用
     * @param interfaceName
     * @param typeAndValuePair
     * @return
     */
    List<Map<String, Object>> invoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair);

    /**
     * 如果是一个基础数据类型或者调用该结果失败，用该方法
     * 同步调用
     * @param request
     * @return
     */
    DubboResponse invoke(DubboRequest request);


    /**
     *
     * @param interfaceName 接口名称
     * @param methodName 方法名称
     * @param typeAndValuePair 参数类型和参数值map
     * @param responseCallback 响应回调接口
     * @return
     */
    List<Map<String, Object>> asyncInvoke(String interfaceName, String methodName, Map<String, Object> typeAndValuePair, ResponseCallback responseCallback);

    /**
     * 异步调用
     * @param request 异步请求参数
     * @param responseCallback 响应回调接口
     * @return
     */
    DubboResponse asyncInvoke(DubboRequest request,  ResponseCallback responseCallback);
    /**
     * 直连调用
     * @param interfaceName
     * @param typeAndValuePair
     * @return
     */
    List<Map<String, Object>> directedInvoke(String interfaceName,String methodName, Map<String, Object> typeAndValuePair);

    /**
     * 直连
     * @param request
     * @return
     */
    DubboResponse directedInvoke(DubboRequest request);

}
