package com.kipa.dubbo.excute;

import com.kipa.core.InvokeResponse;
import com.kipa.core.ResponseConverter;

public class DubboResponseConverter extends ResponseConverter<Object> {

    @Override
    public Object getResponse() {
        return reference.get();
    }

    @Override
    public InvokeResponse convert(Object o) throws Exception {
        DubboResponse dubboResponse = new DubboResponse();
        reference.set(o);
        dubboResponse.setSuccess(true);
        dubboResponse.setResponseData(o);
        dubboResponse.setMessage("dubbo接口rpc调用成功");
        return dubboResponse;
    }
}
