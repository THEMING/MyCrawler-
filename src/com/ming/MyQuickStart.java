/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.ming;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class MyQuickStart {

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet("http://www.baidu.com/");//http://wbj05791467.blog.163.com/blog/static/120329697201332644650104/
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            StringBuffer strBuf = new StringBuffer();   
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            try {
//                System.out.println(response1.getStatusLine());
//                HttpEntity entity1 = response1.getEntity();
//                // do something useful with the response body
//                // and ensure it is fully consumed
//                EntityUtils.consume(entity1);
                
                if (HttpStatus.SC_OK == response1.getStatusLine().getStatusCode()) {     
                    HttpEntity entity = response1.getEntity();  
                    
                    String xmlContent=EntityUtils.toString(entity);
                    System.out.println(xmlContent);
                    System.out.println(response1.getHeaders("Content-Type")[0].getValue().toString());
                    
//                    oper(entity);
                    if (entity != null) {     
                        BufferedReader reader = new BufferedReader(     
                            new InputStreamReader(entity.getContent(), "GBK"));     
                        String line = null;     
                        if (entity.getContentLength() > 0) {     
                            strBuf = new StringBuffer((int) entity.getContentLength());     
                            while ((line = reader.readLine()) != null) {     
                                strBuf.append(line);     
                            }     
                        }     
                    }     
                    if (entity != null) {     
                        EntityUtils.consume(entity);  
                    }   
                    
                    HeaderIterator it = response1.headerIterator("Set-Cookie");
                    while (it.hasNext()) {
                    System.out.println(it.next());
                    }
                    System.out.println(strBuf.toString());
                }
            } finally {
                response1.close();
            }

        } finally {
            httpclient.close();
        }
    }
    
    private static void oper(HttpEntity entity) {
        try {
            String ss = entity.getContent().toString();
            BufferedReader reader = new BufferedReader(     
                    new InputStreamReader(entity.getContent(), "UTF-8"));     
            System.out.println(ss);
            
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

    // 解压缩   
    public static String uncompress(String str) throws IOException {   
       if (str == null || str.length() == 0) {   
         return str;   
     }   
      ByteArrayOutputStream out = new ByteArrayOutputStream();   
      ByteArrayInputStream in = new ByteArrayInputStream(str   
           .getBytes("ISO-8859-1"));   
       GZIPInputStream gunzip = new GZIPInputStream(in);   
       byte[] buffer = new byte[256];   
       int n;   
      while ((n = gunzip.read(buffer))>= 0) {   
       out.write(buffer, 0, n);   
       }   
       // toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)   
       return out.toString();   
     }   

}
