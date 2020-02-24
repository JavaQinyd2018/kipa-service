package com.kipa.common.run;

import java.util.List;
import java.util.Map;

public interface TestNGHandler<T> {

    List<Class<? extends T>> getLaunchClass();

    Map<String, List<Class<? extends T>>> getMultiLaunchClass();
}
