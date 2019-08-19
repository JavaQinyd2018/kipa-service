package com.kipa.data.ehcache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * @author Qinyadong
 * @date 2019/8/16 10:20
 * @desciption
 * @since
 */
@Slf4j
public class SpringCacheManager implements CacheManager {

    private org.ehcache.CacheManager cacheManager;

    public SpringCacheManager(org.ehcache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Cache getCache(String name) {
        org.ehcache.Cache<String, Object> cache = cacheManager.getCache(name, String.class, Object.class);
        return new Cache() {
            @Override
            public String getName() {
                //直接返回当前的name
                return name;
            }

            @Override
            public Object getNativeCache() {
                //返回本地缓存
                return cache;
            }

            @Override
            public ValueWrapper get(Object o) {
                Object value = cache.get(String.valueOf(o));
                //包装当前缓存里面的值
                return value == null ? null : () -> value;
            }

            @Override
            public <T> T get(Object o, Class<T> aClass) {
                T entity = null;
                Object value = cache.get(String.valueOf(o));
                if (value != null && aClass != null ) {
                    if (aClass.isInstance(value)) {
                        entity = (T)value;
                    }else {
                        throw  new IllegalStateException("缓存值的类型和当前类型" + aClass.getName() + "不匹配");
                    }
                }
                return entity;
            }

            @Override
            public <T> T get(Object o, Callable<T> callable) {
                try {
                    return callable.call();
                } catch (Exception e) {
                    log.error("缓存回调异常：{}",e);
                    return null;
                }
            }

            @Override
            public void put(Object o, Object o1) {
                cache.put(String.valueOf(o), o1);
            }

            @Override
            public ValueWrapper putIfAbsent(Object o, Object o1) {
                Object value = cache.putIfAbsent(String.valueOf(o), o1);
                return value == null ? null : () -> value;
            }

            //根据缓存key移除value
            @Override
            public void evict(Object o) {
                cache.remove(String.valueOf(o));
            }

            @Override
            public void clear() {
                cache.clear();
            }
        };
    }

    @Override
    public Collection<String> getCacheNames() {
        return null;
    }

}
