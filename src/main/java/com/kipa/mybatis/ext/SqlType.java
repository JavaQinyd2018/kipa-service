package com.kipa.mybatis.ext;

import lombok.Getter;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 17:02
 * sql类型
 */
@Getter
public enum  SqlType {

    /**
     * 插入
     */
    INSERT,

    /**
     * 修改
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 查询
     */
    SELECT,

    /**
     * 统计
     */
    COUNT,

    /**
     * 查询字段
     */
    SELECT_COLUMN,
    /**
     * 分页查询
     */
    SELECT_PAGE;

}
