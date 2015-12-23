package com.scut.gof.coordinator.libs.nereo.multi_image_selector.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 时间处理工具
 * Created by Nereo on 2015/4/8.
 */
public class TimeUtils {

    public static String timeFormat(long timeMillis, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time){
        return timeFormat(time, "yyyy-MM-dd");
    }

    public static String formatPhotoDate(String path){
        File file = new File(path);
        if(file.exists()){
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }

    /**
     * 现在的年月日时分的显示
     */
    public static String displayCurDetailTime() {
        return displayDetailTime(System.currentTimeMillis());
    }

    public static String displayDetailTime(long timeInMillis) {
        StringBuilder builder = new StringBuilder();
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(timeInMillis);
        builder.append(calendar.get(Calendar.YEAR));
        builder.append("年");
        builder.append(calendar.get(Calendar.MONTH) + 1);
        builder.append("月");
        builder.append(calendar.get(Calendar.DAY_OF_MONTH));
        builder.append("日");
        builder.append("  ");
        builder.append(calendar.get(Calendar.HOUR_OF_DAY));
        builder.append("时");
        builder.append(calendar.get(Calendar.MINUTE));
        builder.append("分");
        return builder.toString();
    }
    
}
