package com.ming.testOne;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkQueue {
 // �ѷ��ʵ� url ����
    private static Set visitedUrl = new HashSet();
    // �����ʵ� url ����
    private static Queue unVisitedUrl = new PriorityQueue();
    // ���URL����
    public static Queue getUnVisitedUrl() {
     return unVisitedUrl;
    }
    // ��ӵ����ʹ���URL������
    public static void addVisitedUrl(String url) {
        
     visitedUrl.add(url);
    }
    // �Ƴ����ʹ���URL
    public static void removeVisitedUrl(String url) {
     visitedUrl.remove(url);
    }
    // δ���ʵ�URL������
    public static Object unVisitedUrlDeQueue() {
     return unVisitedUrl.poll();
    }
    // ��֤ÿ�� url ֻ������һ��
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
    // ����Ѿ����ʵ�URL��Ŀ
    public static int getVisitedUrlNum() {
     return visitedUrl.size();
    }
    // �ж�δ���ʵ�URL�������Ƿ�Ϊ��
    public static boolean unVisitedUrlsEmpty() {
     return unVisitedUrl.isEmpty();
    }
}
