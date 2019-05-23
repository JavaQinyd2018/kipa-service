package com.kipa.http.emuns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: Qinyadong
 * @date: 2019/2/14 13:30
 */
@Getter
@AllArgsConstructor
public enum SSLProtocolVersion {

    /**
     * 安全证书版本
     */
    SSL("SSL"),

    SSLv3("SSLv3"),

    TLSv1("TLSv1"),

    TLSv1_1("TLSv1_1"),

    TLSv1_2("TLSv1_1");

    private String name;

    public static SSLProtocolVersion getByName(String name) {
        for (SSLProtocolVersion sslProtocolVersion : SSLProtocolVersion.values()) {
            if (StringUtils.equalsIgnoreCase(sslProtocolVersion.getName(), name)) {
                return sslProtocolVersion;
            }
        }
        throw new RuntimeException("未支持当前SSL的版本号" +name);
    }
}
