package com.kipa.mybatis.dao.mapper4;

import com.kipa.mybatis.provider.UpdateSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:17
 * 更新
 */
public interface UpdateMapper4 {

    @UpdateProvider(type = UpdateSqlProvider.class, method = "update")
    int update(@Param("tableName") String tableName,
               @Param("setFieldList") List<String> setFieldList,
               @Param("updateConditionList") List<String> updateConditionList);

    @UpdateProvider(type = UpdateSqlProvider.class, method = "updateBySql")
    int updateBySql(@Param("sql") String sql);
}
