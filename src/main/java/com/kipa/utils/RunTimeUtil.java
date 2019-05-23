package com.kipa.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Qinyadong
 * @date: 2019/4/23 17:31
 *
 */
public class RunTimeUtil {

    private RunTimeUtil() {

    }
    private static AtomicInteger index = new AtomicInteger();

    private static int getPid() {
        String info = getRunTimeInfo();
        int pid = (new Random()).nextInt();
        int index = info.indexOf("@");
        if(index > 0) {
            pid = Integer.parseInt(info.substring(0, index));
        }
        return pid;
    }

    private static String getRunTimeInfo() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        return runtime.getName();
    }
    public static String getRocketMqUniqeInstanceName() {
        return "pid" + getPid() + "_index" + index.incrementAndGet();
    }
}
