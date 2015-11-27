package com.scut.gof.coordinator.main.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.activity.CreateProActivity;
import com.scut.gof.coordinator.main.activity.ProjectActivity;
import com.scut.gof.coordinator.main.adapter.HomeAdapter;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
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
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment implements BottomBarController {

    public static String TAGNAME = "basefragment";
    int lastVisibleView = 0;
    RecyclerView mRec;
    SwipeRefreshLayout mSwipelayout;
    List<Project> proData = new ArrayList<>();
    List<String> msgData=new ArrayList<>();
    HomeAdapter adapter;
    HomeAdapter.MOnClick clickListener = new HomeAdapter.MOnClick() {
        @Override
        public void onClick(int type, long id) {
            switch (type) {
                case HomeAdapter.TYPE_PROJECT: {
                    final Intent intent = new Intent(getActivity(), ProjectActivity.class);
                    intent.putExtra(ProjectActivity.EXTRA_PROID, id);
                    //为什么延迟100ms呢，为了按钮动画不阻塞
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }
                    }, 100);
                }
                break;
            }
        }
    };
    public HomeFragment() {
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
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
        mSwipelayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRec.setLayoutManager(manager);
        mSwipelayout.setColorSchemeColors(R.color.colorPrimary);
        adapter = new HomeAdapter(getActivity());
        adapter.setProData(proData);
        adapter.setMsgData(msgData);
        adapter.setListener(clickListener);
        mRec.setAdapter(adapter);
        mSwipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipelayout.setRefreshing(true);
                refreshProjects();
            }
        });
    }

    protected void iniData(){
        proData.clear();
        msgData.clear();
        proData = UserManager.getMyProjects(getActivity());
        msgData.add("未处理消息的概括1好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊");
        msgData.add("未处理消息的概括2");
        msgData.add("未处理消息的概括3");
    }

    @Override
    public void refreshView(BottomToolBar toolBar) {
        if (isDetached()) return;
        if (getActivity() == null) return;
        toolBar.setText(new String[]{getString(R.string.action_newpro), "", ""});
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {
        startActivity(new Intent(getActivity(), CreateProActivity.class));
    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {
    }

    @Override
    public void controllright(BottomToolBar toolBar) {

    }

    private void refreshProjects() {
        HttpClient.post(getActivity(), "project/relaproject", new RequestParams(), new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("data");
                    List<Project> data = Project.insertOrUpdate(object.getJSONArray("projects"));
                    RelaProject.insertOrUpdate(object.getJSONArray("projects"), UserManager.getUserid(getActivity()));
                    adapter.setProData(data);
                    adapter.notifyDataSetChanged();
                    mSwipelayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                toast("onfailure");
                mSwipelayout.setRefreshing(false);
            }
        });
    }

}
