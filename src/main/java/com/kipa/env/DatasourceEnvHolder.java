package com.kipa.env;

import com.kipa.mybatis.service.condition.EnvFlag;

/**
 * @author: Qinyadong
 * @date: 2019/6/26 22:57
 */
public class DatasourceEnvHolder {
    private DatasourceEnvHolder() {}

    private static ThreadLocal<EnvFlag[]> envHolder = new ThreadLocal<>();

    public static EnvFlag[] getEnv() {
        return envHolder.get();
    }

    public static void setFlag(EnvFlag[] env) {
        envHolder.set(env);
    }

    public static void removeFlag() {
        envHolder.remove();
    }
}
