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
import android.widget.TextView;

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
    //用来记录当前的fragment
    Fragment curFragment;
    //下面是侧栏顶部显示的几个信息
    CircleImageView mCiravatar;
    TextView mTvname;
    TextView mTvsignature;
    //缓存一个homefragment
    HomeFragment mHomeFragment;
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
        mTvname = (TextView) drawerHeader.findViewById(R.id.drawer_name);
        mTvsignature = (TextView) drawerHeader.findViewById(R.id.drawer_desc);

        mBar = (BottomToolBar) findViewById(R.id.bottombar);
        mBtnfab = (FloatingActionButton) findViewById(R.id.btn_fab);
        //为底部工具栏附上按钮
        mBar.attachFloatingButton(mBtnfab);
        //设置侧滑栏数据
        PicassoProxy.loadAvatar(this, UserManager.getUserThumbAvatar(this), mCiravatar);
        mTvname.setText(UserManager.getUserName(this));
        mTvsignature.setText(UserManager.getSignature(this));
        //设置默认fragment
        mHomeFragment = HomeFragment.newInstance();
        curFragment = mHomeFragment;
        getFragmentManager().beginTransaction().add(R.id.fragment_container, curFragment)
                .commit();
        if (curFragment instanceof BottomBarController) {
            mBar.setButtonListener((BottomBarController) curFragment);
        }
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
                if (mBar.getVisibility() == View.VISIBLE) {
                    mBar.resetImmediate();
                }
                switch (item.getItemId()) {
                    case R.id.btn_userdata: {
                        if (curFragment instanceof UserDataFragment) break;
                        replaceFragment(UserDataFragment.newInstance());
                    }
                    break;
                    case R.id.btn_home: {
                        if (curFragment instanceof HomeFragment) break;
                        replaceFragment(mHomeFragment);
                    }
                    break;
                    case R.id.btn_feedback: {
                        if (curFragment instanceof FeedBackFragment) break;
                        replaceFragment(FeedBackFragment.newInstance());
                    }
                    break;
                    case R.id.btn_setting: {
                        if (curFragment instanceof SettingFragment) break;
                        replaceFragment(SettingFragment.newInstance());
                    }
                    break;
                    case R.id.btn_test: {
                    }
                    break;
                }
                mDrwer.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
    }

    //用来切换fragment , 不保存状态到回退栈
    protected void replaceFragment(Fragment fragment) {
        if (fragment instanceof BottomBarController) {
            mBar.setButtonListener((BottomBarController) fragment);
            if (!mBtnfab.isShown()) {
                mBtnfab.show();
            }
        } else {
            mBtnfab.hide();
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended
                = new FragmentTransactionExtended(this, fragmentTransaction, curFragment, fragment, R.id.fragment_container);
        fragmentTransactionExtended.addTransition(FragmentTransactionExtended.SLIDE_HORIZONTAL);
        fragmentTransactionExtended.commit(false);
        curFragment = fragment;
    }

    //此处逻辑是这样的，按返回键可以返回到homefragment，其他不行
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBar.getVisibility() == View.VISIBLE || mDrwer.isDrawerVisible(Gravity.LEFT)) {
                if (mBar.isAnimating()) {
                    return true;
                }
                if (mDrwer.isDrawerOpen(Gravity.LEFT)) {
                    mDrwer.closeDrawer(Gravity.LEFT);
                }
                mBar.reset();
                return true;
            }
            if (!(curFragment instanceof HomeFragment)) {
                replaceFragment(HomeFragment.newInstance());
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

