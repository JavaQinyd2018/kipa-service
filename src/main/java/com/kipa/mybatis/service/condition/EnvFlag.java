package com.kipa.mybatis.service.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Qinyadong
 * @date: 2019/6/27 11:17
 * 环境标识
 */
@AllArgsConstructor
@Getter
public enum EnvFlag {

    /**
     * 环境1
     */
    ENV1("env1"),

    ENV2("env2"),

    ENV3("env3"),

    ENV4("env4");

    private String code;
}
