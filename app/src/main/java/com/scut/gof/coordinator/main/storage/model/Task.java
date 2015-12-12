package com.scut.gof.coordinator.main.storage.model;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.scut.gof.coordinator.CooApplication;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.utils.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/5.
 */
@Table(name = "task")
public class Task extends Model {
    public static final int TASKSTATUS_ONPREPARE = 0;
    public static final int TASKSTATUS_ON = 1;
    public static final int TASKSTATUS_COMMIT = 2;
    public static final int TASKSTATUS_DELETED = 3;
    final int RELATION_NORELATION = -2;
    @Column(name = "tid")
    private long tid;
    @Column(name = "parentid")
    private long parentid;
    @Column(name = "tname")
    private String tname;
    @Column(name = "status")
    private int status;
    @Column(name = "planstarttime")
    private long planstarttime;
    @Column(name = "planendtime")
    private long planendtime;
    @Column(name = "content")
    private String content;
    @Column(name = "description")
    private String description;
    @Column(name = "priority")
    private int priority;
    @Column(name = "category")
    private String category;
    @Column(name = "mark")
    private int mark;
    @Column(name = "child_uncompletecount")
    private int child_uncompletecount;
    @Column(name = "childcount")
    private int childcount;
    @Column(name = "proid")
    private long proid;
    @Column(name = "tag")
    private String tag;
    @Column(name = "peopleneedcount")
    private int peopleneedcount;
    @Column(name = "peoplecount")
    private int peoplecount;
    @Column(name = "creator")
    private long creator;
    private int role;

    public Task() {
        content = "";
        description = "";
        category = "";
        tag = "";
        role = RELATION_NORELATION;
    }

