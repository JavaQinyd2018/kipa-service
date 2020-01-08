package com.kipa.mybatis.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:19
 */
@SuppressWarnings("all")
public class DeleteSqlProvider {

    public String delete(Map<String, Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        SQL sql = new SQL().DELETE_FROM(tableName);
        List<String> deleteConditionList = (List<String>) paramMap.get("deleteConditionList");
        deleteConditionList.forEach(sql::WHERE);
        return sql.toString();
    }

    public String deleteBySql(Map<String, Object> paramMap) {
        return (String) paramMap.get("sql");
    }
}
