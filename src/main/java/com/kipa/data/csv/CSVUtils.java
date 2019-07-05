package com.kipa.data.csv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kipa.common.KipaProcessException;
import org.apache.commons.collections4.CollectionUtils;
import java.util.List;
import java.util.Map;


/**
 * @Author: Qinyadong
 * @Date: 2019/1/9 15:30
 * Csv工具类
 */
public final class CSVUtils {

    private static final String MESSAGE = "json转化成实体对象失败";

    private CSVUtils() {}

    /**
     * 创建不带数据的csv文件
     * @param clazz csv文件对应的实体类
     * @param csvFilePath csv文件路径
     * @param csvType csv数据文件类型
     * @param <T>
     */
    public static <T> void createCsvFile(Class<T> clazz, String csvFilePath, CSVType csvType) {
        switch (csvType) {
            case TRANSVERSE:
                TransverseCSVResolver.createTransverseCsvFile(clazz, csvFilePath);
                break;
            case VERTICAL:
                VerticalCSVResolver.createVerticalCsvFile(clazz, csvFilePath);
                break;
            case CHECK_DB:
                VerticalCSVResolver.createUnderlineCsvFile(clazz, csvFilePath);
                break;
            default:
                break;
        }
    }

    /**
     * 通过map集合创建不带数据的csv文件
     * @param map 集合
     * @param csvFilePath csv文件路径
     * @param csvType csv数据文件类型
     */
    public static void createCsvFile(Map<String, Object> map, String csvFilePath, CSVType csvType) {
        switch (csvType) {
            case TRANSVERSE:
                TransverseCSVResolver.createTransverseCsvFile(map, csvFilePath);
                break;
            case VERTICAL:
                VerticalCSVResolver.createVerticalCsvFile(map, csvFilePath);
                break;
            case CHECK_DB:
                VerticalCSVResolver.createUnderlineCsvFile(map, csvFilePath);
                break;
            default:
                break;
        }
    }


    /**
     * 解析csv文件
     * @param csvFilePath
     * @param csvType
     * @return 封装成List<Map> 形式
     */
    public static List<Map<String,Object>>  parseCsvFile(String csvFilePath, CSVType csvType) {
        List<Map<String,Object>> list = Lists.newArrayList();
        switch (csvType) {
            case TRANSVERSE:
                list = TransverseCSVResolver.parseTransverseCSVFile(csvFilePath);
                break;
            case VERTICAL:
                list = VerticalCSVResolver.parseVerticalCsvFile(csvFilePath,CSVType.VERTICAL);
                break;
            case CHECK_DB:
                list = VerticalCSVResolver.parseVerticalCsvFile(csvFilePath,CSVType.CHECK_DB);
                break;
            default:
                break;
        }
        return list;
    }

    /**
     * 根据位置所以解析csv文件
     * @param csvFilePath
     * @param index 位置索引
     * @param csvType
     * @return 结果封装成map
     */
    public static Map<String, Object> parseCsvFile(String csvFilePath, int index, CSVType csvType) {
        Map<String, Object> map = Maps.newHashMap();
        switch (csvType) {
            case TRANSVERSE:
                List<Map<String, Object>> list = TransverseCSVResolver.parseTransverseCSVFile(csvFilePath);
                if (CollectionUtils.isNotEmpty(list)) {
                    if (index < 0 || index > list.size()-1) {
                        throw new IllegalArgumentException(String.format("index的取值范围为：[0-%s]",list.size() - 1));
                    }
                    map = list.get(index);
                }
                break;
            case VERTICAL:
                map = VerticalCSVResolver.parseVerticalCsvFile(csvFilePath, index);
                break;
            case CHECK_DB:
                List<Map<String, Object>> mapList = VerticalCSVResolver.parseVerticalCsvFile(csvFilePath, CSVType.CHECK_DB);
                map = CollectionUtils.isNotEmpty(mapList) ? mapList.get(index) : map;
                break;
            default:
                break;
        }
        return map;
    }

    /**
     * 解析 带有json字符串的csv数据文件
     * @param csvFilePath
     * @param csvType
     * @return
     */
    public static List<Map<String, Object>> parseWithJson2List(String csvFilePath, CSVType csvType) {
        List<Map<String, Object>> list = Lists.newArrayList();
        switch (csvType) {
            case TRANSVERSE:
                list = TransverseCSVResolver.parseTransverseWithJson2List(csvFilePath);
                break;
            case VERTICAL:
                list = VerticalCSVResolver.parseVerticalWithJson2List(csvFilePath,CSVType.VERTICAL);
                break;
            case CHECK_DB:
                list = VerticalCSVResolver.parseVerticalWithJson2List(csvFilePath,CSVType.CHECK_DB);
                break;
            default:
                break;
        }
        return list;
    }


