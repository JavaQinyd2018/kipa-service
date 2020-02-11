package com.kipa.mq.producer;

import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 17:42
 * 生产者服务
 */
@Slf4j
@Service("mqProducerManager")
public class MQProducerManagerImpl implements MQProducerManager, InitializingBean, SmartLifecycle {

    private AtomicBoolean running = new AtomicBoolean(false);
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private DefaultMQProducer defaultMQProducer;
    private TransactionMQProducer transactionMQProducer;

    @Autowired
    private BaseProducerService baseProducerService;

    @Autowired
    private MQProducerProperties mqProducerProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        defaultMQProducer = (DefaultMQProducer) new MQProducerGenerator(ProducerType.DEFAULT_PRODUCER).create(mqProducerProperties);
        transactionMQProducer = (TransactionMQProducer) new MQProducerGenerator(ProducerType.TRANSACTION_PRODUCER).create(mqProducerProperties);
        initialized.set(true);
    }

    @Override
    public void start() {
        if (initialized.get() && !running.get()) {
            try {
                defaultMQProducer.start();
                transactionMQProducer.start();
                log.debug("===================生产者启动成功===================");
            } catch (MQClientException e) {
                log.error("生产者启动错误，错误信息是:{}",e);
            }
        }
        running.set(true);
    }

    @Override
    public void stop() {
        if (running.get()) {
            defaultMQProducer.shutdown();
            transactionMQProducer.shutdown();
            running.set(false);
            if (log.isDebugEnabled()) {
                log.debug("===================生产者停止成功===================");
            }
        }

    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }


    @Override
    public SendResult send(MQMessage mqMessage, MessageType messageType) {
        return baseProducerService.send(defaultMQProducer, mqMessage, messageType);
    }

    @Override
    public void asyncSend(MQMessage mqMessage, MessageType messageType) {
        baseProducerService.asyncSend(defaultMQProducer, mqMessage, messageType);
    }

    @Override
    public void sendOneWay(MQMessage mqMessage, MessageType messageType) {
        baseProducerService.sendOneWay(defaultMQProducer, mqMessage, messageType);
    }

    @Override
    public TransactionSendResult sendInTransaction(MQMessage mqMessage, MessageType messageType) {
        return baseProducerService.sendInTransaction(transactionMQProducer, mqMessage, messageType);
    }
}
