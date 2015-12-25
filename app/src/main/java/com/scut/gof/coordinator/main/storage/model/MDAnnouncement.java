package com.scut.gof.coordinator.main.storage.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

/**
 * Created by gjz on 12/26/15.
 */
@Table(name = "mdannouncement")
public class MDAnnouncement extends Model {

    //公告id
    @Column(name = "announcement_id")
    public int id;

    //所属项目id
    @Column(name = "proid")
    public int proid;

    //所属项目的名字
    @Column(name = "proname")
    public String proneme;

    //发布时间
    @Column(name = "time")
    public int time;

    //公告内容
    @Column(name = "content")
    public String content;

    //项目的图标url
    @Column(name = "logourl")
    public String logourl;

    //已读未读
    @Column(name = "isread")
    public boolean isread;

    public static void clearData() {
        new Delete().from(MDAnnouncement.class).execute();
    }
}
