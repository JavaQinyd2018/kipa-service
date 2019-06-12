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

    private static SftpConfig getConfig(String env) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(SFTP_CONFIG);
            String host = properties.getProperty(StringUtils.isBlank(env) ? "sftp.connection.host" : String.format("%s.sftp.connection.host", env));
            Integer port = Integer.valueOf(properties.getProperty(StringUtils.isBlank(env) ? "sftp.connection.port" : String.format("%s.sftp.connection.port", env)));
            String username = properties.getProperty(StringUtils.isBlank(env) ? "sftp.connection.username" : String.format("%s.sftp.connection.username", env));
            String password = properties.getProperty(StringUtils.isBlank(env) ? "sftp.connection.password" : String.format("%s.sftp.connection.password", env));
            return new SftpConfig(host, port, username, password);
        } catch (IOException e) {
            throw new RuntimeException("获取并解析sftp的配置文件异常",e);
        }
    }

    private static SftpConnection getConnection(SftpConfig sftpConfig) {
        SftpConnection sftpConnection = null;
        final SftpConnectionFactory sftpConnectionFactory = new SftpConnectionFactory(sftpConfig);
        SftpConnectionPool sftpConnectionPool = new SftpConnectionPool(sftpConnectionFactory);
        try {
            sftpConnection = sftpConnectionPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("获取对象池中的对象失败.",e);
        }
        return sftpConnection;
    }

     static ChannelSftp getSftp(String env) {
        SftpConfig config = getConfig(env);
        SftpConnection connection = getConnection(config);
        PreCheckUtils.checkEmpty(connection, "sftp连接不能为空");
        return connection.getChannelSftp();
    }

    static void closeSftp(ChannelSftp channelSftp) {
        if (channelSftp != null) {
            channelSftp.quit();
            channelSftp.disconnect();
        }
    }

    static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
