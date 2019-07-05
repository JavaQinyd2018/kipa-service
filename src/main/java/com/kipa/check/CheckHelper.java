package com.kipa.check;

import com.kipa.common.KipaProcessException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/11 9:26
 * 校验对象工具
 */
public final class CheckHelper {

    private CheckHelper() {}
    /**
     * 校验某个类型是否一致
     * @param actual
     * @param expect
     * @param message
     * @param <T>
     */
    public static <T> void checkEntityEquals(T actual, T expect, String message) {
        process(EntityChecker.checkEntity(actual, expect, ""), message);
    }

    /**
     * 校验集合
     * @param actualCollection
     * @param exceptCollection
     * @param message
     * @param <T>
     */
    public static <T> void  checkEntityEquals(Collection<T> actualCollection, Collection<T> exceptCollection, String message) {
       process(EntityChecker.checkEntityCollection(actualCollection, exceptCollection), message);
    }


    /**
     * Javabean数组校验
     * @param actual
     * @param expect
     * @param message
     */
    public static void checkBeanEquals(Object[] actual, Object[] expect, String message) {
        process(BeanChecker.checkBeanArray(actual, expect), message);
    }

    /**
     * Javabean map校验
     * @param actualMap
     * @param expectMap
     * @param message
     */
    public static void checkBeanEquals(Map<Object, Object> actualMap, Map<Object, Object> expectMap, String message) {
        process(BeanChecker.checkBeanMap(actualMap, expectMap), message);
    }


    /**
     * javabean对象校验
     * @param actualCollection
     * @param exceptCollection
     * @param message
     */
    public static void  checkBeanEquals(Collection actualCollection, Collection exceptCollection, String message) {
       process(BeanChecker.checkBeanCollection(actualCollection, exceptCollection), message);
    }

    /**
     * 大对象的校验  对象里面套对象，对象里面有属性值是对象的校验
     * @param actual 实际值
     * @param expect 期望值
     * @param message 错误信息
     */
    public static void checkBeanEquals(Object actual, Object expect, String message) {
        process(BeanChecker.checkBean(actual, expect, ""), message);
    }

    private static void process(String result, String message) {
        if (StringUtils.isNotBlank(result)) {
            String[] strings = new String[]{message, "==================================================================",result, "=================================================================="};
            throw new KipaProcessException(StringUtils.join(strings, "\n"));
        }
    }
}
