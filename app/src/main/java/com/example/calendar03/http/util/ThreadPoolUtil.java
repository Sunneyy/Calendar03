package com.example.calendar03.http.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
    private static final int DEFAULT_CORE_SIZE = 100;
    private static final int MAX_QUEUE_SIZE = 500;
    private volatile static ThreadPoolExecutor executor;

    private ThreadPoolUtil() {
    }

    // 获取单例的线程池对象
    public static ThreadPoolExecutor getInstance() {
        if (executor == null) {
            synchronized (ThreadPoolUtil.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE,// 核心线程数
                            MAX_QUEUE_SIZE, // 最大线程数
                            Integer.MAX_VALUE, // 闲置线程存活时间
                            TimeUnit.MILLISECONDS,// 时间单位
                            new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),// 线程队列
                            Executors.defaultThreadFactory()// 线程工厂
                    );
                }
            }
        }
        return executor;
    }
}
