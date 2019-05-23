package com.kipa.mybatis.mapper;

import com.kipa.mybatis.provider.DeleteSqlProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:18
 * 删除
 */
public interface DeleteMapper {

    @DeleteProvider(type = DeleteSqlProvider.class, method = "delete")
    int delete(@Param("tableName") String tableName,
               @Param("deleteConditionList") List<String> deleteConditionList);

    @DeleteProvider(type = DeleteSqlProvider.class, method = "deleteBySql")
    int deleteBySql(@Param("sql") String sql);
}
