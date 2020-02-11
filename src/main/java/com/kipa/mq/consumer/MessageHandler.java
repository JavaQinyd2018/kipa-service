package com.kipa.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kipa.common.KipaProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/27
 */
@Slf4j
public class MessageHandler {

    private MessageHandler() {}

    public static void handleMessage(SubscribeConfig subscribeConfig, List<MessageExt> list) {
        list.forEach(messageExt -> {
            String tags = messageExt.getTags();
            Map<Method, Class<?>> methodMap = subscribeConfig.getMethodMap();
            Map<String, Method> tagsMap = subscribeConfig.getTags();
            if (StringUtils.equalsIgnoreCase("*", tags)) {
                if (MapUtils.isNotEmpty(methodMap)) {
                    methodMap.forEach((method, messageType) -> {
                        Parameter[] parameters = method.getParameters();
                        if (ArrayUtils.isEmpty(parameters) || parameters.length > 1) {
                            throw new KipaProcessException("用@Subcribe标注的方法必须要有入参而且只能有一个入参");
                        }
                        //根据具体消息类型调用目标方法
                        invoke(method, messageType, messageExt);
                    });
                }
            }else {
                Method method = tagsMap.get(tags);
                if (method != null) {
                    Class<?> messageType = methodMap.get(method);
                    invoke(method, messageType, messageExt);
                }else {
                    throw new KipaProcessException("未找到相应tag的方法");
                }
            }
        });
    }

    /**
     * 根据消息类型调用目标方法
     * @param method
     * @param messageType
     * @param messageExt
     */
    private static void invoke(Method method, Class<?> messageType, MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        Class<?> clazz = method.getDeclaringClass();
        try {
            if (messageType.isAssignableFrom(String.class)) {
                ReflectionUtils.invokeMethod(method, clazz.newInstance(), new String(body, StandardCharsets.UTF_8));
            } else if (messageType.isAssignableFrom(MessageExt.class)) {
                ReflectionUtils.invokeMethod(method, clazz.newInstance(), messageExt);
            } else {
                ReflectionUtils.invokeMethod(method, clazz.newInstance(), JSONObject.parseObject(new String(body, StandardCharsets.UTF_8), messageType));
            }
        } catch (Exception e) {
            throw new KipaProcessException("方法"+method.getName()+"执行失败.",e);
        }
    }


    public static void handleException(final Exception e) {
        if (e instanceof KipaProcessException) {
            log.error("调用执行：{}",e.getMessage());
        }else if (e instanceof UnsupportedOperationException){
            log.error("出现异常：{}",e);
        }
    }
}
