package com.scut.gof.coordinator.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment {

    RecyclerView mRec;
    List<String> proData=new ArrayList<>();
    List<String> msgData=new ArrayList<>();
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        iniData();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRec=(RecyclerView)view.findViewById(R.id.recyclerview);
        mRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        HomeAdapter adapter=new HomeAdapter(getActivity());
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
