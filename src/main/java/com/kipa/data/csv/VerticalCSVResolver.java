package com.kipa.data.csv;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.util.stream.Collectors;

/**
 * @author: Qinyadong
 * @date: 2019/5/17 18:39
 * 列模式csv文件解析器
 */
@Slf4j
final class VerticalCSVResolver {

    private VerticalCSVResolver() {}

    /**
     * 根据下标获取csv对应的数据
     * @param csvFilePath
     * @param index
     * @return
     */
    static Map<String, Object> parseVerticalCsvFile(String csvFilePath, int index) {
        Map<String, Object> stringMap = Maps.newLinkedHashMap();
        PreCheckUtils.checkEmpty(csvFilePath, "列模式csv文件路径不能为空");
        csvFilePath = String.format("%s%s", DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        log.debug("解析的csv文件路径为：{}",csvFilePath);
        CSVReader csvReader = AroundHandler.getReader(csvFilePath);
        try {
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();

            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("csv内容不能为空");
            }

            int max = headers.length - 3;
            if (index < 0 || index > max) {
                throw new IllegalArgumentException("索引超出范围不存在数据，索引index的范围为：[0-"+max + "]");
            }

            list.forEach(strings -> {
                if (headers.length != strings.length || headers.length < 4) {
                    throw new RuntimeException("csv文件格式不正确或者数据为空");
                }
                if (ArrayUtils.isEmpty(strings)) {
                    throw new RuntimeException("csv文件中不允许存在空行");
                }
                String property = strings[1];
                String flag = strings[2];

                if (StringUtils.equalsIgnoreCase(flag, "N")) {
                    return;
                }

                stringMap.put(property, strings[3+index]);
            });

        } catch (IOException e) {
            throw new RuntimeException("解析csv文件失败",e);
        }
        return stringMap;
    }


    /**
     * 根据下标获取csv对应的数据
     * @param csvFilePath
     * @param index
     * @return 返回的结果封装了 value -- csv解析的字段和值map，condition 是标识有C的组成sql语句的查询条件，class--实体类的名
     * map.put("value", stringMap);
     * map.put("condition", conditionMap);
     * map.put("class", className);
     */
    static Map<String, Object> parseVerticalCsvFileWithCondition(String csvFilePath, int index) {
        Map<String, Object> map = Maps.newLinkedHashMap();
        PreCheckUtils.checkEmpty(csvFilePath, "csv数据文件路径不能为空");
        csvFilePath = String.format("%s%s",DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        log.debug("解析的csv文件路径为：{}",csvFilePath);
        CSVReader csvReader = AroundHandler.getReader(csvFilePath);

        try {
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();

            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("csv内容不能为空");
            }

            int max = headers.length - 3;
            if (index < 0 || index > max) {
                throw new IllegalArgumentException("索引超出范围不存在数据，索引index的范围为：[0-"+max + "]");
            }

            Map<String,String> conditionMap = Maps.newLinkedHashMap();
            Map<String, String> stringMap = Maps.newLinkedHashMap();
            String className = list.get(0)[0];
            list.forEach(strings -> {
                if (headers.length != strings.length || headers.length < 4) {
                    throw new RuntimeException("csv文件格式不正确或者数据为空");
                }
                if (ArrayUtils.isEmpty(strings)) {
                    throw new RuntimeException("csv文件中不允许存在空行");
                }
                String property = strings[1];
                String flag = strings[2];

                if (StringUtils.equalsIgnoreCase(flag, "N")) {
                    return;
                }
                if (StringUtils.equalsIgnoreCase(flag, "C")) {
                    conditionMap.put(property, strings[3+index]);
                    return;
                }
                stringMap.put(property, strings[3+index]);
            });

            map.put("value", stringMap);
            map.put("condition", conditionMap);
            map.put("class", className);

        } catch (IOException e) {
            throw new RuntimeException("解析csv文件失败",e);
        }
        return map;
    }

