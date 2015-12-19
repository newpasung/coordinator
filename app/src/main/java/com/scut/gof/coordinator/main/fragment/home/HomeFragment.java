package com.scut.gof.coordinator.main.fragment.home;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.scut.gof.coordinator.main.widget.BottomToolBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment implements BottomBarController {
    final int MSG_WHAT_ONFAILE_REFRESH = 0;
    final int MSG_WHAT_ONSUCCESS_REFRESH = 1;
    RecyclerView mRec;
    SwipeRefreshLayout mSwipelayout;
    List<Project> proData = new ArrayList<>();
    List<String> msgData=new ArrayList<>();
    HomeAdapter adapter;
    HomeAdapter.MOnClick clickListener = new HomeAdapter.MOnClick() {
        @Override
        public void onClick(int type, final long id) {
            switch (type) {
                case HomeAdapter.TYPE_PROJECT: {
                    final Intent intent = new Intent(getActivity(), ProjectActivity.class);
                    intent.putExtra(ProjectActivity.EXTRA_PROID, id);
                    //为什么延迟1ms呢，为了按钮动画不阻塞
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //点一下就加入到最近浏览项目那里
                            UserManager.addRecentProid(getActivity(), id);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_bottom, 0);
                        }
                    }, 50);
                }
                break;
            }
        }
    };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_WHAT_ONFAILE_REFRESH) {
                adapter.setProData(UserManager.getRecentProject(getActivity()));
                adapter.notifyDataSetChanged();
                mSwipelayout.setRefreshing(false);
            } else if (msg.what == MSG_WHAT_ONSUCCESS_REFRESH) {
                mSwipelayout.setRefreshing(false);
            }
        }
    };

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

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.setProData(UserManager.getRecentProject(getActivity()));
            adapter.notifyDataSetChanged();
        }
    }

    protected void iniData(){
        proData.clear();
        msgData.clear();
        proData = UserManager.getRecentProject(getActivity());
        msgData.add("未处理消息的概括1好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊好长啊");
        msgData.add("未处理消息的概括2");
        msgData.add("未处理消息的概括3");
    }

    @Override
    public void refreshView(BottomToolBar toolBar) {
        if (isDetached()) return;
        if (getActivity() == null) return;
        toolBar.setText(new String[]{"", "", getString(R.string.action_newpro)});
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {

    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {
    }

    @Override
    public void controllright(BottomToolBar toolBar) {
        toolBar.reset(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(getActivity(), CreateProActivity.class));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void refreshProjects() {
        HttpClient.post(getActivity(), "project/relaproject", new RequestParams(), new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("data");
                    Project.insertOrUpdate(object.getJSONArray("projects"));
                    Message msg = new Message();
                    msg.what = MSG_WHAT_ONSUCCESS_REFRESH;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                Message msg = new Message();
                msg.what = MSG_WHAT_ONFAILE_REFRESH;
                mHandler.sendMessage(msg);
            }
        });
    }

}
