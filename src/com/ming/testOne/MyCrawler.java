package com.ming.testOne;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCrawler {
    /**
     * 使用种子初始化 URL 队列
     * 
     * @return
     * @param seeds
     *            种子URL
     */
    private void initCrawlerWithSeeds(String[] seeds) {
     for (int i = 0; i < seeds.length; i++)
      LinkQueue.addUnvisitedUrl(seeds[i]);
    }
    /**
     * 抓取过程
     * 
     * @return
     * @param seeds
     */
    public void crawling(String[] seeds,String[] seeds1) { // 定义过滤器，提取以http://www.lietu.com开头的链接
//     LinkFilter filter = new LinkFilter() {
//      public boolean accept(String url) {
//       if (url.startsWith("http://www.baidu.com"))
//        return true;
//       else
//        return false;
//      }
//     };
        LinkFilter filter = new LinkFilter(seeds1);
     // 初始化 URL 队列
     initCrawlerWithSeeds(seeds);
     // 循环条件：待抓取的链接不空且抓取的网页不多于1000
     while (!LinkQueue.unVisitedUrlsEmpty()
//       && LinkQueue.getVisitedUrlNum() <= 1000 
       && EmailQueue.getEmailsNum()<=2000) {
      // 队头URL出队列
      String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();
      if (visitUrl == null)
       continue;
      DownLoadFile downLoader = new DownLoadFile();
      // 下载网页
      downLoader.downloadFile(visitUrl);
      // 该 url 放入到已访问的 URL 中
      LinkQueue.addVisitedUrl(visitUrl);
      // 提取出下载网页中的 URL
      Set<String> links = HtmlParserTool.extracLinks(visitUrl, filter);
      // 新的未访问的 URL 入队
      if(links!=null){
          for (String link : links) {
              LinkQueue.addUnvisitedUrl(link);
             }
      }
     }
     
     System.out.println("========================结束===================================================================");
    }
    // main 方法入口
    public static void main(String[] args) {
     MyCrawler crawler = new MyCrawler();
//     crawler.crawling(new String[] { "http://www.baidu.com/s?wd=%40qq.com&rsv_spt=1&issp=1&f=8&rsv_bp=0&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_sug3=47&rsv_sug4=5330&rsv_sug1=39&rsv_sug2=0&inputT=6680"}
//     crawler.crawling(new String[] { "http://photo.163.com/?from=logo"}
     
     String filePath = "f:\\emails\\emails.txt";
    readFile(filePath);
     
//     crawler.crawling(new String[] { "http://pp.163.com/forum/247001/subject/2816001/"}http://bbs.17500.cn/forum.php
    crawler.crawling(new String[] { "http://blog.sina.com.cn/19721125hyx"}
     ,new String[] {"http://blog.sina.com.cn/"});
    }
    private static void readFile(String filePath) {
        BufferedReader buf;
            try {
                buf = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            
        
                String regex="([0-9_\\-\\.])+@qq.com";
                //String regex="([a-zA-Z0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";各种字符串开头邮箱
    //            String regex="([0-9_\\-\\.])+@(.)+(\\.[a-z]+){1,}";//数字开头邮箱
                Pattern pattern=Pattern.compile(regex);
                String str = null;
                while((str=buf.readLine())!=null)
                {
                    EmailQueue.getEmails().add(str);
                }
            
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}
