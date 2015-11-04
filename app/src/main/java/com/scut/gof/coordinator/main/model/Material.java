package com.scut.gof.coordinator.main.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/4.
 */
public class Material extends Model {

    //物品所属proid
    @Column(name = "proid")
    public long proid;
    //物品id
    @Column(name = "mid")
    public long mid;

    //物品拥有人,注意可能是法人(即一个组织)
    @Column(name = "owner")
    public String owner;

    //物品拥有人id,注意可能是法人代表
    @Column(name = "ownerid")
    public long ownerid;

    //物品分配使用人id
    @Column(name = "userid")
    public long userid;

    //物品负责人
    @Column(name = "principalid")
    public long principalid;

    //物品名
    @Column(name = "name")
    public String name;

    //物品数目
    @Column(name = "number")
    public double number;

    //量词，个/只？
    @Column(name = "quantifier")
    public String quantifier;

    public static Material getMaterial(long proid, long mid) {
        return new Select().from(Material.class).where("proid=" + proid)
                .and("mid=" + mid).executeSingle();
    }

    public static void insertOrUpdate(JSONObject data) {
        Material material = null;
        try {
            long pid = data.getLong("proid");
            long mid = data.getLong("mid");
            material = getMaterial(pid, mid);
            if (material == null) {
                material = new Material();
                material.proid = pid;
                material.mid = mid;
            }
            if (data.has("owner")) {
                material.owner = data.getString("owner");
            }
            if (data.has("ownerid")) {
                material.ownerid = data.getLong("ownerid");
            }
            if (data.has("userid")) {
                material.userid = data.getLong("userid");
            }
            if (data.has("principalid")) {
                material.principalid = data.getLong("principalid");
            }
            if (data.has("number")) {
                material.number = data.getDouble("number");
            }
            if (data.has("quantifier")) {
                material.quantifier = data.getString("quantifier");
            }
            material.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

