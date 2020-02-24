package com.kipa.common.run;

import com.google.common.collect.Lists;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlTestNGConverter<T> implements TestNGConverter<Map<String, List<Class<? extends T>>>, XmlSuite>{

    @Override
    public XmlSuite convert(final Map<String, List<Class<? extends T>>> multiLaunchClass) {
        List<XmlTest> xmlTestList = Lists.newArrayListWithCapacity(multiLaunchClass.size());
        XmlSuite xmlSuite = new XmlSuite();
        multiLaunchClass.forEach((packageName, classeList) -> {
            XmlTest xmlTest = new XmlTest();
            xmlTest.setName(packageName);
            final List<XmlClass> xmlClassList = classeList.stream()
                    .map(aClass -> new XmlClass(aClass.getName()))
                    .collect(Collectors.toList());
            xmlTest.setClasses(xmlClassList);
            xmlTest.setSuite(xmlSuite);
            xmlTestList.add(xmlTest);
        });
        xmlSuite.setTests(xmlTestList);
        xmlSuite.setName("kipa-service-testng-suite");
        return xmlSuite;
    }
}
