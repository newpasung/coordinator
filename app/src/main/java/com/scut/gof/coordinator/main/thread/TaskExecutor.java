package com.scut.gof.coordinator.main.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/10/1.
 */
public class TaskExecutor {
    private static ExecutorService executor;
    private static ScheduledExecutorService scheduledExecutor;
    private static Handler mainHandler;

    public static void execute(Runnable runnable) {
        if (executor == null) executor = Executors.newFixedThreadPool(3);
        executor.execute(runnable);
    }

    public static void schedule(Runnable runnable, long delay) {
        if (scheduledExecutor == null) scheduledExecutor = Executors.newScheduledThreadPool(2);
        scheduledExecutor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public static void executeUI(Runnable runnable) {
        if (mainHandler == null) mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(runnable);
    }

}
