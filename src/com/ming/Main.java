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
            CrawlerUrl url = getNextUrl(); //��ȡ����ȡ�����е���һ��URL     
            if (url != null) {     
                printCrawlInfo();      
                String content = getContent(url); //��ȡURL���ı���Ϣ     
                     
                //�۽�����ֻ��ȡ������������ص���ҳ�������������ƥ��򵥴���     
                if (isContentRelevant(content, this.regexpSearchPattern)) {     
                    saveContent(url, content); //������ҳ������     
        
                    //��ȡ��ҳ�����е����ӣ����������ȡ������     
                    Collection urlStrings = extractUrls(content, url);     
                    addUrlsToUrlQueue(url, urlStrings);     
                } else {     
                    System.out.println(url + " is not relevant ignoring ...");     
                }     
        
                //��ʱ��ֹ���Է�����     
                Thread.sleep(this.delayBetweenUrls);     
            }     
        }     
        closeOutputStream();     
    } 
    
    private CrawlerUrl getNextUrl() throws Throwable {     
        CrawlerUrl nextUrl = null;     
        while ((nextUrl == null) && (!urlQueue.isEmpty())) {     
            CrawlerUrl crawlerUrl = this.urlQueue.remove();     
                        
            //doWeHavePermissionToVisit���Ƿ���Ȩ�޷��ʸ�URL���Ѻõ�����������վ�ṩ��"Robot.txt"�����õĹ��������ȡ     
            //isUrlAlreadyVisited��URL�Ƿ���ʹ������͵�����������������BloomFilter�������أ������ʹ��HashMap     
            //isDepthAcceptable���Ƿ�ﵽָ����������ޡ�����һ���ȡ������ȵķ�ʽ��һЩ��վ�ṹ���������壨�Զ�����һЩ��Ч����ʹ����������ѭ����������������Ƽ��Ա���     
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
        //HttpClient4.1�ĵ�����֮ǰ�ķ�ʽ��ͬ     
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
        //��url���Ϊ�ѷ���     
        markUrlAsVisited(url);     
        return strBuf.toString();     
    }  
    
    
    public static boolean isContentRelevant(String content,     
            Pattern regexpPattern) {     
                boolean retValue = false;     
                if (content != null) {     
                    //�Ƿ����������ʽ������     
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
        
    //�����ⲿ����     
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
        
    //�����ڲ�����     
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
