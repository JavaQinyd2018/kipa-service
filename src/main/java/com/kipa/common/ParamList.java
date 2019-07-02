package com.kipa.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/7/1 15:05
 * @desciption 参数list集合
 */
public final class ParamList<E> {

    private List<E> list;

    private ParamList(List<E> list) {
        this.list = list;
    }

    public static <E> ParamList<E> newList() {
        return new ParamList<>(new ArrayList<>());
    }

    public static <E> ParamList<E> newList(List<E> list) {
        return new ParamList<>(list);
    }

    public ParamList<E> add(E element) {
        this.list.add(element);
        return this;
    }

    public List<E> build() {
        return this.list;
    }
}
