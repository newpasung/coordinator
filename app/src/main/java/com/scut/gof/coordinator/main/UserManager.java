package com.scut.gof.coordinator.main;

import android.content.Context;
import android.text.TextUtils;

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

    public static User mUser;

    /**
     * 这个会返回一个本地账号的user，当然如果你发现数据不是新的，调用一下notifyDataChanged
     */
    public static User getLocalUser(Context context) {
        if (mUser == null) {
            mUser = User.getUserById(getUserid(context));
        }
        return mUser;
    }

    /**
     * 当本地更新user之后需要提示这里要变化，不然会使用旧数据，当然，getLocalUser懒加载不会有问题
     */
    public static void notifyDataChanged() {
        mUser=null;
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

    public static String getSignature(Context context) {
        return getLocalUser(context).getSignature();
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
        //--不会sql排序，所以。。。把最新的放前面
        for (int i = relaProjects.size() - 1; i >= 0; i--) {
            temp = Project.getProById(relaProjects.get(i).getProid());
            projects.add(temp);
        }
        return projects;
    }

    public static ArrayList<Project> getRecentProject(Context context) {
        String str_ids = XManager.getMyRecentProid(context);
        ArrayList<Project> resultList = new ArrayList<>();
        ArrayList<Project> relaProjects = getMyProjects(context);
        ArrayList<Project> unaliveProject = new ArrayList<>();//记录不可用的project
        if (TextUtils.isEmpty(str_ids)) {
            //毛都没有，就把所有都有用的发过去吧
            for (Project project : relaProjects) {
                if (project.getStatus() == 1) {
                    resultList.add(project);
                } else {
                    unaliveProject.add(project);
                }
                if (resultList.size() == 5) {
                    break;
                }
            }
        } else {
            String[] str_proids = str_ids.split(";");
            for (int i = 0; i < str_proids.length; i++) {
                Project project = Project.getProById(Long.parseLong(str_proids[i]));
                //存储的数据中有木有项目是已经不可用的了？
                if (project.getStatus() == 1) {
                    resultList.add(project);
                } else {
                    removeRecentProid(context, project.getProid());
                    unaliveProject.add(project);
                }
            }
            int index = 0;
            while (resultList.size() <= 5 && index < relaProjects.size()) {
                Project project = Project.getProById(relaProjects.get(index).getProid());
                if (!str_ids.contains(String.valueOf(project.getProid()))) {
                    //不在缓存在xml的proid里面
                    if (project.getStatus() == 1) {
                        resultList.add(project);
                    } else {
                        unaliveProject.add(project);
                    }
                }
                index++;
            }
        }
        int index = 0;
        while (resultList.size() < 5 && index < unaliveProject.size()) {
            //看看result里面有木有
            boolean isUseful = true;
            for (Project project : resultList) {
                if (project.getProid() == unaliveProject.get(index).getProid()) {
                    isUseful = false;
                    break;
                }
            }
            if (isUseful)
                resultList.add(unaliveProject.get(index++));
        }
        return resultList;
    }

    public static void addRecentProid(Context context, long proid) {
        XManager.setMyRecentProject(context, String.valueOf(proid));
    }

    public static void removeRecentProid(Context context, long proid) {
        String source = XManager.getMyRecentProid(context);
        String target = String.valueOf(proid) + ";";
        source = source.replace(target, "");
        XManager.getUserManager(context).edit().putString(XManager.PARAM_RECENTPROJECT
        ,source).commit();
    }

}
