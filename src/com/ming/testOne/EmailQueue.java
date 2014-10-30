package com.ming.testOne;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class EmailQueue {
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
       && !unVisitedUrl.contains(url))
      unVisitedUrl.add(url);
    }
    // ����Ѿ����ʵ�URL��Ŀ
    public static int getVisitedUrlNum() {
     return visitedUrl.size();
    }
    // �ж�δ���ʵ�URL�������Ƿ�Ϊ��
    public static boolean unVisitedUrlsEmpty() {
     return unVisitedUrl.isEmpty();
    }
    
    private static Set emails = new HashSet();
    
//    private static FileWriter writer = new FileWriter("f:\\emails\\emails.txt", true);
    
    public static void addEmails(String url) {
        try {
        if (url != null && !url.trim().equals("") && !emails.contains(url)){
            System.out.println(url);
            emails.add(url);
            FileWriter writer = new FileWriter("f:\\emails\\emails.txt", true);
            writer.write(url+"\r\n");
            writer.close();
        }else{
            System.out.println("================================="+url+"������");
        }
        
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       }
    
    public static Set getEmails(){
        return emails;
    }
    
    public static int getEmailsNum() {
        return emails.size();
       }
}
