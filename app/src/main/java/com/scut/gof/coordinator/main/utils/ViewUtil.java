package com.scut.gof.coordinator.main.utils;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/10/29.
 */
public class ViewUtil {

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

    /**
     * 以textview，button，edittext为参数，判断是否所有的文字都不回空白
     */
    public static boolean isAllTextEmpty(View... views) {
        boolean isEmpty = false;
        for (int i = 0; i < views.length; i++) {
            //button edittext等继承自textview
            if (views[i] instanceof TextView) {
                if (TextUtils.isEmpty(((TextView) views[i]).getText().toString())) {
                    isEmpty = true;
                    break;
                }
            }
        }
        return isEmpty;
    }

}
