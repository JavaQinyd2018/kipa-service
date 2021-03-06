package com.kipa.redis;

import com.kipa.common.KipaProcessException;
import com.kipa.config.EnableRedis;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: Qinyadong
 * @date: 2019/4/24 15:24
 * 单机redis开启配置条件
 */
public class StandAloneRedisCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        AtomicBoolean flag = new AtomicBoolean(false);
        ConfigurableListableBeanFactory beanFactory = conditionContext.getBeanFactory();
        assert beanFactory != null;
        Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(EnableRedis.class);
        if (MapUtils.isNotEmpty(beansWithAnnotation)) {
            if (beansWithAnnotation.size() > 1) {
                throw new KipaProcessException("只允许在主配置类上面配置@EnableRedis注解,只允许开启一次");
            }
            beansWithAnnotation.forEach((s, o) -> {
                Class<?> clazz = o.getClass();
                EnableRedis enableRedis = AnnotationUtils.findAnnotation(clazz, EnableRedis.class);
                flag.set(enableRedis != null && enableRedis.model() == RedisModel.STAND_ALONE);
            });
        }
        return flag.get();
    }
}
