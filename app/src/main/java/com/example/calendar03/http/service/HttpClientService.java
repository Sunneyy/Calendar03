package com.example.calendar03.http.service;

import java.util.Map;

/**
 * http请求接口
 */
public interface HttpClientService {
    /**
     * @param method   请求方式
     * @param httpUrl  请求地址
     * @param paramMap 请求参数
     */
    String getHttpInvokeResult(String method, String httpUrl, Map<String, String> paramMap);
}
