package com.kipa.sftp;

import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BaseObjectPool;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/13
 */
@Slf4j
public class SftpConnectionPool extends BaseObjectPool<SftpConnection> {

    private static final int DEFAULT_POOL_SIZE = 5;
    private BlockingQueue<SftpConnection> pool = null;
    private SftpConnectionFactory factory = null;
    private static final int timeout = 3;

    SftpConnectionPool(SftpConnectionFactory factory) {
        this(DEFAULT_POOL_SIZE, factory);
    }

    private SftpConnectionPool(int maxSize, SftpConnectionFactory factory) {
        this.factory = factory;
        this.pool = new ArrayBlockingQueue<>(maxSize);
        this.initPool(maxSize);
    }

    private void initPool(int maxSize) {
        for (int i = 0; i < maxSize; i++) {
            try {
                addObject();
            } catch (Exception e) {
                throw new RuntimeException("Sftp 连接池初始化失败",e);
            }
        }
    }

    @Override
    public SftpConnection borrowObject() throws Exception {
        SftpConnection sftpConnection = pool.take();
        if (ObjectUtils.isEmpty(sftpConnection)) {
            sftpConnection = factory.create();
        }else if (!factory.validateObject(factory.wrap(sftpConnection))) {
            invalidateObject(sftpConnection);
            sftpConnection = factory.create();
        }
        return sftpConnection;
    }


    @Override
    public void returnObject(SftpConnection sftpConnection) throws Exception {
        if (sftpConnection != null) {
            ChannelSftp channelSftp = sftpConnection.getChannelSftp();
            if (channelSftp != null) {
                if (channelSftp.isConnected()) {
                    if (pool.size() < DEFAULT_POOL_SIZE) {
                        if (pool.offer(sftpConnection, timeout, TimeUnit.SECONDS)) {
                            factory.destroy(sftpConnection);
                        }
                    }
                }else {
                    factory.destroy(sftpConnection);
                }
            }else {
                factory.destroy(sftpConnection);
            }
        }
    }

    @Override
    public void invalidateObject(SftpConnection sftpConnection) throws Exception {
        try {
            pool.remove(sftpConnection);
            factory.destroy(sftpConnection);
        } catch (Exception e) {
            throw new RuntimeException("sftp连接置失效失败",e);
        }
    }

    @Override
    public void addObject() throws Exception, UnsupportedOperationException {
        pool.offer(factory.create(), timeout, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        for (SftpConnection sftpConnection : pool) {
            try {
                SftpConnection connection = pool.take();
                factory.destroy(connection);
            } catch (Exception e) {
                throw new RuntimeException("sftp关闭释放资源失败",e);
            }
        }
    }
}
