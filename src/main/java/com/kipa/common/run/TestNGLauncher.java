package com.kipa.common.run;

public interface TestNGLauncher<T> {

    void launch(ClassTestNGConverter<T> converter, ClassTestNGRunner runner);

    void launch(XmlTestNGConverter<T> converter, XmlTestNGRunner runner);
}
