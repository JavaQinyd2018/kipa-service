package com.kipa.sftp;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.kipa.common.DataConstant;
import com.kipa.common.KipaProcessException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/7/5 11:12
 * @desciption
 * @since
 */
public final class CommandHelper {

    private CommandHelper() {}

    public static void executeCommand(String env, String command) {
        if (StringUtils.isBlank(command)) {
            return ;
        }
        ChannelHandler handler = new ChannelHandler(env, "exec");
        ChannelExec channelExec = (ChannelExec) handler.borrowChannel();
        try {
            channelExec.setCommand(command);
            channelExec.connect();
            InputStream inputStream = channelExec.getInputStream();
            List<String> strings = IOUtils.readLines(inputStream, "UTF-8");
            File file = new File(DataConstant.BASE_PATH + "log\\command.log");
            FileUtils.writeStringToFile(file, StringUtils.join(strings, "\n"),"UTF-8");
        } catch (JSchException | IOException e) {
            throw new KipaProcessException("命令执行并输入文件失败",e);
        } finally {
            handler.returnChannel(channelExec);
        }
    }
}
