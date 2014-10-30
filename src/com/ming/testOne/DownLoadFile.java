package com.ming.testOne;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class DownLoadFile {
    /**
     * 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
     */
    public String getFileNameByUrl(String url, String contentType) {
     // remove http://
     url = url.substring(7);
     // text/html类型
     if (contentType.indexOf("html") != -1) {
      url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
      return url;
     }
     // 如application/pdf类型
     else {
      return url.replaceAll("[\\?/:*|<>\"]", "_") + "."
        + contentType.substring(contentType.lastIndexOf("/") + 1);
     }
    }
    /**
     * 保存网页字节数组到本地文件 filePath 为要保存的文件的相对地址
     */
    private void saveToLocal(byte[] data, String filePath) {
     try {
      DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
      for (int i = 0; i < data.length; i++)
       out.write(data[i]);
      out.flush();
      out.close();
     } catch (IOException e) {
      e.printStackTrace();
     }
    }
    /* 下载 url 指向的网页 */
    public String downloadFile(String url) {
     String filePath = null;
//     /* 1.生成 HttpClinet 对象并设置参数 */
//     HttpClient httpClient = new HttpClient();
//     // 设置 Http 连接超时 5s
//     httpClient.getHttpConnectionManager().getParams()
//       .setConnectionTimeout(5000);
//     /* 2.生成 GetMethod 对象并设置参数 */
//     GetMethod getMethod = new GetMethod(url);
//     // 设置 get 请求超时 5s
//     getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
//     // 设置请求重试处理
//     getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
//     /* 3.执行 HTTP GET 请求 */
     try {
//      int statusCode = httpClient.executeMethod(getMethod);
//      // 判断访问的状态码
//      if (statusCode != HttpStatus.SC_OK) {
//       System.err.println("Method failed: "
//         + getMethod.getStatusLine());
//       filePath = null;
//      }
//      /* 4.处理 HTTP 响应内容 */
//      byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
     
     CloseableHttpClient httpclient = HttpClients.createDefault();
     System.out.println(url +"=="+EmailQueue.getEmailsNum());
     HttpGet httpGet = new HttpGet(url);//http://wbj05791467.blog.163.com/blog/static/120329697201332644650104/
     CloseableHttpResponse response1 = httpclient.execute(httpGet);
         if (HttpStatus.SC_OK == response1.getStatusLine().getStatusCode()) {     
             HttpEntity entity = response1.getEntity();  
             
             byte[] xmlContent=EntityUtils.toByteArray(entity);
             
             InputStream in = new ByteArrayInputStream(xmlContent);
             BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
             in));
             getEmail(bufferReader);
         
//          // 根据网页 url 生成保存时的文件名
//          filePath = "e:\\spider\\"
//            + getFileNameByUrl(url,
//                    "text/html");
////              getMethod.getResponseHeader("Content-Type")
////                .getValue());
//          saveToLocal(xmlContent, filePath);
//          getEmail(filePath);
         }
     } catch (IOException e) {
      // 发生网络异常
      e.printStackTrace();
     } catch (Exception e) {
         // 发生网络异常
//       e.printStackTrace();
          System.out.println("error");
       return null;
      } finally {
      // 释放连接
//      getMethod.releaseConnection();
     }
     return filePath;
    }
    
    public static void getEmail(BufferedReader buf){
        try {
        
            String regex="([a-zA-Z0-9_\\-\\.])+@(qq.com|sina.com)";
            //String regex="([a-zA-Z0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";各种字符串开头邮箱
//            String regex="([0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";//数字开头邮箱
            Pattern pattern=Pattern.compile(regex);
            String str = null;
            while((str=buf.readLine())!=null)
            {
                Matcher matcher=pattern.matcher(str);
                while(matcher.find()){
                    EmailQueue.addEmails(matcher.group());
//                    System.out.println(matcher.group());
                }
            }
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void getEmail(String filePath){
        BufferedReader buf;
        try {
            buf = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        
            String regex="([a-zA-Z0-9_\\-\\.])+@(qq.com|sina.com)";
            //String regex="([a-zA-Z0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";各种字符串开头邮箱
//            String regex="([0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";//数字开头邮箱
            Pattern pattern=Pattern.compile(regex);
            String str = null;
            while((str=buf.readLine())!=null)
            {
                Matcher matcher=pattern.matcher(str);
                while(matcher.find()){
                    EmailQueue.addEmails(matcher.group());
//                    System.out.println(matcher.group());
                }
            }
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
