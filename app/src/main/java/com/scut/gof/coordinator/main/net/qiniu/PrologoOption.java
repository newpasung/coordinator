package com.scut.gof.coordinator.main.net.qiniu;

import com.loopj.android.http.RequestParams;

/**
 * Created by Administrator on 2015/11/26.
 */
public class PrologoOption extends UploadOption {
    long proid;
    public PrologoOption(long proid) {
        super();
        this.proid = proid;
    }

    @Override
    RequestParams getParams() {
        this.mParams.put("proid", this.proid);
        this.mParams.put("category", this.category);
        return this.mParams;
    }

    @Override
    void setCategory() {
        setCategory(FILETYPE_PROLOGO);
    }
}
