package com.zinnaworks.deploy.util;


import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SftpChannel {
    Session session = null;
    Channel channel = null;

    public static final int SFTP_DEFAULT_PORT = 22;

    public ChannelSftp connectByIdentity(SftpConfig sftpConfig) throws JSchException {
        JSch jsch = new JSch();
        int port = SFTP_DEFAULT_PORT;
        if (StringUtils.isNotBlank(sftpConfig.getPrivateKeyPath())) {
            if (StringUtils.isNotBlank(sftpConfig.getPassphrase())) {
                jsch.addIdentity(sftpConfig.getPrivateKeyPath(), sftpConfig.getPassphrase());
            } else {
                jsch.addIdentity(sftpConfig.getPrivateKeyPath());
            }
        }
        if (sftpConfig.getPort() != null) {
            port = Integer.valueOf(sftpConfig.getPort());
        }
        if (port > 0) {
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getIp(), port);
        } else {
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getIp());
        }
        if (session == null) {
            throw new JSchException("session is Empty, connect fail");
        }
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.setTimeout(30000);
        session.connect();

        channel = (Channel) session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    public ChannelSftp connectByPwd(SftpConfig sftpConfig) throws JSchException {
        JSch jsch = new JSch();
        int port = SFTP_DEFAULT_PORT;
        if (sftpConfig.getPort() != null) {
            port = Integer.valueOf(sftpConfig.getPort());
        }
        if (port > 0) {
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getIp(), port);
        } else {
            //connect by default port
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getIp());
        }
        if (session == null) {
            throw new JSchException("session is Empty, connect fail");
        }
        session.setPassword(sftpConfig.getPwd());
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.setTimeout(30000);
        session.connect();
        //sftp channel생성 
        channel = (Channel) session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    public void closeChannel() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

}
