package com.kipa.sftp;

import com.jcraft.jsch.*;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/13
 */
@Slf4j
public class SftpConnectionFactory extends BasePooledObjectFactory<SftpConnection> {


    /**
     *SSH 客户端的 StrictHostKeyChecking 配置指令
     */
    private static final String STRICT_KEY = "StrictHostKeyChecking";

    /**
     *第一次连接服务器时，自动接受新的公钥
     */
    private static final String STRICT_VALUE = "no";

    private SftpConfig sftpConfig;

    SftpConnectionFactory(SftpConfig sftpConfig) {
        this.sftpConfig = sftpConfig;
    }

     @Override
    public SftpConnection create() throws Exception {
        SftpConnection connection = new SftpConnection();
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
                 log.error("连接服务器失败,请检查主机[{}],端口[{}],用户名[{}],密码等是否正确," +
                         "以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.",host, port, username);
             }
         }
         Channel channel = session.openChannel("sftp");
         try {
             channel.connect();
         } catch (JSchException e) {
             if (channel.isConnected()) {
                 channel.disconnect();
                 log.error("连接服务器失败,请检查主机[{}],端口[{}],用户名[{}]等是否正确," +
                         "以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.",host, port, username);
             }
         }
         ChannelSftp channelSftp = (ChannelSftp) channel;
         connection.setCreateDate(new Date());
         connection.setIndex(UUID.randomUUID().toString());
         connection.setChannelSftp(channelSftp);
         return connection;
    }

    void destroy(SftpConnection sftpConnection) throws Exception {
        destroyObject(wrap(sftpConnection));
    }
    @Override
    public PooledObject<SftpConnection> wrap(SftpConnection sftpConnection) {
        return new DefaultPooledObject<>(sftpConnection);
    }


    @Override
    public void destroyObject(PooledObject<SftpConnection> p) throws Exception {
        if (p == null) {
            return;
        }
        SftpConnection sftpConnection = p.getObject();
        try {
            sftpConnection.getChannelSftp().getSession().disconnect();
            sftpConnection.getChannelSftp().quit();
            sftpConnection.getChannelSftp().disconnect();
        } catch (JSchException e) {
            log.error("关闭sftp连接失败，index为：{}",sftpConnection.getIndex());
        }
    }


    @Override
    public boolean validateObject(PooledObject<SftpConnection> p) {
        SftpConnection sftpConnection = p.getObject();
        if (sftpConnection == null) {
            return false;
        }

        ChannelSftp channelSftp = sftpConnection.getChannelSftp();
        if (channelSftp == null) {
            return false;
        }

        try {
            if (channelSftp.getSession() == null) {
                return false;
            }
            if (!channelSftp.getSession().isConnected() || !channelSftp.isConnected()) {
                return false;
            }
        } catch (JSchException e) {
            log.error("异常信息：{}",e);
        }
        return true;
    }
}
