package com.kipa.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kipa.common.KipaProcessException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/3 13:36
 * 对象反射获取相关数据的工具类
 */
public class ReflectUtils {

    private ReflectUtils() {}

    /*public static <T> Object invokeGetMethod(T entity, Class<T> clazz, String fieldName) {
        if (entity == null) {
            throw new IllegalArgumentException("传入的实体对象不能为空");
        }
        Method method = getGetMethod(clazz, fieldName);
        if (method != null) {
            try {
                return method.invoke(entity);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new KipaProcessException("字段"+fieldName+"对应的方法调用失败",e);
            }
        }
        return null;
    }

    public static <T> Method getGetMethod(Class<T> clazz, String fieldName) {
        Method methodReturn = null;
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("字段名不能为空");
        }
        Set<Method> getSet = getAllMethods(clazz, withPrefix("get"), withParametersCount(0));
        if (CollectionUtils.isEmpty(getSet)) {
            throw new KipaProcessException("该类型没有get方法");
        }
        for (Method method : getSet) {
            String get = method.getName().substring(method.getName().indexOf("get") + 3);
            if (StringUtils.equalsIgnoreCase(fieldName,get)) {
                methodReturn = method;
            }
        }
        return methodReturn;
    }*/

    public static List<String> getFieldNameList(Class<?> clazz) {
        Set<Field> allFields = getAllFields(clazz);
        return allFields.stream().map(Field::getName).collect(Collectors.toList());
    }

    public static Map<String, Class<?>> getFieldNameTypeMap(Class<?> clazz) {
        Map<String, Class<?>> map = Maps.newLinkedHashMap();
        Set<Field> allFields = getAllFields(clazz);
        allFields.forEach(field -> map.put(field.getName(), field.getType()));
        return map;
    }

    private static Set<Field> getAllFields(Class<?> clazz) {
        Field[] allFields = FieldUtils.getAllFields(clazz);
        return new HashSet<>(Arrays.asList(allFields));
    }
    /**
     * 获取set方法
     * @param clazz
     * @param fieldName
     * @param <T>
     * @return
     */
    /*public static <T> Method getSetMethod(Class<T> clazz, String fieldName) {
        Method methodReturn = null;
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("字段名不能为空");
        }
        Set<Method> methodSet = getAllMethods(clazz, withPrefix("set"), withParametersCount(1));
        if (CollectionUtils.isEmpty(methodSet)) {
            throw new KipaProcessException("该类型没有set方法");
        }
        for (Method method : methodSet) {
            String set = method.getName().substring(method.getName().indexOf("set") + 3);
            if (StringUtils.equalsIgnoreCase(fieldName,set)) {
                methodReturn = method;
            }
        }
        return methodReturn;
    }*/

    /**
     * 调用set方法
     * @param entity
     * @param clazz
     * @param fieldName
     * @param value
     * @param <T>
     */
    /*public static <T> void invokeSetMethod(T entity, Class<T> clazz, String fieldName, Object value) {
        Method method = getSetMethod(clazz, fieldName);
        if (method != null) {
            try {
                method.invoke(entity, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new KipaProcessException("字段"+fieldName+"对应的方法调用失败",e);
            }
        }
    }*/


    /*public static <T> T invokeSet(Class<T> clazz, Map<String, Object> mapParam) {
        T entity = null;
        try {
            entity = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        List<String> fieldNameList = getFieldNameList(clazz);
        for (String fieldName : fieldNameList) {
            Method setMethod = getSetMethod(clazz, fieldName);
            Object value = mapParam.get(fieldName);
            if (value != null && entity != null) {
                try {
                    setMethod.invoke(entity, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new KipaProcessException("字段"+fieldName+"对应的set方法调用失败",e);
                }
            }
        }
        return entity;
    }*/

