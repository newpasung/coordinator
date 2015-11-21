package com.scut.gof.coordinator.main.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.ProinfoActivty;
import com.scut.gof.coordinator.main.activity.ProjectActivity;
import com.scut.gof.coordinator.main.adapter.HomeAdapter;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment implements BottomBarController {

    RecyclerView mRec;
    List<Project> proData = new ArrayList<>();
    List<String> msgData=new ArrayList<>();
    HomeAdapter.MOnClick clickListener = new HomeAdapter.MOnClick() {
        @Override
        public void onClick(int type, long id) {
            switch (type) {
                case HomeAdapter.TYPE_PROJECT: {
                    Intent intent = new Intent(getActivity(), ProjectActivity.class);
                    intent.putExtra(ProjectActivity.EXTRA_PROID, id);
                    startActivity(intent);
                }
                break;
            }
        }
    };
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
        adapter.setListener(clickListener);
        mRec.setAdapter(adapter);
    }

    protected void iniData(){
        proData.clear();
        msgData.clear();
        proData = Project.getUsersProjects(getActivity());
        msgData.add("未处理消息的概括1好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊");
        msgData.add("未处理消息的概括2");
        msgData.add("未处理消息的概括3");
    }

    @Override
    public void refreshView(BottomToolBar toolBar) {
        toolBar.setText(new String[]{getString(R.string.action_newpro), "", ""});
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {
        startActivity(new Intent(getActivity(), ProinfoActivty.class));
    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {
    }

    @Override
    public void controllright(BottomToolBar toolBar) {

    }
}
