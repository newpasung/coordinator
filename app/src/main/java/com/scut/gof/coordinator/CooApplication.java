package com.scut.gof.coordinator;

import android.app.Application;
import android.util.Log;

import com.activeandroid.ActiveAndroid;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2015/11/8.
 */
public class CooApplication extends Application implements Thread.UncaughtExceptionHandler {

    public static Application application;
    public static boolean DEBUGMODE = true;

    public static Application getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ActiveAndroid.initialize(this);
        JPushInterface.init(this);
        JPushInterface.setDebugMode(DEBUGMODE);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("uncaughtException", ex.getCause().toString());
    }
}
