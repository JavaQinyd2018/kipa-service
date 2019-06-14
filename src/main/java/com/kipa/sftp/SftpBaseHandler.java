package com.kipa.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.kipa.utils.PreCheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author: Qinyadong
 * @date: 2019/6/12 16:14
 * @since: sftp基础数据的处理
 */
class SftpBaseHandler {

    private SftpBaseHandler() {}

    private static final String SFTP_CONFIG = "config/sftp.properties";

    private static SftpConnectionPool sftpConnectionPool = null;

    private static SftpConnectionPool getInstance(String env) {
        final SftpConnectionFactory sftpConnectionFactory = new SftpConnectionFactory(getConfig(env));
        if (sftpConnectionPool == null) {
            synchronized (SftpBaseHandler.class){
                if (sftpConnectionPool == null) {
                    sftpConnectionPool = new SftpConnectionPool(sftpConnectionFactory);
                }
                return sftpConnectionPool;
            }
        }
        return sftpConnectionPool;

    }

    private static SftpConfig getConfig(String env) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(SFTP_CONFIG);
            String host = properties.getProperty(StringUtils.isBlank(env) ? "sftp.connection.host" : String.format("%s.sftp.connection.host", env));
            Integer port = Integer.valueOf(properties.getProperty(StringUtils.isBlank(env) ? "sftp.connection.port" : String.format("%s.sftp.connection.port", env)));
            String username = properties.getProperty(StringUtils.isBlank(env) ? "sftp.connection.username" : String.format("%s.sftp.connection.username", env));
            String password = properties.getProperty(StringUtils.isBlank(env) ? "sftp.connection.password" : String.format("%s.sftp.connection.password", env));
            PreCheckUtils.checkEmpty(host, "sftp的host不能为空");
            PreCheckUtils.checkEmpty(port, "sftp的端口号不能为空");
            PreCheckUtils.checkEmpty(username, "sftp主机的用户名不能为空");
            PreCheckUtils.checkEmpty(password, "sftp主机的密码不能为空");
            return new SftpConfig(host, port, username, password);
        } catch (IOException e) {
            throw new RuntimeException("获取并解析sftp的配置文件异常",e);
        }

    }

    private static SftpConnection getConnection(SftpConnectionPool sftpConnectionPool) {
        SftpConnection sftpConnection = null;
        try {
            sftpConnection = sftpConnectionPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("获取对象池中的对象失败.",e);
        }
        return sftpConnection;
    }

     static ChannelSftp getSftp(String env) {
         SftpConnectionPool connectionPool = getInstance(env);
         SftpConnection connection = getConnection(connectionPool);
         PreCheckUtils.checkEmpty(connection, "sftp连接不能为空");
        return connection.getChannelSftp();
    }

    static void returnObject(ChannelSftp channelSftp) {
        SftpConnection connection = new SftpConnection();
        connection.setChannelSftp(channelSftp);
        try {
            sftpConnectionPool.returnObject(connection);
        } catch (Exception e) {
            throw new RuntimeException("释放sftp连接失败",e);
        }
    }

    static void closeConnection() {
        sftpConnectionPool.close();
    }

    static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ignore) {

            }
        }
    }

    static void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ignore) {

            }
        }
    }
}
