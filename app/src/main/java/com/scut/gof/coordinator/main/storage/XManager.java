package com.scut.gof.coordinator.main.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Administrator on 2015/11/1.
 * 用于管理系统生成的xml文件，多为一些基础的配置文件
 */
public class XManager {
    //关于一些与特定用户无关的资料
    public static final String FILENAME_SYSTEM = "systemconfig";
    //用于特定用户的配置信息
    public static final String FILENAME_USER = "userconfig";
    public static SharedPreferences sysPref;
    public static SharedPreferences userPref;

    //一些用户无关的参数
    public static String PARAM_OPENED = "hasopened";//是否已经打开过app，用于判断是否显示引导页
    public static String PARAM_LOGINED = "haslogined";//是否已经登录
    //一些用户相关的参数
    public static String PARAM_TOKEN = "token";//用户token
    public static String PARAM_UID = "uid";//用户uid
    public static String PARAM_RECENTPROJECT = "recentprojectsid";//最近浏览过的5个自己的项目
    public static synchronized SharedPreferences getSystemManager(Context context){
        if(sysPref==null){
            sysPref=context.getSharedPreferences(FILENAME_SYSTEM, Context.MODE_PRIVATE);
        }
        return sysPref;
    }

    public static synchronized SharedPreferences getUserManager(Context context){
        if(userPref==null){
            userPref=context.getSharedPreferences(FILENAME_USER,Context.MODE_PRIVATE);
        }
        return userPref;
    }

    //获取是否已登录
    public static boolean isLogined(Context context){
        return getSystemManager(context).getBoolean(PARAM_LOGINED,false);
    }

    /**
    *设置是否已登录
    *@param status  登录状态是或否
    */
    public static void setLoginStatus(Context context, boolean status){
        SharedPreferences.Editor editor =getSystemManager(context).edit();
        editor.putBoolean(PARAM_LOGINED,status);
        editor.apply();
    }

    //获取是否已打开过app
    public static boolean hasOpened(Context context){
        return getSystemManager(context).getBoolean(PARAM_OPENED,false);
    }

    /**
     * 设置是否已经打开过app
     * @param status  是否打开
     */
    public static void setOpenedStatus(Context context, boolean status){
        SharedPreferences.Editor editor =getSystemManager(context).edit();
        editor.putBoolean(PARAM_OPENED, status);
        editor.apply();
    }

    //获取用户token，如果没有则返回空字符串
    public static String getToken(Context context){
        return getUserManager(context).getString(PARAM_TOKEN, null);
    }

    /**
     * 设置用户token
     * @param token 要设置成的token
     */
    public static void setToken(Context context, String token) {
        SharedPreferences.Editor editor =getUserManager(context).edit();
        editor.putString(PARAM_TOKEN, token);
        editor.apply();
    }

    //获取用户uid，如果没有则返回0
    public static long getUid(Context context) {
        return getUserManager(context).getLong(PARAM_UID, 0);
    }

    /**
     * 设置用户token
     * @param uid 要设置成的uid
     */
    public static void setUid(Context context, long uid) {
        SharedPreferences.Editor editor =getUserManager(context).edit();
        editor.putLong(PARAM_UID, uid);
        editor.apply();
    }

    public static void setMyRecentProject(Context context, String proid) {
        String pastData = getMyRecentProid(context);
        String[] str_proids = pastData.split(";");
        StringBuilder builder = new StringBuilder();
        if (str_proids == null) {
            //一个旧数据都木有
            builder.append(proid);
            builder.append(";");
        } else {
            String[] new_str_proids = new String[str_proids.length <= 5 ? str_proids.length : 5];
            if (pastData.contains(proid)) {
                //旧数据中有，就把他放到首位
                new_str_proids[0] = proid;
                for (int i = 0, j = 1; i < new_str_proids.length; i++) {
                    if (!str_proids[i].equals(proid)) {
                        new_str_proids[j++] = str_proids[i];
                    } else {
                        continue;
                    }
                }
            } else {
                //旧数据中木有这个项目的id
                //会舍弃掉最后一个proid数据
                for (int i = new_str_proids.length - 1; i >= 1; i--) {
                    new_str_proids[i] = new_str_proids[i - 1];
                }
                new_str_proids[0] = proid;
            }
            for (int i = 0; i < new_str_proids.length; i++) {
                builder.append(new_str_proids[i]);
                builder.append(";");
            }
        }
        SharedPreferences.Editor editor = getUserManager(context).edit();
        editor.putString(PARAM_RECENTPROJECT, builder.toString());
        editor.commit();
        Log.i("setMyRecentProject", builder.toString());
    }

    public static String getMyRecentProid(Context context) {
        return getUserManager(context).getString(PARAM_RECENTPROJECT, "");
    }

    /**
     * 删除所有xml文件数据
     */
    public static void clearData(Context context) {
        getUserManager(context).edit().clear().commit();
        getSystemManager(context).edit().clear().commit();
    }

}
