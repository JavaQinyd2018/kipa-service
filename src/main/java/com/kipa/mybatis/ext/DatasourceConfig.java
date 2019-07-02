package com.kipa.mybatis.ext;

import lombok.Data;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/31
 */
@Data
public class DatasourceConfig {

    /**
     * 数据源 驱动名称
     */
    private String driverName;
    /**
     * 数据源 url
     */
    private String url;

    /**
     * 数据源 用户名
     */
    private String username;

    /**
     * 数据源密码
     */
    private String password;
}
