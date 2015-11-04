package com.scut.gof.coordinator.main.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Administrator on 2015/11/4.
 * 用于本地组件间通信
 */
public class LocalBrCast {
    //broadcast的消息参数
    public static final String PARAM_REFRESHADAPTER = "localbroadcast_refresh_adapter";

    //向系统注册一个接受广播的接收器
    public static void register(Context context, String actionKey, BroadcastReceiver receiver) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(actionKey);
        manager.registerReceiver(receiver, filter);
    }

    //反注册接收器！！一定要反注册
    public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context)
                .unregisterReceiver(receiver);
    }

    //发送广播，不需要注册
    public static void sendBroadcast(Context context, String actionKey) {
        Intent intent = new Intent();
        intent.setAction(actionKey);
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(intent);
    }

}
