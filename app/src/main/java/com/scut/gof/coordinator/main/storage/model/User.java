package com.scut.gof.coordinator.main.storage.model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.scut.gof.coordinator.main.utils.CharacterParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/20.
 */
@Table(name = "user")
public class User extends Model {
    @Column(name = "uid")
    private long uid;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "thumbnailavatar")
    private String thumbnailavatar;
    //下面是展示的信息
    @Column(name = "name")
    private String name;
    @Column(name = "name_pinyin")
    private String name_pinyin;
    @Column(name = "gender")
    private int gender;
    @Column(name = "workphone")
    private String workphone;
    @Column(name = "email")
    private String email;
    @Column(name = "signature")
    private String signature;
    @Column(name = "locale")
    private String locale;
    @Column(name = "birthday")
    private String birthday;

    public User() {
        this.avatar = "";
        this.thumbnailavatar = "";
        this.gender = 2;
        this.name = "";
        this.birthday = "0-0-0";
        this.email = "";
        this.workphone = "";
        this.signature = "";
        this.locale = "";
        this.name_pinyin="";
    }

    public static void clearData() {
        new Delete().from(User.class).execute();
    }

    /**
     * 直插入简单数据，加快效率
     */
    public static User insertOrUpdateSimply(JSONObject user) {
        User mUser = null;
        try {
            long id = user.getLong("uid");
            mUser = User.getUserById(id);
            if (mUser == null) {
                mUser = new User();
                mUser.uid = id;
            }
            if (user.has("name")) {
                if (mUser.name==null||!mUser.name.equals(user.getString("name"))){
                    mUser.name = user.getString("name");
                    mUser.name_pinyin = CharacterParser.getInstance().getContactPinyin(mUser.name);
                }
            }
            if (user.has("thumbnailavatar")) {
                mUser.thumbnailavatar = user.getString("thumbnailavatar");
            }
            if (user.has("avatar")) {
                mUser.avatar = user.getString("avatar");
            }
            if (user.has("proids")){
                RelaProject.insertOrUpdate(user.getJSONArray("proids"),id);
            }
            mUser.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mUser;
    }

    public static List<User> insertOrUpdateSimply(JSONArray array){
        List<User> users =new ArrayList<>();
        ActiveAndroid.beginTransaction();
        try {
            for (int i=0;i<array.length();i++){
                User user =insertOrUpdateSimply(array.getJSONObject(i));
                if (user!=null){
                    users.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        return users;
    }

    public static User insertOrUpdate(JSONObject jsonObject) {
        User user = null;
        try {
            long uid = jsonObject.getLong("uid");
            user = getUserById(uid);
            if (user == null) {
                user = new User();
                user.uid = uid;
            }
            if (jsonObject.has("name")) {
                if (user.name==null||!jsonObject.getString("name").equals(user.name)){
                    user.name = jsonObject.getString("name");
                    user.name_pinyin = CharacterParser.getInstance().getContactPinyin(user.name);
                }
            }
            if (jsonObject.has("gender")) {
                user.gender = jsonObject.getInt("gender");
            }
            if (jsonObject.has("avatar")) {
                user.avatar = jsonObject.getString("avatar");
            }
            if (jsonObject.has("thumbnailavatar")) {
                user.thumbnailavatar = jsonObject.getString("thumbnailavatar");
            }
            if (jsonObject.has("workphone")) {
                user.workphone = jsonObject.getString("workphone");
            }
            if (jsonObject.has("email")) {
                user.email = jsonObject.getString("email");
            }
            if (jsonObject.has("signature")) {
                user.signature = jsonObject.getString("signature");
            }
            if (jsonObject.has("locale")) {
                user.locale = jsonObject.getString("locale");
            }
            if (jsonObject.has("birthday")) {
                user.birthday = jsonObject.getString("birthday");
            }
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUserById(long uid) {
        return new Select().from(User.class).where("uid=" + uid).executeSingle();
    }

    public String getAvatar() {
        return avatar;
    }

    public String getGender() {
        if (gender == 1) {
            return "男";
        } else if (gender == 0) {
            return "女";
        } else {
            return "";
        }
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getThumbnailavatar() {
        return thumbnailavatar;
    }

    public String getBirthday() {
        return birthday.equals("0-0-0") ? "" : birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getLocale() {
        return locale;
    }

    public String getSignature() {
        return signature;
    }

    public String getWorkphone() {
        return workphone;
    }

    public String getName_pinyin() {
        return name_pinyin;
    }
}
