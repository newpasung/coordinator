package com.scut.gof.coordinator.main.models;

/**
 * Created by Administrator on 2015/12/17.
 */
public class SimpleContact {

    private String name;
    private String pinyin;
    private String avaterUrl;

    public String getAvaterUrl() {
        return avaterUrl;
    }

    public void setAvaterUrl(String avaterUrl) {
        this.avaterUrl = avaterUrl;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
