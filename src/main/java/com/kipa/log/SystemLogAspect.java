package com.kipa.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: Yadong Qin
 * @Date: 2018/11/30
 */
@Aspect
@Slf4j
@Component
public class SystemLogAspect {

    @Before("@annotation(com.kipa.log.SystemLog)")
    public void before(JoinPoint joinPoint) {
        SystemLog systemLog = getSystemLog(joinPoint);
        log.info("【{}】:请求参数是：【{}】",systemLog.value(),joinPoint.getArgs());
    }

    @AfterReturning(value = "@annotation(com.kipa.log.SystemLog)",returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        SystemLog systemLog = getSystemLog(joinPoint);
        log.info("【{}】:响应的结果是：【{}】",systemLog.value(),result);
    }

    @AfterThrowing(value = "@annotation(com.kipa.log.SystemLog)",throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Throwable exception) {
        SystemLog systemLog = getSystemLog(joinPoint);
        log.error("【{}】：接口执行错误抛出异常：{}",systemLog.value(),exception);
    }

    private static SystemLog getSystemLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();
        return method.getAnnotation(SystemLog.class);
    }
}
