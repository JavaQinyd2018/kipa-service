package com.kipa.check;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Qinyadong
 * @date: 2019/5/18 22:27
 * 对象比较器
 */
final class ObjectComparator {

    private ObjectComparator() {}

    static String checkEquals(String field, Object actual, Object expect) {

        final String messageFormat = "%s字段对应的实际值为：[%s]，期望值为：[%s]，二者不相等";

        if (actual != null && expect == null || actual == null && expect != null) {
            return String.format(messageFormat, field, actual, expect);
        }

        if (actual == null) {
            return "";
        }

        Class<?> actualClass = actual.getClass();
        Class<?> expectClass = expect.getClass();
        if (actualClass != expectClass) {
            return String.format("%s字段对应的实际类型为[%s]，期望类型是：[%s]，二者不相等", field, actualClass, expectClass);
        }


        if (ClassUtils.isPrimitiveOrWrapper(actualClass)) {
            int compare = ObjectUtils.compare((Comparable) actual, (Comparable) expect);
            if (compare != 0) {
                return String.format(messageFormat, field, String.valueOf(actual), String.valueOf(expect));
            }
        }

        if (ClassUtils.isAssignable(actualClass, Date.class)) {
            Date actualDate = (Date) actual;
            Date expectDate = (Date) expect;
            if (actualDate.compareTo(expectDate) != 0) {
                String actualFormat = DateFormatUtils.format(actualDate, "yyyy-MM-dd HH:mm:ss");
                String expectFormat = DateFormatUtils.format(expectDate, "yyyy-MM-dd HH:mm:ss");
                return String.format(messageFormat, field, actualFormat, expectFormat);
            }
        }

        if (ClassUtils.isAssignable(actualClass, BigDecimal.class)) {
            BigDecimal actualBigDecimal = (BigDecimal) actual;
            BigDecimal expectBigDecimal = (BigDecimal) expect;
            if (actualBigDecimal.compareTo(expectBigDecimal) != 0) {
                return String.format(messageFormat, field,
                        actualBigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString(),
                        expectBigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString());
            }
        }

        if (ClassUtils.isAssignable(actualClass, String.class)) {
            if (!StringUtils.equals((String)actual, (String)expect)) {
                return String.format(messageFormat, field, actual, expect);
            }
        }

        if (ClassUtils.isAssignable(actualClass, Enum.class)) {
            Enum actualEnum = (Enum) actual;
            Enum expectEnum = (Enum) expect;
            if (StringUtils.equals(actualEnum.name(), expectEnum.name())) {
                return String.format(messageFormat, field, actualEnum.name(), expectEnum.name());
            }
        }
        return null;

    }

    static String checkCollectionEquals(String field, final Collection<Object> actualCollection, final Collection<Object> expectCollection) {

        String message = "%s字段当前集合中第%s对象";
        List<String> errorMessageList = Lists.newArrayList();
        String s = ObjectComparator.preCheck(actualCollection, expectCollection);
        if (StringUtils.isNotBlank(s)) {
            return s;
        }
        if (actualCollection instanceof List && expectCollection instanceof List) {
            List list = (List) actualCollection;
            List list1 = (List) expectCollection;
            for (int i = 0; i <list .size(); i++) {
                Object actual = list.get(i);
                Object expect = list1.get(i);
                String result = checkEquals(String.format(message, field, (i +1)), actual, expect);
                if (StringUtils.isNotBlank(result)) {
                    errorMessageList.add(result);
                }
            }
        }

        if (actualCollection instanceof Set && expectCollection instanceof Set) {
            Object[] objects = ((Set) actualCollection).toArray();
            Object[] objects1 = ((Set) expectCollection).toArray();
            for (int i = 0; i < objects.length; i++) {
                Object actual = objects[i];
                Object expect = objects1[i];
                String result = checkEquals(String.format(message, (i +1)), actual, expect);
                if (StringUtils.isNotBlank(result)) {
                    errorMessageList.add(result);
                }
            }
        }

        return CollectionUtils.isNotEmpty(errorMessageList) ? StringUtils.join(errorMessageList, "\n"): "";
    }


