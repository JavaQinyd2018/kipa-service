package com.kipa.mybatis.provider;

import com.kipa.utils.PreCheckUtils;
import com.kipa.mybatis.type.SqlParser;
import com.kipa.mybatis.type.SqlType;
import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:19
 */
@SuppressWarnings("all")
public class InsertSqlProvider {

    public String insert(Map<String, Object> paramMap) {
        String tableName = (String) paramMap.get("tableName");
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SQL sql = new SQL().INSERT_INTO(tableName);
        Map<String, String> insertParamMap = (Map<String, String>) paramMap.get("insertParamMap");
        PreCheckUtils.checkEmpty(insertParamMap, "插入数据库的属性不能为空");
        insertParamMap.forEach(sql::VALUES);
        return sql.toString();
    }

    public String insertBySql(Map<String, Object> paramMap) {
        String sql = (String) paramMap.get("sql");
        PreCheckUtils.checkEmpty(sql,"insert的sql语句不能为空");
        SqlParser.checkSql(sql, SqlType.INSERT);
        return sql;
    }
}
