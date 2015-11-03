package com.scut.gof.coordinator.main.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.scut.gof.coordinator.R;

/**
 * Created by Administrator on 2015/11/2.
 */
public class AlarmDialog extends Dialog {

    private TextView mTvalarm;
    public AlarmDialog(Context context) {
        super(context, R.style.commondialog);
    }

    protected AlarmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public AlarmDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textdialog_layout);
        ImageView imageView=(ImageView)findViewById(R.id.image);
        imageView.setImageResource(R.drawable.alarm);
        mTvalarm=(TextView)findViewById(R.id.tv_text);
    }

    public void setMessage(String text){
        mTvalarm.setText(text);
    }

}
