package com.scut.gof.coordinator.main.activity.task;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.BaseActivity;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Task;
import com.scut.gof.coordinator.main.thread.TaskExecutor;
import com.scut.gof.coordinator.main.widget.CircleImageView;
import com.scut.gof.coordinator.main.widget.CirclePrgbar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2015/12/10.
 */
public class TaskHierarchyActivity extends BaseActivity {

    final int REQUESTCODE_TASKDETAIL = 0;
    long tid;
    long proid;
    RecyclerView mRecyclerView;
    CirclePrgbar mCirprg;
    ChildTaskAdapter adapter;
    HashMap<Long, List<TaskDescriptor>> taskMap;
    Stack<Long> keyList;
    //开始的时候是不是传了proid呢？？
    boolean isProid;
    boolean isNetWorking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskhierarchy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("clicked");
                finish();
            }
        });
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mCirprg = (CirclePrgbar) findViewById(R.id.circleprgbar);
        iniData();
        adapter = new ChildTaskAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(TaskHierarchyActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(TaskHierarchyActivity.this).build());
        mRecyclerView.setAdapter(adapter);
        netGetChildTasks(isProid, isProid ? proid : tid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_TASKDETAIL && resultCode == TaskDetailActivity.RESULTCODE_DELETE) {
            long tid_deleted = data.getLongExtra("tid", 0);
            if (tid_deleted == 0) return;
            List<TaskDescriptor> list = taskMap.get(tid_deleted);
            //adapter里面删除
            adapter.removeItem(tid);
            //从源数据删除
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).tid == tid_deleted) {
                    list.remove(i);
                }
            }
        }
    }

    private void iniData() {
        taskMap = new HashMap<>();
        keyList = new Stack<>();
        tid = getIntent().getLongExtra("tid", 0);
        proid = getIntent().getLongExtra("proid", 0);
        if (tid == 0 && proid == 0) {
            toast("id错误");
            finish();
        }
        isProid = proid != 0;
        isNetWorking = false;
    }

    private void netGetChildTasks(boolean isProid, long id) {
        if (isNetWorking) return;
        isNetWorking = true;
        mCirprg.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        if (isProid) {
            params.put("tid", 0);
            params.put("proid", id);
        } else {
            params.put("tid", id);
        }
        HttpClient.post(TaskHierarchyActivity.this, "task/childtask", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    final JSONArray taskData = response.getJSONObject("data").getJSONArray("tasks");
                    TaskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            Task.insertOrUpdate(taskData);
                        }
                    });
                    List<TaskDescriptor> taskDescriptorList = new ArrayList<>();
                    for (int i = 0; i < taskData.length(); i++) {
                        taskDescriptorList.add(new TaskDescriptor(taskData.getJSONObject(i)));
                    }
                    long parentid = taskDescriptorList.get(0).parentid;
                    if (taskMap.containsKey(parentid)) {
                        taskMap.remove(parentid);
                    }
                    taskMap.put(parentid, taskDescriptorList);
                    if (adapter.getCurParentid() != 0) {
                        keyList.push(adapter.getCurParentid());
                    }
                    adapter.setTaskList(taskMap.get(parentid));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isNetWorking = false;
                if (mCirprg.getVisibility() == View.VISIBLE) {
                    mCirprg.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                isNetWorking = false;
                if (mCirprg.getVisibility() == View.VISIBLE) {
                    mCirprg.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyList.size() > 0) {
                adapter.setTaskList(taskMap.get(keyList.pop()));
                adapter.notifyDataSetChanged();
                return true;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    class ChildTaskAdapter extends RecyclerView.Adapter {
        List<TaskDescriptor> taskList = new ArrayList<>();

        public long getCurParentid() {
            if (taskList.size() != 0) {
                return taskList.get(0).parentid;
            } else {
                return 0;
            }
        }

        public void removeItem(long tid) {
            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).tid == tid) {
                    taskList.remove(i);
                    notifyItemRemoved(i);
                    return;
                }
            }
        }

        public void setTaskList(List<TaskDescriptor> taskList) {
            this.taskList = taskList;
        }

        public TaskDescriptor getItem(int position) {
            return taskList.get(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_childtaskhierarchy, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((MHolder) holder).mTvname.setText(getItem(position).name);
            ((MHolder) holder).mCiricon.setBackgroundResource(
                    getItem(position).hasChildtask ? R.drawable.assignment_color : R.drawable.single_assignment_color
            );
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItem(position).hasChildtask) {
                        if (taskMap.containsKey(getItem(position).tid)) {
                            keyList.push(getItem(position).parentid);
                            setTaskList(taskMap.get(getItem(position).tid));
                            notifyDataSetChanged();
                        } else {
                            netGetChildTasks(false, getItem(position).tid);
                        }
                    } else {
                        Intent intent = new Intent(TaskHierarchyActivity.this, TaskDetailActivity.class);
                        intent.putExtra("tid", getItem(position).tid);
                        startActivityForResult(intent, REQUESTCODE_TASKDETAIL);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }

        class MHolder extends RecyclerView.ViewHolder {
            CircleImageView mCiricon;
            TextView mTvname;

            public MHolder(View itemView) {
                super(itemView);
                mCiricon = (CircleImageView) itemView.findViewById(R.id.cir_icon);
                mTvname = (TextView) itemView.findViewById(R.id.tv_name);
            }
        }
    }

    class TaskDescriptor {
        long tid;
        String name;
        boolean hasChildtask;
        long parentid;

        public TaskDescriptor(JSONObject data) {
            try {
                this.tid = data.getInt("tid");
                this.name = data.getString("tname");
                this.hasChildtask = (data.getInt("childcount") > 0);
                this.parentid = data.getInt("parentid");
            } catch (JSONException e) {
                toast("缺少参数" + e.getCause());
            }

        }

    }
}
