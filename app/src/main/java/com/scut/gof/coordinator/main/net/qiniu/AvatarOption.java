package com.scut.gof.coordinator.main.net.qiniu;

import com.loopj.android.http.RequestParams;

/**
 * Created by Administrator on 2015/11/26.
 */
public class AvatarOption extends UploadOption {

    public AvatarOption() {
        setCategory(QiniuHelper.FILETYPE_AVATAR);
    }

    @Override
    RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.put("category", category);
        return params;
    }
}
