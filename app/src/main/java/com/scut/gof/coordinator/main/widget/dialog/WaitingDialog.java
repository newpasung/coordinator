package com.scut.gof.coordinator.main.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.scut.gof.coordinator.R;

/**
 * Created by Administrator on 2015/9/20.
 * 只显示文字和等待icon的dialog
 */
public class WaitingDialog extends Dialog {
    private ImageView imageView;
    private TextView textView;
    public WaitingDialog(Context context) {
        super(context, R.style.commondialog);
    }

    public WaitingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textdialog_layout);
        imageView=(ImageView)findViewById(R.id.image);
        textView=(TextView)findViewById(R.id.tv_text);
        RotateAnimation animation=new RotateAnimation(0,359,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(1500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        imageView.startAnimation(animation);
    }

    public void setTextView(String text){
        textView.setText(text);
    }

}
