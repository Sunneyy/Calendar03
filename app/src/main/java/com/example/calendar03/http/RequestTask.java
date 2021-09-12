package com.example.calendar03.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendar03.gson.BaseResult;
import com.example.calendar03.http.util.HttpUrlRequest;
import com.example.calendar03.http.util.ThreadPoolUtil;
import com.google.gson.Gson;

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
    private Map<String, String> paramMap;

    private int id;
    private BaseResult<Output> apiResult; // 请求成功
    private Exception exception; // 请求失败
    private ResponseListener<Output> responseListener;


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

    /**
     * 通知task中的Listener
     */
    public void notifyResponseListener() {
        if (responseListener == null) {
            return;
        }
        if (apiResult != null) {
            if (apiResult.getError_code() == 0) {
                responseListener.onSuccess(apiResult.getResult());
            } else {
                responseListener.onFailure(new HttpException(apiResult.getError_code(), apiResult.getReason()));
            }
        } else {
            responseListener.onFailure(exception);
        }
    }


    @Override
    public void run() {
        try {
            // Step1: 获取http结果
            String httpResult = null;
            if ("GET".equals(method)) {
                httpResult = HttpUrlRequest.Request(url);
            } else if ("POST".equals(method)) {
                httpResult = HttpUrlRequest.Request(url, paramMap);
            }
            // Step2：解析http结果
            if (responseListener != null) {
                Type type = new ParameterizedType() {
                    @NonNull
                    @Override
                    public Type[] getActualTypeArguments() {
                        // 获取泛型接口，获取的类型必定为带泛型参数的类型
                        ParameterizedType ltype = (ParameterizedType) responseListener.getClass().getGenericInterfaces()[0];
                        // 获取具体的参数类型
                        Type atype = ltype.getActualTypeArguments()[0];
                        System.out.println(atype);
                        return new Type[]{atype};
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

                Gson gson = new Gson();
                this.apiResult = gson.fromJson(httpResult, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.exception = e;
        } finally {
            if (responseListener != null) {
                ThreadPoolUtil.getInstance().dispatcher(this);
            }
        }
    }
}
