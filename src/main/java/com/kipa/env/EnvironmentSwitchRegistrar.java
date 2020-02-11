package com.kipa.env;

import com.kipa.common.KipaProcessException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class EnvironmentSwitchRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String GLOBAL_PROPERTIES_FORMATTER = "classpath:application-%s.properties";
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        final Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableEnvironmentSwitch.class.getName(), false);
        final AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationAttributes);
        if (attributes != null) {
            String globalPropertiesFile;
            EnvironmentType env = attributes.getEnum("env");
            if (env == EnvironmentType.DEFAULT) {
                globalPropertiesFile = "classpath:application.properties";
            }else {
                globalPropertiesFile = String.format(GLOBAL_PROPERTIES_FORMATTER, env.getRemark());
            }
            System.setProperty("globalProperties",globalPropertiesFile);
            try {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(GlobalEnvironmentProperties.class);
                builder.addConstructorArgValue(globalPropertiesFile);
                BeanDefinition beanDefinition = builder.getBeanDefinition();
                registry.registerBeanDefinition("globalEnvironmentProperties",beanDefinition);
            } catch (Exception e) {
                throw new KipaProcessException("环境初始化失败",e);
            }
        }
    }
}
