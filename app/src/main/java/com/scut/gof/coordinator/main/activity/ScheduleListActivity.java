package com.scut.gof.coordinator.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.fragment.ScheduleListFragment;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

/**
 * Created by Administrator on 2015/12/22.
 */
public class ScheduleListActivity extends BaseActivity {

    BottomToolBar mBar;
    FloatingActionButton mBtnfab;
    ScheduleListFragment scheduleListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulelist);
        setDefaultFrag();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        mBar = (BottomToolBar) findViewById(R.id.bottombar);
        mBtnfab = (FloatingActionButton) findViewById(R.id.btn_fab);
        mBar.attachFloatingButton(mBtnfab);
        mBtnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBar.reveal(ScheduleListActivity.this);
            }
        });
        mBar.setButtonListener(scheduleListFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBar.getVisibility() == View.VISIBLE) {
            mBar.reset();
        }
    }

    private void setDefaultFrag() {
        scheduleListFragment = new ScheduleListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, scheduleListFragment, "default")
                .commit();
    }

}
