package com.kipa.mock.service.execute;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 16:11
 * @since:
 */
public interface MockConvert<R,T> {
    
    T convert(R request);
}
