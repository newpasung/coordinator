package com.scut.gof.coordinator.main.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.fragment.FragmentTransactionExtended;
import com.scut.gof.coordinator.main.fragment.home.FeedBackFragment;
import com.scut.gof.coordinator.main.fragment.home.HomeFragment;
import com.scut.gof.coordinator.main.fragment.home.SettingFragment;
import com.scut.gof.coordinator.main.fragment.home.UserDataFragment;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.widget.BottomToolBar;
import com.scut.gof.coordinator.main.widget.CircleImageView;

public class HomeActivity extends BaseActivity {
    DrawerLayout mDrwer;
    BottomToolBar mBar;
    FloatingActionButton mBtnfab;
    UserDataFragment userDataFragment;
    HomeFragment homeFragment;
    FeedBackFragment feedBackFragment;
    SettingFragment settingFragment;
    //用来记录当前的fragment和上一个fragment
    Fragment lastFragment;
    Fragment curFragment;
    CircleImageView mCiravatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        iniListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBar.getVisibility() == View.VISIBLE) {
            mBar.reset();
        }
    }

    //初始化几个结构化的界面
    protected void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationview);
        View drawerHeader = navigationView.inflateHeaderView(R.layout.drawer_header);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrwer.openDrawer(Gravity.LEFT);
            }
        });
        toolbar.setNavigationIcon(R.mipmap.bar_drawer);
        toolbar.setTitleTextColor(Color.WHITE);
        mDrwer = (DrawerLayout) findViewById(R.id.drawerlayout);
        mDrwer.setScrimColor(getResources().getColor(R.color.black_54));
        mCiravatar = (CircleImageView) drawerHeader.findViewById(R.id.drawer_avatar);
        mBar = (BottomToolBar) findViewById(R.id.bottombar);
        mBtnfab = (FloatingActionButton) findViewById(R.id.btn_fab);
        //为底部工具栏附上按钮
        mBar.attachFloatingButton(mBtnfab);
        //设置侧滑栏数据
        PicassoProxy.loadAvatar(this, UserManager.getUserThumbAvatar(this), mCiravatar);
        //设置默认fragment
        homeFragment = new HomeFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment, homeFragment)
                .commit();
        if (homeFragment instanceof BottomBarController) {
            mBar.setButtonListener(homeFragment);
        }
        curFragment = homeFragment;
        lastFragment = homeFragment;
    }

    protected void iniListener() {
        //点击头像
        mCiravatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ImageBrowserActivity.EXTRA_PARAMETER_PICURL, UserManager.getUserAvatar(HomeActivity.this));
                intent.setClass(HomeActivity.this, ImageBrowserActivity.class);
                startActivity(intent);
            }
        });
        mBtnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBar.reveal(HomeActivity.this);
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
                        if (feedBackFragment == null)
                            feedBackFragment = new FeedBackFragment();
                        replaceFragment(feedBackFragment);
                    }
                    break;
                    case R.id.btn_setting: {
                        if (settingFragment == null)
                            settingFragment = new SettingFragment();
                        replaceFragment(settingFragment);
                    }
                    break;
                    case R.id.btn_test: {
                        startActivity(new Intent(HomeActivity.this, ProjectActivity.class));
                    }
                    break;
                }
                return true;
            }
        });
    }

    //用来切换fragment
    //TODO 怎么处理缓存呢
    protected void replaceFragment(Fragment fragment) {
        if (curFragment == fragment) {
            //不应该发生什么
        }
        //如果正好是之前点击过的我就pop
        else if (lastFragment == fragment) {
            getFragmentManager().popBackStack();
            curFragment = lastFragment;
        } else {
            //怎么清空回退栈呢？？
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentTransactionExtended fragmentTransactionExtended
                    = new FragmentTransactionExtended(this, fragmentTransaction, curFragment, fragment, R.id.fragment);
            fragmentTransactionExtended.addTransition(FragmentTransactionExtended.ZOOM_SLIDE_HORIZONTAL2);
            //回退栈只保存一个状态，暂时是这样吧
            fragmentTransactionExtended.commit(true);
            if (fragment instanceof BottomBarController) {
                mBar.setButtonListener((BottomBarController) fragment);
            }
            lastFragment = curFragment;
            curFragment = fragment;
        }
        mDrwer.closeDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBar.getVisibility() == View.VISIBLE) {
                if (mBar.isAnimating()) {
                    return true;
                }
                mBar.reset();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}

