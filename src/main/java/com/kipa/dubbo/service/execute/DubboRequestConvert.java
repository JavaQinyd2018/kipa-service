package com.kipa.dubbo.service.execute;

import com.kipa.dubbo.entity.DubboRequest;
import com.kipa.dubbo.entity.WrappedDubboParameter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/3
 * 请求转化
 */
@Component("dubboRequestConvert")
public class DubboRequestConvert implements DubboConvert<DubboRequest, WrappedDubboParameter>{


    @Override
    public WrappedDubboParameter convert(DubboRequest request) {
        WrappedDubboParameter wrappedDubboParameter = new WrappedDubboParameter();
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
        wrappedDubboParameter.setResponseCallback(request.getResponseCallback());
        return wrappedDubboParameter;
    }
}
