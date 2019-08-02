package com.yangyh.video.download.service.impl;

import com.yangyh.video.download.service.VideoDownloadService;
import com.yangyh.video.download.util.excel.ExcelUtil;
import com.yangyh.video.download.util.http.HttpClientUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: yangyh
 * @create: 2019-08-01 13:40
 */
@Service
public class VideoDownloadServiceImpl implements VideoDownloadService {

    private static final Logger logger = LoggerFactory.getLogger(VideoDownloadServiceImpl.class);

    @Value("${prop.video.list.excel.path}")
    private String excelPath;

    @Value("${prop.video.list.save.path}")
    private String videoSavePath;

    private ExecutorService executorService = Executors.newFixedThreadPool(100);

    private int pageNum = 2000;

    private List<List<String>> dataList;

    private List<List<String>> init() {
        // 1.读取excel文件
        dataList = ExcelUtil.saxReadListStringV2007(excelPath);
        logger.info("读取excel文件成功，filePath[{}], 数据行数[{}]", excelPath, dataList.size());
        return dataList;
    }

    @Override
    public void execute() {

        init();

        // 开始计时
        StopWatch sw = new StopWatch();
        sw.start();
        // 线程开启停止控制
        int pageSize = (int)Math.ceil((double) dataList.size()/pageNum);
        CountDownLatch finsh = new CountDownLatch(pageSize);

        // 2.多线程下载
        videoDownload(dataList, pageSize, finsh);

        // 3.线程等待
//        try {
//            finsh.wait();
//            sw.stop();
//            logger.info("线程总耗时：[{}]毫秒", sw.getTime());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private void videoDownload(List<List<String>> dataList, int pageSize, CountDownLatch finsh) {
        int start = 0;
        int end = 0;
        int p = pageSize - 1;
        for (int i = 0; i < pageSize; i++) {
            end = (i + 1) * pageNum;
            start = i * pageNum;
            if (i == p) {
                end = dataList.size();
            }
            List list = dataList.subList(start, end);
            executorService.execute(() -> doSomeT(list, finsh));
        }
    }

    private void doSomeT(List<List<String>> list, CountDownLatch finsh) {

        for (List videoInfoList : list) {
            // 文件ID
            String fileId = (String) videoInfoList.get(0);
            fileId = fileId.replaceAll("\r|\n|\t", "");

            // 视频地址（原始）
            String videoUrl = (String) videoInfoList.get(9);
            // 文件名后缀
            int fileNameSuffixIndex = videoUrl.lastIndexOf('.');
            String fileNameSuffix = videoUrl.substring(fileNameSuffixIndex);

            String fileName = fileId + fileNameSuffix;
            HttpClientUtil.downLoadFromUrl(videoUrl, fileName, videoSavePath);
        }

    }


    @Override
    public void videoDownload() {

        init();

        // 开始计时
        StopWatch sw = new StopWatch();
        sw.start();

        int count = 0;

        for (List videoInfoList : dataList) {
            // 文件ID
            String fileId = (String) videoInfoList.get(0);
            fileId = fileId.replaceAll("\r|\n|\t", "");

            // 视频地址（原始）
            String videoUrl = (String) videoInfoList.get(9);
            // 文件名后缀
            int fileNameSuffixIndex = videoUrl.lastIndexOf('.');
            String fileNameSuffix = videoUrl.substring(fileNameSuffixIndex);

            String fileName = fileId + fileNameSuffix;
            HttpClientUtil.downLoadFromUrl(videoUrl, fileName, videoSavePath);

            logger.info("第" + ++count +"个文件下载成功，url[{}], dir[{}]", videoUrl, videoSavePath + fileName);
        }

        sw.stop();
        logger.info("下载文件数：[{}], 线程总耗时：[{}]毫秒", count, sw.getTime());
    }


}
