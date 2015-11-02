package com.scut.gof.coordinator.main.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/11/1.
 * 用于管理系统生成的xml文件，多为一些基础的配置文件
 */
public class XManager {
    //关于一些与特定用户无关的资料
    public static final String FILENAME_SYSTEM = "systemconfig";
    public static SharedPreferences sysPref;

    //用于特定用户的配置信息
    public static final String FILENAME_USER = "userconfig";
    public static SharedPreferences userPref;

    //下面是一些参数
    public static String PARAM_OPENED = "hasopened";//是否已经打开过app，用于判断是否显示引导页
    public static String PARAM_LOGINED = "haslogined";//是否已经登录
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
    public static void setLoginStatus(Context context,boolean status){
        SharedPreferences.Editor editor =getSystemManager(context).edit();
        editor.putBoolean(PARAM_LOGINED,status);
        editor.apply();
    }

    //获取是否已打开过app
    public static boolean hasOpened(Context context){
        return getSystemManager(context).getBoolean(PARAM_OPENED,false);
    }

}
