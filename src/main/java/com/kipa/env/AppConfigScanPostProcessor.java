package com.kipa.env;

import com.kipa.utils.PackageScanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
public class AppConfigScanPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Map<String, Object> beansWithAnnotation = configurableListableBeanFactory.getBeansWithAnnotation(AppConfigScan.class);
        if (MapUtils.isNotEmpty(beansWithAnnotation)) {
            if (beansWithAnnotation.size() > 1) {
                throw new RuntimeException("@AppConfigScan注解只允许标注一次");
            }

            Object bean = beansWithAnnotation.entrySet().iterator().next().getValue();
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
        }
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
