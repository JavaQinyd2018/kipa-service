package com.kipa.mybatis.ext;

import com.kipa.utils.PreCheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.portable.UnknownException;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 17:01
 * sql解析器
 */

public class SqlParser {

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
                default:
                    throw new UnknownException(new IllegalArgumentException("没有对应类型的sql"));
        }
    }

}
