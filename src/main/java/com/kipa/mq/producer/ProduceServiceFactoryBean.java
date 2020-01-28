package com.kipa.mq.producer;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 18:38
 * 实现代理
 */
@Service("baseProducerService")
public class ProduceServiceFactoryBean implements FactoryBean<BaseProducerService> {

    @Override
    public BaseProducerService getObject() throws Exception {
        return (BaseProducerService) Proxy.newProxyInstance(BaseProducerService.class.getClassLoader(),
                new Class[]{BaseProducerService.class}, new ProducerInvocationHandler());
    }

    @Override
    public Class<?> getObjectType() {
        return BaseProducerService.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    private static class ProducerInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            MQMessage message = (MQMessage) args[1];
            MessageType messageType = (MessageType) args[2];
            if (StringUtils.equalsIgnoreCase(methodName, "send")) {
                DefaultMQProducer producer = (DefaultMQProducer) args[0];
                return invokeSend(producer, message, messageType);
            } else if (StringUtils.equalsIgnoreCase(methodName, "asyncSend")) {
                DefaultMQProducer producer = (DefaultMQProducer) args[0];
                invokeAsyncSend(producer, message, messageType);
            } else if (StringUtils.equalsIgnoreCase(methodName, "sendInTransaction")) {
                TransactionMQProducer producer = (TransactionMQProducer) args[0];
                return producer.sendMessageInTransaction(message.getMessage(), message.getArg());
            } else if (StringUtils.endsWithIgnoreCase(methodName, "sendOneWay")) {
                DefaultMQProducer producer = (DefaultMQProducer) args[0];
                invokeSendOneWay(producer, message, messageType);
            }
            return null;
        }

        /**
         * 同步调用
         *
         * @param producer
         * @param mqMessage
         * @param messageType
         * @return
         * @throws InterruptedException
         * @throws RemotingException
         * @throws MQClientException
         * @throws MQBrokerException
         */
        private SendResult invokeSend(DefaultMQProducer producer, MQMessage mqMessage, MessageType messageType) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
            switch (messageType) {
                case NORMAL:
                    return producer.send(mqMessage.getMessage(), mqMessage.getTimeout());
                case ORDERLY:
                    return producer.send(mqMessage.getMessage(), mqMessage.getSelector(), mqMessage.getArg(), mqMessage.getTimeout());
                case SPECIAL:
                    return producer.send(mqMessage.getMessage(), mqMessage.getMessageQueue(), mqMessage.getTimeout());
                default:
                    break;
            }
            return null;
        }

        /**
         * 异步调用
         *
         * @param producer
         * @param mqMessage
         * @param messageType
         * @throws RemotingException
         * @throws MQClientException
         * @throws InterruptedException
         */
        private void invokeAsyncSend(DefaultMQProducer producer, MQMessage mqMessage, MessageType messageType) throws RemotingException, MQClientException, InterruptedException {
            switch (messageType) {
                case NORMAL:
                    producer.send(mqMessage.getMessage(), mqMessage.getSendCallback(), mqMessage.getTimeout());
                    break;
                case ORDERLY:
                    producer.send(mqMessage.getMessage(), mqMessage.getSelector(), mqMessage.getArg(), mqMessage.getSendCallback());
                    break;
                case SPECIAL:
                    producer.send(mqMessage.getMessage(), mqMessage.getMessageQueue(), mqMessage.getSendCallback(), mqMessage.getTimeout());
                    break;
                default:
                    break;
            }
        }


        /**
         * 单向发送
         * @param producer
         * @param mqMessage
         * @param messageType
         * @throws RemotingException
         * @throws MQClientException
         * @throws InterruptedException
         */
        private void invokeSendOneWay(DefaultMQProducer producer, MQMessage mqMessage, MessageType messageType) throws RemotingException, MQClientException, InterruptedException {
            switch (messageType) {
                case NORMAL:
                    producer.sendOneway(mqMessage.getMessage());
                    break;
                case ORDERLY:
                    producer.sendOneway(mqMessage.getMessage(), mqMessage.getSelector(), mqMessage.getArg());
                    break;
                case SPECIAL:
                    producer.sendOneway(mqMessage.getMessage(), mqMessage.getMessageQueue());
                    break;
                default:
                    break;
            }
        }
    }
}
