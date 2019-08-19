package com.kipa.http.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kipa.http.core.HttpResponse;
import com.kipa.http.exception.HttpProcessException;
import com.kipa.http.service.HttpMetaService;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/4/1 13:05
 */
abstract class AbstractHttpServiceImpl implements HttpMetaService {


    List<Map<String, Object>> parseHttpResponseToMap(HttpResponse httpResponse, boolean receiveAllInfo) {
        //封装结果成list
        List<Map<String, Object>> mapList = Lists.newArrayList();
        if (receiveAllInfo) {
            mapList.add(JSONObject.parseObject(JSONObject.toJSONString(httpResponse), Map.class));
        }else if (httpResponse.isJsonFormat()) {
            Object dataType = httpResponse.getBodyMap().get("dataType");
            Object data = httpResponse.getBodyMap().get("data");
            if (dataType != null) {
                if (StringUtils.equalsIgnoreCase((String)dataType, "JSONObject")) {
                    mapList.add(JSONObject.parseObject(JSONObject.toJSONString(data), Map.class)) ;
                }else if (StringUtils.equalsIgnoreCase((String)dataType, "JSONArray")) {
                    List<Map> maps = JSONArray.parseArray(JSONObject.toJSONString(data), Map.class);
                    maps.forEach(mapList::add);
                }
            }
        }else {
            Map<String, Object> map = Maps.newHashMap();
            map.put("data",httpResponse.getBodyMap().get("data"));
            mapList.add(map);
        }
        return mapList;
    }

    <T> List<T> parseHttpResponseToInstance(HttpResponse httpResponse, Class<T> clazz) {
        List<T> list = Lists.newArrayList();
        if (!httpResponse.isJsonFormat()) {
            throw new HttpProcessException("响应的结果不是一个json格式");
        }
        Object data = httpResponse.getBodyMap().get("data");
        Object dataType = httpResponse.getBodyMap().get("dataType");
        if (dataType != null) {
            if (StringUtils.equalsIgnoreCase((String)dataType, "JSONObject")) {
                list.add(JSONObject.parseObject(JSONObject.toJSONString(data), clazz)) ;
            }else if (StringUtils.equalsIgnoreCase((String)dataType, "JSONArray")) {
                List<T> maps = JSONArray.parseArray(JSONObject.toJSONString(data), clazz);
                list.addAll(maps);
            }
        }
        return list;
    }


    String parseHttpResponseToString(HttpResponse httpResponse, boolean receiveAllInfo) {
        if (receiveAllInfo) {
            return JSONObject.toJSONString(httpResponse);
        }else if(httpResponse.isJsonFormat()){
            Object data = httpResponse.getBodyMap().get("data");
            return JSON.toJSONString(data);
        }else {
            return (String) httpResponse.getBodyMap().get("data");
        }
    }

    Map<String, String> parseFileResponseToMap(HttpResponse httpResponse, boolean receiveAllInfo) {
        return receiveAllInfo ? JSONObject.parseObject(JSONObject.toJSONString(httpResponse),Map.class)
                : JSONObject.parseObject(JSON.toJSONString(httpResponse.getBodyMap().get("data")), Map.class);
    }
}
