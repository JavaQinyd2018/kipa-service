package com.kipa.mybatis.service.impl;

import com.google.common.collect.Lists;
import com.kipa.mybatis.dao.mapper.DeleteMapper;
import com.kipa.mybatis.dao.mapper.InsertMapper;
import com.kipa.mybatis.dao.mapper.SelectMapper;
import com.kipa.mybatis.dao.mapper.UpdateMapper;
import com.kipa.mybatis.service.type.TypeHelper;
import com.kipa.utils.PreCheckUtils;
import com.kipa.mybatis.type.SqlParser;
import com.kipa.mybatis.type.SqlType;
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
@Service("databaseService")
@Slf4j
public class DatabaseService extends AbstractDatabaseServiceImpl {
    private static final String TABLE_MESSAGE = "表名不能为空";

    @Autowired
    private TypeHelper typeHelper;

    @Autowired
    private InsertMapper insertMapper;

    @Autowired
    private SelectMapper selectMapper;

    @Autowired
    private UpdateMapper updateMapper;

    @Autowired
    private DeleteMapper deleteMapper;

    @Override
    public String getColumnType(String tableName, String columnName) {
        return typeHelper.getColumnType(tableName, columnName);
    }

    @Override
    public int insertParam(String tableName, Map<String, String> insertParamMap) {
        return insertMapper.insert(tableName, insertParamMap);
    }

    @Override
    public int insert(String sql) {
        return insertMapper.insertBySql(sql);
    }

    @Override
    public Map<String, Object> selectOne(String tableName, List<String> whereConditionList) {
        return selectMapper.selectOneByCondition(tableName, whereConditionList);
    }

    @Override
    public List<Map<String, Object>> selectList(String tableName, List<String> whereConditionList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        List<LinkedHashMap<String, Object>> linkedHashMaps = selectMapper.selectListByCondition(tableName, whereConditionList);
        list.addAll(linkedHashMaps);
        return list;
    }

    @Override
    public Map<String, Object> selectOne(String sql) {
        return selectMapper.selectOneBySql(sql);
    }

    @Override
    public List<Map<String, Object>> selectList(String sql) {
        List<Map<String, Object>> list = Lists.newArrayList();
        List<LinkedHashMap<String, Object>> linkedHashMaps = selectMapper.selectListBySql(sql);
        list.addAll(linkedHashMaps);
        return list;
    }

    @Override
    public Long count(String tableName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper.countByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Long> countColumn(String tableName, String columnName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return selectMapper.countColumnByCondition(tableName, columnName, whereConditionList);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getSelectListByCondition(String tableName, List<String> whereConditionList) {
        return selectMapper.selectListByCondition(tableName, whereConditionList);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getSelectColumnByCondition(String tableName, List<String> columnNameList, List<String> whereConditionList) {
        return selectMapper.selectColumnByCondition(tableName, columnNameList, whereConditionList);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getSelectPageByCondition(String tableName, List<String> whereConditionList, Integer pageNo, Integer pageSize) {
        return selectMapper.selectPageByCondition(tableName, whereConditionList, pageNo, pageSize);
    }

    @Override
    public int update(String tableName, List<String> setFiledList, List<String> updateConditionList) {
        PreCheckUtils.checkEmpty(setFiledList, "修改的set值不能为空");
        PreCheckUtils.checkEmpty(updateConditionList, "修改的where条件不能为空");
        return updateMapper.update(tableName, setFiledList, updateConditionList);
    }


    @Override
    public int update(String sql) {
        SqlParser.checkSql(sql, SqlType.UPDATE);
        return updateMapper.updateBySql(sql);
    }

    @Override
    public int delete(String tableName, List<String> deleteConditionList) {
        PreCheckUtils.checkEmpty(tableName, TABLE_MESSAGE);
        return deleteMapper.delete(tableName, deleteConditionList);
    }


    @Override
    public int delete(String sql) {
        SqlParser.checkSql(sql, SqlType.DELETE);
        return deleteMapper.deleteBySql(sql);
    }

}
