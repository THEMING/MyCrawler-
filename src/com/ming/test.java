package com.ming;

public class test {

    public static void main(String[] args) {
        PostMethod httppost = null;
        HttpClient httpclient = new HttpClient();
        httpclient.getParams().setSoTimeout(10 * 60 * 1000);//���ó���
        httppost = new PostMethod("http://www.baidu.com");
        httppost.addParameter("p_name", "p_value");//���ݲ���[POST]
        httpclient.executeMethod(httppost);
        byte[] by = httppost.getResponseBody();
        String response = new String(by);
        System.out.println(response);
    }
}