    /**
     * 解析 列模式的csv文件
     * @param csvFilePath 列模式的csv文件
     * @return
     */
    static List<Map<String, Object>> parseVerticalCsvFile(String csvFilePath, CSVType csvType) {
        int index = 0;
        List<Map<String, Object>> mapList = Lists.newArrayList();
        PreCheckUtils.checkEmpty(csvFilePath, "csv数据文件路径不能为空");
        csvFilePath = String.format("%s%s",DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        log.debug("解析的csv文件路径为：{}",csvFilePath);
        CSVReader csvReader = AroundHandler.getReader(csvFilePath);
        try {
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();

            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("csv内容不能为空");
            }

            while (index < headers.length - 3) {
                Map<String, Object> map = Maps.newLinkedHashMap();

                for (String[] strings : list) {
                    if (headers.length != strings.length || headers.length < 4) {
                        throw new RuntimeException("csv文件格式不正确或者数据为空");
                    }
                    String property = csvType == CSVType.CHECK_DB ? CamelToUnderlineUtils.underlineToCamel(strings[1]) : strings[1];
                    String flag = strings[2];

                    if (StringUtils.equalsIgnoreCase(flag, "N")) {
                        continue;
                    }
                    map.put(property, strings[3+index]);
                }
                mapList.add(map);
                index ++;
            }

        } catch (IOException e) {
            throw new RuntimeException("解析csv文件失败",e);
        }
        return mapList;
    }


    /**
     * 解析 列模式的csv文件
     * @param csvFilePath 列模式的csv文件
     * @return
     */
    static List<Map<String, Object>> parseVerticalCsvFileWithCondition(String csvFilePath) {
        int index = 0;
        List<Map<String, Object>> mapList = Lists.newArrayList();
        PreCheckUtils.checkEmpty(csvFilePath, "csv数据文件路径不能为空");
        csvFilePath = String.format("%s%s",DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        log.debug("csv文件的路径为：{}",csvFilePath);
        CSVReader csvReader = AroundHandler.getReader(csvFilePath);
        try {
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();

            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("csv内容不能为空");
            }
            while (index < headers.length - 3) {
                Map<String, Object> map = Maps.newLinkedHashMap();
                Map<String,String> conditionMap = Maps.newLinkedHashMap();
                Map<String, String> stringMap = Maps.newLinkedHashMap();
                String className = list.get(0)[0];
                for (String[] strings : list) {
                    if (headers.length != strings.length || headers.length < 4) {
                        throw new RuntimeException("csv文件格式不正确或者数据为空");
                    }
                    String property = strings[1];
                    String flag = strings[2];

                    if (StringUtils.equalsIgnoreCase(flag, "N")) {
                        continue;
                    }
                    if (StringUtils.equalsIgnoreCase(flag, "C")) {
                        conditionMap.put(property, strings[3+index]);
                        continue;
                    }
                    stringMap.put(property, strings[3+index]);
                    map.put("value", stringMap);
                    map.put("condition", conditionMap);
                    map.put("class", className);
                }
                mapList.add(map);
                index ++;
            }

        } catch (IOException e) {
            throw new RuntimeException("解析csv文件失败",e);
        }
        return mapList;
    }

    /**
     * 创建列表csv文件
     * @param clazz
     * @param relativePath
     */
    static void createVerticalCsvFile(Class<?> clazz, String relativePath) {
        PreCheckUtils.checkEmpty(relativePath, "参数csv文件的相对路径不能为空");
        List<Field> fieldList= ReflectUtils.getAllFieldName(clazz);
        List<String> fieldNameList = fieldList.stream().map(Field::getName).collect(Collectors.toList());
        List<String[]> list = Lists.newArrayList();
        list.add(new String[]{"class","property","flag","exp"});
        for (int i = 0; i < fieldNameList.size(); i++) {
            String[] targetArray = new String[4];
            if (i == 0) {
                targetArray[0] = clazz.getSimpleName();
            }else {
                targetArray[0] = "";
            }
            targetArray[1] = fieldNameList.get(i);
            targetArray[2] = "Y";
            targetArray[3] = "";
            list.add(targetArray);
        }
        AroundHandler.write(list, relativePath);
    }

