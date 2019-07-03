package com.kipa.mybatis.provider;

import com.kipa.utils.PreCheckUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:19
 */
public class UpdateSqlProvider {

    @SuppressWarnings("all")
    public synchronized String update(Map<String,Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SQL sql = new SQL().UPDATE(tableName);
        List<String> setFieldList = (List<String>) paramMap.get("setFieldList");
        setFieldList.forEach(setField -> sql.SET(setField));
        List<String> updateConditionList = (List<String>) paramMap.get("updateConditionList");
        updateConditionList.forEach(updateCondition -> sql.WHERE(updateCondition));
        return sql.toString();
    }

    public synchronized String updateBySql(Map<String, Object> paramMap) {
        return (String) paramMap.get("sql");
    }
}
