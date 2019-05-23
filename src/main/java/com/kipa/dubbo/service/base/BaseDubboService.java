package com.kipa.dubbo.service.base;

import com.kipa.dubbo.entity.DubboResponse;
import com.kipa.dubbo.enums.InvokeType;
import com.kipa.dubbo.annotation.Style;
import com.kipa.dubbo.entity.DubboRequest;
import com.kipa.log.SystemLog;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 16:28
 * dubbo调用的底层接口，直接和
 */
public interface BaseDubboService {

    /**
     * 调用，请求provider暴露的服务
     * @param dubboRequest
     * @return
     */
    @SystemLog(value = "dubbo同步接口调用")
    @Style(type = InvokeType.SYNCHRONOUS)
    DubboResponse invoke(DubboRequest dubboRequest);

    /**
     * dubbo接口异步调用
     * @param dubboRequest
     * @return
     */
    @SystemLog(value = "dubbo异步接口调用")
    @Style(type = InvokeType.ASYNCHRONOUS)
    DubboResponse asyncInvoke(DubboRequest dubboRequest);

    /**
     * 直连方式方式
     * @param dubboRequest
     * @return
     */
    @SystemLog(value = "dubbo直连接口调用")
    @Style(type = InvokeType.DIRECTED_LINK)
    DubboResponse directInvoke(DubboRequest dubboRequest);
}
