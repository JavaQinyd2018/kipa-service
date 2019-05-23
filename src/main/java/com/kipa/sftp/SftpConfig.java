package com.kipa.sftp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Qinyadong
 * @date: 2019/4/12 12:57
 * sftp的配置信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class SftpConfig {

    /**
     * sftp服务器host
     */
    private String host;
    /**
     * sftp服务器port
     */
    private Integer port;
    /**
     * sftp服务器用户名
     */
    private String username;
    /**
     * sftp服务器密码
     */
    private String password;

}
