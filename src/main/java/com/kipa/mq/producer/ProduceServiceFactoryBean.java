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

    private class ProducerInvocationHandler implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if (StringUtils.equalsIgnoreCase(methodName, "send")) {
                DefaultMQProducer producer = (DefaultMQProducer) args[0];
                MQMessage message = (MQMessage) args[1];
                return invokeSend(producer, message);
            }else if (StringUtils.equalsIgnoreCase(methodName, "asnycSend")) {
                DefaultMQProducer producer = (DefaultMQProducer) args[0];
                MQMessage message = (MQMessage) args[1];
                invokeSend(producer, message);
            }else if (StringUtils.equalsIgnoreCase(methodName, "sendInTransaction")) {
                TransactionMQProducer producer = (TransactionMQProducer) args[0];
                MQMessage message = (MQMessage) args[1];
                return invokeSend(producer, message);
            }
            return null;
        }
    }


    private static SendResult invokeSend(MQProducer mqProducer, MQMessage mqMessage) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        SendResult sendResult = null;
        String methodName = mqMessage.getMethodName();
        ProducerMethod method = ProducerMethod.getMethod(methodName);
        switch (method) {
            case SEND:
                sendResult = mqProducer.send(mqMessage.getMessage());
                break;
            case ASNYC_SEND:
                mqProducer.send(mqMessage.getMessage(), mqMessage.getSendCallback());
                break;
            case SEND_ONE_WAY:
                mqProducer.sendOneway(mqMessage.getMessage());
                break;
            case SEND_ONE_WAY_ORDER:
                mqProducer.sendOneway(mqMessage.getMessage(), mqMessage.getSelector(),mqMessage.getArg());
                break;
            case SEND_IN_TRANSACTION:
                if (mqMessage.getLocalTransactionExecuter() == null) {
                    sendResult =  mqProducer.sendMessageInTransaction(mqMessage.getMessage(), mqMessage.getArg());
                }else {
                    sendResult =  mqProducer.sendMessageInTransaction(mqMessage.getMessage(), mqMessage.getLocalTransactionExecuter(), mqMessage.getArg());
                }
                break;
             default:
                break;
        }
        return sendResult;
    }
}