    public static Map<String, Object> parseWithJson2Map(String csvFilePath, int index, CSVType csvType) {
        Map<String, Object> map = Maps.newHashMap();
        switch (csvType) {
            case TRANSVERSE:
                List<Map<String, Object>> list = TransverseCSVResolver.parseTransverseWithJson2List(csvFilePath);
                if (CollectionUtils.isNotEmpty(list)) {
                    if (index < 0 || index > list.size()-1) {
                        throw new IllegalArgumentException(String.format("index的取值范围为：[0-%s]",list.size() - 1));
                    }
                    map = list.get(index);
                }
                break;
            case VERTICAL:
                map =  VerticalCSVResolver.parseVerticalWithJson2Map(csvFilePath, index, CSVType.VERTICAL);
                break;
            case CHECK_DB:
                map =  VerticalCSVResolver.parseVerticalWithJson2Map(csvFilePath, index, CSVType.CHECK_DB);
                break;
            default:
                break;
        }
        return map;
    }

    /**
     * 写入带有实体类数据的csv问津
     * @param entityList 实体类
     * @param csvFilePath
     * @param csvType
     * @param <T> 泛型
     */
    public static <T> void writeCsvFileWithEntityData(List<T> entityList, String csvFilePath, CSVType csvType) {
        switch (csvType) {
            case TRANSVERSE:
                TransverseCSVResolver.writeCsvFileWithEntityData(entityList,csvFilePath);
                break;
            case VERTICAL:
                VerticalCSVResolver.writeVerticalCsvFileByEntity(entityList, csvFilePath, CSVType.VERTICAL);
                break;
            case CHECK_DB:
                VerticalCSVResolver.writeVerticalCsvFileByEntity(entityList, csvFilePath, CSVType.CHECK_DB);
                break;
                default:
                    break;
        }
    }


    /**
     * 通过List<Map> 形式的数据写入带有数据的csv文件
     * @param entityList
     * @param csvFilePath
     * @param csvType
     */
    public static void writeCsvFileWithData(List<Map<String, Object>> entityList, String csvFilePath, CSVType csvType) {
        switch (csvType) {
            case TRANSVERSE:
                TransverseCSVResolver.writeVerticalCsvFileWithData(entityList,csvFilePath);
                break;
            case VERTICAL:
                VerticalCSVResolver.writeVerticalCsvFile(entityList, csvFilePath, CSVType.VERTICAL);
                break;
            case CHECK_DB:
                VerticalCSVResolver.writeVerticalCsvFile(entityList, csvFilePath, CSVType.CHECK_DB);
                break;
            default:
                break;
        }
    }


    public static List<Map<String, Object>> parseCsvFileWithCondition(String csvFilePath) {
        return VerticalCSVResolver.parseVerticalCsvFileWithCondition(csvFilePath);
    }

    /**
     * 带条件的
     * @param csvFilePath csv文件路径
     * @return 返回的结果map里面有：
     * 获取类名
     * String className = map.get("class");
     * 获取所有的参数集合
     * Map<String, String> paramMap = map.get("value");
     * 3. 获取条件的map集合
     * Map<String, String> conditionMap = map.get("condition");
     */
    public static Map<String, Object> parseCsvFileWithCondition(String csvFilePath, int index) {
        return VerticalCSVResolver.parseVerticalCsvFileWithCondition(csvFilePath, index);
    }

    /**
     * 解析行模式的csv文件成实体类list
     * @param csvFilePath
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> convert2EntityList(Class<T> clazz, String csvFilePath, CSVType csvType) {
        List<Map<String, Object>> list = parseCsvFile(csvFilePath, csvType);
        String json = JSON.toJSONString(list);
        try {
            return JSONArray.parseArray(json, clazz);
        } catch (Exception e) {
            throw new KipaProcessException(MESSAGE, e);
        }
    }

    /**
     * csv文件转化成实体类
     * @param csvFilePath csv文件的相对路径
     * @param clazz 实体类的类型
     * @param index 第几行数据
     * @param <T>
     * @return
     */
    public static <T> T convert2Entity(Class<T> clazz, String csvFilePath, int index, CSVType csvType) {
        Map<String, Object> map = parseCsvFile(csvFilePath, index, csvType);
        String json = JSON.toJSONString(map);
        try {
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            throw new KipaProcessException(MESSAGE, e);
        }
    }

    /**
     * 可以是大对象。也可以是普通对象
     * @param clazz
     * @param csvFilePath
     * @param csvType
     * @param <T>
     * @return
     */
    public static <T> List<T> convert2BeanList(Class<T> clazz, String csvFilePath, CSVType csvType) {
        List<Map<String, Object>> list = parseWithJson2List(csvFilePath, csvType);
        String json = JSON.toJSONString(list);
        try {
            return JSONArray.parseArray(json, clazz);
        } catch (Exception e) {
            throw new KipaProcessException(MESSAGE, e);
        }
    }


    public static <T> T convert2Bean(Class<T> clazz, String csvFilePath, int index, CSVType csvType ) {
        Map<String, Object> map = parseWithJson2Map(csvFilePath, index, csvType);
        String json = JSON.toJSONString(map);
        try {
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            throw new KipaProcessException(MESSAGE, e);
        }
    }
}
