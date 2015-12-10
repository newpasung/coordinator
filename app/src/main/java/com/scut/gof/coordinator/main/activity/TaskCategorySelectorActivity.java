package com.scut.gof.coordinator.main.activity;

import android.os.Bundle;

import com.scut.gof.coordinator.R;

/**
 * Created by Administrator on 2015/12/10.
 */
public class TaskCategorySelectorActivity extends BaseActivity {

    long tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskcategorylist);
        tid = getIntent().getLongExtra("tid", 0);
    }
}
