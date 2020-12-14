package com.zinnaworks.deploy.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import net.sf.json.JSONObject;

public class SftpUtil {

    private static Logger logger = LoggerFactory.getLogger(SftpUtil.class);
    
    /**
     * 获取Sftp对象
     * @param param
     * @return
     */
    public  static SftpConfig getSftpObj(String param) {
        SftpConfig sftpConfig = null;
        if (StringUtils.isNotBlank(param)) {
            JSONObject jsonObj = JSONObject.fromObject(param);
            sftpConfig = (SftpConfig) JSONObject.toBean(jsonObj, SftpConfig.class);
        }
        return sftpConfig;
    }

    /** sftp 上传*/
    public static boolean upload(SftpConfig config, String remoteBaseDir, String fileName, String localFilePath) {
        logger.info("路径：baseDir="+remoteBaseDir);
        SftpChannel sftpChannel = new SftpChannel();
        ChannelSftp sftp = null;
        try {
            if (StringUtils.isNotBlank(config.getPrivateKeyPath())) {
                sftp = sftpChannel.connectByIdentity(config);
            } else {
                sftp = sftpChannel.connectByPwd(config);
            }
            if (sftp.isConnected()) {
                logger.info("连接服务器成功");
            } else {
                logger.error("连接服务器失败");
                return false;
            }
            //检查路径
            if(!isExist(sftp, remoteBaseDir)){
                logger.error("创建sftp服务器路径失败:" + remoteBaseDir);
                return false;
            }
            String dst = remoteBaseDir + "/" + fileName;
            String src = localFilePath + "/" + fileName;
            logger.info("开始上传，本地服务器路径：["+src +"]目标服务器路径：["+dst+"]");
            sftp.put(src, dst);
            logger.info("上传成功");
            return true;
        } catch (Exception e) {
            logger.error("上传失败", e);
            return false;
        } finally {
            sftpChannel.closeChannel();
        }
    }

    /**sftp 下载 */
    public static boolean down(SftpConfig config, String baseDir, String fileName1, String filePath, String fileName2 ) {
        SftpChannel sftpChannel = new SftpChannel();
        ChannelSftp sftp = null;
        try {
            if (StringUtils.isNotBlank(config.getPrivateKeyPath())) {
                sftp = sftpChannel.connectByIdentity(config);
            } else {
                sftp = sftpChannel.connectByPwd(config);
            }
            if (sftp.isConnected()) {
                logger.info("连接服务器成功");
            } else {
                logger.error("连接服务器失败");
                return false;
            }
            String dst = "";
            if (StringUtils.isBlank(fileName2)) {
                dst = filePath + fileName1;
            } else{
                dst = filePath + fileName2;
            }
            String src = baseDir+ "/" + fileName1;
            logger.info("开始下载，sftp服务器路径：["+src +"]目标服务器路径：["+dst+"]");
            sftp.get(src, dst);
            logger.info("下载成功");
            return true;
        } catch (Exception e) {
            logger.error("下载失败", e);
            return false;
        } finally {
            sftpChannel.closeChannel();
        }
    }
    
    /**
     * 判断文件夹是否存在
     * true 目录创建成功，false 目录创建失败
      * @param sftp
     * @param filePath 文件夹路径
     * @return
     */
    public static boolean isExist(ChannelSftp sftp, String filePath) {
        String paths[] = filePath.split("\\/");
        String dir = paths[0];
        for (int i = 0; i < paths.length - 1; i++) {
            dir = dir + "/" + paths[i + 1];
            try{
                sftp.cd(dir);
            }catch(SftpException sException){
                if(sftp.SSH_FX_NO_SUCH_FILE == sException.id){
                    try {
                        sftp.mkdir(dir);
                    } catch (SftpException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
