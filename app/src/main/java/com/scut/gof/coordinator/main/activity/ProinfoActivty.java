package com.scut.gof.coordinator.main.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.FragmentTransactionExtended;
import com.scut.gof.coordinator.main.fragment.material.IniMaterialFragment;
import com.scut.gof.coordinator.main.fragment.project.CreateProFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/11/3.
 * 用于新建一个项目,这个activity结构不咋地
 */
public class ProinfoActivty extends BaseActivity {
    CreateProFragment createProFragment;
    IniMaterialFragment iniMaterialFragment;
    List<Fragment> fragmentList;
    BottomToolBar bottomToolBar;
    int curFrgIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proinfoactivity_frg_container);
        iniUI();
        iniListener();
    }

    protected void iniUI() {
        fragmentList = new ArrayList<>();
        createProFragment = new CreateProFragment();
        iniMaterialFragment = new IniMaterialFragment();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        bottomToolBar = (BottomToolBar) findViewById(R.id.bottombar);
        bottomToolBar.iniStaticBar();
        //设置默认的fragment
        getFragmentManager().beginTransaction().replace(R.id.fragment, createProFragment, "default")
                .commit();
        fragmentList.add(createProFragment);
        fragmentList.add(iniMaterialFragment);
        curFrgIndex = 0;
        //底部三个按钮
    }

    protected void iniListener() {
        bottomToolBar.setButtonListener(new BottomBarController() {
            @Override
            public void refreshView(BottomToolBar bottomToolBar) {
                Fragment fragment = fragmentList.get(curFrgIndex);
                if (fragment instanceof BottomBarController) {
                    ((BottomBarController) fragment).refreshView(bottomToolBar);
                }
            }

            @Override
            public void controllleft(BottomToolBar toolBar) {
                Fragment fragment = fragmentList.get(curFrgIndex);
                if (fragment instanceof BottomBarController) {
                    ((BottomBarController) fragment).controllleft(toolBar);
                }
                if (curFrgIndex > 0) {
                    lastFragment();
                }
            }

            @Override
            public void controllmiddle(BottomToolBar toolBar) {
                Fragment fragment = fragmentList.get(curFrgIndex);
                if (fragment instanceof BottomBarController) {
                    ((BottomBarController) fragment).controllmiddle(toolBar);
                }
            }

            @Override
            public void controllright(BottomToolBar toolBar) {
                Fragment fragment = fragmentList.get(curFrgIndex);
                if (fragment instanceof BottomBarController) {
                    ((BottomBarController) fragment).controllright(toolBar);
                }
                if (curFrgIndex >= 0 && curFrgIndex < fragmentList.size() - 1) {
                    nextFragment();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //用来切换fragment//TODO 加上缓存处理
    protected void nextFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended
                = new FragmentTransactionExtended(this, fragmentTransaction, fragmentList.get(curFrgIndex), fragmentList.get(curFrgIndex + 1), R.id.fragment);
        fragmentTransactionExtended.addTransition(FragmentTransactionExtended.SLIDE_HORIZONTAL);
        fragmentTransactionExtended.commit(true);
        curFrgIndex++;
        switchBarListener();
    }

    //用来切换fragment//TODO 加上缓存处理
    protected void lastFragment() {
        getFragmentManager().popBackStack();
        curFrgIndex--;
        switchBarListener();
    }

    protected void switchBarListener() {
        Fragment fragment = fragmentList.get(curFrgIndex);
        if (fragment instanceof BottomBarController) {
            bottomToolBar.setButtonListener((BottomBarController) fragment);
        }
    }

}
