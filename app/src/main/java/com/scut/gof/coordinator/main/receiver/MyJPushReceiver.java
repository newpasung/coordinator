package com.scut.gof.coordinator.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.activity.HomeActivity;
import com.scut.gof.coordinator.main.activity.WelcomeActivity;
import com.scut.gof.coordinator.main.storage.XManager;
import com.scut.gof.coordinator.main.storage.model.MDAnnouncement;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2015/12/14.
 */
public class MyJPushReceiver extends BroadcastReceiver {
    //标识收到的是什么类型的推送
    private static String FLAG_ANNOUNCEMENT = "announcement";//收到公告

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("[JPush Reveiver]", "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            String message = bundle.getString(JPushInterface.EXTRA_ALERT);
            int messageId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d("[JPush Reveiver]", "收到了通知，ID: " + messageId);
            Log.d("[JPush Reveiver]", "收到了通知，内容：" + message);
            String extra_info = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (!TextUtils.isEmpty(extra_info)) {
                try {
                    JSONObject extrasJSONObj = new JSONObject(extra_info);
                    String type = extrasJSONObj.getString("type");
                    if (type.equals(FLAG_ANNOUNCEMENT)) {
                        handleAnnouncement(messageId, message, extrasJSONObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d("[JPush Reveiver]", "用户点击打开了通知");

            Intent i = new Intent();
            if (XManager.isLogined(context)) {
                i.setClass(context, HomeActivity.class);
            } else {
                i.setClass(context, WelcomeActivity.class);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);

        }
    }

    private void handleAnnouncement(int messageId, String message, JSONObject extrasJSONObj) {
        try {
            int time = extrasJSONObj.getInt("time");
            JSONObject projectJSONObj = extrasJSONObj.getJSONObject("project");
            String projectName = projectJSONObj.getString("proname");
            int proid = projectJSONObj.getInt("proid");
            String logoUrl = projectJSONObj.getString("thumbnaillogourl");
            MDAnnouncement announcement = new MDAnnouncement();
            announcement.time = time;
            announcement.id = messageId;
            announcement.content = message;
            announcement.proid = proid;
            announcement.proneme = projectName;
            announcement.logourl = logoUrl;
            announcement.isread = false;
            announcement.save();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
