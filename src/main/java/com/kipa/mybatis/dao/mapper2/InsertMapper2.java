package com.kipa.mybatis.dao.mapper2;

import com.kipa.mybatis.provider.InsertSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:18
 * 插入
 */
public interface InsertMapper2 {

    @InsertProvider(type = InsertSqlProvider.class, method = "insert")
    int insert(@Param("tableName") String tableName, @Param("insertParamMap") Map<String, String> insertParamMap);

    @InsertProvider(type = InsertSqlProvider.class, method = "insertBySql")
    int insertBySql(@Param("sql") String sql);
}
