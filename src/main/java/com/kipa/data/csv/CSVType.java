package com.kipa.data.csv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: Qinyadong
 * @date: 2019/5/17 17:53
 * csv文件类型
 */
@Getter
@AllArgsConstructor
public enum CSVType {

    /**
     * 水平行模式的csv文件
     */
    TRANSVERSE("T"),
    /**
     * 竖直列模式的csv文件
     */
    VERTICAL("V"),
    /**
     * 竖直列模式的数据库校验csv文件
     */
    CHECK_DB("C");

    private String flag;


    public static CSVType getByFlag(String flag) {
        for (CSVType value : CSVType.values()) {
            if (StringUtils.equalsIgnoreCase(value.getFlag(),flag)) {
                return value;
            }
        }
        throw new IllegalArgumentException("类型不存在");
    }

}
