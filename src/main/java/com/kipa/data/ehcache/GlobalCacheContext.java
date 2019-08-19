package com.kipa.data.ehcache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * @author Qinyadong
 * @date 2019/8/16 16:25
 * @desciption
 * @since
 */
@Component
public class GlobalCacheContext implements InitializingBean {

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;
    @Override
    public void afterPropertiesSet() throws Exception {
        cache = cacheManager.getCache("globalCache");
    }

    public void setAttribute(String name, Object value) {
        cache.put(name, value);
    }

    public void setAttributeIfAbsent(String name, Object value) {
        cache.putIfAbsent(name, value);
    }

    public Object getAttribute(String name) {
        Cache.ValueWrapper valueWrapper = cache.get(name);
        return valueWrapper == null ? null : valueWrapper.get();
    }

    public void removeAttribute(String name) {
        cache.evict(name);
    }

    public void removeAll() {
        cache.clear();
    }

    public <T> T getAttribute(String name, Class<T> clazz) {
        return cache.get(name, clazz);
    }
}
