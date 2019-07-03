package com.kipa.mybatis.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kipa.data.csv.CSVType;
import com.kipa.data.csv.CSVUtils;
import com.kipa.mybatis.ext.SqlType;
import com.kipa.mybatis.service.BaseDatabaseService;
import com.kipa.mybatis.service.type.TypeConvertor;
import com.kipa.utils.PreCheckUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Qinyadong
 * @date: 2019/6/21 21:01
 * @since: 数据库增删改查抽象类
 */
public abstract class AbstractDatabaseServiceImpl implements BaseDatabaseService {


    @Override
    public int insert(String tableName, Map<String, Object> paramMap) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(paramMap, "插入的参数不能为空");
        Map<String, String> insertParamMap = Maps.newLinkedHashMap();
        paramMap.forEach((columnName, value) -> {
            if (value == null) {
                return;
            }
            String columnType = getColumnType(tableName, columnName);
            if (StringUtils.isBlank(columnType)) {
                throw new RuntimeException(String.format("表名为：【%s】的表中查不到列名为：【%s】的数据类型",tableName, columnName));
            }
            insertParamMap.put(columnName, TypeConvertor.convertSqlSequence(value, columnType));
        });
        return insertParam(tableName, insertParamMap);
    }

    /**
     * 获取列类型抽象方法
     * @param tableName
     * @return
     */
    abstract List<Map<String, Object>> listTableColumn(String tableName);

    private String getColumnType(String tableName, String columnName) {
        final List<Map<String, Object>> list = listTableColumn(tableName);
        if (CollectionUtils.isEmpty(list)) {
            throw  new RuntimeException(tableName+"表字段和数据类型不存在");
        }
        for (Map<String, Object> columnAndTypeMap : list) {
            if (StringUtils.equalsIgnoreCase((String)columnAndTypeMap.get("COLUMN_NAME"), columnName)) {
                return (String) columnAndTypeMap.get("DATA_TYPE");
            }
        }
        return null;
    }

    /**
     * 插入参数的抽象方法
     * @param tableName
     * @param insertParamMap
     * @return
     */
    abstract int insertParam(String tableName, Map<String,String> insertParamMap);

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
    public Map<String, Object> selectOne(String tableName, String condition) {
        String sql = buildSql(SqlType.SELECT, tableName, null, condition, null, null);
        return selectOne(sql);
    }

    @Override
    public Map<String, Object> selectOne(String tableName, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return selectOne(tableName, whereConditionList);
    }

    @Override
    public List<Map<String, Object>> selectList(String tableName, String condition) {
        String sql = buildSql(SqlType.SELECT, tableName, null, condition, null, null);
        return selectList(sql);
    }

    @Override
    public List<Map<String, Object>> selectList(String tableName, List<String> whereConditionList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        List<LinkedHashMap<String, Object>> selectListByCondition = getSelectListByCondition(tableName, whereConditionList);
        list.addAll(selectListByCondition);
        return list;
    }

    /**
     * 根据条件查询数据集合抽象方法
     * @param tableName
     * @param whereConditionList
     * @return
     */
    abstract List<LinkedHashMap<String, Object>> getSelectListByCondition(String tableName, List<String> whereConditionList);

    @Override
    public List<Map<String, Object>> selectList(String tableName, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return selectList(tableName, whereConditionList);
    }

    @Override
    public Long count(String tableName, String condition) {
        String sql = buildSql(SqlType.COUNT, tableName, null, condition, null, null);
        return count(sql);
    }

    @Override
    public Long count(String tableName, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return count(tableName, whereConditionList);
    }

    @Override
    public Map<String, Long> countColumn(String tableName, String columnName, String condition) {
        String sql = buildSql(SqlType.COUNT, tableName, columnName, condition, null, null);
        return countColumn(sql);
    }

    @Override
    public Map<String, Long> countColumn(String tableName, String columnName, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return countColumn(tableName, columnName, whereConditionList);
    }

    @Override
    public List<Map<String, Object>> selectColumn(String tableName, String columnName, String condition) {
        String sql = buildSql(SqlType.SELECT_COLUMN, tableName, columnName, condition, null, null);
        return selectColumn(sql);
    }

    @Override
    public List<Map<String, Object>> selectColumn(String tableName, List<String> columnNameList, List<String> whereConditionList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        List<LinkedHashMap<String, Object>> linkedHashMaps = getSelectColumnByCondition(tableName, columnNameList, whereConditionList);
        list.addAll(linkedHashMaps);
        return list;
    }

    /**
     * 根据条件查询指定列对应的属性值
     * @param tableName
     * @param columnNameList
     * @param whereConditionList
     * @return
     */
    abstract List<LinkedHashMap<String, Object>> getSelectColumnByCondition(String tableName, List<String> columnNameList, List<String> whereConditionList);

    @Override
    public List<Map<String, Object>> selectColumn(String tableName, List<String> columnNameList, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(whereConditionMap,"查询条件map不能为空");
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return selectColumn(tableName, columnNameList, whereConditionList);
    }

    @Override
    public List<Map<String, Object>> selectPage(String tableName, String condition, Integer pageNo, Integer pageSize) {
        String sql = buildSql(SqlType.SELECT_PAGE, tableName, null, condition, pageNo, pageSize);
        return selectPage(sql);
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
        List<LinkedHashMap<String, Object>> linkedHashMaps = getSelectPageByCondition(tableName, whereConditionList, pageNo, pageSize);
        list.addAll(linkedHashMaps);
        return list;
    }

    /**
     * 获取分页查询抽象方法
     * @param tableName
     * @param whereConditionList
     * @param pageNo
     * @param pageSize
     * @return
     */
    abstract List<LinkedHashMap<String, Object>> getSelectPageByCondition(String tableName, List<String> whereConditionList, Integer pageNo, Integer pageSize);


    @Override
    public List<Map<String, Object>> selectPage(String tableName, Map<String, Object> whereConditionMap, Integer pageNo, Integer pageSize) {
        List<String> whereConditionList = buildConditionList(tableName, whereConditionMap);
        return selectPage(tableName, whereConditionList, pageNo, pageSize);
    }

    @Override
    public int update(String tableName, String setField, String condition) {
        String sql = buildSql(SqlType.UPDATE, tableName, setField, condition, null, null);
        return update(sql);
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
    public int delete(String tableName, String condition) {
        String sql = buildSql(SqlType.DELETE, tableName, null, condition, null, null);
        return delete(sql);
    }

    @Override
    public int delete(String tableName, Map<String, Object> deleteConditionMap) {
        List<String> deleteConditionList = buildConditionList(tableName, deleteConditionMap);
        return delete(tableName, deleteConditionList);
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
            int i = delete(sql);
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
            String columnType = getColumnType(tableName, columnName);
            if (StringUtils.isBlank(columnType)) {
                throw new RuntimeException(String.format("表名为：【%s】的表中查不到列名为：【%s】的数据类型",tableName, columnName));
            }
            whereConditionList.add(String.format("%s = %s",columnName, TypeConvertor.convertSqlSequence(value, columnType)));
        });
        return whereConditionList;
    }

    private String buildSql(SqlType sqlType, String tableName, String target, String condition, Integer pageNo, Integer pageSize) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        switch (sqlType) {
            case SELECT:
                return String.format("SELECT * FROM %s WHERE %s",tableName, condition);
            case COUNT:
                if (StringUtils.isBlank(target)) {
                    return String.format("SELECT COUNT(*) FROM %s WHERE %s",tableName, condition);
                }else {
                    return String.format("SELECT COUNT(%s) FROM %s WHERE %s",target, tableName, condition);
                }
            case SELECT_COLUMN:
                PreCheckUtils.checkEmpty(target, "查询字段的信息不能为空");
                return String.format("SELECT %s FROM %s WHERE %s",target,tableName, condition);
            case SELECT_PAGE:
                if (pageNo == null) {
                    pageNo = 1;
                }

                if (pageSize == null) {
                    pageSize = 10;
                }
                Integer start = (pageNo - 1) * pageSize;
                return String.format("SELECT * FROM %s WHERE %s limit %s,%s", tableName, condition, start, pageSize);
            case UPDATE:
                PreCheckUtils.checkEmpty(target, "更新字段的信息不能为空");
                return String.format("UPDATE %s SET %s WHERE %s",tableName, target, condition);
            case DELETE:
                return String.format("DELETE FROM %s WHERE %s",tableName, condition);
            default:
                return "";
        }
    }

}
