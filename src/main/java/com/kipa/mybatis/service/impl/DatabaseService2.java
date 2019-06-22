package com.kipa.mybatis.service.impl;

import com.google.common.collect.Lists;
import com.kipa.mybatis.dao.mapper2.DeleteMapper2;
import com.kipa.mybatis.dao.mapper2.InsertMapper2;
import com.kipa.mybatis.dao.mapper2.SelectMapper2;
import com.kipa.mybatis.dao.mapper2.UpdateMapper2;
import com.kipa.mybatis.service.type.TypeHelper2;
import com.kipa.mybatis.type.SqlParser;
import com.kipa.mybatis.type.SqlType;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/22 15:59
 */
@Service("databaseService2")
@Slf4j
public class DatabaseService2 extends AbstractDatabaseServiceImpl {

    private static final String TABLE_MESSAGE = "表名不能为空";

    @Autowired
    private TypeHelper2 typeHelper2;

    @Autowired
    private InsertMapper2 insertMapper2;

    @Autowired
    private SelectMapper2 selectMapper2;

    @Autowired
    private UpdateMapper2 updateMapper2;

    @Autowired
    private DeleteMapper2 deleteMapper2;

    @Override
    public String getColumnType(String tableName, String columnName) {
        return typeHelper2.getColumnType(tableName, columnName);
    }

    @Override
    public int insertParam(String tableName, Map<String, String> insertParamMap) {
        return insertMapper2.insert(tableName, insertParamMap);
    }

    @Override
    public int insert(String sql) {
        return insertMapper2.insertBySql(sql);
    }

    @Override
    public Map<String, Object> selectOne(String tableName, List<String> whereConditionList) {
        return selectMapper2.selectOneByCondition(tableName, whereConditionList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> getSelectListByCondition(String tableName, List<String> whereConditionList) {
        return selectMapper2.selectListByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Object> selectOne(String sql) {
        return selectMapper2.selectOneBySql(sql);
    }

    @Override
    public List<Map<String, Object>> selectList(String sql) {
        List<Map<String, Object>> list = Lists.newArrayList();
        List<LinkedHashMap<String, Object>> linkedHashMaps = selectMapper2.selectListBySql(sql);
        list.addAll(linkedHashMaps);
        return list;
    }

    @Override
    public Long count(String tableName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper2.countByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Long> countColumn(String tableName, String columnName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper2.countColumnByCondition(tableName, columnName, whereConditionList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> getSelectColumnByCondition(String tableName, List<String> columnNameList, List<String> whereConditionList) {
        return selectMapper2.selectColumnByCondition(tableName, columnNameList, whereConditionList);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getSelectPageByCondition(String tableName, List<String> whereConditionList, Integer pageNo, Integer pageSize) {
        return selectMapper2.selectPageByCondition(tableName, whereConditionList, pageNo, pageSize);
    }

    @Override
    public int update(String tableName, List<String> setFiledList, List<String> updateConditionList) {
        PreCheckUtils.checkEmpty(setFiledList, "修改的set值不能为空");
        PreCheckUtils.checkEmpty(updateConditionList, "修改的where条件不能为空");
        return updateMapper2.update(tableName, setFiledList, updateConditionList);
    }


    @Override
    public int update(String sql) {
        SqlParser.checkSql(sql, SqlType.UPDATE);
        return updateMapper2.updateBySql(sql);
    }

    @Override
    public int delete(String tableName, List<String> deleteConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return deleteMapper2.delete(tableName, deleteConditionList);
    }


    @Override
    public int delete(String sql) {
        SqlParser.checkSql(sql, SqlType.DELETE);
        return deleteMapper2.deleteBySql(sql);
    }
}
