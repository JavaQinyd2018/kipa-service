package com.kipa.mybatis.service.impl;

import com.kipa.common.KipaProcessException;
import com.kipa.mybatis.service.SqlScriptService;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author: Qinyadong
 * @date: 2019/5/10 14:43
 */
@Service
@Slf4j
public class SqlScriptServiceImpl implements SqlScriptService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void executeSqlScript(String sqlScriptFilePath) {
        PreCheckUtils.checkEmpty(sqlScriptFilePath, "sql脚本文件路径参数不能为空");
        ScriptRunner scriptRunner = null;
        Connection connection = null;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            connection = sqlSession.getConnection();
            scriptRunner = new ScriptRunner(connection);
            //设置字符集,不然中文乱码插入错误
            Resources.setCharset(Charset.forName("GBK"));
            //设置为false能够识别plsql的代码块，否则无法执行pl/sql的脚本，只能执行正常的sql语句
            scriptRunner.setEscapeProcessing(false);
            //设置是否输出日志
            scriptRunner.setLogWriter(null);
            //设置异常日志
            scriptRunner.setErrorLogWriter(null);
            //每条命令间的分隔符
            scriptRunner.setDelimiter(";");
            scriptRunner.runScript(Resources.getResourceAsReader(sqlScriptFilePath));
        } catch (IOException e) {
            throw new KipaProcessException("sql文件脚本执行失败，错误原因是：" + e.getMessage());
        } finally {
            if (scriptRunner != null) {
                scriptRunner.closeConnection();
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("连接关闭失败，错误原因是：{}", e);
                }
            }
        }
    }
}
