package com.kipa.mybatis.service.type;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.sql.JDBCType.valueOf;

/**
 * @author: Qinyadong
 * @date: 2019/6/21 21:40
 * 类型转化器
 */
public final class TypeConvertor {

    private TypeConvertor() {}
    /**
     * 将属性值转化成sql片段
     * @param value
     * @param typeName
     * @return
     */
    public static String convertSqlSequence(Object value, String typeName) {
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

    private static String convertValueByType(Object value, String typeName) {
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
    private static String convertDateToString(Object value) {
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
