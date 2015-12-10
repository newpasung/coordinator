package com.scut.gof.coordinator.main.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import com.scut.gof.coordinator.main.fragment.FragmentTransactionExtended;
import com.scut.gof.coordinator.main.fragment.project.ProDetailFragment;
import com.scut.gof.coordinator.main.fragment.task.TaskListContainerFragment;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.widget.BottomToolBar;
import com.scut.gof.coordinator.main.widget.CircleImageView;

/**
 * Created by Administrator on 2015/11/7.
 * 用于主要project事务
 */
public class ProjectActivity extends BaseActivity {
    public static final String EXTRA_PROID = "proid";
    long proid = -1;
    Project project;

    CircleImageView mCirprologo;
    TextView mTvproname;
    TextView mTvdesc;

    //一些结构化的组件
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout mDraw;
    BottomToolBar mBottomBar;
    FloatingActionButton mBtnfab;

    //fragment
    Fragment curFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        iniData();
        iniUI();
        iniListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBottomBar != null) {
            if (mBottomBar.getVisibility() == View.VISIBLE) {
                mBottomBar.reset();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBottomBar.getVisibility() == View.VISIBLE) {
                mBottomBar.reset();
                if (mDraw.isDrawerOpen(Gravity.LEFT)) {
                    mDraw.closeDrawer(Gravity.LEFT);
                }
                return true;
            }
            if (mDraw.isDrawerOpen(Gravity.LEFT)) {
                mDraw.closeDrawer(Gravity.LEFT);
                return true;
            }
            if (!(curFragment instanceof TaskListContainerFragment)) {
                replaceFragment(TaskListContainerFragment.newInstance(proid));
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void iniData() {
        if (getIntent() != null) {
            proid = getIntent().getLongExtra(EXTRA_PROID, 0);
            project = Project.getProById(proid);
            if (project == null) {
                toast("proid invalid");
                finish();
            }
        }
    }

    protected void iniUI() {
        View drawerHeader = ((NavigationView) findViewById(R.id.navigationview)).inflateHeaderView(R.layout.drawer_header);
        mTvdesc = (TextView) drawerHeader.findViewById(R.id.drawer_desc);
        mTvproname = (TextView) drawerHeader.findViewById(R.id.drawer_name);
        mCirprologo = (CircleImageView) drawerHeader.findViewById(R.id.drawer_avatar);

        mBtnfab = (FloatingActionButton) findViewById(R.id.btn_fab);
        mBottomBar = (BottomToolBar) findViewById(R.id.bottombar);
        PicassoProxy.loadAvatar(ProjectActivity.this, project.getThumbnailLogo(), mCirprologo);
        mBottomBar.attachFloatingButton(mBtnfab);
        mTvproname.setText(project.getProname());
        mTvdesc.setText(project.getDescription());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDraw = (DrawerLayout) findViewById(R.id.drawerlayout);
        toolbar.setNavigationIcon(R.drawable.bar_app);
        toolbar.setTitle(project.getProname());
        toolbar.setTitleTextColor(ApiUtil.getColor(this, R.color.white));
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        mDraw.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        setSupportActionBar(toolbar);
        //加载默认fragment
        curFragment = TaskListContainerFragment.newInstance(proid);
        getFragmentManager().beginTransaction().add(R.id.fragment_container, curFragment)
                .commit();
    }

    protected void iniListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDraw.openDrawer(Gravity.LEFT);
            }
        });
        mCirprologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectActivity.this, ImageBrowserActivity.class);
                intent.putExtra(ImageBrowserActivity.EXTRA_PARAMETER_PICCOUNT, 1);
                intent.putExtra(ImageBrowserActivity.EXTRA_PARAMETER_PICURL, project.getPrologo());
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_taskflow: {
                        if (curFragment instanceof TaskListContainerFragment) break;
                        replaceFragment(TaskListContainerFragment.newInstance(proid));
                    }
                    break;
                    case R.id.btn_showproinfo: {
                        if (curFragment instanceof ProDetailFragment) break;
                        replaceFragment(ProDetailFragment.newInstance(proid));
                    }
                    break;
                }
                mDraw.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
        mBtnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomBar.reveal(ProjectActivity.this);
            }
        });
        if (curFragment instanceof BottomBarController) {
            mBottomBar.setButtonListener((BottomBarController) curFragment);
        }
    }

    //用来切换fragment , 不保存状态到回退栈
    protected void replaceFragment(Fragment fragment) {
        if (fragment == curFragment) {
            return;
        }
        if (fragment instanceof BottomBarController) {
            mBottomBar.setButtonListener((BottomBarController) fragment);
            mBtnfab.show();
        } else {
            if (mBtnfab.isShown()) {
                mBtnfab.hide();
            }
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended
                = new FragmentTransactionExtended(this, fragmentTransaction, curFragment, fragment, R.id.fragment_container);
        fragmentTransactionExtended.addTransition(FragmentTransactionExtended.SLIDE_HORIZONTAL);
        fragmentTransactionExtended.commit(false);
        curFragment = fragment;
        mDraw.closeDrawer(Gravity.LEFT);
    }

}
