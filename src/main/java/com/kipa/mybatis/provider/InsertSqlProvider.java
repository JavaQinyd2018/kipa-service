package com.kipa.mybatis.provider;

import com.kipa.utils.PreCheckUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.*;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:19
 */
@SuppressWarnings("all")
public class InsertSqlProvider {

    public String insert(Map<String, Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        SQL sql = new SQL().INSERT_INTO(tableName);
        Map<String, String> insertParamMap = (Map<String, String>) paramMap.get("insertParamMap");
        PreCheckUtils.checkEmpty(insertParamMap, "插入数据库的属性不能为空");
        insertParamMap.forEach(sql::VALUES);
        return sql.toString();
    }

    public String insertBySql(Map<String, Object> paramMap) {
        return (String) paramMap.get("sql");
    }
}
