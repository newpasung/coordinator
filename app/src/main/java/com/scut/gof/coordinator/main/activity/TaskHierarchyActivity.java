package com.scut.gof.coordinator.main.activity;

import android.os.Bundle;

import com.scut.gof.coordinator.R;

/**
 * Created by Administrator on 2015/12/10.
 */
public class TaskHierarchyActivity extends BaseActivity {

    long tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskhierarchy);
        tid = getIntent().getLongExtra("tid", 0);
        if (tid == 0) {
            toast("tid错误");
            finish();
        }
    }
}
