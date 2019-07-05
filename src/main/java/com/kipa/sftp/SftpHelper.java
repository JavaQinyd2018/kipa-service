package com.kipa.sftp;

import com.google.common.collect.Lists;
import com.jcraft.jsch.*;
import com.kipa.common.KipaProcessException;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

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

    public static void batchUpload(String localDirectory, String remoteDirectory) {
        batchUpload(null, localDirectory, remoteDirectory);
    }

    public static void download(String remoteFilePath, String localFilePath) {
       download(null, remoteFilePath, localFilePath);
    }

    public static void batchDownload(String remoteDirectory, String localDirectory) {
        batchDownload(null, remoteDirectory, localDirectory);
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


    public static boolean remoteMkdir(String remoteDirectory) {
        return remoteMkdir(null, remoteDirectory);
    }

    public static boolean remoteRmdir(String remoteDirectory) {
        return remoteRmdir(null, remoteDirectory);
    }

    public static void upload(String env, String localFilePath, String directory, String uploadFileName) {
        PreCheckUtils.checkEmpty(localFilePath, "本地文件路径不能为空");
        PreCheckUtils.checkEmpty(directory, "文件上传的目录不能为空");
        PreCheckUtils.checkEmpty(uploadFileName, "上传的文件名称不能为空");
        checkDirIfLegal(directory);
        ChannelHandler handler = new ChannelHandler(env, "sftp");
        ChannelSftp channelSftp = handler.borrowChannelSftp();
        File file = new File(localFilePath);
        if (!file.exists()) {
            throw new KipaProcessException("路径为："+localFilePath+"本地文件不存在");
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
            }else {
                throw new KipaProcessException("创建或者切换目录失败",e);
            }
        }
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            channelSftp.put(inputStream, uploadFileName,ChannelSftp.OVERWRITE);
        } catch (Exception e) {
            throw new KipaProcessException("文件上传失败.", e);
        }finally {
            closeInputStream(inputStream);
            handler.returnChannel(channelSftp);
        }
    }

    public static void batchUpload(String env, String localDirectory, String remoteDirectory) {
        PreCheckUtils.checkEmpty(remoteDirectory, "参数远程目录不能为空");
        PreCheckUtils.checkEmpty(localDirectory, "参数本地目录不能为空");
        checkDirIfLegal(localDirectory, remoteDirectory);
        File localTargetDir = new File(localDirectory);
        if (!localTargetDir.exists()) {
            throw new KipaProcessException("本地目录"+localDirectory+"不存在");
        }else if (localTargetDir.isFile()) {
            throw new KipaProcessException(localTargetDir+"不是目录一个文件");
        }

        //recursive 为true的时候回吧文件夹下面的文件以及文件夹的所有文件都扫描到
        final Collection<File> files = FileUtils.listFiles(localTargetDir, null, true);
        if (CollectionUtils.isNotEmpty(files)) {
            files.forEach(file -> upload(env, file.getAbsolutePath(), remoteDirectory, file.getName()));
        }
    }

    public static void download(String env, String remoteFilePath, String localFilePath) {
        PreCheckUtils.checkEmpty(remoteFilePath,"目录参数不能为空");
        PreCheckUtils.checkEmpty(localFilePath, "本地下载的文件路径不能为空");
        ChannelHandler handler = new ChannelHandler(env, "sftp");
        ChannelSftp channelSftp = handler.borrowChannelSftp();
        File file = new File(localFilePath);
        String remoteFileName = remoteFilePath.substring(remoteFilePath.lastIndexOf('/') +1);
        long start = System.currentTimeMillis();
        if (file.isDirectory()) {
            log.warn("本地路径是个目录，文件将会下载到该目录下，并且和远程文件名一致");
            file = new File(localFilePath + remoteFileName);
        }
        long end = System.currentTimeMillis();
        log.info("判断时长：" + (end - start));
        String remoteDirectory = remoteFilePath.substring(0, remoteFilePath.lastIndexOf('/'));

        BufferedOutputStream outputStream = null;
        try {
            Vector vector = channelSftp.ls(remoteDirectory);
            if (vector == null) {
                throw new KipaProcessException("sftp服务器上面没有对应的文件路径："+remoteFilePath);
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            channelSftp.get(remoteFilePath, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            throw new KipaProcessException("文件下载失败.",e);
        }finally {
            closeOutputStream(outputStream);
            handler.returnChannel(channelSftp);
        }
    }

    public static void batchDownload(String env, String remoteDirectory, String localDirectory) {
        PreCheckUtils.checkEmpty(remoteDirectory, "参数远程目录不能为空");
        PreCheckUtils.checkEmpty(localDirectory, "参数本地目录不能为空");
        checkDirIfLegal(remoteDirectory, localDirectory);
        ChannelHandler handler = new ChannelHandler(env, "sftp");
        ChannelSftp channelSftp = handler.borrowChannelSftp();
        File localTargetDir = new File(localDirectory);
        if (!localTargetDir.exists()) {
            if (!localTargetDir.mkdirs()) {
                throw new KipaProcessException("目录"+localDirectory+"创建失败");
            }
        }else if (localTargetDir.isFile()) {
            throw new KipaProcessException(localTargetDir+"不是目录一个文件");
        }

        try {
            channelSftp.ls(remoteDirectory);
        } catch (SftpException e) {
            throw new KipaProcessException("sftp服务器上面的路径："+remoteDirectory+"不存在",e);
        }

        final List<String> fileNameList = listRemoteFile(env, remoteDirectory);
        try {
            if (CollectionUtils.isNotEmpty(fileNameList)) {
                fileNameList.forEach(fileName -> {
                    String remoteFilePath = remoteDirectory + fileName;
                    String localFilePath = localDirectory + fileName;
                    download(env, remoteFilePath, localFilePath);
                });
            }
        } catch (Exception e) {
            throw new KipaProcessException("文件批量下载异常",e);
        }finally {
            handler.returnChannel(channelSftp);
        }
    }


    public static void remoteDelete(String env, String remoteFilePath) {
        PreCheckUtils.checkEmpty(remoteFilePath, "本地下载的文件路径不能为空");
        ChannelHandler handler = new ChannelHandler(env, "sftp");
        ChannelSftp channelSftp = handler.borrowChannelSftp();
        String remoteDirectory = null;
        String remoteFileName = null;
        try {
            remoteDirectory = remoteFilePath.substring(0, remoteFilePath.lastIndexOf('/'));
            remoteFileName = remoteFilePath.substring(remoteFilePath.lastIndexOf('/')+1);
        } catch (Exception e) {
            throw new KipaProcessException("文件路径格式不正确");
        }
        log.info("上传sftp远程服务器的文件目录为：{}，文件名为：{}",remoteDirectory, remoteFileName);
        try {
            channelSftp.cd(remoteDirectory);
        } catch (SftpException e) {
            log.error("切换目录路径为：{}失败，错误信息为：{}",remoteFileName,e);
            throw new KipaProcessException("切换目录路径失败.",e);
        }

        try {
            channelSftp.rm(remoteFileName);
        } catch (SftpException e) {
            throw new KipaProcessException("远程删除文件失败.",e);
        }finally {
            handler.returnChannel(channelSftp);
        }
    }

    public static boolean isRemoteExist(String env, String remoteFilePath) {
        PreCheckUtils.checkEmpty(remoteFilePath, "本地下载的文件路径不能为空");
        ChannelHandler handler = new ChannelHandler(env, "sftp");
        ChannelSftp channelSftp = handler.borrowChannelSftp();
        String remoteDirectory = null;
        String remoteFileName = null;
        try {
            remoteDirectory = remoteFilePath.substring(0, remoteFilePath.lastIndexOf('/'));
            remoteFileName = remoteFilePath.substring(remoteFilePath.lastIndexOf('/')+1);
        } catch (Exception e) {
            throw new KipaProcessException("文件路径格式不正确",e);
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
        }finally {
            handler.returnChannel(channelSftp);
        }
        return false;
    }

    public static List<String> listRemoteFile(String env, String remoteDirectory) {
        List<String> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(remoteDirectory, "参数不能为空");
        checkDirIfLegal(remoteDirectory);
        ChannelHandler handler = new ChannelHandler(env, "sftp");
        ChannelSftp channelSftp = handler.borrowChannelSftp();
        try {
            Vector<ChannelSftp.LsEntry> ls = channelSftp.ls(remoteDirectory);
            for (ChannelSftp.LsEntry entry : ls) {
                if (StringUtils.equals(entry.getFilename(), ".") || StringUtils.equals(entry.getFilename(), "..")) {
                    continue;
                }
                list.add(entry.getFilename());
            }
        } catch (SftpException e) {
            throw new KipaProcessException("列表展开目录文件失败." ,e);
        }finally {
            handler.returnChannel(channelSftp);
        }
        return list;
    }

    public static boolean remoteMkdir(String env, String remoteDirectory) {
        PreCheckUtils.checkEmpty(remoteDirectory, "参数远程目录不能为空");
        checkDirIfLegal(remoteDirectory);
        ChannelHandler handler = new ChannelHandler(env, "sftp");
        ChannelSftp channelSftp = handler.borrowChannelSftp();
        try {
            channelSftp.cd(remoteDirectory);
        } catch (SftpException e) {
            if (ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id) {
                try {
                    channelSftp.mkdir(remoteDirectory);
                    return true;
                } catch (SftpException e1) {
                    log.error("创建目录失败，错误信息：{}",e1);
                   return false;
                }
            }
           return false;
        }finally {
            handler.returnChannel(channelSftp);
        }
        throw new KipaProcessException("目录'"+remoteDirectory+"'已经存在，不能重复创建");
    }

    public static boolean remoteRmdir(String env, String remoteDirectory) {
        PreCheckUtils.checkEmpty(remoteDirectory, "参数远程目录不能为空");
        checkDirIfLegal(remoteDirectory);
        ChannelHandler handler = new ChannelHandler(env, "sftp");
        ChannelSftp channelSftp = handler.borrowChannelSftp();
        try {
            channelSftp.cd(remoteDirectory);
        } catch (SftpException e) {
            if (ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id) {
                log.error("目录'"+remoteDirectory+"'不存在");
            }
            return false;
        }
        try {
            channelSftp.rmdir(remoteDirectory);
        } catch (SftpException e) {
            log.error("创建删除目录失败，错误信息为：{}",e);
            return false;
        }finally {
            handler.returnChannel(channelSftp);
        }
        return true;
    }

    private static void checkDirIfLegal(String... dirPaths) {
        for (String dirPath : dirPaths) {
            if (!StringUtils.endsWith(dirPath, "\\") && !StringUtils.endsWith(dirPath,"/")) {
                throw new IllegalArgumentException("目录'"+dirPath+"'不合法， 目录应该以'\\'或者'/'结束");
            }
        }
    }

    private static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new KipaProcessException("释放输入流失败",e);
            }
        }
    }

    private static void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new KipaProcessException("释放输入流失败",e);
            }
        }
    }

}
