package com.kipa.sftp;

import com.jcraft.jsch.Channel;
import com.kipa.common.KipaProcessException;
import org.apache.commons.pool2.BaseObjectPool;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/13
 */
public class SftpChannelPool extends BaseObjectPool<Channel> {

    private static final int DEFAULT_POOL_SIZE = 5;
    private BlockingQueue<Channel> pool;
    private SftpChannelFactory factory;
    private static final int TIME_OUT = 3;

    SftpChannelPool(SftpChannelFactory factory) {
        this(DEFAULT_POOL_SIZE, factory);
    }

    private SftpChannelPool(int maxSize, SftpChannelFactory factory) {
        this.factory = factory;
        this.pool = new ArrayBlockingQueue<>(maxSize);
        this.initPool(maxSize);
    }

    private void initPool(int maxSize) {
        for (int i = 0; i < maxSize; i++) {
            try {
                addObject();
            } catch (Exception e) {
                throw new KipaProcessException("sftp连接池初始化失败",e);
            }
        }
    }

    @Override
    public Channel borrowObject() throws Exception {
        Channel channel = pool.take();
        if (ObjectUtils.isEmpty(channel)) {
            channel = factory.create();
        }else if (!factory.validateObject(factory.wrap(channel))) {
            invalidateObject(channel);
            channel = factory.create();
        }
        return channel;
    }


    @Override
    public void returnObject(Channel channel) throws Exception {
        if (channel != null) {
            if (channel.isConnected()) {
                if (pool.size() < DEFAULT_POOL_SIZE && !pool.offer(channel, TIME_OUT, TimeUnit.SECONDS)) {
                    //超时了销毁掉
                    factory.destroy(channel);
                }
            }else {
                //断开连接的销毁掉
                factory.destroy(channel);
            }
        }
    }

    @Override
    public void invalidateObject(Channel channel) throws Exception {
        if (pool.remove(channel)) {
            factory.destroy(channel);
        }
    }

    @Override
    public void addObject() throws Exception {
        assert pool.offer(factory.create(), TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        pool.forEach(sftpChannel -> {
            try {
                factory.destroy(sftpChannel);
            } catch (Exception e) {
                throw new KipaProcessException("sftp关闭释放资源失败",e);
            }
        });
    }

}
