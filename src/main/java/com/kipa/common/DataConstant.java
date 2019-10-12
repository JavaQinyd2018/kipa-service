package com.kipa.common;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Qinyadong
 * @date: 2019/4/10 13:08
 */
public final class DataConstant {

    private DataConstant() {}
    /**
     * 基础数据类型名称集合
     */
    public static final List<String> CLASS_NAME_LIST = Arrays.asList(
            "java.lang.Boolean","java.lang.Byte","java.lang.Character","java.lang.Short","java.lang.Integer",
            "java.lang.Long","java.lang.Float","java.lang.Double","java.lang.Enum","java.util.Date","java.lang.String",
            "java.sql.Date","java.math.BigDecimal");

    public static final List<String> CLASS_NAME = Arrays.asList("java.lang.Enum","java.util.Date","java.lang.String", "java.sql.Date","java.math.BigDecimal");


    /**
     * 默认的数据文件的base目录
     */
    public static final String BASE_PATH = System.getProperty("user.dir") + (StringUtils.containsIgnoreCase(System.getProperty("os.name"), "windows") ? "\\src\\main\\resources\\" : "/src/main/resources/");

    /**
     * 框架配置文件名称以及位置：classpath:application.properties
     */
    public static final String CONFIG_FILE = "application.properties";
}
