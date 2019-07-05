package com.kipa.data;

import com.kipa.common.KipaProcessException;
import com.kipa.mock.http.annotation.MockHttp;
import com.kipa.utils.AnnotationUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.ReflectionUtils;
import org.testng.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/20
 * 注解DataMeta，testng的监听器
 */
public class DataMetaAnnotationListener implements IInvokedMethodListener2 {
    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult, ITestContext iTestContext) {

        if (iInvokedMethod.isTestMethod()) {
            Class<?> declaringClass = iInvokedMethod.getTestMethod().getConstructorOrMethod().getDeclaringClass();
            Set<Method> methodWithAnnotation = AnnotationUtils.getMethodWithAnnotation(declaringClass, MockHttp.class);
            if (CollectionUtils.isNotEmpty(methodWithAnnotation)) {
                if (methodWithAnnotation.size() > 1) {
                    throw new KipaProcessException("当前类中只允许存在一个被@MockHttp注解标识的方法");
                }
                Method method = methodWithAnnotation.iterator().next();
                DataMeta dataMeta = method.getAnnotation(DataMeta.class);
                if (dataMeta != null) {
                    DataParam[] dataParams = dataMeta.value();
                    Parameter[] parameters = method.getParameters();
                    if (dataParams.length != parameters.length) {
                        throw new KipaProcessException("方法"+method.getName()+"参数个数个注解参数个数不一致，注解参数个数为："
                                + dataParams.length+"接口参数的个数为："+parameters.length);
                    }
                    if (ArrayUtils.isNotEmpty(dataParams)) {
                        //获取注解里面所有的参数对应的参数值
                        List<String> list = Arrays.stream(dataParams).map(DataParam::paramValue).collect(Collectors.toList());
                        try {
                            //调用目标方法，注意：如果target 直接new instance的话，注入进来的spring bean都会为空，导致调用失败
                            ReflectionUtils.invokeMethod(method, iInvokedMethod.getTestMethod().getInstance(), list.toArray());
                        } catch (Exception e) {
                            throw new KipaProcessException("方法"+method.getName()+"调用失败" ,e);
                        }

                    }

                }else {
                    DataParam dataParam = method.getAnnotation(DataParam.class);
                    if (dataParam != null) {
                        try {
                            //因此，通过当前测试方法获取当前调用的实例，再去在@Test方法运行之前运行@MockHttp方法，达到在http接口调用之前mock的效果
                            ReflectionUtils.invokeMethod(method, iInvokedMethod.getTestMethod().getInstance(), dataParam.paramValue());
                        } catch (Exception e) {
                            throw new KipaProcessException("方法"+method.getName()+"调用失败",e);
                        }
                    }
                }

            }

            Method method = iTestResult.getMethod().getConstructorOrMethod().getMethod();
            DataMeta dataMeta = method.getAnnotation(DataMeta.class);
            if (dataMeta != null ) {
                throw new UnsupportedOperationException("@Test的测试方法上不支持使用@DataMeta," +
                        "建议使用@DataParameter和dataprovider组合方式进行数据驱动");
            }

        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult, ITestContext iTestContext) {

    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    }
}
