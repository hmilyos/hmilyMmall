package com.hmily.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Getter
@Setter
public class FTPUtil {

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    public FTPUtil(String ip, int port, String user, String pwd){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    //批量上传文件
    public static boolean uploadFile(List<File> fileList) throws IOException {
        /**
         * 上传到ftp服务器的思路：
         *    1.从resources文件夹下的 mmall.properties文件里获取ftp服务器的ip，端口号、用户名、密码
         *    2.用户名和密码，开始连接和登录ftp服务器
         *    3.连接成功后设置FTPClient的编码格式、缓冲区是不是需要切换文件夹、把文件类型设置为二进制的文件、打开本地的被动模式
         *    4.获得文件流
         *    5.把文件名和文件流存储起来
         *    6.关闭流，释放连接，返回上传结果
         */
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        log.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        log.info("开始连接ftp服务器,结束上传,上传结果:{}", result);
        return result;
    }


    //如果需要上传到ftp文件夹的下一级文件夹就要用到 remotePath
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true; //是否成功
        FileInputStream fis = null;
        //连接FTP服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            //连接ftp服务器成功
            try {
                ftpClient.changeWorkingDirectory(remotePath);   //是不是需要切换文件夹，remotePath为空也不会报错
                ftpClient.setBufferSize(1024);      //设置缓冲区
                ftpClient.setControlEncoding("UTF-8");      //编码格式
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  //把文件类型设置为二进制的文件
                ftpClient.enterLocalPassiveMode();  //打开本地的被动模式
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);    //获得文件流
                    ftpClient.storeFile(fileItem.getName(), fis);   //把文件名和文件流存储起来
                }

            } catch (IOException e) {
                log.error("上传文件异常", e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                fis.close();    //关闭流
                ftpClient.disconnect();     //释放连接
            }
        }
        return uploaded;
    }

    //连接ftp服务器
    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);  //连接
            isSuccess = ftpClient.login(user, pwd); //登录
        } catch (IOException e) {
            log.error("连接FTP服务器异常", e);
        }
        return isSuccess;   //连接成功Or失败
    }

}
