package com.scut.gof.coordinator.main.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.FragmentTransactionExtended;
import com.scut.gof.coordinator.main.fragment.material.IniMaterialFragment;
import com.scut.gof.coordinator.main.fragment.project.CreateProFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.storage.model.RelaProject;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/11/3
 */
public class CreateProActivity extends BaseActivity {
    public static final String BRCAST_KEY_NEWPRO = "UPLOADNEWPRODATA";
    //这个act管理的frg的总数
    final int FRAGMENT_COUNT = 2;
    CreateProFragment createProFragment;
    IniMaterialFragment iniMaterialFragment;
    List<Fragment> fragmentList;
    BottomToolBar bottomToolBar;
    int curFrgIndex = 0;
    RequestParams requestParams;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BRCAST_KEY_NEWPRO)) {
                uploadInfo();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proinfoactivity_frg_container);
        requestParams = new RequestParams();
        iniUI();
        iniListener();
        LocalBrCast.register(this, BRCAST_KEY_NEWPRO, receiver);
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
        //设置默认的fragment
        getFragmentManager().beginTransaction().replace(R.id.fragment, createProFragment, "default")
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
                = new FragmentTransactionExtended(this, fragmentTransaction, fragmentList.get(curFrgIndex), fragmentList.get(curFrgIndex + 1), R.id.fragment);
        fragmentTransactionExtended.addTransition(FragmentTransactionExtended.ROTATE_DOWN);
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
                    RelaProject.insertOrUpdate(data.getJSONObject("project"), UserManager.getUserid(CreateProActivity.this));
                    Intent intent = new Intent();
                    intent.putExtra(ProjectActivity.EXTRA_PROID, project.getProid());
                    intent.setClass(CreateProActivity.this, ProjectActivity.class);
                    startActivity(intent);
                    finish();
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
