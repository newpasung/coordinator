package com.scut.gof.coordinator.main.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/4.
 */
public class Project extends Model {
    //项目id
    @Column(name = "proid")
    public long proid;
    //项目名
    @Column(name = "proname")
    public String proname;
    //计划开始时间
    @Column(name = "planstarttime")
    public String planstarttime;
    //计划终止时间
    @Column(name = "planendtime")
    public String planendtime;
    //负责人
    @Column(name = "principalid")
    public long principalid;
    //描述
    @Column(name = "description")
    public String description;
    //项目类型
    @Column(name = "category")
    public String category;

    //项目归属
    @Column(name = "affiliation")
    public String affiliation;

    public Project() {
        super();
    }

    public static Project getProById(long proid) {
        return new Select().from(Project.class).where("proid=" + proid).executeSingle();
    }

    //插入一行数据或者更新一行数据
    public static void insertOrUpdate(JSONObject data) {
        Project project = null;
        try {
            long id = data.getLong("proid");
            project = getProById(id);
            if (project == null) {
                project = new Project();
                project.proid = id;
            }
            if (data.has("proname")) {
                project.proname = data.getString("proname");
            }
            if (data.has("planstarttime")) {
                project.planstarttime = data.getString("planstarttime");
            }
            if (data.has("planendtime")) {
                project.planendtime = data.getString("planendtime");
            }
            if (data.has("principalid")) {
                project.principalid = data.getLong("principalid");
            }
            if (data.has("description")) {
                project.description = data.getString("description");
            }
            if (data.has("category")) {
                project.category = data.getString("category");
            }
            if (data.has("affiliation")) {
                project.affiliation = data.getString("affiliation");
            }
            project.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
