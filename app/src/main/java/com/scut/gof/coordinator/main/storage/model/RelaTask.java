package com.scut.gof.coordinator.main.storage.model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/6.
 */
@Table(name = "relatask")
public class RelaTask extends Model {
    @Column(name = "tid")
    private long tid;
    @Column(name = "uid")
    private long uid;
    @Column(name = "role")
    private int role;

    public static void clearData() {
        new Delete().from(RelaTask.class).execute();
    }

    /**
     * 应该传入一个至少包含uid和tid的数据
     */
    public static RelaTask insertOrUpdate(JSONObject data, long uid) {
        RelaTask RelaTask = null;
        try {
            long tid = data.getLong("tid");
            RelaTask = getRelation(tid, uid);
            if (RelaTask == null) {
                RelaTask = new RelaTask();
                RelaTask.tid = tid;
                RelaTask.uid = uid;
            }
            if (data.has("role")) {
                RelaTask.role = data.getInt("role");
            }
            RelaTask.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RelaTask;
    }

    /**
     * 数据中包含uid
     */
    public static RelaTask insertOrUpdate(JSONObject data) {
        RelaTask RelaTask = null;
        try {
            long tid = data.getLong("tid");
            long uid = data.getLong("uid");
            RelaTask = getRelation(tid, uid);
            if (RelaTask == null) {
                RelaTask = new RelaTask();
                RelaTask.tid = tid;
                RelaTask.uid = uid;
            }
            if (data.has("role")) {
                RelaTask.role = data.getInt("role");
            }
            RelaTask.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RelaTask;
    }

    /**
     * 传入一个project数组数据和用户id
     */
    public static ArrayList<RelaTask> insertOrUpdate(JSONArray data, long uid) {
        ArrayList<RelaTask> list = new ArrayList<>();
        ActiveAndroid.beginTransaction();
        boolean isOk = true;
        JSONObject projectdata;
        RelaTask project;
        for (int i = 0; i < data.length(); i++) {
            try {
                projectdata = data.getJSONObject(i);
                project = insertOrUpdate(projectdata, uid);
                if (project != null) {
                    list.add(project);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                isOk = false;
            }
        }
        if (isOk) {
            ActiveAndroid.setTransactionSuccessful();
        }
        ActiveAndroid.endTransaction();
        return list;
    }

    /**
     * 数据中应包含uid
     */
    public static ArrayList<RelaTask> insertOrUpdate(JSONArray data) {
        ArrayList<RelaTask> list = new ArrayList<>();
        ActiveAndroid.beginTransaction();
        boolean isOk = true;
        JSONObject projectdata;
        RelaTask project;
        for (int i = 0; i < data.length(); i++) {
            try {
                projectdata = data.getJSONObject(i);
                project = insertOrUpdate(projectdata);
                if (project != null) {
                    list.add(project);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                isOk = false;
            }
        }
        if (isOk) {
            ActiveAndroid.setTransactionSuccessful();
        }
        ActiveAndroid.endTransaction();
        return list;
    }

    /**
     * 传入proid 和uid ，返回一个关系对象
     */
    public static RelaTask getRelation(long tid, long uid) {
        return new Select().from(RelaTask.class).where("tid=" + tid + " and uid=" + uid).executeSingle();
    }

    public long getProid() {
        return tid;
    }

    public int getRole() {
        return role;
    }

    public long getUid() {
        return uid;
    }

}
