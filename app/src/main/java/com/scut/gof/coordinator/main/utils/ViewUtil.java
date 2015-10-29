package com.scut.gof.coordinator.main.utils;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by Administrator on 2015/10/29.
 */
public class ViewUtil {

    public static void setBackground(View view ,Drawable drawable){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN){
            view.setBackgroundDrawable(drawable);
        }
        else{
            view.setBackground(drawable);
        }
    }

    /*返回左上角全局x坐标*/
    public static int getScreenX(View view){
        Rect rect =new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.left;
    }

    /*返回左上角全局y坐标*/
    public static int getScreenY(View view){
        Rect rect =new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.top;
    }

}
