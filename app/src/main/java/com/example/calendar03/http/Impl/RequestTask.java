package com.example.calendar03.http.Impl;

import android.os.Handler;

import java.util.Map;

public class RequestTask implements Runnable {
    private String method;
    private String url;
    private String result;
    private Map<String, String> paramMap;
    private Handler handler;

    public RequestTask(String method, String url, Map<String, String> paramMap,Handler handler) {
        this.method = method;
        this.url = url;
        this.paramMap = paramMap;
        this.handler=handler;
        //this.result=result;
    }

    @Override
    public void run() {
        try {
            HttpClientServiceImpl httpClientService = new HttpClientServiceImpl();
            result = httpClientService.getHttpInvokeResult(method, url, paramMap);
            //Thread.sleep(10*1000);
            handler.obtainMessage(0, result).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
