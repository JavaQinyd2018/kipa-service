package com.kipa.mybatis.service.type;

import com.kipa.mybatis.dao.mapper.DatabaseInfoMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.sql.JDBCType.valueOf;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 17:44
 * 数据库基本结构信息获取
 */
@Component
public class TypeHelper {

    @Autowired
    private DatabaseInfoMapper databaseInfoMapper;

    /**
     * 根据表名和列名获取字段数据类型名称
     * @param tableName
     * @param columnName
     * @return
     */
    public String getColumnType(String tableName, String columnName) {
        final List<Map<String, Object>> list = databaseInfoMapper.listTableColumn(tableName);
        if (CollectionUtils.isEmpty(list)) {
            throw  new RuntimeException(tableName+"表字段和数据类型不存在");
        }
        for (Map<String, Object> columnAndTypeMap : list) {
           if (StringUtils.equalsIgnoreCase((String)columnAndTypeMap.get("COLUMN_NAME"), columnName)) {
               return (String) columnAndTypeMap.get("DATA_TYPE");
           }
        }
        return null;
    }


}
