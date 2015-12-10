package com.scut.gof.coordinator.main.net.qiniu;

import com.loopj.android.http.RequestParams;

/**
 * Created by Administrator on 2015/11/26.
 */
public class AvatarOption extends UploadOption {

    public AvatarOption() {
        super();
    }

    @Override
    RequestParams getParams() {
        this.mParams.put("category", category);
        return this.mParams;
    }

    @Override
    void setCategory() {
        setCategory(FILETYPE_AVATAR);
    }
}
