package com.kipa.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.google.common.collect.Lists;
import com.kipa.common.DataConstant;
import com.kipa.common.KipaProcessException;
import com.kipa.redis.ClusterRedisCondition;
import com.kipa.redis.RedisConfig;
import com.kipa.redis.StandAloneRedisCondition;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/14
 * spring整合Redis的配置文件
 */
@Configuration
@PropertySource("classpath:redis/redis-client.properties")
public class RedisConfiguration {

    private RedisConfig redisConfig;

    @Value("${spring.redis.timeout}")
    private Integer timeout;

    @Value("${spring.redis.default.db}")
    private Integer db;

    @Value("${spring.redis.maxTotal}")
    private Integer maxTotal;

    @Value("${spring.redis.maxIdle}")
    private Integer maxIdle;

    @Value("${spring.redis.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;

    @Value("${spring.redis.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;

    @Value("${spring.redis.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.redis.blockWhenExhausted}")
    private boolean blockWhenExhausted;

    @Value("${spring.redis.maxWaitMillis}")
    private Long maxWaitMillis;

    @PostConstruct
    private void init() {
        redisConfig = new RedisConfig();
        Properties properties = PropertiesUtils.loadProperties(DataConstant.CONFIG_FILE);
        String clusterAddress = PropertiesUtils.getProperty(properties, null, "spring.redis.cluster.address");
        List<String> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(clusterAddress, "redis服务器的地址不能为空");
        String clusterPassword = properties.getProperty("spring.redis.cluster.password");
        String aloneAddress = PropertiesUtils.getProperty(properties, null, "spring.redis.standalone.address");
        String standAlonePassword = properties.getProperty("spring.redis.standalone.password");
        if (StringUtils.isBlank(clusterAddress) && StringUtils.isBlank(aloneAddress)) {
            throw new KipaProcessException("请配置redis的服务地址");
        }

        try {
            if (StringUtils.isNotBlank(clusterAddress)) {
                String[] clusterArray = clusterAddress.split(",");
                list.addAll(Arrays.asList(clusterArray));
                redisConfig.setList(list);
                redisConfig.setClusterPassword(clusterPassword);
            }
            if (StringUtils.isNotBlank(standAlonePassword)) {
                String[] split = aloneAddress.split(":");
                String standAloneHost = split[0];
                Integer standAlonePort = Integer.valueOf(split[1]);
                redisConfig.setStandAloneHost(standAloneHost);
                redisConfig.setStandAlonePassword(standAlonePassword);
                redisConfig.setStandAlonePort(standAlonePort);
            }
        } catch (Exception e) {
            throw new KipaProcessException("redis的服务地址格式有误");
        }

    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        init();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        return jedisPoolConfig;
    }

    /**
     * 集群版
     * @return
     */
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        List<RedisNode> redisNodes = Lists.newArrayList();
        redisConfig.getList().forEach(address -> {
            String[] split = address.split(":");
            RedisNode redisNode = new RedisClusterNode(split[0], Integer.valueOf(split[1]));
            redisNodes.add(redisNode);
        });
        redisClusterConfiguration.setClusterNodes(redisNodes);
        redisClusterConfiguration.setMaxRedirects(3);
        redisClusterConfiguration.setPassword(redisConfig.getClusterPassword());
        return redisClusterConfiguration;
    }

    /**
     * 单机版
     * @return
     */
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisConfig.getStandAloneHost());
        redisStandaloneConfiguration.setPort(redisConfig.getStandAlonePort());
        redisStandaloneConfiguration.setPassword(redisConfig.getStandAlonePassword());
        redisStandaloneConfiguration.setDatabase(db);
        return redisStandaloneConfiguration;
    }

    @Conditional(StandAloneRedisCondition.class)
    @Bean("connectionFactory")
    public JedisConnectionFactory standAloneConnectionFactory() {
        return new JedisConnectionFactory(redisStandaloneConfiguration());
    }


    @Conditional(ClusterRedisCondition.class)
    @Bean("connectionFactory")
    public JedisConnectionFactory clusterConnectionFactory() {
        return new JedisConnectionFactory(redisClusterConfiguration(), jedisPoolConfig());
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(@Qualifier("connectionFactory") JedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(@Qualifier("connectionFactory") JedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return stringRedisTemplate;
    }

}
