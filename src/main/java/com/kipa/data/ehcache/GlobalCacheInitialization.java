package com.kipa.data.ehcache;

import com.kipa.env.GlobalEnvironmentProperties;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

/**
 * @author Qinyadong
 * @date 2019/8/19 16:17
 * @description
 * <p>
 *     全局缓存的初始化类，在Spring容器启动完成之后或者再次刷新容器的时候,
 *     读取框架配置文件 application-{flag}.properties中key以common开头的属性，
 *     同时会将它加入的全局缓存GlobalCacheContext中
 * </p>
 * @since 2.1.0
 */
@Component
public class GlobalCacheInitialization implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private GlobalCacheContext cacheContext;

    @Autowired
    private GlobalEnvironmentProperties globalEnvironmentProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            final Map<String, Object> properties = globalEnvironmentProperties.getSource();
            properties.forEach((o, o2) -> {
                if (StringUtils.startsWithIgnoreCase(o, "common")) {
                    cacheContext.setAttribute(o, o2);
                }
            });
        }
    }
}
