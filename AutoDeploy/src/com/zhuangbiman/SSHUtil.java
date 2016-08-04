
/**    
 * 文件名：SSHUtil.java    
 *    
 * 版本信息：    
 * 日期：2016-8-4    
 * Copyright 足下 Corporation 2016     
 * 版权所有    
 *    
 */ package com.zhuangbiman; /**    
 *     
 * 项目名称：auto    
 * 类名称：SSHUtil    
 * 类描述：    
 * 创建人：Administrator    
 * 创建时间：2016-8-4 下午11:12:35    
 * 修改人：Administrator    
 * 修改时间：2016-8-4 下午11:12:35    
 * 修改备注：    
 * @version     
 *     
 */
 package com.zhuangbiman;

 import ch.ethz.ssh2.Connection;
 import ch.ethz.ssh2.Session;
 import ch.ethz.ssh2.StreamGobbler;
 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.util.ArrayList;
 import java.util.List;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;

 public class SSHUtil
 {
   private static Logger logger = LoggerFactory.getLogger(SSHUtil.class);

   public static List<String> getCommandResults(String hostname, String user, String password, String command)
     throws IOException
   {
     List results = new ArrayList();
     Connection connHost = new Connection(hostname);
     connHost.connect();
     boolean result = connHost.authenticateWithPassword(user, password);
     if (!result)
     {
       throw new IOException("认证失败！");
     }
     Session sess = connHost.openSession();
     sess.execCommand(command);
     InputStream is = new StreamGobbler(sess.getStdout());
     BufferedReader br = new BufferedReader(new InputStreamReader(is));
     while (true)
     {
       String line = br.readLine();
       if (line == null)
       {
         break;
       }
       results.add(line);
       logger.trace(line);
     }

     InputStream is2 = new StreamGobbler(sess.getStderr());
     BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
     while (true)
     {
       String line2 = br2.readLine();
       if (line2 == null)
       {
         break;
       }
       results.add(line2);
       logger.trace(line2);
     }
     sess.close();
     connHost.close();

     return results;
   }

   public static void executeCommand(String hostname, String user, String password, String command) throws IOException
   {
     Connection connHost = new Connection(hostname);
     connHost.connect();
     boolean result = connHost.authenticateWithPassword(user, password);
     if (!result)
     {
       throw new IOException("认证失败！");
     }
     Session sess = connHost.openSession();
     sess.execCommand(command);
     sess.close();
     connHost.close();
   }
 }

