package com.scut.gof.coordinator.main.storage.model;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.scut.gof.coordinator.main.storage.XManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
@Table(name = "project")
public class Project extends Model {
    //项目id
    @Column(name = "proid")
    private long proid;
    //项目名
    @Column(name = "proname")
    private String proname;
    //计划开始时间
    @Column(name = "planstarttime")
    private String planstarttime;
    //计划终止时间
    @Column(name = "planendtime")
    private String planendtime;
    //负责人
    @Column(name = "principalid")
    private long principalid;
    //描述
    @Column(name = "description")
    private String description;
    //项目类型
    @Column(name = "category")
    private String category;

    //项目归属
    @Column(name = "affiliation")
    private String affiliation;

    public Project() {
        super();
    }

    public static Project getProById(long proid) {
        return new Select().from(Project.class).where("proid=" + proid).executeSingle();
    }

    /**
     * 插入一行数据或者更新一行数据
     *
     * @return 是否插入成功
     */
    public static boolean insertOrUpdate(JSONObject data) {
        Project project;
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
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Project> getUsersProjects(Context context) {
        List<Project> projects = new ArrayList<>();
        long[] id = XManager.getUsersProjectId(context);
        if (id != null) {
            for (int i = 0; i < id.length; i++) {
                projects.add(getProById(id[i]));
            }
        }
        return projects;
    }

    public static void joinProject(Context context, JSONObject data) {
        if (insertOrUpdate(data)) {
            try {
                long old_ids[] = XManager.getUsersProjectId(context);
                if (old_ids == null) return;
                long new_ids[] = new long[old_ids.length];
                long id = data.getLong("proid");
                new_ids[new_ids.length - 1] = id;
                XManager.saveUsersProjectId(context, new_ids);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPlanendtime() {
        return planendtime;
    }

    public String getPlanstarttime() {
        return planstarttime;
    }

    public long getPrincipalid() {
        return principalid;
    }

    public long getProid() {
        return proid;
    }

    public String getProname() {
        return proname;
    }
}
