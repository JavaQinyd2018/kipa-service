package com.kipa.common.run;

import com.google.common.collect.Lists;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Qinyadong
 * @date 2019/8/17 12:51
 * @desciption 步骤拦截器，会对每个带有@Step的注解做拦截，按照特定的规则执行
 * @since 2.2.0
 */
public class StepInterceptor implements IMethodInterceptor {

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {
        //1
        List<Object[]> orderList = Lists.newArrayList();
        List<IMethodInstance> otherMethodList = Lists.newArrayList();
        list.forEach(iMethodInstance -> {
            Method method = iMethodInstance.getMethod().getConstructorOrMethod().getMethod();
            Step step = method.getAnnotation(Step.class);
            if (step != null) {
                Object[] orderArray = new Object[2];
                int order = step.order();
                orderArray[0] = order;
                orderArray[1] = iMethodInstance;
                orderList.add(orderArray);
            }else {
                otherMethodList.add(iMethodInstance);
            }
        });
        List<IMethodInstance> methodInstanceList = orderMethod(orderList);
        methodInstanceList.addAll(otherMethodList);
        return methodInstanceList;
    }

    private List<IMethodInstance> orderMethod(final List<Object[]> orderList) {
        return orderList.stream()
                //排序
                .sorted(Comparator.comparingInt(o -> (Integer) o[0]))
                //获取测试方法
                .map(objects -> (IMethodInstance)objects[1])
                .collect(Collectors.toList());
    }
}
