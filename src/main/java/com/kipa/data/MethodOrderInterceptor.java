package com.kipa.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/20
 * 获取所有的@Test的方法，根据方法名称排序，具体的排序规则是：
 * 方法名_n 按照n的数值自动排序进行运行，不符合规则的还是按照正常的执行顺序执行
 * 如果一个方法上已经标有执行的优先级了，那该类的所有的方法都按照testng已经提供的priority属性的优先级执行
 */
@Slf4j
public class MethodOrderInterceptor implements IMethodInterceptor {

    /**
     * 方法名正则表达式
     */
    private static final String METHOD_REGEX = "^[a-z A-Z]*_[1-9]+";

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {
        List<Object[]> methodInfoList = Lists.newArrayList();
        List<IMethodInstance> methodInstanceList = Lists.newArrayList();
        list.forEach(iMethodInstance -> {
            final Object[] methodInfo = new Object[3];
            ITestNGMethod method = iMethodInstance.getMethod();
            if (method.isTest()) {
                Test test = method.getConstructorOrMethod().getMethod().getAnnotation(Test.class);
                if (test.priority() == 0) {
                    String methodName = method.getMethodName();
                    //匹配方法后面的数字，比如方法名：hello_1, 按照数字排序进行执行
                    if (Pattern.matches(METHOD_REGEX, methodName)) {
                        String[] methodArray = StringUtils.split(methodName, "_");
                        methodInfo[0] = iMethodInstance;
                        methodInfo[1] = methodArray[0];
                        methodInfo[2] = NumberUtils.isDigits(methodArray[1]) ? Integer.valueOf(methodArray[1]) : 0;
                        methodInfoList.add(methodInfo);
                    }else {
                        //不符合规则的直接放到list里面
                        methodInstanceList.add(iMethodInstance);
                    }
                }
            }
        });
        List<IMethodInstance> iMethodInstances = orderMethod(methodInfoList);
        iMethodInstances.addAll(methodInstanceList);
        log.debug("拦截器传入的方法的个数为：{}，处理之后方法的个数为：{}",list.size(), iMethodInstances.size());
        return iMethodInstances;
    }

    /**
     * 按照数字排序进行执行
     * @param methodInfoList 方法信息
     * @return 排序之后的方法
     */
   private static List<IMethodInstance> orderMethod(final List<Object[]> methodInfoList) {
       List<IMethodInstance> methodInstanceList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(methodInfoList)) {
            methodInstanceList = methodInfoList.stream().sorted((o1, o2) ->
                    //先比较方法名，按照首字母先后顺序排序，如果方法名相同，则按照下划线后面的数字排序（升序）
                StringUtils.compareIgnoreCase((String) o1[1], (String) o2[1]) == 0 ?
                        Integer.compare((Integer) o1[2], (Integer) o2[2]) : StringUtils.compareIgnoreCase((String) o1[1], (String) o2[1])
            ).map(objects -> (IMethodInstance)objects[0]).collect(Collectors.toList());
        }
        return methodInstanceList;
   }

}
