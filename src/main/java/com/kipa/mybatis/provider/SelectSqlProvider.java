package com.kipa.mybatis.provider;

import com.kipa.utils.PreCheckUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:19
 */
@SuppressWarnings("all")
public class SelectSqlProvider {

    public synchronized String selectOneByCondition(final Map<String, Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SQL sql = new SQL()
                .SELECT("*")
                .FROM(tableName);
        assmebleWhereSQL(paramMap, sql);
        return sql.toString() + " limit 0,1";
    }

    public synchronized String selectListByCondition(final Map<String, Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SQL sql = new SQL()
                .SELECT("*")
                .FROM(tableName);
        assmebleWhereSQL(paramMap, sql);
        return sql.toString();
    }

    public synchronized String selectOneBySql(final Map<String,Object> paramMap) {
        return (String) paramMap.get("sql") + " limit 0,1";
    }

    public synchronized String selectListBySql(final Map<String,Object> paramMap) {
        return (String) paramMap.get("sql");
    }

    public synchronized String countBysql(final Map<String,Object> paramMap) {
        return (String) paramMap.get("sql");
    }

    public synchronized String countByCondition(final Map<String,Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SQL sql = new SQL()
                .SELECT("count(1)")
                .FROM(tableName);
        @SuppressWarnings("all")
        List<String> conditionList = (List)paramMap.get("whereConditionList");
        if (CollectionUtils.isNotEmpty(conditionList)) {
            conditionList.forEach(sql::WHERE);
        }
        return sql.toString();
    }


    public synchronized String countColumnBySql(final Map<String,Object> paramMap) {
        return (String) paramMap.get("sql");
    }

    public synchronized String countColumnByCondition(final Map<String,Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        String columnName = (String) paramMap.get("columnName");
        SQL sql = new SQL()
                .SELECT(String.format("count(%s)",columnName))
                .FROM(tableName);
        @SuppressWarnings("all")
        List<String> conditionList = (List)paramMap.get("whereConditionList");
        if (CollectionUtils.isNotEmpty(conditionList)) {
            conditionList.forEach(sql::WHERE);
        }
        return sql.toString();
    }

    public synchronized String selectColumnBySql(final Map<String,Object> paramMap) {
        return (String) paramMap.get("sql");
    }

    public synchronized String selectColumnByCondition(final Map<String,Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        if (StringUtils.isBlank(tableName)) {
            throw new IllegalArgumentException("表名不能为空");
        }
        List<String> columnNameList = (List<String>) paramMap.get("columnNameList");
        if (CollectionUtils.isEmpty(columnNameList)) {
            throw new IllegalArgumentException("被查的列名不能为空");
        }
        SQL sql = new SQL().FROM(tableName);
        columnNameList.forEach(sql::SELECT);
        List<String> conditionList = (List)paramMap.get("whereConditionList");
        if (CollectionUtils.isNotEmpty(conditionList)) {
            conditionList.forEach(sql::WHERE);
        }
        return sql.toString();
    }

    public synchronized String selectPageBySql(final Map<String,Object> paramMap) {
        return (String) paramMap.get("sql");
    }

    public synchronized String selectPageByCondition(final Map<String,Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SQL sql = new SQL()
                .SELECT("*")
                .FROM(tableName);
        assmebleWhereSQL(paramMap, sql);
        Integer pageNo = (Integer) paramMap.get("pageNo");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        Integer start = (pageNo - 1) * pageSize;
        return String.format("%s limit %s,%s", sql.toString(), start, pageSize);
    }

    private synchronized void assmebleWhereSQL(final Map<String,Object> paramMap, SQL sql) {
        List<String> conditionList = (List)paramMap.get("whereConditionList");
        if (CollectionUtils.isNotEmpty(conditionList)) {
            conditionList.forEach(condition -> {
                if (StringUtils.containsIgnoreCase(condition, "\bdesc")) {
                    sql.ORDER_BY(condition);
                }else {
                    sql.WHERE(condition);
                }
            });
        }
    }



}
