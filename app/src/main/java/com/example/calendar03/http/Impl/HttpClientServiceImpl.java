package com.example.calendar03.http.Impl;

import android.util.Log;

import com.example.calendar03.http.service.HttpClientService;
import com.example.calendar03.http.util.HttpUrlRequest;

import java.io.IOException;
import java.util.Map;

/**
 * 客户端请求接口实现
 */

public class HttpClientServiceImpl implements HttpClientService {
    @Override
    public String getHttpInvokeResult(String method, String httpUrl, Map<String, String> paramMap) {
        String result = new String();
        if ("GET".equals(method)) {
            try {
                result = HttpUrlRequest.Request(httpUrl);
            } catch (IOException e) {
                Log.i("tag", "GET ERROR");
            }
        } else if ("POST".equals(method)) {
            try {
                result = HttpUrlRequest.Request(httpUrl, paramMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