    /**
     * 创建列表csv文件
     * @param clazz
     * @param relativePath
     */
    static void createUnderlineCsvFile(Class<?> clazz, String relativePath) {
        PreCheckUtils.checkEmpty(relativePath, "参数csv文件的相对路径不能为空");
        List<Field> fieldList= ReflectUtils.getAllFieldName(clazz);
        List<String> fieldNameList = fieldList.stream().map(Field::getName).collect(Collectors.toList());
        List<String[]> list = Lists.newArrayList();
        list.add(new String[]{"class","property","flag","exp"});
        for (int i = 0; i < fieldNameList.size(); i++) {
            String[] targetArray = new String[4];
            if (i == 0) {
                targetArray[0] = CamelToUnderlineUtils.camelToUnderline(clazz.getSimpleName());
            }else {
                targetArray[0] = "";
            }
            targetArray[1] = CamelToUnderlineUtils.camelToUnderline(fieldNameList.get(i));
            targetArray[2] = "Y";
            targetArray[3] = "";
            list.add(targetArray);
        }
        AroundHandler.write(list, relativePath);
    }

    /**
     * 创建列表csv文件
     * @param relativePath
     */
    static void createUnderlineCsvFile(Map<String, Object> map, String relativePath) {
        PreCheckUtils.checkEmpty(relativePath, "参数csv文件的相对路径不能为空");
        List<String> fieldNameList = new ArrayList<>(map.keySet());
        List<String[]> list = Lists.newArrayList();
        list.add(new String[]{"class","property","flag","exp"});
        for (int i = 0; i < fieldNameList.size(); i++) {
            String[] targetArray = new String[4];
            if (i == 0) {
                targetArray[0] = "map";
            }else {
                targetArray[0] = "";
            }
            targetArray[1] = CamelToUnderlineUtils.camelToUnderline(fieldNameList.get(i));
            targetArray[2] = "Y";
            targetArray[3] = "";
            list.add(targetArray);
        }
        AroundHandler.write(list, relativePath);
    }

    /**
     * 创建列表csv文件
     * * @param relativePath
     */
    static void createVerticalCsvFile(Map<String, Object> map, String relativePath) {
        PreCheckUtils.checkEmpty(relativePath,"参数不能为空");
        PreCheckUtils.checkEmpty(map, "map不能为空");
        //所有的key
        List<String> fieldNameList = new ArrayList<>(map.keySet());
        List<String[]> list = Lists.newArrayList();
        list.add(new String[]{"class","property","flag","exp"});
        for (int i = 0; i < fieldNameList.size(); i++) {
            String[] targetArray = new String[4];
            targetArray[0] = i == 0 ?"map" : "";
            targetArray[1] = fieldNameList.get(i);
            targetArray[2] = "Y";
            targetArray[3] = "";
            list.add(targetArray);
        }
        AroundHandler.write(list, relativePath);
    }


