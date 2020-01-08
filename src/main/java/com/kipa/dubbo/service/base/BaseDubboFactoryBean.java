package com.kipa.dubbo.service.base;

import com.alibaba.dubbo.config.AbstractConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.common.KipaProcessException;
import com.kipa.core.BaseExecutor;
import com.kipa.dubbo.excute.*;
import com.kipa.dubbo.enums.InvokeType;
import com.kipa.dubbo.annotation.Style;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 17:30
 * dubbo基础服务的factory bean
 */
@Slf4j
@Service("baseDubboService")
public class BaseDubboFactoryBean implements FactoryBean<BaseDubboService>, InitializingBean {

    @Autowired
    private DubboProperties dubboProperties;
    /**
     * 消费方引用的配置类
     */
    private ReferenceConfig referenceConfig;
    private BaseExecutor<GenericService, WrappedDubboParameter, Object> syncExecutor;
    private BaseExecutor<GenericService, WrappedDubboParameter, Object> asyncExecutor;

    @Override
    public BaseDubboService getObject() throws Exception {
        return (BaseDubboService) Proxy.newProxyInstance(BaseDubboService.class.getClassLoader(), new Class<?>[]{BaseDubboService.class}, new DubboInvokeHandler());
    }

    @Override
    public Class<?> getObjectType() {
        return BaseDubboService.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final ReferenceConfigClientFactory clientFactory = new ReferenceConfigClientFactory();
        referenceConfig = clientFactory.create(dubboProperties);
        final DubboRequestConverter requestConverter = new DubboRequestConverter();
        final DubboResponseConverter responseConverter = new DubboResponseConverter();
        final DubboSyncInvoker syncInvoker = new DubboSyncInvoker();
        final DubboAsyncInvoker asyncInvoker = new DubboAsyncInvoker();
        syncExecutor = new BaseExecutor<>(syncInvoker, requestConverter, responseConverter);
        asyncExecutor = new BaseExecutor<>(asyncInvoker, requestConverter, responseConverter);
    }

    class DubboInvokeHandler implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //dubbo 响应数据
            Style annotation = method.getAnnotation(Style.class);
            InvokeType type = annotation.type();

            DubboRequest dubboRequest = (DubboRequest) args[0];
            //设置泛化
            referenceConfig.setGeneric(true);
            referenceConfig.setInterface(dubboRequest.getInterfaceName());
            referenceConfig.setVersion(dubboRequest.getVersion());

            //ReferenceConfig的实例太重了，需要缓存
            ReferenceConfigCache cache = ReferenceConfigCache.getCache(dubboProperties.getAddress(), AbstractConfig::toString);
            Object result = null;
            GenericService genericService = null;
            //按照不同的类型配置
            switch (type) {
                case SYNCHRONOUS:
                    referenceConfig.setAsync(false);
                    genericService = getGenericService(cache, referenceConfig);
                    syncExecutor.setClient(genericService);
                    result = syncExecutor.execute(dubboRequest);
                    break;
                case ASYNCHRONOUS:
                    referenceConfig.setAsync(true);
                    genericService = getGenericService(cache, referenceConfig);
                    asyncExecutor.setClient(genericService);
                    result = asyncExecutor.execute(dubboRequest);
                    break;
                case DIRECTED_LINK:
                    String rpcProtocol = StringUtils.isBlank(dubboProperties.getRpcProtocol()) ? "dubbo" : dubboProperties.getRpcProtocol();
                    String url = String.format("%s://%s", rpcProtocol, dubboProperties.getAddress());
                    referenceConfig.setUrl(url);
                    genericService = getGenericService(cache, referenceConfig);
                    syncExecutor.setClient(genericService);
                    result = syncExecutor.execute(dubboRequest);
                    break;
                default:
                    break;
            }
            return result;
        }

        /**
         * 获取泛化的服务
         * @param cache
         * @param referenceConfig
         * @return
         */
        private GenericService getGenericService(ReferenceConfigCache cache, ReferenceConfig referenceConfig) {
            GenericService genericService = null;
            try {
                genericService = (GenericService) cache.get(referenceConfig);
            } catch (Exception e) {
                throw new KipaProcessException("dubbo服务调用失败，获取泛化服务失败，找不到服务提供者",e);
            }
            return genericService;
        }
    }


}
