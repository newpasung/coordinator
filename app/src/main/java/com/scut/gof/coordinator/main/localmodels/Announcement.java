package com.scut.gof.coordinator.main.localmodels;

/**
 * Created by Administrator on 2015/12/23.
 */
public class Announcement extends HomeMessage {
    String url;
    String content;

    public Announcement(String content, String url) {
        this.content = content;
        this.url = url;
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
}
