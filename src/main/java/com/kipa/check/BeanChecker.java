package com.kipa.check;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kipa.utils.ReflectUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Qinyadong
 * @date: 2019/5/19 8:23
 * 对象校验器
 */
final class BeanChecker {

    private BeanChecker() {}

    static String checkBean(Object actual, Object expect, String errorMessage) {

        List<String> errorMessageList = Lists.newArrayList();

        if (actual != null && expect == null || actual == null && expect != null) {
            return errorMessage + "实际对象或者期望对象为空，二者不匹配";
        }

        if (actual == null) {
            return "";
        }

        Class<?> actualClass = actual.getClass();
        Class<?> expectClass = expect.getClass();
        if (actualClass != expectClass) {
            return String.format("%s实际的类型为[%s]，期望类型是：[%s]，二者不相等",errorMessage,  actualClass, expectClass);
        }

        if (ClassUtils.isAssignable(actualClass, Collection.class)) {
            String s = ObjectComparator.checkCollectionEquals(errorMessage, (Collection) actual, (Collection) expect);
            if (StringUtils.isNotBlank(s)) {
                errorMessageList.add(s);
            }

        }else if (ClassUtils.isAssignable(actualClass, Map.class)) {
            String s = ObjectComparator.checkMapEquals(errorMessage, (Map) actual, (Map) expect);
            if (StringUtils.isNotBlank(s)) {
                errorMessageList.add(s);
            }
        }else if (actualClass.isArray()) {
            String s = ObjectComparator.checkArrayEquals(errorMessage, (Object[]) actual, (Object[]) expect);
            if (StringUtils.isNotBlank(s)) {
                errorMessageList.add(s);
            }
        }else if (ClassUtils.isPrimitiveOrWrapper(actualClass) || DataConstant.CLASS_NAME.contains(actualClass.getName())) {
            String s = ObjectComparator.checkEquals(errorMessage, actual, expect);
            if (StringUtils.isNotBlank(s)) {
                errorMessageList.add(s);
            }
        }else {
            Map<String, Class<?>> fieldNameTypeMap = ReflectUtils.getFieldNameTypeMap(actualClass);
            if (MapUtils.isNotEmpty(fieldNameTypeMap)) {
                JSONObject actualObject = JSONObject.parseObject(JSON.toJSONString(actual));
                JSONObject expectObject = JSONObject.parseObject(JSON.toJSONString(expect));

                fieldNameTypeMap.forEach((filedName, type) -> {
                    Object o = actualObject.get(filedName);
                    Object o1 = expectObject.get(filedName);
                    String s = checkBean(o, o1, filedName);
                    if (StringUtils.isNotBlank(s)) {
                        errorMessageList.add(s);
                    }
                });
            }

        }

        return CollectionUtils.isNotEmpty(errorMessageList) ? StringUtils.join(errorMessageList, "\n"): "";
    }

    static String checkBeanCollection(final Collection actualCollection, final Collection expectCollection) {

        List<String> errorMessageList = Lists.newArrayList();

        String s = ObjectComparator.preCheck(actualCollection, expectCollection);

        if (StringUtils.isNotBlank(s)) {
            return s;
        }

        if (actualCollection instanceof List && expectCollection instanceof List) {
            List list = (List) actualCollection;
            List list2 = (List) expectCollection;
            for (int i = 0; i <list .size(); i++) {
                Object actual = list.get(i);
                Object expect = list2.get(i);
                String s1 = checkBean(actual, expect,"当前集合第" +(i +1) + "个元素");
                if (StringUtils.isNotBlank(s1)) {
                    errorMessageList.add(s1);
                }
            }
        }else if (actualCollection instanceof Set && expectCollection instanceof Set) {
            Object[] objects = ((Set) actualCollection).toArray();
            Object[] objects2 = ((Set) actualCollection).toArray();
            for (int i = 0; i < objects.length; i++) {
                Object actual = objects[i];
                Object expect = objects2[i];
                String s1 = checkBean(actual, expect, "当前集合第" +(i +1) + "个元素");
                if (StringUtils.isNotBlank(s1)) {
                    errorMessageList.add(s1);
                }
            }
        }

        return CollectionUtils.isNotEmpty(errorMessageList) ? StringUtils.join(errorMessageList, "\n"): "";
    }


    static String checkBeanMap(final Map actualMap, final Map expectMap) {

        List<String> errorMessageList = Lists.newArrayList();

        String s1 = ObjectComparator.preCheckMap(actualMap, expectMap);
        if (StringUtils.isNotBlank(s1)) {
            return s1;
        }

        List<String> list = Lists.newArrayList();
        List<String> expectList = Lists.newArrayList();
        Map<Object, Object> concurrentHashMap = new ConcurrentHashMap();
        expectMap.forEach(concurrentHashMap::put);
        if (MapUtils.isNotEmpty(actualMap)) {
            actualMap.forEach((key, value) -> {
                Object expect = concurrentHashMap.get(key);
                if (key != null && value != null ) {
                    checkBean(value, expect, key.toString());
                    concurrentHashMap.remove(key);
                    return;
                }
                list.add(String.format("当前Map的对应的实际值为：[%s <==> %s]，", key, value));
            });

            concurrentHashMap.forEach((s, o) -> expectList.add(String.format("期望值为：[%s <==> %s]", s, o)));
        }

        if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(expectList) && list.size() == expectList.size()) {
            String[] strings = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                strings[i]  = list.get(i) + expectList.get(i);
            }
            errorMessageList.addAll(Arrays.asList(strings));
        }
        return CollectionUtils.isNotEmpty(errorMessageList) ? StringUtils.join(errorMessageList, "\n"): "";
    }

    static String checkBeanArray(final Object[] actualArray, final Object[] expectArray) {

        List<String> errorMessageList = Lists.newArrayList();

        String s = ObjectComparator.preCheckArray(actualArray, expectArray);

        if (StringUtils.isNotBlank(s)) {
            return s;
        }

        if (ArrayUtils.isNotEmpty(actualArray) && ArrayUtils.isNotEmpty(expectArray)) {
            for (int i = 0; i < actualArray.length; i++) {

                if (actualArray[i] != null && expectArray[i] == null || actualArray[i] == null && expectArray[i] != null) {
                    String errorMessage2 = String.format("当前实际值为：[%s]，期望值为：[%s]", actualArray[i] , expectArray[i]);
                    if (StringUtils.isNotBlank(errorMessage2)) {
                        errorMessageList.add(errorMessage2);
                    }
                }else if (actualArray[i] != null) {
                    String s1 = checkBean(actualArray[i], expectArray[i], "数组中的第" + (i + 1));
                    if (StringUtils.isNotBlank(s1)) {
                        errorMessageList.add(s1);
                    }
                }
            }
        }

        return CollectionUtils.isNotEmpty(errorMessageList) ? StringUtils.join(errorMessageList, "\n"): "";
    }
}
