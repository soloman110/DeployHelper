package com.zinnaworks.deploy.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
 
public class ExecuteShellUtil {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteShellUtil.class);
 
    private static final String DONOT_INIT_ERROR_MSG = "please invoke init(...) first!";
 
    private Session session;
 
    private Channel channel;
 
    private ChannelExec channelExec;
    
    private static final int CONNECT_TIMEOUT = 60 * 1000;
 
    private ExecuteShellUtil() {
    }
 
    /**
     * @return 实例
     * @date 2019/4/29 16:58
     */
    public static ExecuteShellUtil getInstance() {
        return new ExecuteShellUtil();
    }
 
    /**
     * @param ip 원격 서버 주소 
     * @param port 원격 서버 포트 
     * @param username 계정
     * @param password 비밀번호
     * @throws JSchException
     */
    public void init(String ip, Integer port, String username, String password) throws JSchException {
        session = getSession(ip, port,username, password);
        LOGGER.info("Session connected!");
        channel = session.openChannel("exec");
        channelExec = (ChannelExec) channel;
    }
    //Session 생성 
    public Session getSession(String ip, Integer port, String username, String password) throws JSchException {
    	 JSch jsch = new JSch();
         jsch.getSession(username, ip, port);
         session = jsch.getSession(username, ip, port);
         session.setPassword(password);
         Properties sshConfig = new Properties();
         sshConfig.put("StrictHostKeyChecking", "no");
         sshConfig.put("PreferredAuthentications", "password");
         session.setConfig(sshConfig);
         session.connect(CONNECT_TIMEOUT);
         return session;
    }
    /**
     *	Session 및 Channel를 전부 닫아주야된다. 안닫아면 어떻게 돼?
     */
    public void close() {
        if (channelExec != null && channelExec.isConnected()) {
            channelExec.disconnect();
        }
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
    
    public static boolean sftp(String ip, String userName, String ps, String remoteBaseDir, String fileName, String localFilePath) {
    	
    	SftpConfig config = SftpConfig.builder().ip(ip).username(userName).pwd(ps).build();
    	SftpChannel sftpChannel = new SftpChannel();
        ChannelSftp sftp = null;
        try {
            if (StringUtils.isNotBlank(config.getPrivateKeyPath())) {
                sftp = sftpChannel.connectByIdentity(config);
            } else {
                sftp = sftpChannel.connectByPwd(config);
            }
            if (sftp.isConnected()) {
            	LOGGER.info("서버 연결성공");
            } else {
            	LOGGER.error("서버 연결 실패");
                return false;
            }
            if(!isExist(sftp, remoteBaseDir)){
            	LOGGER.error("Make Remote File Path Fail:" + remoteBaseDir);
                return false;
            }
            String dst = remoteBaseDir + "/" + fileName;
            String src = localFilePath + "/" + fileName;
            LOGGER.info("Upload Start----> ，local File Path：["+src +"] Remote FilePath：["+dst+"]");
            sftp.put(src, dst);
            LOGGER.error("Success");
            return true;
        } catch (Exception e) {
        	LOGGER.error("Upload Fail" + e);
            return false;
        } finally {
        	sftp.disconnect();
            sftpChannel.closeChannel();
        }
    }
    
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
    public List<String> remoteExecute(String command) throws JSchException {
    	LOGGER.debug(">> {}", command);
        List<String> resultLines = new ArrayList<>();
        ChannelExec channel = null;
        try{
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            InputStream input = channel.getInputStream();
            channel.connect(CONNECT_TIMEOUT);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
                String inputLine = null;
                while((inputLine = inputReader.readLine()) != null) {
                	LOGGER.debug("   {}", inputLine);
                    resultLines.add(inputLine);
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                    	LOGGER.error("JSch inputStream close error:", e);
                    }
                }
            }
        } catch (IOException e) {
        	LOGGER.error("IOcxecption:", e);
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                	LOGGER.error("JSch channel disconnect error:", e);
                }
            }
        }
        return resultLines;
    }
}
