package com.kipa.mybatis.service.impl;

import com.google.common.collect.Lists;
import com.kipa.mybatis.dao.mapper1.*;
import com.kipa.mybatis.ext.SqlParser;
import com.kipa.mybatis.ext.SqlType;
import com.kipa.utils.PreCheckUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/22 15:59
 */
public class DatabaseService1 extends AbstractDatabaseServiceImpl {

    private static final String TABLE_MESSAGE = "表名不能为空";

    @Autowired
    private DatabaseInfoMapper1 databaseInfoMapper1;

    @Autowired
    private InsertMapper1 insertMapper1;

    @Autowired
    private SelectMapper1 selectMapper1;

    @Autowired
    private UpdateMapper1 updateMapper1;

    @Autowired
    private DeleteMapper1 deleteMapper1;


    @Override
    public List<Map<String, Object>> listTableColumn(String tableName) {
        return databaseInfoMapper1.listTableColumn(tableName);
    }

    @Override
    public int insertParam(String tableName, Map<String, String> insertParamMap) {
        return insertMapper1.insert(tableName, insertParamMap);
    }

    @Override
    public int insert(String sql) {
        return insertMapper1.insertBySql(sql);
    }

    @Override
    public Map<String, Object> selectOne(String tableName, List<String> whereConditionList) {
        return selectMapper1.selectOneByCondition(tableName, whereConditionList);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getSelectListByCondition(String tableName, List<String> whereConditionList) {
        return selectMapper1.selectListByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Object> selectOne(String sql) {
        return selectMapper1.selectOneBySql(sql);
    }

    @Override
    public List<Map<String, Object>> selectList(String sql) {
        List<Map<String, Object>> list = Lists.newArrayList();
        List<LinkedHashMap<String, Object>> linkedHashMaps = selectMapper1.selectListBySql(sql);
        list.addAll(linkedHashMaps);
        return list;
    }

    @Override
    public Long count(String tableName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper1.countByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Long> countColumn(String tableName, String columnName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper1.countColumnByCondition(tableName, columnName, whereConditionList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> getSelectColumnByCondition(String tableName, List<String> columnNameList, List<String> whereConditionList) {
        return selectMapper1.selectColumnByCondition(tableName, columnNameList, whereConditionList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> getSelectPageByCondition(String tableName, List<String> whereConditionList, Integer pageNo, Integer pageSize) {
        return selectMapper1.selectPageByCondition(tableName, whereConditionList, pageNo, pageSize);
    }

    @Override
    public int update(String tableName, List<String> setFiledList, List<String> updateConditionList) {
        PreCheckUtils.checkEmpty(setFiledList, "修改的set值不能为空");
        PreCheckUtils.checkEmpty(updateConditionList, "修改的where条件不能为空");
        return updateMapper1.update(tableName, setFiledList, updateConditionList);
    }


    @Override
    public int update(String sql) {
        SqlParser.checkSql(sql, SqlType.UPDATE);
        return updateMapper1.updateBySql(sql);
    }

    @Override
    public int delete(String tableName, List<String> deleteConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return deleteMapper1.delete(tableName, deleteConditionList);
    }

    @Override
    public int delete(String sql) {
        SqlParser.checkSql(sql, SqlType.DELETE);
        return deleteMapper1.deleteBySql(sql);
    }

}
