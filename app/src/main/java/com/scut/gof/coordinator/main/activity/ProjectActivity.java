package com.scut.gof.coordinator.main.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.task.TaskListContainerFragment;

/**
 * Created by Administrator on 2015/11/7.
 * 用于主要project事务
 */
public class ProjectActivity extends BaseActivity {
    public static final String EXTRA_PROID = "proid";
    long proid = -1;
    TaskListContainerFragment taskContainerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        if (getIntent() != null) {
            proid = getIntent().getIntExtra(EXTRA_PROID, 0);
        }
        iniUI();
    }

    protected void iniUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final DrawerLayout mDraw = (DrawerLayout) findViewById(R.id.drawerlayout);
        mDraw.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.bar_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDraw.openDrawer(Gravity.LEFT);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_taskflow: {
                    }
                    break;
                }
                return true;
            }
        });
        taskContainerFragment = TaskListContainerFragment.newInstance(proid);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, taskContainerFragment)
                .commit();

    }

}
