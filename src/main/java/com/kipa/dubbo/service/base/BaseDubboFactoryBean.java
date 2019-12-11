package com.kipa.dubbo.service.base;

import com.alibaba.dubbo.config.AbstractConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.dubbo.entity.DubboRequest;
import com.kipa.dubbo.entity.DubboResponse;
import com.kipa.dubbo.entity.WrappedDubboParameter;
import com.kipa.dubbo.enums.InvokeType;
import com.kipa.dubbo.annotation.Style;
import com.kipa.dubbo.entity.DubboProperties;
import com.kipa.dubbo.exception.DubboInvokeException;
import com.kipa.dubbo.service.execute.DubboAsyncExecutor;
import com.kipa.dubbo.service.execute.DubboRequestConvert;
import com.kipa.dubbo.service.execute.DubboSyncExecutor;
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

    private ReferenceConfig referenceConfig;

    @Autowired
    private DubboProperties dubboProperties;

    @Autowired
    private ReferenceConfigGenerator referenceConfigGenerator;

    @Autowired
    private DubboSyncExecutor dubboSyncExecutor;

    @Autowired
    private DubboAsyncExecutor dubboAsyncExecutor;

    @Autowired
    private DubboRequestConvert dubboRequestConvert;

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
         referenceConfig = referenceConfigGenerator.build(dubboProperties);
    }

    class DubboInvokeHandler implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //dubbo 响应数据
            DubboResponse dubboResponse = new DubboResponse();

            Style annotation = method.getAnnotation(Style.class);
            InvokeType type = annotation.type();

            DubboRequest dubboRequest = (DubboRequest) args[0];

            referenceConfig.setGeneric(true);
            referenceConfig.setInterface(dubboRequest.getInterfaceName());


            ReferenceConfigCache cache = ReferenceConfigCache.getCache(dubboProperties.getAddress(), AbstractConfig::toString);
            WrappedDubboParameter parameter = dubboRequestConvert.convert(dubboRequest);
            Object result = null;
            GenericService genericService = null;
            switch (type) {
                case SYNCHRONOUS:
                    referenceConfig.setAsync(false);
                    genericService = getGenericService(cache, referenceConfig);
                    result = dubboSyncExecutor.execute(genericService, parameter);
                    break;
                case ASYNCHRONOUS:
                    referenceConfig.setAsync(true);
                    genericService = getGenericService(cache, referenceConfig);
                    result = dubboAsyncExecutor.execute(genericService, parameter);
                    break;
                case DIRECTED_LINK:
                    String rpcProtocol = StringUtils.isBlank(dubboProperties.getRpcProtocol()) ? "dubbo" : dubboProperties.getRpcProtocol();
                    String url = String.format("%s://%s", rpcProtocol, dubboProperties.getAddress());
                    referenceConfig.setUrl(url);
                    genericService = getGenericService(cache, referenceConfig);
                    result = dubboSyncExecutor.execute(genericService, parameter);
                    break;
                default:
                    break;
            }
            dubboResponse.setSuccess(true);
            dubboResponse.setResponseData(result);
            dubboResponse.setMessage("dubbo接口rpc调用成功");
            return dubboResponse;
        }


        private GenericService getGenericService(ReferenceConfigCache cache, ReferenceConfig referenceConfig) {
            GenericService genericService = null;
            try {
                genericService = (GenericService) cache.get(referenceConfig);
            } catch (Exception e) {
                throw new DubboInvokeException("dubbo服务调用失败，获取泛化服务失败，找不到服务提供者",e);
            }
            return genericService;
        }
    }


}
