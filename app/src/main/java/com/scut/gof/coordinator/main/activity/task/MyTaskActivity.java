package com.scut.gof.coordinator.main.activity.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.BaseActivity;
import com.scut.gof.coordinator.main.adapter.PullupDecoratorAdapter;
import com.scut.gof.coordinator.main.adapter.TaskSynopsisAdapter;
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
 * Created by Administrator on 2015/12/13.
 */
public class MyTaskActivity extends BaseActivity {

    final int REQUESTCODE_TASKDETAIL = 1;
    RecyclerView mRecyclerview;
    TaskSynopsisAdapter adapter;
    PullupDecoratorAdapter adapterDecorator;
    SwipeRefreshLayout mSwiperefresh;
    List<Task> taskArrayList;
    WeakReference<Context> contextWeakReference;
    TaskSynopsisAdapter.MActionListener listener = new TaskSynopsisAdapter.MActionListener() {
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
            Intent intent = new Intent(MyTaskActivity.this, TaskDetailActivity.class);
            intent.putExtra("tid", tid);
            startActivityForResult(intent, REQUESTCODE_TASKDETAIL);
        }
    };
    PullupDecoratorAdapter.OnLoadMoreListener loadMoreListener = new PullupDecoratorAdapter.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            netLoadMoreMytask();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytask);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mSwiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        adapter = new TaskSynopsisAdapter(listener);
        adapterDecorator = new PullupDecoratorAdapter(adapter, loadMoreListener);
        mRecyclerview.setAdapter(adapterDecorator);
        mRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        netRefreshMytask();
        taskArrayList = Task.getMytasks();
        if (taskArrayList == null) {
            taskArrayList = new ArrayList<>();
        }
        adapter.setTaskArrayList(taskArrayList);
        mSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netRefreshMytask();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_TASKDETAIL && resultCode == TaskDetailActivity.RESULTCODE_DELETE) {
            long tid_deleted = data.getLongExtra("tid", 0);
            if (tid_deleted == 0) {
                adapterDecorator.notifyChanged();
            } else {
                int index = 0;
                for (int i = 0; i < taskArrayList.size(); i++) {
                    if (taskArrayList.get(i).getTid() == tid_deleted) {
                        index = i;
                        break;
                    }
                }
                taskArrayList.remove(index);
                adapterDecorator.notifyRemoved(index + 1);
            }
        }
    }

    public void netRefreshMytask() {
        mSwiperefresh.setRefreshing(true);
        RequestParams params = new RequestParams();
        HttpClient.post(this, "task/mytasks", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                mSwiperefresh.setRefreshing(false);
                try {
                    taskArrayList = Task.insertOrUpdate(response.getJSONObject("data").getJSONArray("tasks"));
                    adapter.setTaskArrayList(taskArrayList);
                    if (taskArrayList.size() > 9) {
                        adapterDecorator.canShowLoadMoreView(true);
                    }
                    adapterDecorator.notifyChanged();
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

    public void netLoadMoreMytask() {
        RequestParams params = new RequestParams();
        params.put("maxtid", taskArrayList.get(taskArrayList.size() - 1).getTid());
        HttpClient.post(this, "task/mytasks", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                adapterDecorator.setLoadMoreFinished();
                try {
                    ArrayList<Task> newData = Task.insertOrUpdate(response.getJSONObject("data").getJSONArray("tasks"));
                    taskArrayList.addAll(newData);
                    if (newData.size() > 9) {
                        adapterDecorator.canShowLoadMoreView(true);
                    }
                    adapter.setTaskArrayList(taskArrayList);
                    adapterDecorator.notifyChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                adapterDecorator.setLoadMoreFinished();
            }
        });
    }

    public void netJoinTask(final long tid) {
        contextWeakReference = new WeakReference<Context>(this);
        RequestParams requestParam = new RequestParams();
        requestParam.put("tid", tid);
        HttpClient.post(this, "task/jointask", requestParam, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Task.insertOrUpdate(response.getJSONObject("data").getJSONObject("task"));
                    adapterDecorator.notifyChanged();
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
        contextWeakReference = new WeakReference<Context>(this);
        RequestParams requestParam = new RequestParams();
        requestParam.put("tid", tid);
        HttpClient.post(this, "task/quittask", requestParam, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Task task = Task.insertOrUpdate(response.getJSONObject("data").getJSONObject("task"));
                    adapterDecorator.notifyChanged();
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
