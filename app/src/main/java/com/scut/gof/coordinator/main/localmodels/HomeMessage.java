package com.scut.gof.coordinator.main.localmodels;

import android.view.View;

/**
 * Created by Administrator on 2015/12/23.
 */
public abstract class HomeMessage {

    protected int time;
    protected View.OnClickListener onPositiveClick = null;
    protected View.OnClickListener onNegativeClick = null;
    protected String positiveTitle = "查看";
    protected String negativeTitle = "已阅";

    public HomeMessage() {
    }

    public abstract String getDescription();

    public abstract String getIconUrl();

    public abstract String getContent();

    public String getPositiveTitle() {
        return positiveTitle;
    }

    public String getNegativeTitle() {
        return negativeTitle;
    }

    public View.OnClickListener getOnNegativeClick() {
        return onNegativeClick;
    }

    public View.OnClickListener getOnPositiveClick() {
        return onPositiveClick;
    }


}
