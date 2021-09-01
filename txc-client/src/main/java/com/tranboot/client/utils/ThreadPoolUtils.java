package com.tranboot.client.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadPoolUtils {
    private static ExecutorService pool = Executors.newFixedThreadPool(1, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            return new Thread(r, "dbsync-thread");
        }
    });

    public ThreadPoolUtils() {
    }

    public static void submit(Runnable task) {
        pool.execute(task);
    }
}

