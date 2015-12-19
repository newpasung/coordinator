package com.scut.gof.coordinator.main.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.FragmentTransactionExtended;
import com.scut.gof.coordinator.main.fragment.material.IniMaterialFragment;
import com.scut.gof.coordinator.main.fragment.project.CreateProFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/11/3
 */
public class CreateProActivity extends BaseActivity {
    //这个act管理的frg的总数
    final int FRAGMENT_COUNT = 2;
    final int MSG_WHAT_ONUPLOADINFO = 1;
    CreateProFragment createProFragment;
    IniMaterialFragment iniMaterialFragment;
    List<Fragment> fragmentList;
    BottomToolBar bottomToolBar;
    int curFrgIndex = 0;
    RequestParams requestParams;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_WHAT_ONUPLOADINFO) {
                //新建项目成功之后跳转
                if (msg.obj == null) return;
                Intent intent = new Intent();
                intent.putExtra(ProjectActivity.EXTRA_PROID, (long) msg.obj);
                intent.setClass(CreateProActivity.this, ProjectActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocalBrCast.PARAM_NEWPROJECT)) {
                if (createProFragment.canUpload()) {
                    uploadInfo();
                } else {
                    toast("基础信息未填写完整");
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staticbottombar_container);
        requestParams = new RequestParams();
        iniUI();
        iniListener();
        LocalBrCast.register(this, LocalBrCast.PARAM_NEWPROJECT, receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBrCast.unregisterReceiver(this, receiver);
    }

    protected void iniUI() {
        fragmentList = new ArrayList<>();
        createProFragment = new CreateProFragment();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.arrow_back);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getString(R.string.action_newpro));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottomToolBar = (BottomToolBar) findViewById(R.id.bottombar);
        bottomToolBar.iniStaticBar();
        //加一个布局解决状态栏变白-，-
        ((DrawerLayout) findViewById(R.id.drawerlayout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置默认的fragment
        getFragmentManager().beginTransaction().add(R.id.fragment_container, createProFragment, "default")
                .commit();
        fragmentList.add(createProFragment);
        curFrgIndex = 0;
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
                if (curFrgIndex >= 0 && curFrgIndex < FRAGMENT_COUNT - 1) {
                    nextFragment();
                }
            }
        });
    }

    //用来切换fragment//TODO 加上缓存处理
    protected void nextFragment() {
        //懒加载
        if (curFrgIndex == 0) {
            if (fragmentList.size() < 2) {
                iniMaterialFragment = new IniMaterialFragment();
                fragmentList.add(iniMaterialFragment);
            }
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended
                = new FragmentTransactionExtended(this, fragmentTransaction, fragmentList.get(curFrgIndex), fragmentList.get(curFrgIndex + 1), R.id.fragment_container);
        fragmentTransactionExtended.addTransition(FragmentTransactionExtended.SLIDE_HORIZONTAL);
        fragmentTransactionExtended.commit(true);
        curFrgIndex++;
    }

    //用来切换fragment//TODO 加上缓存处理
    protected void lastFragment() {
        getFragmentManager().popBackStack();
        curFrgIndex--;
    }

    public void addReqParams(String param, String value) {
        requestParams.put(param, value);
    }

    public void uploadInfo() {
        HttpClient.post(this, "project/newproject", requestParams, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    Project project = Project.insertOrUpdate(data.getJSONObject("project"));
                    Message message = new Message();
                    message.what = MSG_WHAT_ONUPLOADINFO;
                    if (project != null) {
                        message.obj = project.getProid();
                    }
                    mHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                Log.i("NewProFail", message);
            }
        });
    }

}
