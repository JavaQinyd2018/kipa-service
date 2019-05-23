package com.kipa.data.csv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kipa.check.DataConstant;
import com.kipa.utils.CamelToUnderlineUtils;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.ReflectUtils;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author: Qinyadong
 * @date: 2019/5/17 18:06
 * 行模式csv文件解析器
 */
@Slf4j
final class TransverseCSVResolver {

    private TransverseCSVResolver() {}

    static List<Map<String,Object>> parseTransverseCSVFile(String csvFilePath) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        PreCheckUtils.checkEmpty(csvFilePath, "csv文件的路径不能为空");
        csvFilePath = String.format("%s%s", DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        log.debug("解析的csv文件的路径为：{}",csvFilePath);
        CSVReader csvReader = AroundHandler.getReader(csvFilePath);
        try {
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();
            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("csv内容不能为空");
            }

            list.forEach(strings -> {
                if (headers.length != strings.length) {
                    throw new RuntimeException("csv文件格式不正确");
                }
                if (ArrayUtils.isEmpty(strings)) {
                    throw new RuntimeException("csv文件中不允许存在空行");
                }
                Map<String, Object> map = Maps.newLinkedHashMap();
                for (int i = 0; i < headers.length; i++) {
                    if (StringUtils.isBlank(strings[i])) {
                        continue;
                    }
                    map.put(headers[i], strings[i]);
                }
                mapList.add(map);
            });
        } catch (IOException e) {
            throw new RuntimeException("解析csv文件失败",e);
        }
        return mapList;
    }


    /**
     * 转化为带下划线
     * @param csvFilePath
     * @return
     */
    static List<Map<String,Object>> parseCSVFileToUnderline(String csvFilePath) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        PreCheckUtils.checkEmpty(csvFilePath, "csv文件的路径不能为空");
        csvFilePath = String.format("%s%s",DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        log.debug("解析的csv文件的路径为：{}",csvFilePath);

        CSVReader csvReader = AroundHandler.getReader(csvFilePath);
        try {
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();
            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("csv内容不能为空");
            }

            list.forEach(strings -> {
                if (headers.length != strings.length) {
                    throw new RuntimeException("csv文件格式不正确");
                }
                if (ArrayUtils.isEmpty(strings)) {
                    throw new RuntimeException("csv文件中不允许存在空行");
                }
                Map<String, Object> map = Maps.newLinkedHashMap();
                for (int i = 0; i < headers.length; i++) {
                    if (StringUtils.isBlank(strings[i])) {
                        continue;
                    }
                    String property = CamelToUnderlineUtils.camelToUnderline(headers[i]);
                    map.put(property, strings[i]);
                }
                mapList.add(map);
            });
        } catch (IOException e) {
            throw new RuntimeException("解析csv文件失败",e);
        }
        return mapList;
    }

    /**
     * 创建行模式的csv文件
     * @param clazz
     * @param relativePath
     */
    static void createTransverseCsvFile(Class<?> clazz, String relativePath) {

        PreCheckUtils.checkEmpty(relativePath, "csv文件的相对路径不能为空");
        List<Field> fieldList= ReflectUtils.getAllFieldName(clazz);
        List<String> fieldNameList = fieldList.stream().map(Field::getName).collect(Collectors.toList());
        List<String[]> list = Lists.newArrayList();
        list.add(ArrayUtils.toStringArray(fieldNameList.toArray()));
        AroundHandler.write(list, relativePath);
    }

    /**
     * 创建行模式的csv文件
     * @param relativePath
     */
    static void createTransverseCsvFile(final Map<String, Object> map, String relativePath) {

        PreCheckUtils.checkEmpty(relativePath, "csv文件的相对路径不能为空");
        ArrayList<String>  fieldNameList = new ArrayList<>(map.keySet());
        List<String[]> list = Lists.newArrayList();
        list.add(ArrayUtils.toStringArray(fieldNameList.toArray()));
        AroundHandler.write(list, relativePath);
    }

    static <T> void writeCsvFileWithEntityData(List<T> entityList, String csvFilePath) {
        PreCheckUtils.checkEmpty(entityList, "实体对象信息不能空");
        PreCheckUtils.checkEmpty(csvFilePath, "生成的csv文件路径不能为空");
        List<String[]> list = new ArrayList<>(entityList.size() + 1);
        Class<?> clazz = entityList.get(0).getClass();
        List<Field> fieldList= ReflectUtils.getAllFieldName(clazz);
        List<String> fieldNameList = fieldList.stream().map(Field::getName).collect(Collectors.toList());
        list.add(ArrayUtils.toStringArray(fieldNameList.toArray()));
        if (CollectionUtils.isNotEmpty(fieldList)) {
            entityList.forEach(t -> {
                List<String> value = Lists.newArrayList();
                fieldNameList.forEach(field -> {
                    Method method = ReflectUtils.getGetMethod(clazz, field);
                    Object o = ReflectionUtils.invokeMethod(method, t);
                    if (o instanceof String) {
                        value.add((String)o);
                    }else if (o instanceof Enum) {
                        Enum anEnum=(Enum)o;
                        value.add(anEnum.name());
                    }else {
                        value.add(JSON.toJSONString(o));
                    }
                    list.add(ArrayUtils.toStringArray(value.toArray()));
                });
            });
        }

        AroundHandler.write(list, csvFilePath);
    }

    static void writeVerticalCsvFileWithData(List<Map<String, Object>> entityList, String csvFilePath) {
        PreCheckUtils.checkEmpty(entityList, "插入的数据集合为空");
        PreCheckUtils.checkEmpty(csvFilePath, "生成的csv文件路径不能为空");
        List<String[]> list = new ArrayList<>(entityList.size() + 1);
        ArrayList<String> fieldNameList = new ArrayList<>(entityList.get(0).keySet());
        list.add(ArrayUtils.toStringArray(fieldNameList.toArray()));
        if (CollectionUtils.isNotEmpty(fieldNameList)) {
            entityList.forEach(map -> {
                List<String> value = Lists.newArrayList();
                fieldNameList.forEach(fieldName -> {
                    Object o = map.get(fieldName);
                    if (o instanceof String) {
                        value.add((String)o);
                    }else if (o instanceof Enum) {
                        Enum anEnum=(Enum)o;
                        value.add(anEnum.name());
                    }else {
                        value.add(JSON.toJSONString(o));
                    }
                    list.add(ArrayUtils.toStringArray(value.toArray()));
                });
            });
        }
        AroundHandler.write(list,csvFilePath);
    }


    static List<Map<String, Object>> parseTransverseWithJson2List(String csvFilePath) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        PreCheckUtils.checkEmpty(csvFilePath, "csv文件的路径不能为空");
        csvFilePath = String.format("%s%s", DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        log.debug("解析的csv文件的路径为：{}",csvFilePath);
        CSVReader csvReader = AroundHandler.getReader(csvFilePath);
        try {
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();
            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("csv内容不能为空");
            }

            list.forEach(strings -> {
                if (headers.length != strings.length) {
                    throw new RuntimeException("csv文件格式不正确");
                }
                if (ArrayUtils.isEmpty(strings)) {
                    throw new RuntimeException("csv文件中不允许存在空行");
                }
                Map<String, Object> map = new ConcurrentHashMap<>(headers.length);
                for (int i = 0; i < headers.length; i++) {
                    if (StringUtils.isBlank(strings[i])) {
                        continue;
                    }
                    String field = headers[i];
                    String value = strings[i].trim();
                    if (StringUtils.startsWith(value, "{") && StringUtils.endsWith(value, "}")) {
                        Map valueMap = JSON.parseObject(value, Map.class);
                        map.put(field, valueMap);
                    }else if (StringUtils.startsWith(value, "[") && StringUtils.endsWith(value,"]")) {
                        List<Map> valueMapList = JSONArray.parseArray(value, Map.class);

                        map.put(field, valueMapList);
                    }else {
                        map.put(field ,value);
                    }
                }
                mapList.add(map);
            });
        } catch (IOException e) {
            throw new RuntimeException("解析csv文件失败",e);
        }
        return mapList;
    }
}
