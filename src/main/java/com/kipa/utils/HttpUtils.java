package com.kipa.utils;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * http url工具类
 */
public class HttpUtils {

    private HttpUtils() {}

    public static String buildGetUrl(String url, Map<String, String> paramMap) {
        List<String> list = Lists.newArrayList();
        if (MapUtils.isNotEmpty(paramMap)) {
            paramMap.forEach((param, value) -> {
                list.add(String.format("%s=%s", param, value));
            });
            return String.format("%s?%s",url, StringUtils.join(list, "&"));
        }else {
            return url;
        }
    }

}
