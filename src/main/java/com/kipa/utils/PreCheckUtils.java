package com.kipa.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.Collection;
import java.util.Map;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 12:54
 * 预校验的工具类
 */
public class PreCheckUtils {

    private PreCheckUtils() {}

     public static <T> void checkEmpty(T reference, String message) {
        if (reference == null) {
            throw new IllegalArgumentException(message);
        }
        if (reference instanceof String && StringUtils.isEmpty((String) reference)) {
            throw new IllegalArgumentException(message);
        }

        if (reference instanceof CharSequence && StringUtils.isEmpty((CharSequence) reference)) {
            throw new IllegalArgumentException(message);
        }

        if (reference instanceof Collection && CollectionUtils.isEmpty((Collection)reference)) {
            throw new IllegalArgumentException(message);
        }
        if (reference instanceof Map && (MapUtils.isEmpty((Map<?, ?>) reference))) {
            throw new IllegalArgumentException(message);
        }

    }



}
