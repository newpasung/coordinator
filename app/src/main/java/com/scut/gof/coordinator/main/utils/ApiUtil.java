package com.scut.gof.coordinator.main.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by Administrator on 2015/10/31.
 * 所有提示过期的函数都应该在这里做选择
 */
public class ApiUtil {
    public static void setBackground(View view ,Drawable drawable){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN){
            view.setBackgroundDrawable(drawable);
        }
        else{
            view.setBackground(drawable);
        }
    }

    public static Drawable getDrawable(Resources resources, int resid) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            return resources.getDrawable(resid);
        } else {
            return resources.getDrawable(resid, null);
        }
    }

    public static int getColor(Context context,int resId){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return context.getResources().getColor(resId);
        }else{
            return context.getResources().getColor(resId,null);
        }
    }

}
