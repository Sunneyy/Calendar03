package com.example.calendar03.http.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTool {
    //读取数据
    public static byte[] read(InputStream inStream) throws IOException {
        // 创建字节输出流对象
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 定义缓冲区
        byte[] buffer = new byte[1024];
        // 定义读取的长度
        int len = 0;
        // 按照缓冲区的大小，循环读取
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        // 释放资源
        inStream.close();
        return outStream.toByteArray();
    }
}
