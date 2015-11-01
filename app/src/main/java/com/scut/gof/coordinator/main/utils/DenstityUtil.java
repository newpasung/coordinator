package com.scut.gof.coordinator.main.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2015/10/29.
 * 用来获取尺寸
 */
public class DenstityUtil {

    public static int getScreenWidth(Context context){
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics =new DisplayMetrics();
        Display display=wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context){
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics =new DisplayMetrics();
        Display display=wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getWindowWidth(Activity activity){
        return getScreenWidth(activity);
    }

    public static int getWindowHeight(Activity activity){
        return getScreenHeight(activity) -getStatusHeight(activity);
    }

    public static int getStatusHeight(Activity activity){
        Window window =activity.getWindow();
        View view =window.getDecorView();
        Rect rect=new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.top;
    }

    //单位转换
    public static int dp2px(float dp,Resources resources){
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
            return (int) px;
    }

}