    /**
     * 递归获取某个类型的全部字段（包括父类的。。。）
     * @param clazz 类型
     * @return 字段List
     */
    public static List<Field> getAllFieldName(Class<?> clazz) {
        List<Field> fieldList = Lists.newArrayList();
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fieldList.addAll(getAllFieldName(clazz.getSuperclass()));
        }
        return fieldList.stream()
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && Modifier.isPrivate(field.getModifiers()))
                .collect(Collectors.toList());
    }

    public static Map<String, Class<?>> getFiledNameAndType(Class<?> clazz) {
        Map<String, Class<?>> map = Maps.newLinkedHashMap();
        List<Field> fieldList = getAllFieldName(clazz);
        fieldList.forEach(field -> map.put(field.getName(), field.getType()));
        return map;
    }

    public static Method getGetOrIsMethod(Class<?> clazz, String fieldName) {
        Method[] methods = clazz.getMethods();
        if (ArrayUtils.isNotEmpty(methods)) {
            for (Method method : methods) {
                String methodName = method.getName();
                if (StringUtils.startsWithIgnoreCase(methodName, "get")
                        && method.getParameters().length == 0
                        && StringUtils.substringAfter(methodName, "get").equalsIgnoreCase(fieldName)) {
                    return method;
                }else if (StringUtils.startsWithIgnoreCase(methodName, "is")
                        && method.getParameters().length == 0
                        && StringUtils.substringAfter(methodName, "is").equalsIgnoreCase(fieldName)) {
                    return method;
                }
            }
        }
        throw new KipaProcessException("字段"+ fieldName+"不存在对应的get方法或者is方法");
    }

    public static <T> Object invokeGetOrIsMethod(T entity, Class<?> clazz, String fieldName) {
        Method getOrIsMethod = getGetOrIsMethod(clazz, fieldName);
        try {
            return getOrIsMethod.invoke(entity);
        } catch (Exception e) {
            throw new KipaProcessException("字段"+fieldName+"调用get或者is方法失败",e);
        }
    }

    /**
     * 非基础数据类型，非对象类型集合
     */
    private static final List<Class<?>> COMMON_TYPE = Arrays.asList(Date.class, BigDecimal.class, Map.class, Collection.class, java.sql.Date.class, String.class);

    /**
     * 递归获取实体类中所有的字段以及对应的属性值
     * @param clazz 实体类类型
     * @param bean 实体类实例
     * @return 字段和值的map
     */
    public static Map<String, Object> getFileNameAndValueMap(Class<?> clazz, Object bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (clazz == null) {
            return map;
        }

        final Field[] fields = FieldUtils.getAllFields(clazz);
        if (ArrayUtils.isEmpty(fields)) {
            return map;
        }

        final Method[] methods = clazz.getMethods();

        for (Field field : fields) {
            Class<?> type = field.getType();
            try {
                if (!ClassUtils.isPrimitiveOrWrapper(type) && !COMMON_TYPE.contains(type) && !type.isArray() && !type.isEnum()) {
                    Method method = getGetOrIsMethod(methods, field.getName());
                    if (method == null) {
                        continue;
                    }
                    Object invoke = method.invoke(bean);
                    map.putAll(getFileNameAndValueMap(type, invoke));

                }else {
                    Method method = getGetOrIsMethod(methods, field.getName());
                    if (method == null) {
                        continue;
                    }
                    Object invoke = method.invoke(bean);
                    map.put(field.getName(), invoke);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new KipaProcessException("字段"+field.getName()+"获取值失败",e);
            }

        }
        return map;
    }

    private static Method getGetOrIsMethod(Method[] methods, String fileName) {
        if (ArrayUtils.isNotEmpty(methods)) {
            for (Method method : methods) {
                String methodName = method.getName();
                if (method.getParameters().length == 0
                        && StringUtils.substringAfter(methodName, "get").equalsIgnoreCase(fileName)
                        || StringUtils.substringAfter(methodName, "is").equalsIgnoreCase(fileName)) {
                    return method;
                }
            }
        }
        return null;
    }

}