    /**
     * 解析有大字段有json的csv文件
     * @param csvFilePath 其中Y-会去拼成字段，N-不会拼成字段，M-json格式为{"username":"kobe","password":"123456"}
     *                    L-json的格式为：[{"username":"kobe","password":"123456"},{"username":"kobe","password":"123456"}]
     * @return
     */
    static  List<Map<String, Object>> parseVerticalWithJson2List(String csvFilePath, CSVType csvType) {
        PreCheckUtils.checkEmpty(csvFilePath, "csv文件路径不能为空");
        int index = 0;
        List<Map<String, Object>> mapList = Lists.newArrayList();
        csvFilePath = String.format("%s%s",DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        log.debug("csv的文件路径为：{}",csvFilePath);
        CSVReader csvReader = AroundHandler.getReader(csvFilePath);
        try {
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();

            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("csv内容不能为空");
            }
            while (index < headers.length - 3) {
                Map<String, Object> stringMap = Maps.newLinkedHashMap();
                for (String[] strings : list) {
                    if (headers.length != strings.length || headers.length < 4) {
                        throw new RuntimeException("csv文件格式不正确或者数据为空");
                    }
                    String property = csvType == CSVType.CHECK_DB ? CamelToUnderlineUtils.underlineToCamel(strings[1]) : strings[1];
                    String flag = strings[2];

                    if (StringUtils.equalsIgnoreCase(flag, "N")) {
                        continue;
                    }
                    String value = strings[3 + index].trim();
                    if (StringUtils.isBlank(value)) {
                        continue;
                    }
                    if (StringUtils.equalsIgnoreCase(flag, "M")) {
                        if (!(value.startsWith("{") && value.endsWith("}"))) {
                            throw new IllegalArgumentException("flag为M的值，必须是一个以'{}'格式的json串");
                        }
                        JSONObject jsonObject = JSONObject.parseObject(value);
                        stringMap.put(property, jsonObject);
                    }else if (StringUtils.equalsIgnoreCase(flag, "L")){
                        if (!(value.startsWith("[") && value.endsWith("]"))) {
                            throw new IllegalArgumentException("flag为M的值，必须是一个以'[]'格式的json串");
                        }
                        JSONArray jsonArray = JSONArray.parseArray(value);
                        stringMap.put(property, jsonArray);
                    }else {
                        stringMap.put(property, value);
                    }
                }
                mapList.add(stringMap);
                index ++;
            }

        } catch (Exception e) {
            throw new RuntimeException("解析csv文件失败",e);
        }
        return mapList;
    }

    /**
     * 通过将实体类对象的数据转化为csv文件
     * @param entityList
     * @param csvFilePath 生成csv文件相对路径
     * @param csvType
     * @param <T>
     */
    static <T> void writeVerticalCsvFileByEntity(List<T> entityList, String csvFilePath, CSVType csvType) {

        if (CollectionUtils.isNotEmpty(entityList)) {
            Class<?> clazz = entityList.get(0).getClass();
            List<String> collect = ReflectUtils.getAllFieldName(clazz).stream().map(Field::getName).collect(Collectors.toList());
            //csv文件的行数=字段集合的长度+1
            List<String[]> list = new ArrayList<>(collect.size() +1);
            String[] strings = new String[entityList.size() +3];
            strings[0] = "class";
            strings[1] = "property";
            strings[2] = "flag";
            for (int i = 3; i < strings.length; i++) {
                strings[i] = "exp";
            }
            list.add(strings);
            for (int index = 0; index < collect.size(); index++) {
                String fileName = collect.get(index);
                //csv一共有对象长度+3个列，前面三个列分别为class， property，flag
                String[] stringArray = new String[entityList.size() +3];
                String simpleName = clazz.getSimpleName();
                if (index == 0) {
                    stringArray[0] = csvType == CSVType.CHECK_DB ?  CamelToUnderlineUtils.camelToUnderline(simpleName):simpleName;
                }else {
                    stringArray[0] = "";
                }
                stringArray[1] = csvType == CSVType.CHECK_DB ?  CamelToUnderlineUtils.camelToUnderline(fileName):fileName;
                stringArray[2] = "Y";
                for (int i = 0; i < entityList.size(); i++) {
                    Method getMethod = ReflectUtils.getGetOrIsMethod(clazz, fileName);
                    Object o = ReflectionUtils.invokeMethod(getMethod, entityList.get(i));
                    if (o instanceof String) {
                        stringArray[i + 3] = (String)o;
                    }else if (o instanceof Enum){
                        stringArray[i + 3] = ((Enum)o).name();
                    }else {
                        stringArray[i + 3] = JSONObject.toJSONString(o);
                    }
                }

                list.add(stringArray);
            }
            AroundHandler.write(list, csvFilePath);
        }
    }

