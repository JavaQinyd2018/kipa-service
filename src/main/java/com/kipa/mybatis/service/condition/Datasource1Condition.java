package com.kipa.mybatis.service.condition;

import java.util.List;

/**
 * @author: Qinyadong
 * @date: 2019/6/25 17:57
 *  数据源开启condition
 */
public class Datasource1Condition extends AbstractEnableDatabaseCondition{

    @Override
    public boolean matchDatasource(List<EnvFlag> env) {
        return env.contains(EnvFlag.ENV1);
    }
}
