package com.kipa.mybatis.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 17:19
 * 获取数据库信息
 */
public interface DatabaseInfoMapper {

    @Select("select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA=(select database())")
    List<Map<String, Object>> listTable();

    @Select("select COLUMN_NAME,DATA_TYPE from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME=#{tableName}")
    List<Map<String, Object>> listTableColumn(String tableName);
}
