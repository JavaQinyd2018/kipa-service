package com.kipa.data.ehcache;

import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Qinyadong
 * @date 2019/8/19 16:17
 * @desciption
 * @since
 */
@Component
public class GlobalCacheInitialization implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private GlobalCacheContext cacheContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            Properties properties = PropertiesUtils.loadProperties("application.properties");
            properties.forEach((o, o2) -> {
                if (StringUtils.startsWithIgnoreCase((String)o, "common")) {
                    cacheContext.setAttribute((String)o, o2);
                }
            });
        }
    }
}
