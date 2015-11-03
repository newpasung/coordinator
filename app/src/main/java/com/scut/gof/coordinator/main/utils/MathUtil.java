package com.scut.gof.coordinator.main.utils;

/**
 * Created by Administrator on 2015/10/29.
 * 一些常用的数学计算
 */
public class MathUtil {

    /*计算两点之间的距离*/
    public static long calDis(long x1,long y1,long x2,long y2){
        return Math.round(Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
    }

}
