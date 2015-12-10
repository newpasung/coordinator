package com.scut.gof.coordinator.main.net.qiniu;

import com.loopj.android.http.RequestParams;

/**
 * Created by Administrator on 2015/11/26.
 */
public abstract class UploadOption {
    public static final int FILETYPE_PROLOGO = 0;
    public static final int FILETYPE_AVATAR = 1;
    protected int category;
    protected RequestParams mParams;

    public UploadOption() {
        this.mParams = new RequestParams();
        setCategory();
    }

    public void setCategory(int category) {
        this.category = category;
    }

    abstract RequestParams getParams();

    abstract void setCategory();
}
