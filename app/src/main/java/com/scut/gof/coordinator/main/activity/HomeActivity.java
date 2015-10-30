package com.scut.gof.coordinator.main.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.adapter.HomeAdapter;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    DrawerLayout mDrwer;
    BottomToolBar mBar;
    FloatingActionButton mBtnfab;
    Button button;
    RecyclerView mRec;
    List<String> proData=new ArrayList<>();
    List<String> msgData=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        iniData();
        initUI();
    }

    protected void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        mRec=(RecyclerView)findViewById(R.id.recyclerview);
        mRec.setLayoutManager(new LinearLayoutManager(this));
        HomeAdapter adapter=new HomeAdapter(this);
        adapter.setProData(proData);
        adapter.setMsgData(msgData);
        mRec.setAdapter(adapter);
    }

    protected void iniData(){
        proData.add("我的项目1");
        proData.add("我的项目2");
        proData.add("我的项目3");
        msgData.add("未处理消息的概括1好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊");
        msgData.add("未处理消息的概括2");
        msgData.add("未处理消息的概括3");
    }

}
