package com.kipa.utils;

import com.kipa.common.KipaProcessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author: Qinyadong
 * @date: 2019/4/19 15:23
 * 属性文件解析工具类
 */
public class PropertiesUtils {

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
}
