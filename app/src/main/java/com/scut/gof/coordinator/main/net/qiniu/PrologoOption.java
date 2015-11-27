package com.scut.gof.coordinator.main.net.qiniu;

import com.loopj.android.http.RequestParams;

/**
 * Created by Administrator on 2015/11/26.
 */
public class PrologoOption extends UploadOption {

    long proid;

    public PrologoOption(long proid) {
        this.proid = proid;
        setCategory(QiniuHelper.FILETYPE_PROLOGO);
    }

    @Override
    RequestParams getParams() {
        return null;
    }
}
