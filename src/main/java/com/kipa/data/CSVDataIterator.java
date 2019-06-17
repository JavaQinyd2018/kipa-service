package com.kipa.data;

import com.google.common.collect.Lists;
import com.kipa.check.DataConstant;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/7
 * 数据迭代器
 */
@Slf4j
public class CSVDataIterator implements Iterator<Object[]> {
    private CSVReader csvReader;
    private List<Object[]> csvDataList = Lists.newArrayList();
    //值是从2行开始的，也就是索引是1
    private int currentRowNum = 0;
    private int totalRow;

    public CSVDataIterator(Method method, String csvFilePath) {
        Parameter[] parameters = method.getParameters();
        if (StringUtils.isBlank(csvFilePath)) {
            throw new IllegalArgumentException("数据驱动的csv文件路径不能为空");
        }
        File file = new File(csvFilePath);
        if (!file.isFile() || !file.exists()) {
            throw new IllegalArgumentException("数据驱动的csv文件不存在");
        }
        try {
            csvReader = new CSVReader(new FileReader(file));
            final List<String[]> strings = csvReader.readAll();
            if (CollectionUtils.isEmpty(strings) || strings.get(0).length == 0) {
                throw new RuntimeException("数据驱动csv文件数据内容为空");
            }

            if (strings.size() < 2) {
                throw new RuntimeException("数据驱动csv文件数据参数对应的值不能为空，文件中至少存在一行表头和一行值");
            }

            if (strings.get(0).length != parameters.length) {
                throw new RuntimeException(String.format("数据驱动csv文件的内容参数个数：%s与测试方法参数个数:%s个数不一致",
                        strings.get(0).length,parameters.length));
            }
            for (int i = 1; i < strings.size(); i++) {
                String[] row = strings.get(i);
                if (ArrayUtils.isEmpty(row)) {
                    throw new RuntimeException("csv文件中不允许存在空行");
                }
                csvDataList.add(convertString2Object(row, parameters));
            }
            totalRow = strings.size()-1;
        } catch (Exception e) {
            throw new RuntimeException("数据驱动csv文件数据解析失败",e);
        }
    }

    @Override
    public boolean hasNext() {
        if (currentRowNum < 0 || currentRowNum >= totalRow) {
            try {
                csvReader.close();
            } catch (IOException e) {
                log.error("csvReader关闭资源失败，错误信息：{}",e);
            }
            return false;
        }else {
            return true;
        }

    }

    @Override
    public Object[] next() {
        Object[] row = csvDataList.get(currentRowNum);
        currentRowNum ++;
        return row;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("迭代过程中不支持移除操作");
    }

    @Override
    public void forEachRemaining(Consumer<? super Object[]> action) {
    }

    private Object[] convertString2Object(String[] valueArray, Parameter[] parameterArray) {
        Object[] objects = new Object[valueArray.length];
        for (int i = 0; i < parameterArray.length; i++) {
            Class<?> type = parameterArray[i].getType();
            String value = valueArray[i];
            if (StringUtils.isBlank(value)) {
                objects[i] = null;
            }else {
                if (type.isPrimitive()) {
                    if (type.isAssignableFrom(int.class)) {
                        objects[i] = Integer.parseInt(value);
                    }else if (type.isAssignableFrom(boolean.class)) {
                        objects[i] = Boolean.parseBoolean(value);
                    }else if (type.isAssignableFrom(byte.class)) {
                        objects[i] = Byte.parseByte(value);
                    }else if (type.isAssignableFrom(char.class)) {
                        objects[i] = value.toCharArray()[0];
                    }else if (type.isAssignableFrom(short.class)) {
                        objects[i] = Short.parseShort(value);
                    }else if (type.isAssignableFrom(long.class)) {
                        objects[i] = Long.parseLong(value);
                    }else if (type.isAssignableFrom(float.class)) {
                        objects[i] = Float.parseFloat(value);
                    }else if (type.isAssignableFrom(double.class)) {
                        objects[i] = Double.parseDouble(value);
                    }

                }else if (DataConstant.CLASS_NAME_LIST.contains(type.getName())) {
                    if (type.isAssignableFrom(Integer.class)) {
                        objects[i] = Integer.valueOf(value);
                    }else if (type.isAssignableFrom(Boolean.class)) {
                        objects[i] = Boolean.valueOf(value);
                    }else if (type.isAssignableFrom(Byte.class)) {
                        objects[i] = Byte.valueOf(value);
                    }else if (type.isAssignableFrom(Character.class)) {
                        objects[i] = value.toCharArray()[0];
                    }else if (type.isAssignableFrom(Short.class)) {
                        objects[i] = Short.valueOf(value);
                    }else if (type.isAssignableFrom(Long.class)) {
                        objects[i] = Long.valueOf(value);
                    }else if (type.isAssignableFrom(Float.class)) {
                        objects[i] = Float.valueOf(value);
                    }else if (type.isAssignableFrom(Double.class)) {
                        objects[i] = Double.valueOf(value);
                    }else if (type.isAssignableFrom(Enum.class)) {
                        objects[i] = value;
                    }else if (type.isAssignableFrom(Date.class)) {
                        try {
                            Date date = null;
                            if (value.contains("-")) {
                                date = DateUtils.parseDate(value, "yyyy-MM-dd");
                                if (value.contains(":")) {
                                    date = DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss");
                                }
                                objects[i] = date;
                            }else {
                                throw new IllegalArgumentException("日期格式必须符合'yyyy-MM-dd HH:mm:ss'");
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }else if (type.isAssignableFrom(String.class)) {
                        objects[i] = value;
                    }else if (type.isAssignableFrom(BigDecimal.class)) {
                        objects[i] = new BigDecimal(value);
                    }else if (type.isAssignableFrom(java.sql.Date.class)) {

                        try {
                            Date date = null;
                            if (value.contains("-")) {
                                date = DateUtils.parseDate(value, "yyyy-MM-dd");
                                if (value.contains(":")) {
                                    date = DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss");
                                }
                                objects[i] = new java.sql.Date(date.getTime());
                            }else {
                                throw new IllegalArgumentException("日期格式必须符合'yyyy-MM-dd HH:mm:ss'");
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException("日期转化失败", e);
                        }
                    }
                }else {
                    throw new UnsupportedOperationException("不支持复合对象的处理");
                }
            }
            }
        return objects;
    }
}
