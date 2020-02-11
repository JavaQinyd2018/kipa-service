package com.kipa.env;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnvironmentType {

    /**
     * 默认的环境
     */
    DEFAULT("def"),
    /**
     * 测试环境
     */
    TEST("test"),

    /**
     * 开发环境
     */
    DEVELOPMENT("dev"),

    /**
     * 预生产
     */
    PRE_PRODUCT("pre"),

    /**
     * 生产
     */
    PRODUCT("prod");

    private String remark;

}
