package com.kipa.sftp;

import com.jcraft.jsch.ChannelSftp;
import lombok.Data;

import java.util.Date;

/**
 * @author: Qinyadong
 * @date: 2019/4/12 18:57
 * sftp连接
 */
@Data
class SftpConnection {

    private ChannelSftp channelSftp;
    private String index;
    private Date createDate;
}
