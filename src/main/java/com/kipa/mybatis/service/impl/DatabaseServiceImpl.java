package com.kipa.mybatis.service.impl;

import com.google.common.collect.Maps;
import com.kipa.data.csv.CSVType;
import com.kipa.data.csv.CSVUtils;
import com.kipa.mybatis.mapper.DeleteMapper;
import com.kipa.mybatis.mapper.InsertMapper;
import com.kipa.mybatis.mapper.SelectMapper;
import com.kipa.mybatis.mapper.UpdateMapper;
import com.kipa.mybatis.service.DatabaseService;
import com.kipa.mybatis.type.TypeHelper;
import com.kipa.utils.PreCheckUtils;
import com.kipa.mybatis.type.SqlParser;
import com.kipa.mybatis.type.SqlType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testng.collections.Lists;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Qinyadong
 * @date: 2019/3/22 15:59
 */
@Service
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {


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
    public int insert(String tableName, Map<String, Object> paramMap) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(paramMap, "插入的参数不能为空");
        Map<String, String> insertParamMap = Maps.newLinkedHashMap();
        paramMap.forEach((columnName, value) -> {
            if (value == null) {
                return;
            }
            String columnType = typeHelper.getColumnType(tableName, columnName);
            if (StringUtils.isBlank(columnType)) {
                throw new RuntimeException(String.format("表名为：【%s】的表中查不到列名为：【%s】的数据类型",tableName, columnName));
            }
            insertParamMap.put(columnName, typeHelper.convertSqlSequence(value, columnType));
        });
        return insertMapper.insert(tableName, insertParamMap);
    }

    @Override
    public int insert(String sql) {
        return insertMapper.insertBySql(sql);
    }

    @Override
    public int batchInsert(String tableName, String csvFilePath) {
        List<Map<String, Object>> list = CSVUtils.parseCsvFile(csvFilePath, CSVType.TRANSVERSE);
        if (CollectionUtils.isNotEmpty(list)) {
            return batchInsert(tableName, list);
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchInsert(String tableName, List<Map<String, Object>> paramMapList) {
        PreCheckUtils.checkEmpty(paramMapList, "批量插入的数据集合不能为空");
        AtomicInteger count = new AtomicInteger();
        paramMapList.forEach(map -> {
            int i = insert(tableName, map);
            count.set(count.get() + i);
        });
        if (count.get() != paramMapList.size()) {
            throw new RuntimeException("批量插入数据失败");
        }
        return count.get();
    }

    @Override
    public Map<String, Object> selectOne(String tableName, List<String> whereConditionList) {
        return selectMapper.selectOneByCondition(tableName, whereConditionList);
    }

    @Override
    public Map<String, Object> selectOne(String tableName, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return selectOne(tableName, whereConditionList);
    }

    @Override
    public List<Map<String, Object>> selectList(String tableName, List<String> whereConditionList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        List<LinkedHashMap<String, Object>> linkedHashMaps = selectMapper.selectListByCondition(tableName, whereConditionList);
        list.addAll(linkedHashMaps);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectList(String tableName, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return selectList(tableName, whereConditionList);
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
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        return selectMapper.countByCondition(tableName, whereConditionList);
    }

    @Override
    public Long count(String tableName, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return count(tableName, whereConditionList);
    }

    @Override
    public Map<String, Long> countColumn(String tableName, String columnName, List<String> whereConditionList) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        return selectMapper.countColumnByCondition(tableName, columnName, whereConditionList);
    }

    @Override
    public Map<String, Long> countColumn(String tableName, String columnName, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return countColumn(tableName, columnName, whereConditionList);
    }

    @Override
    public List<Map<String, Object>> selectColumn(String tableName, List<String> columnNameList, List<String> whereConditionList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        List<LinkedHashMap<String, Object>> linkedHashMaps = selectMapper.selectColumnByCondition(tableName, columnNameList, whereConditionList);
        list.addAll(linkedHashMaps);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectColumn(String tableName, List<String> columnNameList, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return selectColumn(tableName, columnNameList, whereConditionList);
    }

    @Override
    public List<Map<String, Object>> selectPage(String tableName, List<String> whereConditionList, Integer pageNo, Integer pageSize) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        List<LinkedHashMap<String, Object>> linkedHashMaps = selectMapper.selectPageByCondition(tableName, whereConditionList, pageNo, pageSize);
        list.addAll(linkedHashMaps);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectPage(String tableName, Map<String, Object> whereConditionMap, Integer pageNo, Integer pageSize) {
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return selectPage(tableName, whereConditionList, pageNo, pageSize);
    }

    @Override
    public int update(String tableName, List<String> setFiledList, List<String> updateConditionList) {
        PreCheckUtils.checkEmpty(setFiledList, "修改的set值不能为空");
        PreCheckUtils.checkEmpty(updateConditionList, "修改的where条件不能为空");
        return updateMapper.update(tableName, setFiledList, updateConditionList);
    }

    @Override
    public int update(String tableName, Map<String, Object> setFiledMap, Map<String, Object> updateConditionMap) {
        PreCheckUtils.checkEmpty(setFiledMap,"修改的set值不能为空");
        PreCheckUtils.checkEmpty(updateConditionMap,"修改的where条件不能为空");
        List<String> setFieldList = buildConditionList(tableName, setFiledMap);
        List<String> updateConditionList = buildConditionList(tableName, updateConditionMap);
        return update(tableName, setFieldList, updateConditionList);
    }

    @Override
    public int update(String sql) {
        SqlParser.checkSql(sql, SqlType.UPDATE);
        return updateMapper.updateBySql(sql);
    }

    @Override
    public int delete(String tableName, List<String> deleteConditionList) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        return deleteMapper.delete(tableName, deleteConditionList);
    }

    @Override
    public int delete(String tableName, Map<String, Object> deleteConditionMap) {
        List<String> deleteConditionList = buildConditionList(tableName, deleteConditionMap);
        return delete(tableName, deleteConditionList);
    }

    @Override
    public int delete(String sql) {
        SqlParser.checkSql(sql, SqlType.DELETE);
        return deleteMapper.deleteBySql(sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(final Map<String, String> tableNameAndConditionMap) {
        PreCheckUtils.checkEmpty(tableNameAndConditionMap, "删除的表和条件集合不能为空");
        AtomicInteger count = new AtomicInteger();
        tableNameAndConditionMap.forEach((tableName, condition) -> {
            PreCheckUtils.checkEmpty(tableName, "表名不能为空");
            PreCheckUtils.checkEmpty(condition, "删除条件不能为空");
            String sql = String.format("delete from %s where %s", tableName, condition);
            int i = deleteMapper.deleteBySql(sql);
            count.set(count.get() + i);
        });
        if (count.get() != tableNameAndConditionMap.size()) {
            throw new RuntimeException("批量删除数据失败");
        }
        return count.get();
    }

    private List<String> buildConditionList(String tableName, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        List<String> whereConditionList = Lists.newArrayList();
        whereConditionMap.forEach((columnName, value) -> {
            if (value == null) {
                return;
            }
            String columnType = typeHelper.getColumnType(tableName, columnName);
            whereConditionList.add(String.format("%s = %s",columnName, typeHelper.convertSqlSequence(value, columnType)));
        });
        return whereConditionList;
    }
}
