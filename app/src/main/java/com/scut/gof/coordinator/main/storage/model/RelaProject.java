package com.scut.gof.coordinator.main.storage.model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/21.
 */
@Table(name = "relaproject")
public class RelaProject extends Model {
    @Column(name = "proid")
    private long proid;
    @Column(name = "uid")
    private long uid;
    @Column(name = "role")
    private int role;
    @Column(name = "mark")
    private int mark;
    /**
     * 应该传入一个至少包含proid的数据,数据中不含uid需要传入uid
     */
    public static RelaProject insertOrUpdate(JSONObject data) {
        RelaProject relaProject = null;
        try {
            long proid = data.getLong("proid");
            long uid = data.getLong("uid");
            relaProject = getRelation(proid, uid);
            if (relaProject == null) {
                relaProject = new RelaProject();
            }
            relaProject.proid = proid;
            relaProject.uid = uid;
            if (data.has("role")) {
                relaProject.role = data.getInt("role");
            }
            if (data.has("mark")) {
                relaProject.mark = data.getInt("mark");
            }
            relaProject.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return relaProject;
    }

    /**
     * 应该传入一个至少包含uid和proid的数据
     */
    public static RelaProject insertOrUpdate(JSONObject data, long uid) {
        RelaProject relaProject = null;
        try {
            long proid = data.getLong("proid");
            relaProject = getRelation(proid, uid);
            if (relaProject == null) {
                relaProject = new RelaProject();
                relaProject.proid = proid;
                relaProject.uid = uid;
            }
            if (data.has("role")) {
                relaProject.role = data.getInt("role");
            }
            relaProject.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return relaProject;
    }

    /**
     * 传入一个project数组数据和用户id
     */
    public static ArrayList<RelaProject> insertOrUpdate(JSONArray data, long uid) {
        ArrayList<RelaProject> list = new ArrayList<>();
        ActiveAndroid.beginTransaction();
        boolean isOk = true;
        JSONObject projectdata;
        RelaProject project;
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
     * 传入一个project数组数据
     */
    public static ArrayList<RelaProject> insertOrUpdate(JSONArray data) {
        ArrayList<RelaProject> list = new ArrayList<>();
        ActiveAndroid.beginTransaction();
        boolean isOk = true;
        JSONObject projectdata;
        RelaProject project;
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
    public static RelaProject getRelation(long proid, long uid) {
        return new Select().from(RelaProject.class).where("proid=" + proid + " and uid=" + uid).executeSingle();
    }

    /**
     * 获取某个人所在的所有项目
     */
    public static List<RelaProject> getRelaProjects(long uid) {
        return new Select().from(RelaProject.class).where("uid=" + uid).orderBy("proid").execute();
    }

    public static List<RelaProject> getRelaProjects(long uid, int maxcount) {
        return new Select().from(RelaProject.class).where("uid=" + uid)
                .orderBy("proid").limit(maxcount).execute();
    }

    public int getMark() {
        return mark;
    }

    public long getProid() {
        return proid;
    }

    public int getRole() {
        return role;
    }

    public long getUid() {
        return uid;
    }


}
