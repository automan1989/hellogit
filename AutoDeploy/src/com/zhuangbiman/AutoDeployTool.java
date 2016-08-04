/**
 * 
 */
package com.zhuangbiman;

/**
 * @author Administrator
 *
 */
package com.automan;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoDeployTool
{
  private static Logger logger = LoggerFactory.getLogger(AutoDeployTool.class);
  private static final String DIRECTORY = "/home/zxin10/was/tomcat/webapps/uniportal";
  private static final int PORT = 22;
  private static final String USER = "zxin10";
  private static final String PASSWORD = "zxin10";
  private static final String HOST = "127.0.0.1";
  
  public static void main(String[] args)
  {
    PropertyConfigurator.configure("./conf/log4j.properties");
    logger.info("Hello {}", "SFTP");
    
    String host = HOST;
    String username = USER;
    String password = PASSWORD;
    int port = PORT;
    String uploadFile = "anyservice-iecs.jar";
    String directory = DIRECTORY;

    if((args == null) || (args.length != 6)){
        logger.info("params invalid, please check again: host:{}, username:{}, password:{},port:{},uploadFile:{},directory:{}", new Object[] { args[0].trim(), args[1].trim(), 
                args[2].trim(), Integer.valueOf(args[3]), args[4].trim(), args[5].trim() });
    }
    else
    {
       host = args[0].trim();
       username = args[1].trim();
       password = args[2].trim();
       port = Integer.parseInt(args[3].trim());
       uploadFile = args[4].trim();
       directory = args[5].trim();
      logger.info("Params: host:{}, username:{}, password:{},port:{},uploadFile:{},directory:{}", new Object[] { host, username, 
        password, Integer.valueOf(port), uploadFile, directory });
    }

    logger.info("@@@@@@@@@@@ Begin to backup file");

    String cmd = "cd " + directory + " \n mv " + uploadFile + "    " + uploadFile + "_bak  \n ";
    try
    {
      SSHUtil.getCommandResults(host, username, password, cmd);
    }
    catch (IOException e)
    {
      logger.error("SSHUtil getCommandResults Error ", e);
    }

    logger.info("@@@@@@@@@@@ Backup file finished!");

    AutoDeployTool sf = new AutoDeployTool();
    ChannelSftp sftp = sf.connect(host, port, username, password);
    logger.info("======= Begin to uploadFile");
    sf.upload(directory, uploadFile, sftp);

    logger.info("======= Upload file finished!");

    logger.info("******* Begin to Unzip file");

    String cmd2 = " cd " + directory + "  \n " + " unzip  " + uploadFile + "  << EOF\n" + "A\n EOF \n";
    try
    {
      SSHUtil.getCommandResults(host, username, password, cmd2);
    }
    catch (IOException e)
    {
      logger.error("SSHUtil getCommandResults Error ", e);
    }

    logger.info("******* Unzip file finished!");

    logger.info("$$$$$$  Begin to Restart Tomcat");
    String cmd3 = "jtool -mrestart \n ";
    try
    {
      SSHUtil.getCommandResults(host, username, password, cmd3);
    }
    catch (IOException e)
    {
      logger.error("SSHUtil getCommandResults Error ", e);
    }

    logger.info("$$$$$$  Restart Tomcat finished!");
  }

  public ChannelSftp connect(String host, int port, String username, String password)
  {
    ChannelSftp sftp = null;
    try
    {
      JSch jsch = new JSch();
      jsch.getSession(username, host, port);
      Session sshSession = jsch.getSession(username, host, port);
      logger.info("Session created.");
      sshSession.setPassword(password);
      Properties sshConfig = new Properties();
      sshConfig.put("StrictHostKeyChecking", "no");
      sshSession.setConfig(sshConfig);
      sshSession.connect();
      logger.info("Session connected.");
      logger.info("Opening Channel.");
      Channel channel = sshSession.openChannel("sftp");
      channel.connect();
      sftp = (ChannelSftp)channel;
      logger.info("Connected to {}", host);
    }
    catch (Exception e)
    {
      logger.error("", e);
    }
    return sftp;
  }

  public void upload(String directory, String uploadFile, ChannelSftp sftp)
  {
    try
    {
      sftp.cd(directory);
      File file = new File(uploadFile);
      sftp.put(new FileInputStream(file), file.getName());
    }
    catch (Exception e)
    {
      logger.error("", e);
    }
  }

  public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp)
  {
    try
    {
      sftp.cd(directory);
      File file = new File(saveFile);
      sftp.get(downloadFile, new FileOutputStream(file));
    }
    catch (Exception e)
    {
      logger.error("", e);
    }
  }

  public void delete(String directory, String deleteFile, ChannelSftp sftp)
  {
    try
    {
      sftp.cd(directory);
      sftp.rm(deleteFile);
    }
    catch (Exception e)
    {
      logger.error("", e);
    }
  }

  public Vector listFiles(String directory, ChannelSftp sftp)
    throws SftpException
  {
    return sftp.ls(directory);
  }
}