    /**
     * 根据map生成csv文件
     * @param entityList
     * @param csvFilePath 生成csv文件相对路径
     * @param csvType
     */
    static void writeVerticalCsvFile(List<Map<String, Object>> entityList, String csvFilePath, CSVType csvType) {

        if (CollectionUtils.isNotEmpty(entityList)) {
            ArrayList<String> collect = new ArrayList<>(entityList.get(0).keySet());
            //csv文件的行数=字段集合的长度+1
            List<String[]> list = new ArrayList<>(collect.size() +1);
            String[] strings = new String[entityList.size() +3];
            strings[0] = "class";
            strings[1] = "property";
            strings[2] = "flag";
            for (int i = 3; i < strings.length; i++) {
                strings[i] = "exp";
            }
            list.add(strings);
            for (int index = 0; index < collect.size(); index++) {
                String fileName = collect.get(index);
                //csv一共有对象长度+3个列，前面三个列分别为class， property，flag
                String[] stringArray = new String[entityList.size() +3];
                if (index == 0) {
                    stringArray[0] = "map";
                }else {
                    stringArray[0] = "";
                }
                stringArray[1] = csvType == CSVType.CHECK_DB ?  CamelToUnderlineUtils.camelToUnderline(fileName):fileName;
                stringArray[2] = "Y";
                for (int i = 0; i < entityList.size(); i++) {
                    Object o = entityList.get(i).get(fileName);
                    if (o instanceof String) {
                        stringArray[i + 3] = (String)o;
                    }else if (o instanceof Enum){
                        stringArray[i + 3] = ((Enum)o).name();
                    }else {
                        stringArray[i + 3] = JSONObject.toJSONString(o);
                    }
                }

                list.add(stringArray);
            }
            AroundHandler.write(list, csvFilePath);
        }
    }


    static Map<String, Object> parseVerticalWithJson2Map(String csvFilePath, int index, CSVType csvType) {
        PreCheckUtils.checkEmpty(csvFilePath, "csv文件路径不能为空");
        csvFilePath = String.format("%s%s",DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        Map<String, Object> stringMap = Maps.newLinkedHashMap();
        log.debug("csv的文件路径为：{}",csvFilePath);
        CSVReader csvReader = AroundHandler.getReader(csvFilePath);
        try {
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();

            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("csv内容不能为空");
            }
            int max = headers.length - 3;
            if (index < 0 || index > max) {
                throw new IllegalArgumentException("索引超出范围不存在数据，索引index的范围为：[0-"+max + "]");
            }

            for (String[] strings : list) {
                if (headers.length != strings.length || headers.length < 4) {
                    throw new RuntimeException("csv文件格式不正确或者数据为空");
                }
                String property = csvType == CSVType.CHECK_DB ? CamelToUnderlineUtils.underlineToCamel(strings[1]) : strings[1];
                String flag = strings[2];

                if (StringUtils.equalsIgnoreCase(flag, "N")) {
                    continue;
                }
                String value = strings[3 + index].trim();
                if (StringUtils.isBlank(value)) {
                    continue;
                }
                if (StringUtils.equalsIgnoreCase(flag, "M")) {
                    if (!StringUtils.startsWith(value, "{") || !StringUtils.endsWith(value, "}")) {
                        throw new IllegalArgumentException("flag为M的值，必须是一个以'{}'格式的json串");
                    }
                    JSONObject jsonObject = JSONObject.parseObject(value);
                    stringMap.put(property, jsonObject);
                }else if (StringUtils.equalsIgnoreCase(flag, "L")){
                    if (!StringUtils.startsWith(value, "[") || !StringUtils.endsWith(value, "]")) {
                        throw new IllegalArgumentException("flag为M的值，必须是一个以'[]'格式的json串");
                    }
                    JSONArray jsonArray = JSONArray.parseArray(value);
                    stringMap.put(property, jsonArray);
                }else {
                    stringMap.put(property, value);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("解析csv文件失败",e);
        }
        return stringMap;
    }
}
