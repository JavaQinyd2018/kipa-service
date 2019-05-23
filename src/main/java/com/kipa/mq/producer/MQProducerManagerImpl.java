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

/**
 * @author: Qinyadong
 * @date: 2019/4/22 17:42
 * 生产者服务
 */
@Slf4j
@Service("mqProducerManager")
public class MQProducerManagerImpl implements MQProducerManager, InitializingBean, SmartLifecycle {

    private volatile  boolean isRunning = false;
    private DefaultMQProducer defaultMQProducer = null;
    private TransactionMQProducer transactionMQProducer = null;
    private volatile boolean initialized = false;

    @Autowired
    private BaseProducerService baseProducerService;

    @Autowired
    private MQProducerConfig mqProducerConfig;

    @Autowired
    private MQProducerGenerator mqProducerGenerator;

    @Override
    public boolean send(Message message) {
        PreCheckUtils.checkEmpty(message, "发送的消息不能为空");
        MQMessage mqMessage = MQMessage.builder().methodName(ProducerMethod.SEND.getMessage()).message(message).build();
        SendResult sendResult = baseProducerService.send(defaultMQProducer, mqMessage);
        return parseResult(sendResult);
    }

    @Override
    public void asyncSend(Message message, SendCallback sendCallback) {
        PreCheckUtils.checkEmpty(message, "发送的消息不能为空");
        PreCheckUtils.checkEmpty(sendCallback, "发送的消息不能为空");
        MQMessage mqMessage = MQMessage.builder().
                methodName(ProducerMethod.ASNYC_SEND.getMessage())
                .message(message)
                .sendCallback(sendCallback).build();
        baseProducerService.asnycSend(defaultMQProducer, mqMessage);
    }

    @Override
    public void sendOneWay(Message message) {
        PreCheckUtils.checkEmpty(message, "发送的消息不能为空");
        MQMessage mqMessage = MQMessage.builder()
                .methodName(ProducerMethod.SEND_ONE_WAY.getMessage())
                .message(message).build();
        baseProducerService.send(defaultMQProducer, mqMessage);
    }

    @Override
    public boolean sendOrder(Message message, MessageQueueSelector selector, Object arg) {
        PreCheckUtils.checkEmpty(message, "发送的消息不能为空");
        PreCheckUtils.checkEmpty(selector, "发送的消息selector不能为空");
        PreCheckUtils.checkEmpty(arg, "发送的参数不能为空");
        MQMessage mqMessage = MQMessage.builder().methodName(ProducerMethod.SEND_ORDER.getMessage())
                .selector(selector).arg(arg).build();
        SendResult sendResult = baseProducerService.send(defaultMQProducer, mqMessage);
        return parseResult(sendResult);
    }

    @Override
    public boolean sendOneWayOrder(Message message, MessageQueueSelector selector, Object arg) {
        PreCheckUtils.checkEmpty(message, "发送的消息不能为空");
        PreCheckUtils.checkEmpty(selector, "发送的消息selector不能为空");
        PreCheckUtils.checkEmpty(arg, "发送的参数不能为空");
        MQMessage mqMessage = MQMessage.builder().methodName(ProducerMethod.SEND_ONE_WAY_ORDER.getMessage())
                .selector(selector).arg(arg).build();
        SendResult sendResult = baseProducerService.send(defaultMQProducer, mqMessage);
        return parseResult(sendResult);
    }

    @Override
    public boolean sendInTransaction(Message message, Object arg) {
        PreCheckUtils.checkEmpty(message, "发送的消息不能为空");
        PreCheckUtils.checkEmpty(arg, "发送的参数不能为空");
        MQMessage mqMessage = MQMessage.builder().methodName(ProducerMethod.SEND_IN_TRANSACTION.getMessage())
                .message(message).arg(arg).build();
        TransactionSendResult sendResult = baseProducerService.sendInTransaction(transactionMQProducer, mqMessage);
        return parseResult(sendResult);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        defaultMQProducer = (DefaultMQProducer) mqProducerGenerator.build(mqProducerConfig, ProducerType.DEFAULT_PRODUCER);
        transactionMQProducer = (TransactionMQProducer) mqProducerGenerator.build(mqProducerConfig, ProducerType.TRANSACTION_PRODUCER);
        initialized = true;
    }

    @Override
    public void start() {
        if (initialized) {
            try {
                defaultMQProducer.start();
                transactionMQProducer.start();
                log.debug("===================生产者启动成功===================");
            } catch (MQClientException e) {
                log.error("生产者启动错误，错误信息是：{}",e);
            }
        }
        isRunning = true;
    }

    @Override
    public void stop() {
        defaultMQProducer.shutdown();
        transactionMQProducer.shutdown();
        isRunning = false;
        log.debug("===================生产者停止成功===================");
    }

    @Override
    public boolean isRunning() {
        return isRunning;
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

    private boolean parseResult(SendResult result) {
        if (result != null) {
            if (result instanceof TransactionSendResult) {
                TransactionSendResult transactionSendResult = (TransactionSendResult) result;
                return transactionSendResult.getLocalTransactionState().compareTo(LocalTransactionState.COMMIT_MESSAGE) == 0;
            }
            return result.getSendStatus().compareTo(SendStatus.SEND_OK) == 0;
        }
        return false;
    }

}
