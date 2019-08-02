package com.yangyh.video.download.util.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @description: excel读写工具类 采用阿里开源框架easyexcel
 * @author: yangyh
 * @create: 2019-03-05 17:39
 **/
public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);


    /**
     * 无表头写excel
     **/
    public static void writeWithoutHead(List<List<String>> data, Sheet sheet, String sheetName, String filePath) {

        if (sheet == null) {
            sheet = new Sheet(1, 0);
        }
        if (StringUtils.isBlank(sheetName)) {
            sheetName = "sheet1";
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, false);
            sheet.setSheetName(sheetName);
            writer.write0(data, sheet);
            writer.finish();
        } catch (FileNotFoundException e) {
            logger.error("文件写入失败", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("资源关闭失败", e);
                }
            }
        }
    }

    /**
     * 07版本excel读数据量大于1千行，内部采用回调方法.
     */
    public static List<List<String>> saxReadListStringV2007(String filePath) {

        File file = new File(filePath);
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            ExcelListener excelListener = new ExcelListener();
            EasyExcelFactory.readBySax(inputStream, new Sheet(1, 1), excelListener);
            List<List<String>> dataList =  excelListener.getData();
            return dataList;

        } catch (FileNotFoundException e) {
            logger.error("无效路径，文件不存在...", e);
            return null;
        } finally {
            if (null ==inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("资源关闭失败...", e);
                    return null;
                }
            }
        }

    }


}
