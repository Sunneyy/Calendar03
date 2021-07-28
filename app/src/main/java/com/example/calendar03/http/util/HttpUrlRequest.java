package com.example.calendar03.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUrlRequest {
    //get
    public static String Request(String httpUrl) throws IOException {
        //1 创建url对象
        URL url = new URL(httpUrl);
        //2 调用openConnection获取HttpURLConnection对象实例
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //3 设置请求方式 get
        conn.setRequestMethod("GET");
        //4 设置连接超时
        conn.setConnectTimeout(5000);
        String html = new String();
        if (conn.getResponseCode() == 200) {
            // 获取响应的输入流对象
            InputStream in = conn.getInputStream();
            byte[] data = StreamTool.read(in);
            html = new String(data, "UTF-8");
            return html;
        }
        return html;
    }

    //post
    public static String Request(String httpUrl, Map<String, String> paramMap) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(httpUrl).openConnection();
        //设置请求方式
        conn.setRequestMethod("POST");
        //设置请求超时
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        //设置运行输入,输出:
        conn.setDoOutput(true);
        conn.setDoInput(true);
        //Post方式不能缓存,需手动设置为false
        conn.setUseCaches(false);
        //请求的数据
//        String data = "username=" + URLEncoder.encode(parameterMap.get("userNmae"), "UTF-8") +
//                "&password=" + URLEncoder.encode(parameterMap.get("userPwd"), "UTF-8");
        StringBuffer sb = new StringBuffer();
        //遍历paramMap中的参数
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        sb.deleteCharAt(sb.length() - 1);
        String data = sb.toString();
        //获取输出流
        OutputStream out = conn.getOutputStream();
        out.write(data.getBytes());
        out.flush();
        String msg = new String();
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            byte[] buffer = StreamTool.read(in);
            msg = new String(buffer, "UTF-8");
            return msg;
        }
        return msg;
    }

}
