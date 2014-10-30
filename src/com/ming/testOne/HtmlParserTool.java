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
    // 获取一个网站上的链接,filter 用来过滤链接
    public static Set<String> extracLinks(String url, LinkFilter filter) {
        urll = url;
        filterr = filter;
//     Set<String> links = new HashSet<String>();
     try {
      Parser parser = new Parser();
      parser.setURL(url);
      parser.setEncoding("GB2312");
      // 过滤 <frame >标签的 filter，用来提取 frame 标签里的 src 属性所表示的链接
      NodeFilter frameFilter = new NodeFilter() {
       public boolean accept(Node node) {
        if (node.getText().startsWith("frame src=")) {
         return true;
        } else {
         return false;
        }
       }
      };
      // OrFilter 来设置过滤 <a> 标签，和 <frame> 标签
      OrFilter linkFilter = new OrFilter(new NodeClassFilter(
        LinkTag.class), frameFilter);
      // 得到所有经过过滤的标签
      NodeList list = parser.extractAllNodesThatMatch(linkFilter);
      for (int i = 0; i < list.size(); i++) {
       Node tag = list.elementAt(i);
       if (tag instanceof LinkTag)// <a> 标签
       {
        LinkTag link = (LinkTag) tag;
        String linkUrl = link.getLink();// url
        if (filter.accept(linkUrl))
         links.add(linkUrl);
       } else// <frame> 标签
       {
        // 提取 frame 里 src 属性的链接如 <frame src="test.html"/>
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
