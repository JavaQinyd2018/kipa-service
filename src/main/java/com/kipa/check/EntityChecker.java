package com.kipa.check;

import com.google.common.collect.Lists;
import com.kipa.utils.ReflectUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author: Qinyadong
 * @date: 2019/5/19 8:16
 * 实体类校验器
 */
final class EntityChecker {

    private EntityChecker() {}

    static <T> String checkEntity(T actual, T expect, String errorMessage) {
        final String message = "%s：当前实体对象实际值为：[%s]，期望值为：[%s], 二者不匹配";
        List<String> errorMessageList = Lists.newArrayList();
        if (actual != null && expect == null || actual == null && expect != null) {
            return String.format(message, errorMessage, actual, expect);
        }

        if (actual != null) {
            Class<?> clazz = actual.getClass();
            List<String> fieldNameList = ReflectUtils.getFieldNameList(clazz);
            fieldNameList.forEach(field -> {
                Object o = ReflectUtils.invokeGetOrIsMethod(actual, clazz, field);
                Object o1 = ReflectUtils.invokeGetOrIsMethod(expect, clazz, field);
                String checkResult = ObjectComparator.checkEquals(field, o, o1);
                if (StringUtils.isNotBlank(checkResult)) {
                    errorMessageList.add(checkResult);
                }
            });
        }

        return CollectionUtils.isNotEmpty(errorMessageList) ? StringUtils.join(errorMessageList, "\n"): "";
    }

    static <T> String checkEntityCollection(Collection<T> actualCollection, Collection<T> expectCollection) {

        String message = "当前集合中第%s对象";
        List<String> errorMessageList = Lists.newArrayList();
        //基本校验
        String s = ObjectComparator.preCheck(actualCollection, expectCollection);
        if (StringUtils.isNotBlank(s)) {
            return s;
        }

        //值校验
        if (actualCollection instanceof List && expectCollection instanceof List) {
            for (int i = 0; i < ((List) actualCollection).size(); i++) {
                T actual = (T)(((List) actualCollection).get(i));
                T expect = (T)(((List) expectCollection).get(i));
                errorMessageList.add(checkEntity(actual, expect, String.format(message, i + 1)));
            }
        }

        if (actualCollection instanceof Set && expectCollection instanceof Set) {
            for (int i = 0; i < ((Set) actualCollection).toArray().length; i++) {
                T actual = (T)(((Set) actualCollection).toArray()[i]);
                T expect = (T)(((Set) expectCollection).toArray()[i]);
                errorMessageList.add(checkEntity(actual, expect, String.format(message, i + 1)));
            }
        }

        return CollectionUtils.isNotEmpty(errorMessageList) ? StringUtils.join(errorMessageList, "\n"): "";

    }


}
