package com.scut.gof.coordinator.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.adapter.HomeAdapter;
import com.scut.gof.coordinator.main.fragment.UserDataFragment;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.widget.BottomToolBar;
import com.scut.gof.coordinator.main.widget.dialog.TextDialog;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    DrawerLayout mDrwer;
    BottomToolBar mBar;
    FloatingActionButton mBtnfab;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
    }

    protected void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout mCoolBar=(CollapsingToolbarLayout)findViewById(R.id.cooltoolbar);
        mCoolBar.setTitleEnabled(true);
        mCoolBar.setTitle(getString(R.string.app_name));
        mCoolBar.setCollapsedTitleTextColor(Color.WHITE);
        mCoolBar.setExpandedTitleColor(Color.WHITE);
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBar.reset(mBtnfab);
            }
        });
        mDrwer = (DrawerLayout) findViewById(R.id.drawerlayout);
        mDrwer.setScrimColor(getResources().getColor(R.color.black_54));
        mBar = (BottomToolBar) findViewById(R.id.bottombar);
        mBtnfab = (FloatingActionButton) findViewById(R.id.btn_fab);
        mBtnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBar.reveal(HomeActivity.this, mBtnfab);
            }
        });
        //设置fragment切换
        final NavigationView naviView=(NavigationView)findViewById(R.id.navigationview);
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btn_userdata:{
                        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment,new UserDataFragment());
                        transaction.commit();
                        mDrwer.closeDrawer(Gravity.LEFT);
                    }break;
                    case R.id.btn_setting:{}break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
