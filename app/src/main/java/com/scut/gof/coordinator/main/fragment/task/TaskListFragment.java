package com.scut.gof.coordinator.main.fragment.task;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.TaskDetailActivity;
import com.scut.gof.coordinator.main.adapter.TaskSynopsisAdapter;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Task;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/7.
 */
public class TaskListFragment extends BaseFragment {
    final int REQUESTCODE_DETAIL = 0;
    RecyclerView mRecyclerview;
    SwipeRefreshLayout mSwiperefresh;
    TaskSynopsisAdapter mTaskAdapter;
    List<Task> taskArrayList;
    boolean shouldRefresh;
    long proid;
    int status;
    TaskSynopsisAdapter.MActionListener actionListener = new TaskSynopsisAdapter.MActionListener() {
        @Override
        public void onBtnright(long tid) {
        }

        @Override
        public void onItemViewClick(long tid) {
            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
            intent.putExtra("tid", tid);
            startActivityForResult(intent, REQUESTCODE_DETAIL);
        }
    };
    public TaskListFragment() {
        shouldRefresh = true;
    }

    public static TaskListFragment newInstance(long proid, int status) {
        Bundle args = new Bundle();
        args.putLong("proid", proid);
        args.putInt("status", status);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mTaskAdapter == null) mTaskAdapter = new TaskSynopsisAdapter(actionListener);
        View view = inflater.inflate(R.layout.fragment_tasklist, container, false);
        this.mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        this.mSwiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            proid = getArguments().getLong("proid");
            status = getArguments().getInt("status");
        }
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        if (mTaskAdapter.isDataEmpty()) {
            taskArrayList = Task.getTasksByStatus(proid, status);
            if (taskArrayList == null) {
                taskArrayList = new ArrayList<>();
            }
            mTaskAdapter.setTaskArrayList(this.taskArrayList);
        }
        mRecyclerview.setAdapter(mTaskAdapter);
        mRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        mSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netRefreshData();
            }
        });
        if (shouldRefresh) {
            netRefreshData();
            shouldRefresh = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_DETAIL && resultCode == TaskDetailActivity.RESULTCODE_DELETE) {
            this.taskArrayList.clear();
            this.taskArrayList = Task.getTasksByStatus(proid, status);
            mTaskAdapter.setTaskArrayList(taskArrayList);
            mTaskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setData(ArrayList<Task> list) {
        this.taskArrayList = list;
    }

    public void addTaskOnBottom(Task task) {
        if (taskArrayList.size() == 0) {
            taskArrayList.add(task);
            mTaskAdapter.setTaskArrayList(taskArrayList);
            mTaskAdapter.notifyDataSetChanged();
        } else {
            taskArrayList = mTaskAdapter.addTaskOnBottom(task);
        }
    }

    public void netRefreshData() {
        mSwiperefresh.setRefreshing(true);
        RequestParams params = new RequestParams();
        params.put("proid", proid);
        params.put("status", status);
        HttpClient.post(getActivity(), "task/tasksofpro", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    mTaskAdapter.setTaskArrayList(
                            Task.insertOrUpdate(response.getJSONObject("data").getJSONArray("tasks")));
                    mTaskAdapter.notifyDataSetChanged();
                    mSwiperefresh.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                mSwiperefresh.setRefreshing(false);
            }
        });
    }

}
