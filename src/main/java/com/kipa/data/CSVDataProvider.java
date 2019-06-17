package com.kipa.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * @author: Qinyadong
 * @date: 2019/4/11 19:58
 * 数据驱动提供类
 */
@Slf4j
public class CSVDataProvider {

    private static String baseDir = String.format("%s\\%s",System.getProperty("user.dir"),"src\\main\\resources");

    public Iterator<Object[]> providerData(Method method) {
        String csvFilePath = "";
        DataParam annotation = method.getAnnotation(DataParam.class);
        if (annotation != null) {
            String paramName = annotation.paramName();
            String paramValue = annotation.paramValue();
            if (StringUtils.isBlank(paramValue)) {
                throw new RuntimeException("注解@DataParam参数值为空");
            }

            if (StringUtils.isBlank(paramName)) {
                paramName = "dataFile";
                log.debug("使用@DataParam的数据驱动默认的参数名为：{}",paramName);
            }

            csvFilePath = String.format("%s\\%s",baseDir,paramValue.replace("/","\\"));
        } else {
            Class<?> clazz = method.getDeclaringClass();
            String csvDir = String.format("%s\\%s\\%s\\",baseDir, "data\\service",method.getName());
            String csvFileName = String.format("%s.%s.csv",clazz.getSimpleName(), method.getName());
            csvFilePath = csvDir+csvFileName;
            log.debug("csv数据文件默认的目录是：{}，默认的文件名称为：{}",csvDir, csvFileName);
        }
        return new CSVDataIterator(method,csvFilePath);
    }
}
