package com.scut.gof.coordinator.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/12/14.
 */
public class JPushTestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "接受到推送:" + intent.toString(), Toast.LENGTH_SHORT).show();
    }
}
