package com.scut.gof.coordinator.main.fragment.task;


import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Task;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
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
    WeakReference<Context> contextWeakReference;
    TaskSynopsisAdapter.MActionListener actionListener = new TaskSynopsisAdapter.MActionListener() {
        @Override
        public void onBtnright(Task task) {
            if (task.getRole() == 2) {
                netQuitTask(task.getTid());
            } else {
                netJoinTask(task.getTid());
            }
        }

        @Override
        public void onItemViewClick(long tid) {
            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
            intent.putExtra("tid", tid);
            startActivityForResult(intent, REQUESTCODE_DETAIL);
        }
    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            taskArrayList = Task.getTasksByStatus(proid, status);
            mTaskAdapter.setTaskArrayList(taskArrayList);
            mTaskAdapter.notifyDataSetChanged();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBrCast.register(getActivity(), LocalBrCast.PARAM_REFRESHADAPTER, receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBrCast.unregisterReceiver(getActivity(), receiver);
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
                    taskArrayList = Task.insertOrUpdate(response.getJSONObject("data").getJSONArray("tasks"));
                    mTaskAdapter.setTaskArrayList(taskArrayList);
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

    public void netJoinTask(final long tid) {
        contextWeakReference = new WeakReference<Context>(getActivity());
        RequestParams params = new RequestParams();
        params.put("tid", tid);
        HttpClient.post(getActivity(), "task/jointask", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Task task = Task.insertOrUpdate(response.getJSONObject("data").getJSONObject("task"));
                    if (task.getStatus() != status) {
                        /*for (int i =0 ;i<taskArrayList.size();i++){
                            if (taskArrayList.get(i).getTid()==task.getTid()){
                                taskArrayList.remove(i);
                                mTaskAdapter.setTaskArrayList(taskArrayList);
                                break;
                            }
                        }*/
                        Context context = contextWeakReference.get();
                        if (context != null)
                            LocalBrCast.sendBroadcast(context, LocalBrCast.PARAM_REFRESHADAPTER);
                    } else {
                        taskArrayList = Task.getTasksByStatus(proid, status);
                        mTaskAdapter.setTaskArrayList(taskArrayList);
                    }
                    mTaskAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {

            }
        });
    }

    public void netQuitTask(final long tid) {
        contextWeakReference = new WeakReference<Context>(getActivity());
        RequestParams params = new RequestParams();
        params.put("tid", tid);
        HttpClient.post(getActivity(), "task/quittask", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Task task = Task.insertOrUpdate(response.getJSONObject("data").getJSONObject("task"));
                    if (task.getStatus() != status) {
                        /*for (int i =0 ;i<taskArrayList.size();i++){
                            if (taskArrayList.get(i).getTid()==task.getTid()){
                                taskArrayList.remove(i);
                                mTaskAdapter.setTaskArrayList(taskArrayList);
                                break;
                            }
                        }*/
                        Context context = contextWeakReference.get();
                        if (context != null)
                            LocalBrCast.sendBroadcast(context, LocalBrCast.PARAM_REFRESHADAPTER);
                    } else {
                        taskArrayList = Task.getTasksByStatus(proid, status);
                        mTaskAdapter.setTaskArrayList(taskArrayList);
                    }
                    mTaskAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {

            }
        });
    }

}
