package com.scut.gof.coordinator.main.activity.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.utils.DenstityUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2015/10/29.
 * 所有activity的基类
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void toast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(BaseActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    protected void toastWarn(String text) {
        Toast toast = new Toast(this);
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(ApiUtil.getColor(this, R.color.colorAccent));
        toast.setGravity(Gravity.CENTER, 0, (int) ((float) DenstityUtil.getScreenHeight(this) * 0.382f));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(textView);
        toast.show();
    }

}
