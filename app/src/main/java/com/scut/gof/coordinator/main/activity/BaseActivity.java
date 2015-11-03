package com.scut.gof.coordinator.main.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

}
