package com.kipa.data.csv;

import com.kipa.common.DataConstant;
import com.kipa.common.KipaProcessException;
import com.kipa.utils.PreCheckUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;

/**
 * @author: Qinyadong
 * @date: 2019/5/17 18:10
 * 处理器
 */
@Slf4j
final class AroundHandler {

    private AroundHandler() {}

    static void write(List<String[]> list, String csvFilePath) {
        CSVWriter csvWriter = null;
        try {
            File file = AroundHandler.checkAndCreateFile(csvFilePath);
            csvWriter = new CSVWriter(new FileWriter(file));
            csvWriter.writeAll(list);
        } catch (IOException e) {
            throw new KipaProcessException("数据写入csv文件失败", e);
        }finally {
            closeCsvWriter(csvWriter);
        }

    }

    static List<String[]> read(String csvFilePath) {
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(csvFilePath));
            return csvReader.readAll();
        } catch (Exception e) {
            throw new KipaProcessException("从csv文件读取信息失败", e);
        }finally {
            closeCsvReader(csvReader);
        }
    }

    static CSVReader getReader(String csvFilePath) {
        CSVReader csvReader = null;
        try {
            return new CSVReader(new FileReader(csvFilePath));
        } catch (Exception e) {
            throw new KipaProcessException("从csv文件读取信息失败",e);
        }finally {
            closeCsvReader(csvReader);
        }
    }

    static void closeCsvWriter(CSVWriter csvWriter) {
        if (csvWriter != null) {
            try {
                csvWriter.close();
            } catch (IOException e) {
                log.error("csvWriter释放失败，错误信息：{}",e);
            }
        }
    }

    static void closeCsvReader(CSVReader csvReader) {
        if (csvReader != null) {
            try {
                csvReader.close();
            } catch (IOException e) {
                log.error("csvReader释放失败，错误信息：{}",e);
            }
        }
    }

    private static File checkAndCreateFile(String relativePath) throws IOException {
        String fullPath = DataConstant.BASE_PATH + StringUtils.replace(relativePath, "/",File.separator);
        File fileDir = new File(fullPath.substring(0, fullPath.lastIndexOf("/")));
        String csvFileName = fullPath.substring(fullPath.lastIndexOf("/") + 1);
        log.debug("csv文件的路径为：{}",fullPath);
        if (!fileDir.exists() || !fileDir.isDirectory()) {
            if (!fileDir.mkdirs()) {
                throw new KipaProcessException("目录"+fileDir+"创建失败");
            }
        }
        File file = new File(fullPath);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new KipaProcessException("文件"+csvFileName+"创建失败");
            }
        }else if (file.length() != 0) {
            throw new KipaProcessException("文件"+csvFileName+"中已经有内容存在了,请确保文件是个空文件");
        }
        return file;
    }

    static String getCsvFileAbsolutePath(String csvFilePath) {
        PreCheckUtils.checkEmpty(csvFilePath, "csv文件的路径不能为空");
        if (StringUtils.containsIgnoreCase(System.getProperty("os.name"), "windows")) {
            csvFilePath = String.format("%s%s", DataConstant.BASE_PATH, csvFilePath.replace("/","\\"));
        }else {
            csvFilePath =  DataConstant.BASE_PATH + csvFilePath;
        }
        log.debug("解析的csv文件的路径为：{}",csvFilePath);
        return csvFilePath;
    }
}
