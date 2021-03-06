package com.kipa.mybatis.service;

import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/22 15:55
 */
public interface BaseDatabaseService {

    int insert(String tableName, Map<String, Object> paramMap);

    int insert(String sql);

    int batchInsert(String tableName, String csvFilePath);

    int batchInsert(String tableName, List<Map<String, Object>> paramMapList);

    Map<String,Object> selectOne(String sql);

    Map<String,Object> selectOne(String tableName, String condition);

    Map<String, Object> selectOne(String tableName, List<String> whereConditionList);

    Map<String, Object> selectOne(String tableName,Map<String, Object> whereConditionMap);

    List<Map<String,Object>> selectList(String sql);

    List<Map<String,Object>> selectList(String tableName, String condition);

    List<Map<String,Object>> selectList(String tableName, List<String> whereConditionList);

    List<Map<String,Object>> selectList(String tableName,Map<String, Object> whereConditionMap);

    Long count(String sql);

    Long count(String tableName, String condition);

    Long count(String tableName, List<String> whereConditionList);

    Long count(String tableName, Map<String, Object> whereConditionMap);

    Map<String,Long> countColumn(String sql);

    Map<String,Long> countColumn(String tableName, String columnName, String condition);

    Map<String,Long> countColumn( String tableName, String columnName, List<String> whereConditionList);

    Map<String,Long> countColumn( String tableName, String columnName, Map<String, Object> whereConditionMapt);

    List<Map<String,Object>> selectColumn(String sql);

    List<Map<String,Object>> selectColumn(String tableName, String columnName, String condition);

    List<Map<String,Object>> selectColumn(String tableName, List<String> columnNameList, List<String> whereConditionList);

    List<Map<String,Object>> selectColumn(String tableName, List<String> columnNameList, Map<String, Object> whereConditionMap);

    List<Map<String, Object>> selectPage(String sql);

    List<Map<String, Object>> selectPage(String tableName, String condition, Integer pageNo, Integer pageSize);

    List<Map<String, Object>> selectPage(String tableName, Map<String, Object> whereConditionMap, Integer pageNo, Integer pageSize);

    List<Map<String, Object>> selectPage(String tableName, List<String> whereConditionList, Integer pageNo, Integer pageSize);

    int update(String sql);

    int update(String tableName, String setField, String condition);

    int update(String tableName, List<String> setFiledList, List<String> updateConditionList);

    int update(String tableName, Map<String,Object> setFiledMap, Map<String,Object> updateConditionMap);

    int delete(String sql);

    int delete(String tableName, String condition);

    int delete(String tableName, List<String> deleteConditionList);

    int delete(String tableName, Map<String,Object> deleteConditionMap);

    int batchDelete(Map<String, String> tableNameAndConditionMap);
}
