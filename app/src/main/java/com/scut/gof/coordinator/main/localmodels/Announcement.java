package com.scut.gof.coordinator.main.localmodels;

import com.activeandroid.query.Select;
import com.scut.gof.coordinator.main.storage.model.MDAnnouncement;
import com.scut.gof.coordinator.main.widget.RippleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
public class Announcement extends HomeMessage {
    String proname;
    String url;
    String content;
    boolean isread;

    public Announcement(int time, String proname, String content, String url, boolean isread) {
        this.time = time;
        this.proname = proname;
        this.content = content;
        this.url = url;
        this.isread = isread;
    }

    @Override
    public String getDescription() {
        return "公告";
    }

    @Override
    public String getIconUrl() {
        return url;
    }

    @Override
    public String getContent() {
        return content;
    }

    public static ArrayList<HomeMessage> getRecentUnreadAnnouncements() {
        ArrayList<HomeMessage> announcements = new ArrayList<>();

        List<MDAnnouncement> mdAnnouncements = new Select().from(MDAnnouncement.class).orderBy("time desc").execute();
        for (MDAnnouncement mdAnnouncement: mdAnnouncements) {
            if (!mdAnnouncement.isread) {
                announcements.add(new Announcement(mdAnnouncement.time, mdAnnouncement.proneme, mdAnnouncement.content, mdAnnouncement.logourl, false));
            }
        }

        return announcements;
    }
}
