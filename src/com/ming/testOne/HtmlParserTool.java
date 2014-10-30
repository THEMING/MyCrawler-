package com.ming.testOne;

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HtmlParserTool {
    
    static String encoding = "GB2312";
    static String urll;
    static LinkFilter filterr = null;
    static Set<String> links = new HashSet<String>();
    static int iFla = 0;
    // ��ȡһ����վ�ϵ�����,filter ������������
    public static Set<String> extracLinks(String url, LinkFilter filter) {
        urll = url;
        filterr = filter;
//     Set<String> links = new HashSet<String>();
     try {
      Parser parser = new Parser();
      parser.setURL(url);
      parser.setEncoding("GB2312");
      // ���� <frame >��ǩ�� filter��������ȡ frame ��ǩ��� src ��������ʾ������
      NodeFilter frameFilter = new NodeFilter() {
       public boolean accept(Node node) {
        if (node.getText().startsWith("frame src=")) {
         return true;
        } else {
         return false;
        }
       }
      };
      // OrFilter �����ù��� <a> ��ǩ���� <frame> ��ǩ
      OrFilter linkFilter = new OrFilter(new NodeClassFilter(
        LinkTag.class), frameFilter);
      // �õ����о������˵ı�ǩ
      NodeList list = parser.extractAllNodesThatMatch(linkFilter);
      for (int i = 0; i < list.size(); i++) {
       Node tag = list.elementAt(i);
       if (tag instanceof LinkTag)// <a> ��ǩ
       {
        LinkTag link = (LinkTag) tag;
        String linkUrl = link.getLink();// url
        if (filter.accept(linkUrl))
         links.add(linkUrl);
       } else// <frame> ��ǩ
       {
        // ��ȡ frame �� src ���Ե������� <frame src="test.html"/>
        String frame = tag.getText();
        int start = frame.indexOf("src=");
        frame = frame.substring(start);
        int end = frame.indexOf(" ");
        if (end == -1)
         end = frame.indexOf(">");
        String frameUrl = frame.substring(5, end - 1);
        if (filter.accept(frameUrl))
         links.add(frameUrl);
       }
      }
     } catch (ParserException e) {
//      e.printStackTrace();
      if(encoding.equals("GB2312")&&iFla<2){
          encoding = "UTF-8";
          System.out.println("==================================change encoding "+encoding);
          iFla++;
          links = extracLinks(urll, filterr);
      }else if(encoding.equals("UTF-8")&&iFla<2){
          encoding = "GB2312";
          System.out.println("==================================change encoding "+encoding);
          iFla++;
          links = extracLinks(urll, filterr);
      }else if(iFla==2){
          iFla = 0;
          links = new HashSet<String>();
          return links;
      }
     }
     return links;
    }
}
