package com.kipa.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.kipa.common.DataConstant;
import com.kipa.common.KipaProcessException;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * @author: Qinyadong
 * @date: 2019/6/12 16:14
 * @since: sftp基础数据的处理
 */
class ChannelHandler {

    private SftpChannelPool sftpChannelPool;

    ChannelHandler(String env, String channelType) {
        final SftpProperties sftpProperties = getConfig(env);
        final SftpChannelFactory sftpConnectionFactory = new SftpChannelFactory(sftpProperties, channelType);
        sftpChannelPool = new SftpChannelPool(sftpConnectionFactory);
    }

    private static SftpProperties getConfig(String env) {
        String globalProperties = System.getProperty("globalProperties");
        if (StringUtils.isBlank(globalProperties)) {
            globalProperties = DataConstant.CONFIG_FILE;
        }
        final Properties properties = PropertiesUtils.loadProperties(globalProperties);
        String host = PropertiesUtils.getProperty(properties, env, "sftp.connection.host");
        String port = PropertiesUtils.getProperty(properties, env, "sftp.connection.port");
        String username = PropertiesUtils.getProperty(properties, env, "sftp.connection.username");
        String password = PropertiesUtils.getProperty(properties, env, "sftp.connection.password");
        PreCheckUtils.checkEmpty(host, "sftp的host不能为空");
        PreCheckUtils.checkEmpty(port, "sftp的端口号不能为空");
        PreCheckUtils.checkEmpty(username, "sftp主机的用户名不能为空");
        PreCheckUtils.checkEmpty(password, "sftp主机的密码不能为空");
        return new SftpProperties(host, Integer.valueOf(port), username, password);
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
        }finally {
            sftpChannelPool.close();
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
