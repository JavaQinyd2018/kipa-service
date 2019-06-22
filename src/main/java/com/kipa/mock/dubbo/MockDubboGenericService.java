package com.kipa.mock.dubbo;

import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/5/29 15:08
 *  dubbo 泛化接口的实现类
 */
public class MockDubboGenericService implements GenericService {

    private MockDubboResponse mockDubboResponse;

    private MockDubboRequest mockDubboRequest;

    MockDubboGenericService(MockDubboRequest request, MockDubboResponse response) {
        this.mockDubboRequest = request;
        this.mockDubboResponse = response;
    }

    @Override
    public Object $invoke(String s, String[] strings, Object[] objects) throws GenericException {
        String[] interfaceAndMethod = StringUtils.split(s, ".");
        String interfaceName = interfaceAndMethod[0];
        String methodName = interfaceAndMethod[1];
        boolean falg = checkAndReturn(interfaceName, methodName, strings, objects, mockDubboRequest);
        return falg ? mockDubboResponse : null;
    }

    private static boolean checkAndReturn(String interfaceName,
                                          String methodName,
                                          String[] paramTypes,
                                          Object[] values,
                                          MockDubboRequest request) {
        Map<String, Object> classValuePair = request.getClassValuePair();
        if (!StringUtils.equals(interfaceName, request.getInterfaceName())) {
            return false;
        }else if (!StringUtils.equals(methodName, request.getMethodName())) {
            return false;
        }else if (ArrayUtils.isNotEmpty(paramTypes) && MapUtils.isNotEmpty(classValuePair)) {
            if (paramTypes.length != classValuePair.size()) {
                return false;
            }else {
                for (int i = 0; i < paramTypes.length; i++) {
                    if (!classValuePair.get(paramTypes[i]).equals(values[i])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
