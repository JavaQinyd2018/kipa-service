package com.kipa.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
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
            paramMap.forEach((param, value) -> list.add(String.format("%s=%s", param, value)));
            return String.format("%s?%s",url, StringUtils.join(list, "&"));
        }else {
            return url;
        }
    }

    public static Map<String, String> getUrlInfo(String url) {
        Map<String, String> result = Maps.newHashMap();
        PreCheckUtils.checkEmpty(url, "url不能为空");
        final String[] urlArray = StringUtils.split(url, '?');
        if (ArrayUtils.isNotEmpty(urlArray)) {
            String hostAndPath = urlArray[0];
            result.put("url",hostAndPath);
            String paramString = urlArray[1];
            String[] split = StringUtils.split(paramString, "&");
            if (ArrayUtils.isNotEmpty(split)) {
                for (String paramAndValue : split) {
                    String[] paramArray = StringUtils.split(paramAndValue, "=");
                    if (ArrayUtils.isNotEmpty(paramArray)) {
                        String param = paramArray[0];
                        String value = paramArray[1];
                        result.put(param, value);
                    }
                }
            }
        }
        return result;
    }
}
