package com.kipa.mybatis.ext;

import com.kipa.utils.PreCheckUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 17:01
 * sql校验器
 */

public final class SqlParser {

    private SqlParser() {}

    public static void checkSql(String sql, SqlType sqlType) {

        PreCheckUtils.checkEmpty(sql, "sql语句不能为空");
        switch (sqlType) {
            case INSERT:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "insert ")
                        && StringUtils.containsIgnoreCase(sql.trim()," values")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "insert"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,插入类型的sql语句必须以insert的开始,包含values关键字,\nsql是："+sql);
                }
                break;
            case UPDATE:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "update ")
                        && StringUtils.containsIgnoreCase(sql.trim()," set")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "update"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,修改类型的sql语句必须以update的开始,并且包含set关键字,\nsql是："+sql);
                }
                break;
            case DELETE:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "delete ")
                        && StringUtils.containsIgnoreCase(sql.trim()," from")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "delete"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,删除类型的sql语句必须以delete的开始,包含from关键字,\nsql是："+sql);
                }
                break;
            case SELECT:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "select ")
                        && StringUtils.containsIgnoreCase(sql.trim()," from")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "select"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,查询类型的sql语句必须以select的开始,包含from关键字,\nsql是："+sql);
                }
                break;
            case COUNT:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "select ")
                        && StringUtils.containsIgnoreCase(sql.trim()," from")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "select")
                        && StringUtils.containsIgnoreCase(sql.trim(), "count"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,统计类型的sql语句必须以select的开始,包含count, from关键字,\nsql是："+sql);
                }
                break;
            case SELECT_COLUMN:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "select ")
                        && StringUtils.containsIgnoreCase(sql.trim()," from")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "select")
                        && !StringUtils.containsIgnoreCase(sql.trim(), "*"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,指定列查询类型的sql语句必须以select的开始,包含from, limit关键字,以及被查询字段信息,\nsql是："+sql);
                }
                break;
            case SELECT_PAGE:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "select ")
                        && StringUtils.containsIgnoreCase(sql.trim()," from")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "select")
                        && StringUtils.containsIgnoreCase(sql.trim(), "limit"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,分页查询类型的sql语句必须以select的开始,包含from, limit关键字,\nsql是："+sql);
                }
                break;
             default:
                 throw new IllegalArgumentException("没有对应类型的sql");
        }
    }

}