    public static Task insertOrUpdate(JSONObject data) {
        Task task = null;
        try {
            long tid = data.getLong("tid");
            task = getTaskById(tid);
            if (task == null) {
                task = new Task();
                task.tid = tid;
            }
            if (data.has("proid")) {
                task.proid = data.getLong("proid");
            }
            if (data.has("tname")) {
                task.tname = data.getString("tname");
            }
            if (data.has("content")) {
                task.content = data.getString("content");
            }
            if (data.has("description")) {
                task.description = data.getString("description");
            }
            if (data.has("category")) {
                task.category = data.getString("category");
            }
            if (data.has("parentid")) {
                task.parentid = data.getLong("parentid");
            }
            if (data.has("planstarttime")) {
                task.planstarttime = data.getLong("planstarttime");
            }
            if (data.has("planendtime")) {
                task.planendtime = data.getLong("planendtime");
            }
            if (data.has("status")) {
                task.status = data.getInt("status");
            }
            if (data.has("priority")) {
                task.priority = data.getInt("priority");
            }
            if (data.has("mark")) {
                task.mark = data.getInt("mark");
            }
            if (data.has("child_uncompletecount")) {
                task.child_uncompletecount = data.getInt("child_uncompletecount");
            }
            if (data.has("tag")) {
                task.tag = data.getString("tag");
            }
            if (data.has("creator")) {
                JSONObject creator = data.getJSONObject("creator");
                User.insertOrUpdateSimply(creator);
                task.creator = creator.getLong("uid");
            }
            if (data.has("peopleneedcount")) {
                task.peopleneedcount = data.getInt("peopleneedcount");
            }
            if (data.has("peoplecount")) {
                task.peoplecount = data.getInt("peoplecount");
            }
            if (data.has("childcount")) {
                task.childcount = data.getInt("childcount");
            }
            task.save();
            if (data.has("taskrelation")) {
                Object relationdata = data.get("taskrelation");
                if (relationdata instanceof JSONArray) {
                    RelaTask.insertOrUpdate((JSONArray) relationdata);
                }
                if (relationdata instanceof JSONObject) {
                    RelaTask.insertOrUpdate((JSONObject) relationdata);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return task;
    }

    public static ArrayList<Task> insertOrUpdate(JSONArray data) {
        ArrayList<Task> list = new ArrayList<>();
        ActiveAndroid.beginTransaction();
        boolean isOk = true;
        JSONObject taskData;
        Task task;
        for (int i = 0; i < data.length(); i++) {
            try {
                taskData = data.getJSONObject(i);
                task = insertOrUpdate(taskData);
                if (task != null) {
                    list.add(task);
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

    public static void updateTaskStatus(JSONArray array) {
        ActiveAndroid.beginTransaction();
        boolean isOk = true;
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject data = array.getJSONObject(i);
                new Update(Task.class).set("status=" + data.getInt("status"))
                        .where("tid=" + data.getLong("tid")).execute();
            } catch (JSONException e) {
                e.printStackTrace();
                isOk = false;
            }
        }
        if (isOk) ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
    }

    public static Task getTaskById(long tid) {
        return new Select().from(Task.class).where("tid = " + tid).executeSingle();
    }

    public static List<Task> getTasksByStatus(long proid, int status) {
        return new Select().from(Task.class)
                .where("status=" + status + " and proid=" + proid)
                .orderBy("tid")
                .limit(10).execute();
    }

    public String getStrStarttime() {
        return ViewUtil.displayTime(planstarttime, 1);
    }

    public String getStrEndtime() {
        return ViewUtil.displayTime(planendtime, 1);
    }

    public String getStrPriority() {
        if (priority == 1) {
            return "紧急";
        } else if (priority == 2) {
            return "普通";
        } else {
            return "低级";
        }
    }

    public String getDisplayTimeDuration() {
        return ViewUtil.displayTime(planstarttime, 1) + " to " + ViewUtil.displayTime(planendtime, 1);
    }

    //在任务列表显示，任务的子任务状态或前后置任务状态
    public CharSequence getDisplayTaskstatus(Context context) {
        if (child_uncompletecount == 0) return "";
        String count = String.valueOf(child_uncompletecount);
        SpannableString spannableString = new SpannableString(count + " 个子任务未完成");
        spannableString.setSpan(
                new ForegroundColorSpan(ApiUtil.getColor(context, R.color.colorAccent))
                , 0, count.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    //在任务详情显示，子任务的状态
    public String getDisplayChildtaskStatus() {
        if (getChildcount() <= 0) {
            return "无子任务";
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("子任务完成数:");
            builder.append(getChildcount() - getChild_uncompletecount());
            builder.append("/");
            builder.append(getChildcount());
            return builder.toString();
        }
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public long getParentid() {
        return parentid;
    }

    public long getPlanendtime() {
        return planendtime;
    }

    public long getPlanstarttime() {
        return planstarttime;
    }

    public int getPriority() {
        return priority;
    }

    public int getStatus() {
        return status;
    }

    public long getTid() {
        return tid;
    }

    public String getTname() {
        return tname;
    }

    public boolean isMark() {
        return mark == 1;
    }

    public int getChild_uncompletecount() {
        return child_uncompletecount;
    }

    public String getDisplayPeopleCount() {
            StringBuilder builder = new StringBuilder();
            builder.append("人数:");
            builder.append(peoplecount);
            builder.append("/");
            builder.append(peopleneedcount);
            return builder.toString();
    }

    public long getCreator() {
        return creator;
    }

    public int getPeoplecount() {
        return peoplecount;
    }

    public int getPeopleneedcount() {
        return peopleneedcount;
    }

    public long getProid() {
        return proid;
    }

    public String getTag() {
        return tag;
    }

    public int getChildcount() {
        return childcount;
    }

    //传入我的uid
    public int getRole() {
        long uid = UserManager.getUserid(CooApplication.getInstance());
        RelaTask relaTask = new Select().from(RelaTask.class).where("tid=" + this.tid + " and uid =" + uid).executeSingle();
        if (relaTask != null) {
            role = relaTask.getRole();
        } else {
            role = RELATION_NORELATION;
        }
        return role;
    }

    public String getStrStatus() {
        if (status == 0) {
            return "准备中";
        } else if (status == 1) {
            return "进行中";
        } else if (status == 2) {
            return "已完成";
        } else {
            return "已失效";
        }
    }

}
