package com.yangyh.video.download.runner;

import com.yangyh.video.download.service.VideoDownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Spring Boot应用程序在启动后，会遍历CommandLineRunner接口的实例并运行它们的run方法。也可以利用
 * @Order注解（或者实现Order接口）来规定所有CommandLineRunner实例的运行顺序。
 *
 * @description: 服务启动执行
 * @author: yangyh
 * @create: 2019-02-27 15:53
 **/
@Component
@Order(value = 1)
public class MyStartupRunner1 implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyStartupRunner1.class);

    @Autowired
    private VideoDownloadService videoDownloadService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("服务启动执行，执行视频文件下载操作");
//        videoDownloadService.execute();
        videoDownloadService.videoDownload();
    }
}
