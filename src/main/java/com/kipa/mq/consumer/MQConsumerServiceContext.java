package com.kipa.mq.consumer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 15:29
 * MQ消息消费的服务
 */
@Slf4j
@Service
public class MQConsumerServiceContext implements InitializingBean, SmartLifecycle, DisposableBean {

    private volatile boolean initialized = false;
    private final Object monitor = new Object();
    private volatile boolean running = false;

    @Autowired
    private RocketMQListenerProcessor rocketMQListenerProcessor;

    @Autowired
    private MQConsumerConfig mqConsumerConfig;

    @Autowired
    private MQConsumerGenerator mqConsumerGenerator;

    private List<SubscribeConfig> subscribeConfigList = Lists.newArrayList();
    private Map<RocketMQListener, Map<Method,Subscribe>> mqListenerMapMap = new ConcurrentHashMap<>();
    private Map<String, DefaultMQPushConsumer> defaultMQPushConsumerMap = new ConcurrentHashMap<>();
    private Map<String, DefaultMQPullConsumer> defaultMQPullConsumerMap = new ConcurrentHashMap<>();

    private Map<String, DefaultMQPushConsumer> runningConsumerMap = new ConcurrentHashMap<>();
    private Map<String, DefaultMQPushConsumer> errorConsumerMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        mqListenerMapMap = rocketMQListenerProcessor.getMqListenerMapMap();
        //解析 注解信息和订阅配置信息
        parse(mqListenerMapMap);
        //初始化Consumer
        initMQConsumer();
        initialized = true;
    }

    @Override
    public void start() {
        if (initialized && !isRunning()) {
            synchronized (monitor) {
                if (MapUtils.isNotEmpty(defaultMQPushConsumerMap)) {
                    defaultMQPushConsumerMap.forEach((topic, defaultMQPushConsumer) -> {
                        try {
                            defaultMQPushConsumer.start();
                            runningConsumerMap.put(topic, defaultMQPushConsumer);
                            log.debug("=================MQ的监听器启动完成===================");
                        } catch (MQClientException e) {
                            errorConsumerMap.put(topic, defaultMQPushConsumer);
                        }
                    });

                    if (MapUtils.isNotEmpty(errorConsumerMap)) {
                        log.warn("启动失败的监听有：{}",errorConsumerMap);
                    }
                }
            }
        }

    }

    @Override
    public void stop() {
        if (isRunning()) {
            if (MapUtils.isNotEmpty(runningConsumerMap)) {
                runningConsumerMap.forEach((topic, defaultMQPushConsumer) -> {
                    running = false;
                    defaultMQPushConsumer.shutdown();
                });
                log.debug("=================MQ的监听器已经停止===================");
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
        stop();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }


    private void parse(Map<RocketMQListener, Map<Method,Subscribe>> mqListenerMapMap) {
        if (MapUtils.isNotEmpty(mqListenerMapMap)) {
            mqListenerMapMap.forEach((rocketMQListener, methodSubscribeMap) -> {
                SubscribeConfig subscribeConfig = new SubscribeConfig();
                String topic = rocketMQListener.topic();
                MessageModel messageModel = rocketMQListener.messageModel();
                ConsumePattern pattern = rocketMQListener.pattern();
                SubscribeModel subscribeModel = rocketMQListener.subscriberModel();
                ConsumeFromWhere consumeFromWhere = rocketMQListener.consumePosition();
                subscribeConfig.setTopic(topic);
                subscribeConfig.setConsumePosition(consumeFromWhere);
                subscribeConfig.setMessageModel(messageModel);
                subscribeConfig.setPattern(pattern);
                subscribeConfig.setSubscribeModel(subscribeModel);
                PreCheckUtils.checkEmpty(topic, "消费的topic不能为空");
                Map<String, Method> tagMap = Maps.newHashMap();
                if (MapUtils.isEmpty(methodSubscribeMap)) {
                    log.warn("不存在带注解@Subscribe的方法");
                }
                Map<Method, Class<?>> methodClassMap = Maps.newHashMap();
                methodSubscribeMap.forEach((method, subscribe) -> {
                    String tag = subscribe.tag();
                    tagMap.put(tag, method);
                    methodClassMap.put(method, subscribe.messageType());
                });
                subscribeConfig.setTags(tagMap);
                subscribeConfig.setMethodMap(methodClassMap);
                subscribeConfigList.add(subscribeConfig);
            });
        }
    }

    /**
     * 根据注解初始化Consumer
     */
    private void initMQConsumer() {
        if (CollectionUtils.isNotEmpty(subscribeConfigList)) {
            subscribeConfigList.forEach(subscribeConfig -> {
                if (subscribeConfig.getPattern() == ConsumePattern.PUSH) {
                    DefaultMQPushConsumer mqConsumer = (DefaultMQPushConsumer) mqConsumerGenerator.createMQConsumer(mqConsumerConfig, subscribeConfig);
                    if (subscribeConfig.getSubscribeModel() == SubscribeModel.CURRENTLY) {
                        mqConsumer.registerMessageListener(new MessageListenerConcurrentlyImpl(subscribeConfig));
                    }else if (subscribeConfig.getSubscribeModel() == SubscribeModel.ORDERLY) {
                        mqConsumer.registerMessageListener(new MessageListenerOrderlyImpl(subscribeConfig));
                    }
                    defaultMQPushConsumerMap.put(subscribeConfig.getTopic(), mqConsumer);
                }else if (subscribeConfig.getPattern() == ConsumePattern.PULL){
                    //todo 暂时先不支持
                    DefaultMQPullConsumer mqConsumer = (DefaultMQPullConsumer) mqConsumerGenerator.createMQConsumer(mqConsumerConfig, subscribeConfig);
                    defaultMQPullConsumerMap.put(subscribeConfig.getTopic(), mqConsumer);
                }

            });
        }

    }

    @Override
    public void destroy() throws Exception {
        initialized = false;
        stop();
    }

}
