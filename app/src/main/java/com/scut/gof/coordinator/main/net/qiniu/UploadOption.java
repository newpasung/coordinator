package com.scut.gof.coordinator.main.net.qiniu;

import com.loopj.android.http.RequestParams;

/**
 * Created by Administrator on 2015/11/26.
 */
public abstract class UploadOption {
    protected int category;

    public void setCategory(int category) {
        this.category = category;
    }

    abstract RequestParams getParams();
}
