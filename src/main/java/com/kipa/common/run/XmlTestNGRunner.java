package com.kipa.common.run;

import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import java.util.Collections;

public class XmlTestNGRunner extends AbstractTestNGRunner<XmlSuite> {

    public XmlTestNGRunner(boolean useDefaultListeners, Class<? extends ITestNGListener> listenerClass) {
        super(useDefaultListeners, listenerClass);
    }

    @Override
    void doRun(TestNG testNG, XmlSuite test) {
        testNG.setXmlSuites(Collections.singletonList(test));
    }
}
