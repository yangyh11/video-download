package com.yangyh.video.download.util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description:
 * @author: yangyh
 * @create: 2019-08-01 15:06
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) {
        URL url = null;
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为10秒
            conn.setConnectTimeout(10 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //得到输入流
            inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            File file = new File(saveDir + File.separator + fileName);
            fos = new FileOutputStream(file);
            fos.write(getData);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (true) {
            try {
                if (!((len = inputStream.read(buffer)) != -1)) {
                    break;
                }
                bos.write(buffer, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return bos.toByteArray();
    }
}
