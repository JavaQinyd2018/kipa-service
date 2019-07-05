package com.kipa.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.kipa.common.KipaProcessException;
import com.kipa.utils.PreCheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author: Qinyadong
 * @date: 2019/6/12 16:14
 * @since: sftp基础数据的处理
 */
class ChannelHandler {

    private SftpChannelPool sftpChannelPool;

    ChannelHandler(String env, String channelType) {
        final SftpChannelFactory sftpConnectionFactory = new SftpChannelFactory(getConfig(env), channelType);
        sftpChannelPool = new SftpChannelPool(sftpConnectionFactory);
    }

    private static final String SFTP_CONFIG = "config/sftp.properties";


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
            throw new KipaProcessException("获取并解析sftp的配置文件异常",e);
        }

    }

    Channel borrowChannel() {
        try {
            return sftpChannelPool.borrowObject();
        } catch (Exception e) {
            throw new KipaProcessException("获取对象池中的对象失败.",e);
        }
    }

    void returnChannel(Channel channel) {
        try {
            sftpChannelPool.returnObject(channel);
        } catch (Exception e) {
            throw new KipaProcessException("释放sftp连接失败",e);
        }
    }

    ChannelSftp borrowChannelSftp() {
        Channel channel = this.borrowChannel();
        ChannelSftp channelSftp = (ChannelSftp) channel;
        if (!channel.isConnected()) {
            try {
                channelSftp.connect();
            } catch (JSchException e) {
                throw new KipaProcessException("sftp获取channel失败",e);
            }
        }
        return channelSftp;
    }

}
