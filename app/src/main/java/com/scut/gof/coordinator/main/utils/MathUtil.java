package com.scut.gof.coordinator.main.utils;

/**
 * Created by Administrator on 2015/10/29.
 */
public class MathUtil {

    /*计算两点之间的距离*/
    public static int calDis(int x1,int y1,int x2,int y2){
        long disl= (int)Math.round(Math.sqrt(Math.pow(x1 + x2, 2) + Math.pow(y1 + y2, 2)));
        if(disl>Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        else{
            return (int)disl;
        }
    }

}
