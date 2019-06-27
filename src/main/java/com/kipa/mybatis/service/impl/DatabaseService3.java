package com.kipa.mybatis.service.impl;

import com.google.common.collect.Lists;
import com.kipa.mybatis.dao.mapper3.*;
import com.kipa.mybatis.type.SqlParser;
import com.kipa.mybatis.type.SqlType;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/22 15:59
 */
@Slf4j
public class DatabaseService3 extends AbstractDatabaseServiceImpl {

    private static final String TABLE_MESSAGE = "表名不能为空";

    @Autowired
    private DatabaseInfoMapper3 databaseInfoMapper3;

    @Autowired
    private InsertMapper3 insertMapper3;

    @Autowired
    private SelectMapper3 selectMapper3;

    @Autowired
    private UpdateMapper3 updateMapper3;

    @Autowired
    private DeleteMapper3 deleteMapper3;

    @Override
    public List<Map<String, Object>> listTableColumn(String tableName) {
        return databaseInfoMapper3.listTableColumn(tableName);
    }

    @Override
    public int insertParam(String tableName, Map<String, String> insertParamMap) {
        return insertMapper3.insert(tableName, insertParamMap);
    }

    @Override
    public int insert(String sql) {
        return insertMapper3.insertBySql(sql);
    }

    @Override
    public Map<String, Object> selectOne(String tableName, List<String> whereConditionList) {
        return selectMapper3.selectOneByCondition(tableName, whereConditionList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> getSelectListByCondition(String tableName, List<String> whereConditionList) {
        return selectMapper3.selectListByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Object> selectOne(String sql) {
        return selectMapper3.selectOneBySql(sql);
    }

    @Override
    public List<Map<String, Object>> selectList(String sql) {
        List<Map<String, Object>> list = Lists.newArrayList();
        List<LinkedHashMap<String, Object>> linkedHashMaps = selectMapper3.selectListBySql(sql);
        list.addAll(linkedHashMaps);
        return list;
    }

    @Override
    public Long count(String tableName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper3.countByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Long> countColumn(String tableName, String columnName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper3.countColumnByCondition(tableName, columnName, whereConditionList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> getSelectColumnByCondition(String tableName, List<String> columnNameList, List<String> whereConditionList) {
        return selectMapper3.selectColumnByCondition(tableName, columnNameList, whereConditionList);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getSelectPageByCondition(String tableName, List<String> whereConditionList, Integer pageNo, Integer pageSize) {
        return selectMapper3.selectPageByCondition(tableName, whereConditionList, pageNo, pageSize);
    }

    @Override
    public int update(String tableName, List<String> setFiledList, List<String> updateConditionList) {
        PreCheckUtils.checkEmpty(setFiledList, "修改的set值不能为空");
        PreCheckUtils.checkEmpty(updateConditionList, "修改的where条件不能为空");
        return updateMapper3.update(tableName, setFiledList, updateConditionList);
    }


    @Override
    public int update(String sql) {
        SqlParser.checkSql(sql, SqlType.UPDATE);
        return updateMapper3.updateBySql(sql);
    }

    @Override
    public int delete(String tableName, List<String> deleteConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return deleteMapper3.delete(tableName, deleteConditionList);
    }


    @Override
    public int delete(String sql) {
        SqlParser.checkSql(sql, SqlType.DELETE);
        return deleteMapper3.deleteBySql(sql);
    }
}
