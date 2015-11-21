package com.scut.gof.coordinator.main;

import android.content.Context;

import com.scut.gof.coordinator.main.storage.XManager;
import com.scut.gof.coordinator.main.storage.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/20.
 */
public class UserManager {

    public static User getLocalUser(Context context) {
        return User.getUserById(getUserid(context));
    }

    public static String getUserAvatar(Context context) {
        return getLocalUser(context).getAvatar();
    }

    public static String getUserThumbAvatar(Context context) {
        return getLocalUser(context).getThumbnailavatar();
    }

    public static String getUserName(Context context) {
        return getLocalUser(context).getName();
    }

    public static long getUserid(Context context) {
        return XManager.getUid(context);
    }

    public static String getToken(Context context) {
        return XManager.getToken(context);
    }

    /**
     * 对使用者的数据进行初始化，一般出现在登录处
     *
     * @param userData 包含用户数据“user”字段的jsonobject
     */
    public static void iniUser(Context context, JSONObject userData) throws JSONException {
        String token = userData.getString("token");
        long uid = userData.getLong("uid");
        XManager.setToken(context, token);
        XManager.setUid(context, uid);
        User.insertOrUpdate(userData);
    }
}
