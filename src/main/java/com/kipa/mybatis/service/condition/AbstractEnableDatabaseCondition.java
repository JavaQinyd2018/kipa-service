package com.kipa.mybatis.service.condition;

import com.kipa.config.EnableMultipleDataSource;
import com.kipa.env.DatasourceEnvHolder;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/6/25 18:19
 * @since: 数据库条件处理的工具类
 */
public abstract class AbstractEnableDatabaseCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        ConfigurableListableBeanFactory beanFactory = conditionContext.getBeanFactory();
        if (beanFactory != null) {
            Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(EnableMultipleDataSource.class);
            //1. 如果有EnableMultipleDataSource多数据源注解，你们数据源信息由注解控制开启，以及开启的数据源个数
            if (MapUtils.isNotEmpty(beansWithAnnotation)) {
                Object value = beansWithAnnotation.entrySet().iterator().next().getValue();
                EnableMultipleDataSource annotation = AnnotationUtils.findAnnotation(value.getClass(), EnableMultipleDataSource.class);
                if (annotation != null) {
                    EnvFlag[] env = annotation.env();
                    if (ArrayUtils.isNotEmpty(env)) {
                        return matchDatasource(Arrays.asList(env));
                    }
                }
            }else {
                //2. 如果没有注解，你们从环境中尝试获取数据源环境标识，开启数据源配置
                EnvFlag[] env = DatasourceEnvHolder.getEnv();
                if (ArrayUtils.isNotEmpty(env)) {
                    return matchDatasource(Arrays.asList(env));
                }
            }
        }
        return false;
    }

    /**
     * 抽象的数据源匹配
     * @param env
     * @return
     */
    public abstract boolean matchDatasource(List<EnvFlag> env);
}
