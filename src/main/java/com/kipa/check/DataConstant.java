package com.kipa.check;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Qinyadong
 * @date: 2019/4/10 13:08
 */
public interface DataConstant {

    /**
     * 基础数据类型名称集合
     */
    List<String> CLASS_NAME_LIST = Arrays.asList(
            "java.lang.Boolean","java.lang.Byte","java.lang.Character","java.lang.Short","java.lang.Integer",
            "java.lang.Long","java.lang.Float","java.lang.Double","java.lang.Enum","java.util.Date","java.lang.String",
            "java.sql.Date","java.math.BigDecimal");

    List<String> CLASS_NAME = Arrays.asList("java.lang.Enum","java.util.Date","java.lang.String", "java.sql.Date","java.math.BigDecimal");

    /**
     * 默认的数据文件的base目录
     */
    String BASE_PATH = System.getProperty("user.dir")+"\\src\\main\\resources\\";

}
