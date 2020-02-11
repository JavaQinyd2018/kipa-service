package com.kipa.dubbo.excute;

import com.kipa.core.InvokeRequest;
import com.kipa.core.RequestConverter;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DubboRequestConverter extends RequestConverter<WrappedDubboParameter> {
    @Override
    public WrappedDubboParameter getRequest() {
        return reference.get();
    }

    @Override
    public WrappedDubboParameter convert(InvokeRequest invokeRequest) throws Exception {
        WrappedDubboParameter wrappedDubboParameter = new WrappedDubboParameter();
        DubboRequest request = (DubboRequest) invokeRequest;
        wrappedDubboParameter.setMethodName(request.getMethodName());
        Map<String, Object> classValuePair = request.getClassValuePair();
        if (MapUtils.isNotEmpty(classValuePair)) {
            int size = classValuePair.size();
            String[] paramTypeArray = new String[size];
            List<Object> valueList = new ArrayList<>(size);
            int index = 0;
            for (Map.Entry<String, Object> entry : classValuePair.entrySet()) {
                String s = entry.getKey();
                Object o = entry.getValue();
                paramTypeArray[index] = s;
                valueList.add(o);
                index++;
            }
            wrappedDubboParameter.setParamTypeArray(paramTypeArray);
            wrappedDubboParameter.setValueArray(valueList.toArray());
        }
        wrappedDubboParameter.setResponseCallback(request.getCallback());
        reference.set(wrappedDubboParameter);
        return wrappedDubboParameter;
    }
}
