package com.scut.gof.coordinator.main;

import android.content.Context;

import com.scut.gof.coordinator.main.storage.XManager;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.storage.model.RelaProject;
import com.scut.gof.coordinator.main.storage.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/20.
 */
public class UserManager {

    public static User getLocalUser(Context context) {
        return User.getUserById(getUserid(context));
    }

    public static String getUserAvatar(Context context) {
        return getLocalUser(context).getAvatar();
    }

    public static String getUserThumbAvatar(Context context) {
        return getLocalUser(context).getThumbnailavatar();
    }

    public static String getUserName(Context context) {
        return getLocalUser(context).getName();
    }

    public static long getUserid(Context context) {
        return XManager.getUid(context);
    }

    public static String getToken(Context context) {
        return XManager.getToken(context);
    }

    /**
     * 对使用者的数据进行初始化，一般出现在登录处
     *
     * @param data 登录时返回的所有东西
     */
    public static void iniUserData(Context context, JSONObject data) throws JSONException {
        JSONObject userData = data.getJSONObject("user");
        String token = userData.getString("token");
        long uid = userData.getLong("uid");
        XManager.setToken(context, token);
        XManager.setUid(context, uid);
        User.insertOrUpdate(userData);
        JSONArray projectdata = data.getJSONArray("projects");
        //添加进project数据
        Project.insertOrUpdate(projectdata);
        //把用户增加到项目关系中
        RelaProject.insertOrUpdate(projectdata, uid);
    }

    public static ArrayList<Project> getMyProjects(Context context) {
        ArrayList<Project> projects = new ArrayList<>();
        List<RelaProject> relaProjects = RelaProject.getRelaProjects(XManager.getUid(context));
        Project temp;
        //--不会sql排序，所以。。。
        for (int i = relaProjects.size() - 1; i >= 0; i--) {
            temp = Project.getProById(relaProjects.get(i).getProid());
            projects.add(temp);
        }
        return projects;
    }

}
