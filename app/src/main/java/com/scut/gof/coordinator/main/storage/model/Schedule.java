package com.scut.gof.coordinator.main.storage.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/12/22.
 */
@Table(name = "schedule")
public class Schedule extends Model {
    public static final int STATUS_ALIVE = 1;
    public static final int STATUS_DELETED = 3;
    public static final int ALERTTIME_NOALERT = 0;
    @Column(name = "netsid")
    private long netsid;
    @Column(name = "content")
    private String content;
    @Column(name = "alerttime")
    private long alerttime;
    @Column(name = "starttime")
    private long starttime;
    @Column(name = "endtime")
    private long endtime;
    @Column(name = "modified_time")
    private long modified_time;
    @Column(name = "isnetsync")//是否已经网络同步了
    private int isnetsync;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private int status;

    public Schedule() {
        isnetsync = 0;
        status = STATUS_ALIVE;
        modified_time = System.currentTimeMillis();
        alerttime = ALERTTIME_NOALERT;
    }

    public static void clearData() {
        new Delete().from(Schedule.class).execute();
    }

    public static Schedule getScheduleByNetid(long netsid) {
        return new Select().from(Schedule.class).where("status <> " + STATUS_DELETED + " and netsid =" + netsid).executeSingle();
    }

    public static Schedule getSchedleByLocalid(long id) {
        return new Select().from(Schedule.class).where("status<> " + STATUS_DELETED + " and id =" + id).executeSingle();
    }

    public static Schedule insertOrUpdate(JSONObject data) {
        Schedule schedule = null;
        try {
            long netsid = data.getLong("sid");
            schedule = getScheduleByNetid(netsid);
            if (schedule == null) {
                schedule = new Schedule();
                schedule.netsid = netsid;
            } else {
                //网络返回的数据显示它被删除了，就把它删了吧
                if (data.has("status") && data.getInt("status") == STATUS_DELETED) {
                    schedule.delete();
                    return null;
                }
            }
            if (schedule.status == STATUS_DELETED && data.has("status") && data.getInt("status") != STATUS_DELETED) {
                //我们发现手机数据是把
                //他删了,但是网络上面没有删，就联网删一个
                //TODO 联网删除它
                return null;
            }
            if (data.has("modified_time")) {
                schedule.modified_time = data.getLong("modified_time");
            }
            if (data.has("alerttime")) {
                schedule.alerttime = data.getLong("alerttime");
            }
            if (data.has("starttime")) {
                schedule.starttime = data.getLong("starttime");
            }
            if (data.has("endtime")) {
                schedule.endtime = data.getLong("endtime");
            }
            if (data.has("content")) {
                schedule.content = data.getString("content");
            }
            if (data.has("description")) {
                schedule.description = data.getString("description");
            }
            schedule.isnetsync = 1;
            schedule.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    public static List<Schedule> insertOrUpdate(JSONArray array) {
        List<Schedule> schedules = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                Schedule schedule = insertOrUpdate(array.getJSONObject(i));
                if (schedule != null)
                    schedules.add(schedule);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    public static Schedule addLocalSchedule(String content, long starttime, long endtime, long alerttime, String description) {
        Schedule schedule = new Schedule();
        schedule.content = content;
        schedule.starttime = starttime;
        schedule.endtime = endtime;
        schedule.alerttime = alerttime;
        schedule.save();
        return schedule;
    }

    public static List<Schedule> get10Schedules() {
        Log.i("SCHEDULE_SQL", new Select().from(Schedule.class)
                .orderBy("starttime DESC")
                .limit(10).toSql());
        return new Select().from(Schedule.class)
                .orderBy("starttime DESC")
                .limit(10)
                .execute();
    }

    public long getAlerttime() {
        return alerttime;
    }

    public void setAlerttime(long time) {
        this.starttime = time;
        this.modified_time = System.currentTimeMillis();
        this.save();
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public long getEndtime() {
        return endtime;
    }

    public int getIsnetsync() {
        return isnetsync;
    }

    public long getModified_time() {
        return modified_time;
    }

    public long getNetsid() {
        return netsid;
    }

    public long getStarttime() {
        return starttime;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplayStarttime() {
        GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance(Locale.CHINA);
        GregorianCalendar starttimeCalendar = new GregorianCalendar();
        starttimeCalendar.setTimeInMillis(starttime);
        StringBuilder builder = new StringBuilder();
        builder.append("计划开始于: ");
        if (starttimeCalendar.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)) {
            builder.append(starttimeCalendar.get(Calendar.YEAR));
            builder.append("年");
        }
        if (starttimeCalendar.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
            builder.append(starttimeCalendar.get(Calendar.MONTH) + 1);
            builder.append("月");
        }
        if (starttimeCalendar.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH)) {
            builder.append(starttimeCalendar.get(Calendar.DAY_OF_MONTH));
            builder.append("日");
        } else {
            builder.append("今天");
        }
        builder.append(starttimeCalendar.get(Calendar.HOUR_OF_DAY));
        builder.append("时");
        builder.append(starttimeCalendar.get(Calendar.MINUTE));
        builder.append("分");
        return builder.toString();
    }

    public void setStatusDeleted() {
        this.status = STATUS_DELETED;
        this.modified_time = System.currentTimeMillis();
        this.save();
    }

}
