package com.kipa.common.run;

import java.util.List;

@SuppressWarnings("all")
public class ClassTestNGConverter<T> implements TestNGConverter<List<Class<? extends T>>, Class<? extends T>[]> {

    @Override
    public Class<? extends T>[] convert(List<Class<? extends T>> typeListWithAnnotation) {
        Class<? extends T>[] classArray = new Class[typeListWithAnnotation.size()];
        for (int i = 0; i < typeListWithAnnotation.size(); i++) {
            classArray[i] = typeListWithAnnotation.get(i);
        }
        return classArray;
    }
}
