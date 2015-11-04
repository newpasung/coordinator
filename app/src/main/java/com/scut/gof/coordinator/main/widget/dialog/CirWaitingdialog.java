package com.scut.gof.coordinator.main.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.scut.gof.coordinator.R;

/**
 * Created by Administrator on 2015/11/4.
 */
public class CirWaitingdialog extends Dialog {

    public CirWaitingdialog(Context context) {
        super(context, R.style.commondialog);
        init();
    }

    public CirWaitingdialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public CirWaitingdialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected void init() {
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circleprogressbar);
    }
}
