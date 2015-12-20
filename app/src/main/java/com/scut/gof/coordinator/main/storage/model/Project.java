package com.scut.gof.coordinator.main.storage.model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.scut.gof.coordinator.CooApplication;
import com.scut.gof.coordinator.main.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/4.
 */
@Table(name = "project")
public class Project extends Model {
    @Column(name = "thumbnaillogourl")
    String thumbnaillogourl;
    //项目id
    @Column(name = "proid")
    private long proid;
    //项目名
    @Column(name = "proname")
    private String proname;
    //计划开始时间
    @Column(name = "planstarttime")
    private long planstarttime;
    //计划终止时间
    @Column(name = "planendtime")
    private long planendtime;
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
    @Column(name = "status")
    private int status;
    @Column(name = "prologo")
    private String prologo;
    @Column(name = "mark")
    private int mark;
    @Column(name = "role")
    private int role;
    public Project() {
        super();
    }

    public static final int ROLE_CREATOR=1;
    /**
     * 清空所有数据的哦
     */
    public static void clearData() {
        new Delete().from(Project.class).execute();
    }

    public static Project getProById(long proid) {
        return new Select().from(Project.class).where("proid=" + proid).executeSingle();
    }

    /**
     * 插入一行数据或者更新一行数据
     *
     * @return 是否插入成功
     */
    public static Project insertOrUpdate(JSONObject data) throws NullPointerException {
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
                project.planstarttime = data.getLong("planstarttime");
            }
            if (data.has("planendtime")) {
                project.planendtime = data.getLong("planendtime");
            }
            if (data.has("principal")) {
                JSONObject principal = data.getJSONObject("principal");
                project.principalid = principal.getLong("uid");
                User.insertOrUpdateSimply(principal);
            }
            if (data.has("principalid")) {
                project.principalid = data.getLong("principalid");
                RelaProject.insertOrUpdate(id,project.principalid,ROLE_CREATOR);
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
            if (data.has("prologo")) {
                project.prologo = data.getString("prologo");
            }
            if (data.has("thumbnaillogourl")) {
                project.thumbnaillogourl = data.getString("thumbnaillogourl");
            }
            if (data.has("status")) {
                project.status = data.getInt("status");
            }
            if (data.has("mark")) {
                project.mark = data.getInt("mark");
            }
            if (data.has("role")){
                project.role = data.getInt("role");
                RelaProject.insertOrUpdate(id,UserManager.getUserid(CooApplication.getInstance()),project.role);
            }
            project.save();
            return project;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Project> insertOrUpdate(JSONArray data) {
        ArrayList<Project> list = new ArrayList<>();
        ActiveAndroid.beginTransaction();
        boolean isOk = true;
        JSONObject projectdata;
        Project project;
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

    public static void updateLogo(JSONObject data) {
        try {
            Project project = getProById(data.getLong("proid"));
            if (project == null) return;
            if (data.has("prologo")) {
                project.prologo = data.getString("prologo");
            }
            if (data.has("thumbnaillogourl")) {
                project.thumbnaillogourl = data.getString("thumbnaillogourl");
            }
            project.save();
        } catch (JSONException e) {
            e.printStackTrace();
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
        Date date = new Date(this.planstarttime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public String getPlanstarttime() {
        Date date = new Date(this.planstarttime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
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

    public String getPrologo() {
        return prologo;
    }

    public String getThumbnailLogo() {
        return thumbnaillogourl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