    static String checkMapEquals(String field, final Map<Object, Object> actualMap, final Map<Object, Object> expectMap) {

        List<String> errorMessageList = Lists.newArrayList();

        String s1 = preCheckMap(actualMap, expectMap);
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
                if (key != null && value != null && expect != null && isEquals(value, expect)) {
                    concurrentHashMap.remove(key);
                    return;
                }
                list.add(String.format("实际值为：[%s <==> %s]", key, value));
            });

            concurrentHashMap.forEach((s, o) -> expectList.add(String.format("期望值为：[%s <==> %s]", s, o)));
        }

        if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(expectList) && list.size() == expectList.size()) {
            String[] strings = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                strings[i]  = list.get(i) +"，"+ expectList.get(i);
            }
            errorMessageList.addAll(Arrays.asList(strings));
        }

        return CollectionUtils.isNotEmpty(errorMessageList) ? String.format("%s字段对应的%s%s", field,"\n", StringUtils.join(errorMessageList, "\n")): "";

    }

    static String checkArrayEquals(String field, final Object[] actualArray, final Object[] expectArray) {

        List<String> errorMessageList = Lists.newArrayList();

        String s = preCheckArray(actualArray, expectArray);

        if (StringUtils.isNotBlank(s)) {
            return s;
        }

        if (ArrayUtils.isNotEmpty(actualArray) && ArrayUtils.isNotEmpty(expectArray)) {
            for (int i = 0; i < actualArray.length; i++) {
                String errorMessage2 = "";
                if (actualArray[i] != null && expectArray[i] == null || actualArray[i] == null && expectArray[i] != null) {
                    errorMessage2 = String.format("当前实际值为:[%s]，期望值为：[%s]", actualArray[i] , expectArray[i]);
                }else if (actualArray[i] != null && ! isEquals(actualArray[i], expectArray[i])) {
                    errorMessage2 = checkEquals("",actualArray[i] , expectArray[i]);
                }
                if (StringUtils.isNotBlank(errorMessage2)) {
                    errorMessageList.add(errorMessage2);
                }
            }
        }

        return CollectionUtils.isNotEmpty(errorMessageList) ? String.format("%s字段对应的%s", field, StringUtils.join(errorMessageList, "\n")): "";
    }

    static String preCheck(Collection actualCollection, Collection expectCollection) {

        boolean condition1= CollectionUtils.isEmpty(actualCollection) && CollectionUtils.isNotEmpty(expectCollection);
        boolean condition2 = CollectionUtils.isNotEmpty(actualCollection) && CollectionUtils.isEmpty(expectCollection);

        if (condition1   || condition2 ) {
            return String.format("当前集合实际值为：[%s]，期望值为：[%s]", actualCollection, expectCollection);
        }

        if (actualCollection.size() != expectCollection.size()) {
            return String.format("当前集合大小实际为：[%s]，期望值为：[%s]，二者大小不一样",actualCollection.size(), expectCollection.size());
        }

        boolean condition3 = actualCollection instanceof List && expectCollection instanceof Set;
        boolean condition4 = actualCollection instanceof Set && expectCollection instanceof List;
        if (condition3 || condition4) {
            return String.format("当前集合实际值类型为：[%s]，期望值类型为：[%s]，二者不匹配", actualCollection.getClass(), expectCollection.getClass());
        }

        return "";
    }

    static String preCheckMap(final Map actualMap, final Map expectMap) {
        boolean condition1 = MapUtils.isEmpty(actualMap) && MapUtils.isNotEmpty(expectMap);
        boolean condition2 = MapUtils.isNotEmpty(actualMap) && MapUtils.isEmpty(expectMap);
        if ( condition1||condition2 ) {
            return String.format("当前实际值为：[%s]，期望值为：[%s]", actualMap, expectMap);
        }

        if (actualMap.size() != expectMap.size()) {
            return String.format("当前实际值map长度为：[%s]，期望值map长度为：[%s]", actualMap.size(), expectMap.size());
        }
        return "";
    }

    static String preCheckArray(final Object[] actualArray, final Object[] expectArray) {
        boolean condition1 = ArrayUtils.isNotEmpty(actualArray) && ArrayUtils.isEmpty(expectArray);
        boolean condition2 = ArrayUtils.isEmpty(actualArray) && ArrayUtils.isNotEmpty(expectArray);
        if (condition1 ||condition2) {
            return String.format("当前实际值为：[%s]，期望值为：[%s]",  Arrays.asList(actualArray), Arrays.asList(expectArray));
        }
        if (actualArray.length != expectArray.length) {
            return String.format("当前实际值数组长度为：[%s]，期望值数组长度为：[%s]",actualArray.length, expectArray.length);
        }
        return "";
    }

    private static boolean isEquals(Object actual, Object expect) {
        Class<?> actualClass = actual.getClass();
        Class<?> expectClass = expect.getClass();
        if (actualClass != expectClass) {
            return false;
        }

        if (ClassUtils.isPrimitiveOrWrapper(actualClass)) {
            int compare = ObjectUtils.compare((Comparable) actual, (Comparable) expect);
            return compare == 0;
        }

        if (ClassUtils.isAssignable(actualClass, Date.class)) {
            Date actualDate = (Date) actual;
            Date expectDate = (Date) expect;
            return actualDate.compareTo(expectDate) == 0;
        }

        if (ClassUtils.isAssignable(actualClass, BigDecimal.class)) {
            BigDecimal actualBigDecimal = (BigDecimal) actual;
            BigDecimal expectBigDecimal = (BigDecimal) expect;
            return actualBigDecimal.compareTo(expectBigDecimal) == 0;
        }

        if (ClassUtils.isAssignable(actualClass, String.class)) {
            return StringUtils.equals((String)actual, (String)expect);
        }

        if (ClassUtils.isAssignable(actualClass, Enum.class)) {
            Enum actualEnum = (Enum) actual;
            Enum expectEnum = (Enum) expect;
            return StringUtils.equals(actualEnum.name(), expectEnum.name());
        }

        return actual.equals(expect);
    }

}
