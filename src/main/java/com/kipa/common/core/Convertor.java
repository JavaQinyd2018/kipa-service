package com.kipa.common.core;

/**
 * @author Qinyadong
 * @date 2019/12/17 10:32
 * @description 统一的接口转化器
 * @since
 */
public interface Convertor<B, T> {

    /**
     * 调用转化器
     * @param builder
     * @return
     */
    T convert(B builder);
}
