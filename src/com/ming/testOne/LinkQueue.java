package com.ming.testOne;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkQueue {
 // 已访问的 url 集合
    private static Set visitedUrl = new HashSet();
    // 待访问的 url 集合
    private static Queue unVisitedUrl = new PriorityQueue();
    // 获得URL队列
    public static Queue getUnVisitedUrl() {
     return unVisitedUrl;
    }
    // 添加到访问过的URL队列中
    public static void addVisitedUrl(String url) {
        
     visitedUrl.add(url);
    }
    // 移除访问过的URL
    public static void removeVisitedUrl(String url) {
     visitedUrl.remove(url);
    }
    // 未访问的URL出队列
    public static Object unVisitedUrlDeQueue() {
     return unVisitedUrl.poll();
    }
    // 保证每个 url 只被访问一次
    public static void addUnvisitedUrl(String url) {
     if (url != null && !url.trim().equals("") && !visitedUrl.contains(url)
       && !unVisitedUrl.contains(url)){
         String regex="?page=-";
//         Pattern pattern=Pattern.compile(regex);
         String regex2="page=-";
//         Pattern pattern2=Pattern.compile(regex2);
//             Matcher matcher=pattern.matcher(url);
//             Matcher matcher2=pattern2.matcher(url);
//             if(matcher.find()&&matcher.matches()){
//                 return;
//             }
//             if(matcher2.find()&&matcher2.matches()){
//                 return;
//             }
             
             int i = url.indexOf(regex);
             int ii = url.indexOf(regex2);
             if(i!=-1||ii!=-1){
                 return;
             }
             unVisitedUrl.add(url);
     }
      
    }
    // 获得已经访问的URL数目
    public static int getVisitedUrlNum() {
     return visitedUrl.size();
    }
    // 判断未访问的URL队列中是否为空
    public static boolean unVisitedUrlsEmpty() {
     return unVisitedUrl.isEmpty();
    }
}
