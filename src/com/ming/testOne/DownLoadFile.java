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
     * ���� url ����ҳ����������Ҫ�������ҳ���ļ��� ȥ���� url �з��ļ����ַ�
     */
    public String getFileNameByUrl(String url, String contentType) {
     // remove http://
     url = url.substring(7);
     // text/html����
     if (contentType.indexOf("html") != -1) {
      url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
      return url;
     }
     // ��application/pdf����
     else {
      return url.replaceAll("[\\?/:*|<>\"]", "_") + "."
        + contentType.substring(contentType.lastIndexOf("/") + 1);
     }
    }
    /**
     * ������ҳ�ֽ����鵽�����ļ� filePath ΪҪ������ļ�����Ե�ַ
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
    /* ���� url ָ�����ҳ */
    public String downloadFile(String url) {
     String filePath = null;
//     /* 1.���� HttpClinet �������ò��� */
//     HttpClient httpClient = new HttpClient();
//     // ���� Http ���ӳ�ʱ 5s
//     httpClient.getHttpConnectionManager().getParams()
//       .setConnectionTimeout(5000);
//     /* 2.���� GetMethod �������ò��� */
//     GetMethod getMethod = new GetMethod(url);
//     // ���� get ����ʱ 5s
//     getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
//     // �����������Դ���
//     getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
//     /* 3.ִ�� HTTP GET ���� */
     try {
//      int statusCode = httpClient.executeMethod(getMethod);
//      // �жϷ��ʵ�״̬��
//      if (statusCode != HttpStatus.SC_OK) {
//       System.err.println("Method failed: "
//         + getMethod.getStatusLine());
//       filePath = null;
//      }
//      /* 4.���� HTTP ��Ӧ���� */
//      byte[] responseBody = getMethod.getResponseBody();// ��ȡΪ�ֽ�����
     
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
         
//          // ������ҳ url ���ɱ���ʱ���ļ���
//          filePath = "e:\\spider\\"
//            + getFileNameByUrl(url,
//                    "text/html");
////              getMethod.getResponseHeader("Content-Type")
////                .getValue());
//          saveToLocal(xmlContent, filePath);
//          getEmail(filePath);
         }
     } catch (IOException e) {
      // ���������쳣
      e.printStackTrace();
     } catch (Exception e) {
         // ���������쳣
//       e.printStackTrace();
          System.out.println("error");
       return null;
      } finally {
      // �ͷ�����
//      getMethod.releaseConnection();
     }
     return filePath;
    }
    
    public static void getEmail(BufferedReader buf){
        try {
        
            String regex="([a-zA-Z0-9_\\-\\.])+@(qq.com|sina.com)";
            //String regex="([a-zA-Z0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";�����ַ�����ͷ����
//            String regex="([0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";//���ֿ�ͷ����
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
            //String regex="([a-zA-Z0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";�����ַ�����ͷ����
//            String regex="([0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";//���ֿ�ͷ����
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
