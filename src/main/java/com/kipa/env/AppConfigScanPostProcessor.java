package com.kipa.env;

import com.kipa.utils.PackageScanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.testng.collections.Maps;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author: Qinyadong
 * @date: 2019/4/19 19:30
 * 通过包扫描获取flag切换环境
 */
@Slf4j
@Component
public class AppConfigScanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        AppConfigScan appConfigScan = AnnotationUtils.findAnnotation(beanClass, AppConfigScan.class);
        if (!ObjectUtils.isEmpty(appConfigScan)) {
            String basePackage = appConfigScan.basePackage();
            //扫描到所有的带有@Database、@Dubbo、@Http的bean
            Set<BeanDefinition> definitionSet = PackageScanUtils.getBeanWithAnnotationSet(basePackage, Arrays.asList(Database.class, Dubbo.class, Http.class));
            if (CollectionUtils.isNotEmpty(definitionSet)) {
                definitionSet.forEach(beanDefinition -> {
                    Map<String, Object> datasourceAttributes = getAnnotationAttributes(beanDefinition, Database.class);
                    if (MapUtils.isNotEmpty(datasourceAttributes)) {
                        String flag = (String)datasourceAttributes.get("datasourceFlag");
                        DubboContextHolder.setFlag(flag);
                    }
                    Map<String, Object> consumerAttributes = getAnnotationAttributes(beanDefinition, Dubbo.class);
                    if (MapUtils.isNotEmpty(consumerAttributes)) {
                        ConsumerContextHolder.setConfig(consumerAttributes);
                    }

                    Map<String, Object> httpAttributes = getAnnotationAttributes(beanDefinition, Http.class);
                    if (MapUtils.isNotEmpty(httpAttributes)) {
                        HttpContextHolder.setFlag((String) httpAttributes.get("httpFlag"));
                    }
                });
            }

        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 扫描单一的注解
     * @param basePackage
     * @param annotationType
     * @return
     */
    private static Map<String, Object> getAnnotationAttributes(String basePackage, Class<? extends Annotation> annotationType) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(basePackage)) {
            Set<BeanDefinition> beanWithAnnotation = PackageScanUtils.getBeanWithAnnotation(basePackage, annotationType);
            if (CollectionUtils.isNotEmpty(beanWithAnnotation)) {
                beanWithAnnotation.forEach(beanDefinition -> {
                    if (beanDefinition instanceof ScannedGenericBeanDefinition) {
                        ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition) beanDefinition;
                        AnnotationMetadata metadata = scannedGenericBeanDefinition.getMetadata();
                        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(annotationType.getName());
                        if (annotationAttributes != null) {
                            map.putAll(annotationAttributes);
                        }
                    }
                });
            }
        }
        return map;
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
