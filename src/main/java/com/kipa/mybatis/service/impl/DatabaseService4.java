package com.kipa.mybatis.service.impl;

import com.google.common.collect.Lists;
import com.kipa.mybatis.dao.mapper4.*;
import com.kipa.mybatis.ext.SqlParser;
import com.kipa.mybatis.ext.SqlType;
import com.kipa.utils.PreCheckUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 4019/3/44 15:59
 */
public class DatabaseService4 extends AbstractDatabaseServiceImpl {

    private static final String TABLE_MESSAGE = "表名不能为空";

    @Autowired
    private DatabaseInfoMapper4 databaseInfoMapper4;

    @Autowired
    private InsertMapper4 insertMapper4;

    @Autowired
    private SelectMapper4 selectMapper4;

    @Autowired
    private UpdateMapper4 updateMapper4;

    @Autowired
    private DeleteMapper4 deleteMapper4;

    @Override
    public List<Map<String, Object>> listTableColumn(String tableName) {
        return databaseInfoMapper4.listTableColumn(tableName);
    }

    @Override
    public int insertParam(String tableName, Map<String, String> insertParamMap) {
        return insertMapper4.insert(tableName, insertParamMap);
    }

    @Override
    public int insert(String sql) {
        return insertMapper4.insertBySql(sql);
    }

    @Override
    public Map<String, Object> selectOne(String tableName, List<String> whereConditionList) {
        return selectMapper4.selectOneByCondition(tableName, whereConditionList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> getSelectListByCondition(String tableName, List<String> whereConditionList) {
        return selectMapper4.selectListByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Object> selectOne(String sql) {
        return selectMapper4.selectOneBySql(sql);
    }

    @Override
    public List<Map<String, Object>> selectList(String sql) {
        List<Map<String, Object>> list = Lists.newArrayList();
        List<LinkedHashMap<String, Object>> linkedHashMaps = selectMapper4.selectListBySql(sql);
        list.addAll(linkedHashMaps);
        return list;
    }

    @Override
    public Long count(String tableName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper4.countByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Long> countColumn(String tableName, String columnName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper4.countColumnByCondition(tableName, columnName, whereConditionList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> getSelectColumnByCondition(String tableName, List<String> columnNameList, List<String> whereConditionList) {
        return selectMapper4.selectColumnByCondition(tableName, columnNameList, whereConditionList);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getSelectPageByCondition(String tableName, List<String> whereConditionList, Integer pageNo, Integer pageSize) {
        return selectMapper4.selectPageByCondition(tableName, whereConditionList, pageNo, pageSize);
    }

    @Override
    public int update(String tableName, List<String> setFiledList, List<String> updateConditionList) {
        PreCheckUtils.checkEmpty(setFiledList, "修改的set值不能为空");
        PreCheckUtils.checkEmpty(updateConditionList, "修改的where条件不能为空");
        return updateMapper4.update(tableName, setFiledList, updateConditionList);
    }


    @Override
    public int update(String sql) {
        SqlParser.checkSql(sql, SqlType.UPDATE);
        return updateMapper4.updateBySql(sql);
    }

    @Override
    public int delete(String tableName, List<String> deleteConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return deleteMapper4.delete(tableName, deleteConditionList);
    }


    @Override
    public int delete(String sql) {
        SqlParser.checkSql(sql, SqlType.DELETE);
        return deleteMapper4.deleteBySql(sql);
    }
}
