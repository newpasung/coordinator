package com.scut.gof.coordinator.main.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.FeedBackFragment;
import com.scut.gof.coordinator.main.fragment.FragmentTransactionExtended;
import com.scut.gof.coordinator.main.fragment.HomeFragment;
import com.scut.gof.coordinator.main.fragment.SettingFragment;
import com.scut.gof.coordinator.main.fragment.UserDataFragment;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

public class HomeActivity extends BaseActivity {
    DrawerLayout mDrwer;
    BottomToolBar mBar;
    FloatingActionButton mBtnfab;
    Button button;
    UserDataFragment userDataFragment;
    HomeFragment homeFragment;
    FeedBackFragment feedBackFragment;
    SettingFragment settingFragment;
    Fragment curFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        iniListener();
    }

    //初始化几个结构化的界面
    protected void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout mCoolBar = (CollapsingToolbarLayout) findViewById(R.id.cooltoolbar);
        mCoolBar.setTitleEnabled(true);
        mCoolBar.setTitle(getString(R.string.app_name));
        mCoolBar.setCollapsedTitleTextColor(Color.WHITE);
        mCoolBar.setExpandedTitleColor(Color.WHITE);
        button = (Button) findViewById(R.id.button1);
        mDrwer = (DrawerLayout) findViewById(R.id.drawerlayout);
        mDrwer.setScrimColor(getResources().getColor(R.color.black_54));
        mBar = (BottomToolBar) findViewById(R.id.bottombar);
        mBtnfab = (FloatingActionButton) findViewById(R.id.btn_fab);
        homeFragment = (HomeFragment) getFragmentManager().findFragmentById(R.id.fragment);
        curFragment = homeFragment;
    }

    protected void iniListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBar.reset(mBtnfab);
            }
        });
        mBtnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBar.reveal(HomeActivity.this, mBtnfab);
            }
        });
        //设置fragment切换
        final NavigationView naviView = (NavigationView) findViewById(R.id.navigationview);
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_userdata: {
                        if (userDataFragment == null) {
                            userDataFragment = new UserDataFragment();
                        }
                        replaceFragment(userDataFragment);
                    }
                    break;
                    case R.id.btn_home: {
                        if (homeFragment == null) homeFragment = new HomeFragment();
                        replaceFragment(homeFragment);
                    }
                    break;
                    case R.id.btn_feedback: {
                        if (feedBackFragment == null) feedBackFragment = new FeedBackFragment();
                        replaceFragment(feedBackFragment);
                    }
                    break;
                    case R.id.btn_setting: {
                        if (settingFragment == null)
                            settingFragment = new SettingFragment();
                        replaceFragment(settingFragment);
                    }
                    break;
                }
                return true;
            }
        });
    }

    //用来切换fragment//TODO 加上缓存
    protected void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended
                = new FragmentTransactionExtended(this, fragmentTransaction, curFragment, fragment, R.id.fragment);
        fragmentTransactionExtended.addTransition(FragmentTransactionExtended.ROTATE_DOWN);
        fragmentTransactionExtended.commit();
        mDrwer.closeDrawer(Gravity.LEFT);
    }

}
