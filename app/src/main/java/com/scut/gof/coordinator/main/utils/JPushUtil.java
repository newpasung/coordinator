package com.scut.gof.coordinator.main.utils;

import android.content.Context;
import android.util.Log;

import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.storage.model.Project;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by gjz on 12/24/15.
 * 新开一个进程来设置JPush的alias和tags
 */
public class JPushUtil extends Thread implements TagAliasCallback {
    private Context mContext;

    public JPushUtil(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        setAliasAndTags();
    }

    private void setAliasAndTags() {
        if (JPushInterface.isPushStopped(mContext)) {
            JPushInterface.resumePush(mContext);
        }
        long uid = UserManager.getUserid(mContext);
        List<Project> projects = UserManager.getMyProjects(mContext);

        Set<String> proids = new HashSet<>();
        for (Project project:projects) {
            if (project.getStatus() == 1) {
                proids.add("proid_"+project.getProid());
            }
        }

        JPushInterface.setAliasAndTags(mContext, "uid_"+uid, proids, this);
    }

    @Override
    public void gotResult(int code, String alias, Set<String> tags) {
        if (code == 0) {
            String registrationId = JPushInterface.getRegistrationID(mContext);
            Log.e("[JPush]", "Set alias:" + alias + " and tags " + tags + "success" + "registrationId" + registrationId);
            return;
        } else {
            Log.e("[JPush]", "Set alias:" + alias + " and tags " + tags + "fail");
            setAliasAndTags();
        }
    }
}
