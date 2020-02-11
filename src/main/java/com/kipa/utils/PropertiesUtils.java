package com.kipa.utils;

import com.kipa.common.KipaProcessException;
import com.kipa.env.GlobalEnvironmentProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author: Qinyadong
 * @date: 2019/4/19 15:23
 * 属性文件解析工具类
 */
public final class PropertiesUtils {

    private PropertiesUtils() {}

    public static Properties loadProperties(final String configPath) {
        PreCheckUtils.checkEmpty(configPath,"参数配置文件为空");
        try {
            return PropertiesLoaderUtils.loadAllProperties(configPath);
        } catch (IOException e) {
            throw new KipaProcessException("解析classpath:"+configPath+"文件错误",e);
        }
    }

    public static String getProperty(final Properties properties, String flag, final String key) {
       String propertyKey = StringUtils.isBlank(flag) ? key : String.format("%s.%s",flag, key);
       return properties.getProperty(propertyKey);
    }

    public static String getProperty(final GlobalEnvironmentProperties properties, String flag, String key) {
        String propertyKey = StringUtils.isBlank(flag) ? key : String.format("%s.%s",flag, key);
        return String.valueOf(properties.getProperty(propertyKey));
    }

    public static String getProperty(final GlobalEnvironmentProperties properties, String key) {
        Object property = properties.getProperty(key);
        return property != null ? String.valueOf(property) : null;
    }

    public static Integer getIntegerProperty(final GlobalEnvironmentProperties properties, String key) {
        String property = getProperty(properties, key);
        return StringUtils.isNotBlank(property)? Integer.parseInt(property) : 0;
    }

    public static Long getLongProperty(final GlobalEnvironmentProperties properties, String key) {
        String property = getProperty(properties, key);
        return StringUtils.isNotBlank(property)? Long.parseLong(property) : 0;
    }

    public static Boolean getBooleanProperty(final GlobalEnvironmentProperties properties, String key) {
        String property = getProperty(properties, key);
        return StringUtils.isNotBlank(property) && Boolean.parseBoolean(property);
    }

    public static String getStringDefaultIfBlank(final GlobalEnvironmentProperties properties, String key, String defaultValue) {
        String property = getProperty(properties, key);
        return StringUtils.isNotBlank(property) ? property : defaultValue;

    }
    public static Integer getIntegerDefaultIfNull(final GlobalEnvironmentProperties properties, String key, Integer defaultValue) {
        String property = getProperty(properties, key);
        return StringUtils.isNotBlank(property)? Integer.parseInt(property) : defaultValue;
    }

    public static Boolean getBooleanDefaultIfNull(final GlobalEnvironmentProperties properties, String key, Boolean defaultValue) {
        String property = getProperty(properties, key);
        return StringUtils.isNotBlank(property)? Boolean.parseBoolean(property) : defaultValue;
    }

    public static Long getLongDefaultIfNull(final GlobalEnvironmentProperties properties, String key, Long defaultValue) {
        String property = getProperty(properties, key);
        return StringUtils.isNotBlank(property)? Long.parseLong(property) : defaultValue;
    }

}
