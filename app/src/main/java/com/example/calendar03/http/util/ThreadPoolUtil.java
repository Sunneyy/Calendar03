package com.example.calendar03.http.util;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.calendar03.http.RequestTask;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
    private static final int DEFAULT_CORE_SIZE = 100;
    private static final int MAX_QUEUE_SIZE = 500;
    private volatile static ThreadPoolExecutor executor;

    private int sequence = 1;
    private static ThreadPoolUtil instance;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            RequestTask task = (RequestTask) msg.obj;
            task.notifyResponseListener();
        }
    };

    private ThreadPoolUtil() {
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
                instance = new ThreadPoolUtil();
            }
        }
        return instance;
    }

    public void request(final RequestTask task) {
        task.setId(sequence++);
        executor.execute(task);
    }

    /**
     * 分发到UI线程
     *
     * @param task 请求任务
     */
    public void dispatcher(RequestTask task) {
        Message msg = Message.obtain();
        msg.what = task.getId();
        msg.obj = task;
        handler.sendMessage(msg);
    }
}

