package com.scut.gof.coordinator.main.activity;

import android.content.Intent;
import android.os.Bundle;

import com.scut.gof.coordinator.main.storage.XManager;

/**
 * Created by Administrator on 2015/10/29.
 * 初始启动的activity，不显示界面，预处理简单逻辑
 */
public class LeadActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,WelcomeActivity.class));
        finish();
    }
}
