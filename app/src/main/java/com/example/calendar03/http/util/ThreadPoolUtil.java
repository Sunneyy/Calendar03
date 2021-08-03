package com.example.calendar03.http.util;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.calendar03.gson.BaseResult;
import com.example.calendar03.http.RequestTask;
import com.example.calendar03.http.ResponseListener;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
    private static final int DEFAULT_CORE_SIZE = 100;
    private static final int MAX_QUEUE_SIZE = 500;
    private volatile static ThreadPoolExecutor executor;

    private int sequence=1;
    private static ThreadPoolUtil instance;


    Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            TempData obj=(TempData) msg.obj;
            if(obj.exception==null){
                obj.listener.onSuccess(obj.result);
            }
            else {
                obj.listener.onFailure(obj.exception);
            }
        }
    };

    private ThreadPoolUtil(){
        executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE,// 核心线程数
                MAX_QUEUE_SIZE, // 最大线程数
                Integer.MAX_VALUE, // 闲置线程存活时间
                TimeUnit.MILLISECONDS,// 时间单位
                new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),// 线程队列
                Executors.defaultThreadFactory()// 线程工厂
        );
    }
    // 获取单例的线程池对象
    public static ThreadPoolUtil getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolUtil.class) {
                instance=new ThreadPoolUtil();
            }
        }
        return instance;
    }

    public <T> void request(final RequestTask<T> task, final ResponseListener<T> listener) {
        task.setId(sequence++);
        task.setResponseListener(new ResponseListener<T>() {
            @Override
            public void onSuccess(BaseResult<T> result) {
                Message msg = Message.obtain();
                msg.what = task.getId();
                msg.obj = new TempData<T>(null, result, listener);
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Exception e) {
                Message msg = Message.obtain();
                msg.what = task.getId();
                msg.obj = new TempData<T>(e, null, listener);
                handler.sendMessage(msg);
            }
        });
        executor.execute(task);
    }

    private static class TempData<T> {

        private Exception exception;

        private BaseResult<T> result;

        private ResponseListener<T> listener;

        TempData(Exception e, BaseResult<T>result, ResponseListener<T> listener) {
            this.exception = e;
            this.result = result;
            this.listener = listener;
        }
    }

}

