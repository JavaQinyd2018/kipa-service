package com.kipa.sftp;

import com.jcraft.jsch.*;
import com.kipa.common.KipaProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import java.util.Properties;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/13
 */
@Slf4j
public class SftpChannelFactory extends BasePooledObjectFactory<Channel> {


    /**
     *SSH 客户端的 StrictHostKeyChecking 配置指令
     */
    private static final String STRICT_KEY = "StrictHostKeyChecking";

    /**
     *第一次连接服务器时，自动接受新的公钥
     */
    private static final String STRICT_VALUE = "no";

    private SftpConfig sftpConfig;
    private String channelType;

    SftpChannelFactory(SftpConfig sftpConfig, String channelType) {
        this.sftpConfig = sftpConfig;
        this.channelType = channelType;
    }

     @Override
    public Channel create() throws Exception {
         String host = sftpConfig.getHost();
         Integer port = sftpConfig.getPort();
         String username = sftpConfig.getUsername();
         String password = sftpConfig.getPassword();
         JSch jSch = new JSch();
         Session session = jSch.getSession(username, host, port);
         session.setPassword(password);
         Properties properties = new Properties();
         properties.put(STRICT_KEY, STRICT_VALUE);
         session.setConfig(properties);
         try {
             session.connect();
         } catch (JSchException e) {
             if (session.isConnected()) {
                 session.disconnect();
             }
             throw new KipaProcessException("sftp获取session连接失败",e);
         }
         return session.openChannel(channelType);
     }

    void destroy(Channel channel) throws Exception {
        destroyObject(wrap(channel));
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public void destroyObject(PooledObject<Channel> pooledChannel) throws Exception {
        if (pooledChannel == null) {
            return;
        }
        Channel channel = pooledChannel.getObject();
        channel.disconnect();
        channel.getSession().disconnect();
    }


    @Override
    public boolean validateObject(PooledObject<Channel> pooledChannel) {
        Channel channel = pooledChannel.getObject();
        if (channel == null) {
            return false;
        }

        if (channel.isClosed()) {
            return false;
        }

        try {
            return channel.getSession() != null && channel.getSession().isConnected();
        } catch (JSchException e) {
            log.error("sftp处理失败",e);
            return false;
        }
    }
}
