package com.kipa.env;

import com.google.common.collect.Maps;
import com.kipa.common.KipaProcessException;
import com.kipa.config.EnableMultipleDataSource;
import com.kipa.mybatis.service.condition.EnvFlag;
import com.kipa.utils.PackageScanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author: Qinyadong
 * @date: 2019/4/19 19:30
 * 通过包扫描获取flag切换环境
 */
@Component
public class EnableMultiDataSourcePostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        EnableMultipleDataSource multipleDataSource = getAnnotation(configurableListableBeanFactory, EnableMultipleDataSource.class);
        if (!ObjectUtils.isEmpty(multipleDataSource)) {
            EnvFlag[] env = multipleDataSource.env();
            DatasourceEnvHolder.setFlag(env);
        }
    }


    private static <T extends Annotation>  T getAnnotation(ConfigurableListableBeanFactory configurableListableBeanFactory,
                                                           Class<T> annotationClazz) {
        Map<String, Object> beansWithAnnotation = configurableListableBeanFactory.getBeansWithAnnotation(annotationClazz);
        if (MapUtils.isNotEmpty(beansWithAnnotation)) {
            if (beansWithAnnotation.size() > 1) {
                throw new KipaProcessException("@" + annotationClazz.getSimpleName() + "注解只允许标注一次");
            }

            Object bean = beansWithAnnotation.entrySet().iterator().next().getValue();
            Class<?> beanClass = bean.getClass();
            return AnnotationUtils.findAnnotation(beanClass, annotationClazz);
        }
        return null;
    }

    /**
     * 根据bean获取注解属性信息
     * @param beanDefinition
     * @param annotationType
     * @return
     */
    private static Map<String, Object> getAnnotationAttributes(BeanDefinition beanDefinition, Class<? extends Annotation> annotationType) {
        Map<String, Object> map = Maps.newHashMap();
            if (beanDefinition instanceof ScannedGenericBeanDefinition) {
                ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition) beanDefinition;
                AnnotationMetadata metadata = scannedGenericBeanDefinition.getMetadata();
                Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(annotationType.getName());
                if (annotationAttributes != null) {
                    map.putAll(annotationAttributes);
                }
            }
        return map;
    }
}
