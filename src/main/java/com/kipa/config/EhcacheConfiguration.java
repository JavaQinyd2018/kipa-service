package com.kipa.config;

import com.kipa.data.ehcache.SpringCacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.impl.serialization.PlainJavaSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Qinyadong
 * @date 2019/8/16 9:55
 * @desciption 整合ehcache的配置类
 * @since 2.2.0
 */
@Configuration
public class EhcacheConfiguration {

    @Bean(name = "ehcacheCacheManager",initMethod = "init", destroyMethod = "close")
    public org.ehcache.CacheManager ehcacheCacheManager() {
        //1.构建缓存资源内存池信息
        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder()
                //设置缓存堆内存容纳的大小（jvm的内存空间），如果超出了就会存到offhead里面
                .heap(8, MemoryUnit.MB)
                //设置堆外储存大小(内存存储) 超出offheap的大小会淘汰规则被淘汰
                .offheap(9, MemoryUnit.MB);

        //2.构建缓存配置信息，key和value的类型分别为：string和Object类型
        CacheConfiguration<String, Object> cacheConfiguration =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class,Object.class, resourcePoolsBuilder)
                        //value的序列化器为PlainJavaSerializer
                        .withValueSerializer(new PlainJavaSerializer<>(this.getClass().getClassLoader()))
                        //失效日期为30分钟
                        .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(30)))
                        .build();
        //构建缓存管理器
        return CacheManagerBuilder.newCacheManagerBuilder()
                //对象最大值不能超过300
                .withDefaultSizeOfMaxObjectSize(100, MemoryUnit.KB)
                .withDefaultSizeOfMaxObjectGraph(200)
                .withCache("globalCache", cacheConfiguration)
                .build();
    }

    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        return new SpringCacheManager(ehcacheCacheManager());
    }


}
