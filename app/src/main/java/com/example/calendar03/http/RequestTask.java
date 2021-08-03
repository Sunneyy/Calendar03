package com.example.calendar03.http;

import android.icu.util.Output;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendar03.gson.BaseResult;
import com.example.calendar03.gson.CalendarData;
import com.example.calendar03.http.ResponseListener;
import com.example.calendar03.http.util.HttpUrlRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.Map;

/**
 * 输入：请求信息（url,method,params）
 * 输出：请求结果（状态码，信息，数据）
 */

public class RequestTask<Output> implements Runnable {
    private String method;
    private String url;
    private String result;
    private Map<String, String> paramMap;
    private ResponseListener<Output> responseListener;
    private int id;


    public RequestTask(String method, String url, Map<String, String> paramMap) {
        this.method = method;
        this.url = url;
        this.paramMap = paramMap;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setResponseListener(ResponseListener<Output> responseListener) {
        this.responseListener = responseListener;
    }

//    public ResponseListener<Output> getResponseListener() {
//        return responseListener;
//    }


    @Override
    public void run() {
//        try {
            if ("GET".equals(method)) {
                try {
                    result = HttpUrlRequest.Request(url);
                } catch (IOException e) {
                    Log.i("tag", "GET ERROR");
                }
            } else if ("POST".equals(method)) {
                try {
                    result = HttpUrlRequest.Request(url, paramMap);
                } catch (IOException e) {
                    Log.i("tag", "POST ERROR");
                }
            }
            if (responseListener != null) {
                Type type = new ParameterizedType() {
                    @NonNull
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[0];
                    }

                    @NonNull
                    @Override
                    public Type getRawType() {
                        return BaseResult.class;
                    }

                    @Nullable
                    @Override
                    public Type getOwnerType() {
                        return null;
                    }
                };


                //Type type1 = ((ParameterizedType)responseListener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//                Type responseType = new TypeToken<BaseResult<CalendarData>>() {
//                }.getType();
                Gson gson = new Gson();
                BaseResult<Output> jsonResult = gson.fromJson(result, type);
                responseListener.onSuccess(jsonResult);
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (responseListener == null) {
//                responseListener.onFailure(e);
//            }
//        }
    }


}
