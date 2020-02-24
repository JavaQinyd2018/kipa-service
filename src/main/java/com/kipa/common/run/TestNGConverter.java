package com.kipa.common.run;

public interface TestNGConverter<P, R> {

    R convert(P testClass);

}
