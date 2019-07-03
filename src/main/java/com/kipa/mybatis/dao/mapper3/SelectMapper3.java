package com.kipa.mybatis.dao.mapper3;

import com.kipa.mybatis.provider.SelectSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:17
 * 查询
 */
public interface SelectMapper3 {

    @SelectProvider(type = SelectSqlProvider.class, method = "selectOneByCondition")
    LinkedHashMap<String, Object> selectOneByCondition(@Param("tableName") String tableName,
                                                       @Param("whereConditionList") List<String> whereConditionList);

    @SelectProvider(type = SelectSqlProvider.class, method = "selectListByCondition")
    List<LinkedHashMap<String,Object>> selectListByCondition(@Param("tableName") String tableName,
                                                             @Param("whereConditionList") List<String> whereConditionList);

    @SelectProvider(type = SelectSqlProvider.class, method = "selectOneBySql")
    LinkedHashMap<String,Object> selectOneBySql(@Param("sql") String sql);

    @SelectProvider(type = SelectSqlProvider.class, method = "selectListBySql")
    List<LinkedHashMap<String,Object>> selectListBySql(@Param("sql") String sql);

    @SelectProvider(type = SelectSqlProvider.class, method = "countBysql")
    Long countBySql(@Param("sql") String sql);

    @SelectProvider(type = SelectSqlProvider.class, method = "countByCondition")
    Long countByCondition(@Param("tableName") String tableName,
                          @Param("whereConditionList") List<String> whereConditionList);

    @SelectProvider(type = SelectSqlProvider.class, method = "countColumnBySql")
    LinkedHashMap<String, Long> countColumnBySql(@Param("sql") String sql);

    @SelectProvider(type = SelectSqlProvider.class, method = "countColumnByCondition")
    LinkedHashMap<String,Long> countColumnByCondition(@Param("tableName") String tableName,
                                                      @Param("columnName") String columnName,
                                                      @Param("whereConditionList") List<String> whereConditionList);

    @SelectProvider(type = SelectSqlProvider.class, method = "selectColumnBySql")
    List<LinkedHashMap<String, Object>> selectColumnBySql(@Param("sql") String sql);

    @SelectProvider(type = SelectSqlProvider.class, method = "selectColumnByCondition")
    List<LinkedHashMap<String,Object>> selectColumnByCondition(@Param("tableName") String tableName,
                                                               @Param("columnNameList") List<String> columnNameList,
                                                               @Param("whereConditionList") List<String> whereConditionList);

    @SelectProvider(type = SelectSqlProvider.class, method = "selectPageBySql")
    List<LinkedHashMap<String, Object>> selectPageBySql(@Param("sql") String sql);


    @SelectProvider(type = SelectSqlProvider.class, method = "selectPageByCondition")
    List<LinkedHashMap<String,Object>> selectPageByCondition(@Param("tableName") String tableName,
                                                             @Param("whereConditionList") List<String> whereConditionList,
                                                             @Param("pageNo") Integer pageNo,
                                                             @Param("pageSize") Integer pageSize);
}
