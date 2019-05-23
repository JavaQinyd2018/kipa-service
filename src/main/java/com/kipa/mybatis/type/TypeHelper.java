package com.kipa.mybatis.type;

import com.kipa.mybatis.mapper.DatabaseInfoMapper;
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

import static java.sql.JDBCType.*;

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

    /**
     * 将属性值转化成sql片段
     * @param value
     * @param typeName
     * @return
     */
    public String convertSqlSequence(Object value, String typeName) {
        if (StringUtils.equalsIgnoreCase(typeName, "datetime")) {
            return convertDateToString(value);
        }else if (StringUtils.equalsIgnoreCase(typeName,"enum")) {
            if (value instanceof Enum) {
                Enum anEnum = (Enum) value;
                return String.format("'%s'",anEnum.name());
            }else if (value instanceof String) {
                return String.format("'%s'",value);
            }
            throw new IllegalArgumentException(value+"参数类型不合法，数据库类型为："+typeName);
        }else if (StringUtils.equalsIgnoreCase(typeName, "int")) {
            return String.valueOf(value);
        }
        return convertValueByType(value, typeName);
    }

    private String convertValueByType(Object value, String typeName) {
        String sqlSequence = "";
        switch (valueOf(typeName.toUpperCase())) {
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
            case FLOAT:
            case NUMERIC:
                sqlSequence = String.valueOf(value);
                break;
            case DOUBLE:
                if (value instanceof Double) {
                    sqlSequence = new BigDecimal((Double)value).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                }
                break;
            case DECIMAL:
                if (value instanceof BigDecimal) {
                    sqlSequence = ((BigDecimal)value).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                }
                break;
            case DATE:
            case TIME:
            case TIMESTAMP:
                sqlSequence = convertDateToString(value);
                break;
            default:
                sqlSequence = String.format("'%s'",value);
                break;
        }
        return sqlSequence;
    }

    /**
     * 将日期转化成字符串
     * @param value
     * @return
     */
    private String convertDateToString(Object value) {
        String format = "";
        if (value instanceof Date) {
            Date date = (Date) value;
            format = DateFormatUtils.format( date,"yyyy-MM-dd HH:mm:ss");
        }else if (value instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            format = formatter.format(localDateTime);
        }else if (value instanceof String) {
            format = String.valueOf(value);
        }else {
            throw new IllegalArgumentException(value+"参数类型不合法，数据库类型为：日期类型");
        }
        return String.format("'%s'",format);
    }
}
