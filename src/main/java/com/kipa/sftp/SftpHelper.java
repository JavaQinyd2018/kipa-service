package com.kipa.sftp;

import com.jcraft.jsch.*;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.collections.Lists;

import java.io.*;
import java.util.*;

/**
 * @author: Qinyadong
 * @date: 2019/4/12 18:53
 * sftp 工具类
 */
@Slf4j
public final class SftpHelper {

    private SftpHelper() {}

    public static void upload(String localFilePath, String directory, String uploadFileName) {
        upload(null, localFilePath, directory, uploadFileName);
    }

    public static void download(String remoteFilePath, String localFilePath) {
       download(null, remoteFilePath, localFilePath);
    }

    public static void remoteDelete(String remoteFilePath) {
       remoteDelete(null, remoteFilePath);
    }

    public static boolean isRemoteExist(String remoteFilePath) {
        return isRemoteExist(null, remoteFilePath);
    }


    public static List<String> listRemoteFile(String remoteDirectory) {
       return listRemoteFile(null, remoteDirectory);
    }

    public static void upload(String env, String localFilePath, String directory, String uploadFileName) {
        PreCheckUtils.checkEmpty(localFilePath, "本地文件路径不能为空");
        PreCheckUtils.checkEmpty(directory, "文件上传的目录不能为空");
        PreCheckUtils.checkEmpty(uploadFileName, "上传的文件名称不能为空");
        ChannelSftp channelSftp = SftpBaseHandler.getSftp(env);
        File file = new File(localFilePath);
        if (!file.exists()) {
            throw new RuntimeException("路径为："+localFilePath+"本地文件不存在");
        }
        try {
            channelSftp.cd(directory);
        } catch (SftpException e) {
            if (ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id) {
                try {
                    channelSftp.mkdir(directory);
                    channelSftp.cd(directory);
                } catch (SftpException e1) {
                    log.error("创建或者切换目录失败，错误信息为：{}",e1);
                }
                throw new RuntimeException("创建或者切换目录失败",e);
            }
        }
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            channelSftp.put(inputStream, uploadFileName,ChannelSftp.OVERWRITE);
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败.", e);
        }finally {
            SftpBaseHandler.closeInputStream(inputStream);
            SftpBaseHandler.closeSftp(channelSftp);
        }
    }

    public static void download(String env, String remoteFilePath, String localFilePath) {
        PreCheckUtils.checkEmpty(remoteFilePath,"目录参数不能为空");
        PreCheckUtils.checkEmpty(localFilePath, "本地下载的文件路径不能为空");
        ChannelSftp channelSftp = SftpBaseHandler.getSftp(env);
        File file = new File(localFilePath);
        if (file.isDirectory()) {
            log.warn("本地路径是个目录，文件将会下载到该目录下，并且和远程文件名一致");
        }
        String remoteDirectory = remoteFilePath.substring(0, remoteFilePath.lastIndexOf('/'));
        BufferedOutputStream outputStream = null;
        try {
            Vector vector = channelSftp.ls(remoteDirectory);
            if (vector == null) {
                throw new RuntimeException("sftp服务器上面没有对应的文件路径："+remoteFilePath);
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            channelSftp.get(remoteFilePath, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败.",e);
        }finally {
            SftpBaseHandler.closeOutputStream(outputStream);
            SftpBaseHandler.closeSftp(channelSftp);
        }
    }

    public static void remoteDelete(String env, String remoteFilePath) {
        PreCheckUtils.checkEmpty(remoteFilePath, "本地下载的文件路径不能为空");
        ChannelSftp channelSftp = SftpBaseHandler.getSftp(env);
        String remoteDirectory = null;
        String remoteFileName = null;
        try {
            remoteDirectory = remoteFilePath.substring(0, remoteFilePath.lastIndexOf('/'));
            remoteFileName = remoteFilePath.substring(remoteFilePath.lastIndexOf('/')+1);
        } catch (Exception e) {
            throw new RuntimeException("文件路径格式不正确");
        }
        log.info("上传sftp远程服务器的文件目录为：{}，文件名为：{}",remoteDirectory, remoteFileName);
        try {
            channelSftp.cd(remoteDirectory);
        } catch (SftpException e) {
            log.error("切换目录路径为：{}失败，错误信息为：{}",remoteFileName,e);
            throw new RuntimeException("切换目录路径失败.",e);
        }

        try {
            channelSftp.rm(remoteFileName);
        } catch (SftpException e) {
            log.error("远程删除文件名：{}失败，失败信息为：{}",remoteFileName, e);
            throw new RuntimeException("远程删除文件失败.",e);
        }
    }

    public static boolean isRemoteExist(String env, String remoteFilePath) {
        PreCheckUtils.checkEmpty(remoteFilePath, "本地下载的文件路径不能为空");
        ChannelSftp channelSftp = SftpBaseHandler.getSftp(env);
        String remoteDirectory = null;
        String remoteFileName = null;
        try {
            remoteDirectory = remoteFilePath.substring(0, remoteFilePath.lastIndexOf('/'));
            remoteFileName = remoteFilePath.substring(remoteFilePath.lastIndexOf('/')+1);
        } catch (Exception e) {
            throw new RuntimeException("文件路径格式不正确",e);
        }
        try {
            Vector<ChannelSftp.LsEntry> vector = channelSftp.ls(remoteDirectory);
            if (vector == null) {
                return false;
            }else {
                for (ChannelSftp.LsEntry entry : vector) {
                    if (StringUtils.equalsIgnoreCase(entry.getFilename(), remoteFileName)) {
                        return true;
                    }
                }
            }
        } catch (SftpException e) {
            log.error("列表展开目录文件：{}失败，失败信息为：{}",remoteFilePath, e);
            return false;
        }
        return false;
    }

    public static List<String> listRemoteFile(String env, String remoteDirectory) {
        List<String> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(remoteDirectory, "参数不能为空");
        ChannelSftp sftp = SftpBaseHandler.getSftp(env);
        try {
            Vector<ChannelSftp.LsEntry> ls = sftp.ls(remoteDirectory);
            for (ChannelSftp.LsEntry entry : ls) {
                list.add(entry.getFilename());
            }
        } catch (SftpException e) {
            log.error("列表展开目录文件：{}失败，失败信息为：{}",remoteDirectory, e);
            throw new RuntimeException("列表展开目录文件失败." ,e);
        }
        return list;
    }

}
