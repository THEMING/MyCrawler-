package com.ming;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class Main {

    public void crawl() throws Throwable {     
        while (continueCrawling()) {     
            CrawlerUrl url = getNextUrl(); //获取待爬取队列中的下一个URL     
            if (url != null) {     
                printCrawlInfo();      
                String content = getContent(url); //获取URL的文本信息     
                     
                //聚焦爬虫只爬取与主题内容相关的网页，这里采用正则匹配简单处理     
                if (isContentRelevant(content, this.regexpSearchPattern)) {     
                    saveContent(url, content); //保存网页至本地     
        
                    //获取网页内容中的链接，并放入待爬取队列中     
                    Collection urlStrings = extractUrls(content, url);     
                    addUrlsToUrlQueue(url, urlStrings);     
                } else {     
                    System.out.println(url + " is not relevant ignoring ...");     
                }     
        
                //延时防止被对方屏蔽     
                Thread.sleep(this.delayBetweenUrls);     
            }     
        }     
        closeOutputStream();     
    } 
    
    private CrawlerUrl getNextUrl() throws Throwable {     
        CrawlerUrl nextUrl = null;     
        while ((nextUrl == null) && (!urlQueue.isEmpty())) {     
            CrawlerUrl crawlerUrl = this.urlQueue.remove();     
                        
            //doWeHavePermissionToVisit：是否有权限访问该URL，友好的爬虫会根据网站提供的"Robot.txt"中配置的规则进行爬取     
            //isUrlAlreadyVisited：URL是否访问过，大型的搜索引擎往往采用BloomFilter进行排重，这里简单使用HashMap     
            //isDepthAcceptable：是否达到指定的深度上限。爬虫一般采取广度优先的方式。一些网站会构建爬虫陷阱（自动生成一些无效链接使爬虫陷入死循环），采用深度限制加以避免     
            if (doWeHavePermissionToVisit(crawlerUrl)     
                && (!isUrlAlreadyVisited(crawlerUrl))      
                && isDepthAcceptable(crawlerUrl)) {     
                nextUrl = crawlerUrl;     
                // System.out.println("Next url to be visited is " + nextUrl);     
            }     
        }     
        return nextUrl;     
    }
    
    private String getContent(CrawlerUrl url) throws Throwable {     
        //HttpClient4.1的调用与之前的方式不同     
        HttpClient client = new DefaultHttpClient();     
        HttpGet httpGet = new HttpGet(url.getUrlString());     
        StringBuffer strBuf = new StringBuffer();     
        HttpResponse response = client.execute(httpGet);     
        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {     
            HttpEntity entity = response.getEntity();     
            if (entity != null) {     
                BufferedReader reader = new BufferedReader(     
                    new InputStreamReader(entity.getContent(), "UTF-8"));     
                String line = null;     
                if (entity.getContentLength() > 0) {     
                    strBuf = new StringBuffer((int) entity.getContentLength());     
                    while ((line = reader.readLine()) != null) {     
                        strBuf.append(line);     
                    }     
                }     
            }     
            if (entity != null) {     
                entity.consumeContent();     
            }     
        }     
        //将url标记为已访问     
        markUrlAsVisited(url);     
        return strBuf.toString();     
    }  
    
    
    public static boolean isContentRelevant(String content,     
            Pattern regexpPattern) {     
                boolean retValue = false;     
                if (content != null) {     
                    //是否符合正则表达式的条件     
                    Matcher m = regexpPattern.matcher(content.toLowerCase());     
                    retValue = m.find();     
                }     
                return retValue;     
            }  
    
    public List extractUrls(String text, CrawlerUrl crawlerUrl) {     
        Map urlMap = new HashMap();     
        extractHttpUrls(urlMap, text);     
        extractRelativeUrls(urlMap, text, crawlerUrl);     
        return new ArrayList(urlMap.keySet());     
    }     
        
    //处理外部链接     
    private void extractHttpUrls(Map urlMap, String text) {     
        Matcher m = httpRegexp.matcher(text);     
        while (m.find()) {     
            String url = m.group();     
            String[] terms = url.split("a href=\"");     
            for (String term : terms) {     
                // System.out.println("Term = " + term);     
                if (term.startsWith("http")) {     
                    int index = term.indexOf("\"");     
                    if (index > 0) {     
                        term = term.substring(0, index);     
                    }     
                    urlMap.put(term, term);     
                    System.out.println("Hyperlink: " + term);     
                }     
            }     
        }     
    }     
        
    //处理内部链接     
    private void extractRelativeUrls(Map urlMap, String text,     
            CrawlerUrl crawlerUrl) {     
        Matcher m = relativeRegexp.matcher(text);     
        URL textURL = crawlerUrl.getURL();     
        String host = textURL.getHost();     
        while (m.find()) {     
            String url = m.group();     
            String[] terms = url.split("a href=\"");     
            for (String term : terms) {     
                if (term.startsWith("/")) {     
                    int index = term.indexOf("\"");     
                    if (index > 0) {     
                        term = term.substring(0, index);     
                    }     
                    String s = "http://" + host + term;     
                    urlMap.put(s, s);     
                    System.out.println("Relative url: " + s);     
                }     
            }     
        }     
        
    } 
    
    private Queue urlQueue = new LinkedList();  
    
    public static void main(String[] args) {     
        try {     
            String url = "http://www.amazon.com";     
               
            String regexp = "java";     
            urlQueue.add(new CrawlerUrl(url, 0));     
            NaiveCrawler crawler = new NaiveCrawler(urlQueue, 100, 5, 1000L,     
                    regexp);     
            // boolean allowCrawl = crawler.areWeAllowedToVisit(url);     
            // System.out.println("Allowed to crawl: " + url + " " +     
            // allowCrawl);     
            crawler.crawl();     
        } catch (Throwable t) {     
            System.out.println(t.toString());     
            t.printStackTrace();     
        }     
    } 
}
