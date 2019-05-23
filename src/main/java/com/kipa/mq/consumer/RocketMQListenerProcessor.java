package com.kipa.mq.consumer;

import com.kipa.config.EnableRocketMQ;
import com.kipa.utils.PackageScanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.testng.collections.Maps;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/26
 * 注解@RocketMQListener注解的解析器
 */
@Slf4j
@Component
public class RocketMQListenerProcessor implements ApplicationContextAware, InitializingBean {

    private volatile boolean initialized = false;

    private ApplicationContext applicationContext;
    private Map<RocketMQListener, Map<Method,Subscribe>> mqListenerMapMap;

    public Map<RocketMQListener, Map<Method, Subscribe>> getMqListenerMapMap() {
        if (!initialized) {
            log.warn("rocketMq没有消息消费监听配置");
        }
        return mqListenerMapMap;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableRocketMQ.class);
        if (MapUtils.isNotEmpty(beansWithAnnotation) ){
            if (beansWithAnnotation.size() > 1 ) {
                throw new RuntimeException("注解@EnableRocketMQ只允许被在配置了开启一次");
            }
            beansWithAnnotation.forEach((beanName, bean) -> {
                Class<?> beanClass = bean.getClass();
                EnableRocketMQ rocketMq = AnnotationUtils.findAnnotation(beanClass, EnableRocketMQ.class);
                if (rocketMq != null) {
                    String scanPackage = rocketMq.listenerScanPackage();
                    if (StringUtils.isBlank(scanPackage)) {
                        throw new RuntimeException("扫描注解@EnableRocketMQ的包路径不能为空");
                    }
                    mqListenerMapMap = init(scanPackage);
                }
            });
        }

        if (MapUtils.isNotEmpty(mqListenerMapMap)) {
            initialized = true;
        }
    }

    /**
     * 解析注解
     * @param basePackage 注解扫描带有@RocketMQListener的路径
     * @return 封装到一个map里面
     */
    private Map<RocketMQListener, Map<Method,Subscribe>> init(String basePackage) {
        Map<RocketMQListener, Map<Method,Subscribe>> mapMap = Maps.newHashMap();
        Set<Class<?>> classWithAnnotation = PackageScanUtils.getClassWithAnnotationSet(basePackage, RocketMQListener.class);
        if (CollectionUtils.isNotEmpty(classWithAnnotation)) {
            classWithAnnotation.forEach(clazz -> {
                RocketMQListener rocketMQListener = AnnotationUtils.findAnnotation(clazz, RocketMQListener.class);
                Map<Method, Subscribe> withAnnotationMap = PackageScanUtils.getMethodWithAnnotationMap(clazz, Subscribe.class);
                mapMap.put(rocketMQListener, withAnnotationMap);
            });
        }
        return mapMap;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
