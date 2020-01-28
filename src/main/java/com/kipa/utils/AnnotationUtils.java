package com.kipa.utils;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author: Qinyadong
 * @date: 2019/5/14 22:01
 * 注解工具
 */
public final class AnnotationUtils {

    private AnnotationUtils() {

    }

    public static Set<Method> getMethodWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationType) {
        Set<Method> methodSet = Sets.newHashSet();
        Method[] methods = clazz.getDeclaredMethods();
        if (ArrayUtils.isNotEmpty(methods)) {
            for (Method method : methods) {
                Annotation annotation = method.getAnnotation(annotationType);
                if (annotation != null) {
                    methodSet.add(method);
                }
            }
        }
        return methodSet;
    }

    public static Set<Field> findFieldWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationType) {
        Set<Field> fieldSet = Sets.newHashSet();
        Field[] declaredFields = clazz.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                Annotation annotation = field.getAnnotation(annotationType);
                if (annotation != null) {
                    fieldSet.add(field);
                }
            }
        }
        return fieldSet;
    }

}
