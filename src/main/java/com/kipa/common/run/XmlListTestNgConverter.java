package com.kipa.common.run;

import com.google.common.collect.Lists;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlListTestNgConverter<T> implements TestNGConverter<Map<String, List<Class<? extends T>>>, List<XmlSuite>>{

    @Override
    public List<XmlSuite> convert(Map<String, List<Class<? extends T>>> testClass) {
        List<XmlTest> xmlTestList = Lists.newArrayListWithCapacity(testClass.size());
        List<XmlSuite> xmlSuiteList = Lists.newArrayListWithCapacity(testClass.size());

        testClass.forEach((packageName, classList) -> {
            XmlSuite xmlSuite = new XmlSuite();
            XmlTest xmlTest = new XmlTest();
            xmlTest.setName(packageName);
            final List<XmlClass> xmlClassList = classList.stream()
                    .map(aClass -> new XmlClass(aClass.getName()))
                    .collect(Collectors.toList());
            xmlTest.setClasses(xmlClassList);
            xmlTest.setSuite(xmlSuite);
            xmlTestList.add(xmlTest);
            xmlSuite.setTests(xmlTestList);
            xmlSuite.setName(packageName);
            xmlSuiteList.add(xmlSuite);
        });
        return xmlSuiteList;
    }
}
