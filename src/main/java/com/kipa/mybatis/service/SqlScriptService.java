package com.kipa.mybatis.service;

/**
 * @author: Qinyadong
 * @date: 2019/5/10 14:31
 * sql脚本文件执行服务
 */
public interface SqlScriptService {

    /**
     * 直接运行脚本文件
     * @param sqlScriptFilePath
     */
    void executeSqlScript(String sqlScriptFilePath);
}
