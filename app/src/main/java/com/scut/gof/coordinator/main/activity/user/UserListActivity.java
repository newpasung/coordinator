package com.scut.gof.coordinator.main.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.adapter.UserlistAdapter;
import com.scut.gof.coordinator.main.models.SimpleContact;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.storage.model.RelaProject;
import com.scut.gof.coordinator.main.storage.model.User;
import com.scut.gof.coordinator.main.thread.TaskExecutor;
import com.scut.gof.coordinator.main.utils.CharacterParser;
import com.scut.gof.coordinator.main.utils.PinyinComparator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/17.
 */
public class UserListActivity extends BaseActivity {

    final CharacterParser characterParser = CharacterParser.getInstance();
    private final int MSG_SEARCH_SUCCESS = 1;
    private final int MSG_NET_DATASUCCESS=2;
    RecyclerView mRecyclerview;
    SwipeRefreshLayout mSwipeRefresh;
    List<User> contactData;
    UserlistAdapter adapter;
    Spinner mSpinnerFilter;
    EditText mEtsearch;
    String searchTarget;
    List<Project> myProjects;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SEARCH_SUCCESS) {
                adapter.updateData((List<User>) msg.obj);
            }
            else if (msg.what==MSG_NET_DATASUCCESS){
                contactData = localGetUsers(null);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        iniData();
        iniUi();
        if (contactData==null||contactData.size()==0){
            netGetUserlist();
        }
    }


    private void iniUi() {
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mSwipeRefresh =(SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSpinnerFilter = (Spinner) findViewById(R.id.spinner_filter);
        mEtsearch = (EditText) findViewById(R.id.et_search);
        mSpinnerFilter.setAdapter(getSpinnerAdapter());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtsearch.getVisibility() == View.VISIBLE) {
                    hideSearch();
                } else {
                    finish();
                }
            }
        });
        adapter = new UserlistAdapter(new UserlistAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(long uid) {
                Intent intent =new Intent(UserListActivity.this,UserinfoActivity.class);
                intent.putExtra(UserinfoActivity.EXTRA_UID,uid);
                startActivity(intent);
            }
        });
        adapter.setData(contactData);
        mRecyclerview.setAdapter(adapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mEtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start == 0 && before > 0) {
                    //删除到空白
                    adapter.updateData(contactData);
                } else {
                    searching(characterParser.getSpelling(String.valueOf(s)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netGetUserlist();
            }
        });
    }

    private void iniData() {
        myProjects=getMyProjects();
        contactData = new ArrayList<>();
        contactData = localGetUsers(null);
        Collections.sort(contactData, new PinyinComparator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: {
                if (mEtsearch.getVisibility() != View.VISIBLE) {
                    showSearch();
                }
            }
            break;
        }
        return true;
    }

    private BaseAdapter getSpinnerAdapter() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_whiteconner);
        arrayAdapter.add("全部");
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    private void hideSearch() {
        if (mEtsearch.getVisibility() == View.VISIBLE) {
            mEtsearch.setVisibility(View.GONE);
            mSpinnerFilter.setVisibility(View.VISIBLE);
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(mEtsearch.getWindowToken(), 0);
        }
    }

    private void showSearch() {
        if (mSpinnerFilter.getVisibility() == View.VISIBLE) {
            mSpinnerFilter.setVisibility(View.GONE);
            mEtsearch.setVisibility(View.VISIBLE);
            mEtsearch.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(mEtsearch, 0);
        }
    }

    private void searching(String inputpinyin) {
        if (TextUtils.isEmpty(searchTarget)) {
            searchTarget = inputpinyin;
            TaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    while (!TextUtils.isEmpty(searchTarget)) {
                        List<User> contacts = new ArrayList<>();
                        String targetCopy = searchTarget;
                        for (int i = 0; i < contactData.size(); i++) {
                            //就是，用户木有新输入
                            if (targetCopy.equals(searchTarget)) {
                                if (contactData.get(i).getName_pinyin().contains(searchTarget)) {
                                    contacts.add(contactData.get(i));
                                }
                            } else {
                                break;
                            }
                            if (i == contactData.size() - 1) {
                                Message msg = new Message();
                                msg.what = MSG_SEARCH_SUCCESS;
                                msg.obj = contacts;
                                mHandler.sendMessage(msg);
                                searchTarget = null;
                            }
                        }

                    }
                }
            });
        } else {
            searchTarget = inputpinyin;
        }
    }

    private void netGetUserlist(){
        mSwipeRefresh.setRefreshing(true);
        HttpClient.post(UserListActivity.this, "relation/list", null, new JsonResponseHandler() {
            @Override
            public void onSuccess(final JSONObject response) {
                mSwipeRefresh.setRefreshing(false);
                TaskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RelaProject.clearData();//因为没有标记只能清空
                                User.insertOrUpdateSimply(response.getJSONObject("data").getJSONArray("users"));
                                Project.insertOrUpdate(response.getJSONObject("data").getJSONArray("projects"));
                                Message msg =new Message();
                                msg.what = MSG_NET_DATASUCCESS;
                                mHandler.sendMessage(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            }
            @Override
            public void onFailure(String message, String for_param) {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private List<Project> getMyProjects(){
        List<Project> projects= new Select().from(Project.class)
                .innerJoin(RelaProject.class)
                .on("relaproject.proid = project.proid " +
                        "and relaproject.uid =" + UserManager.getUserid(UserListActivity.this))
                .execute();
        if (projects==null){
            projects =new ArrayList<>();
        }
        return projects;
    }

    /**@category 可以输入一个项目的id来筛选，或者输入null获取全部*/
    private List<User> localGetUsers(String category){
        if (myProjects==null){
            myProjects=getMyProjects();
            if (myProjects.size()==0){
                return new ArrayList<>(0);
            }
        }
        StringBuilder onCondition=new StringBuilder();
        onCondition.append("user.uid=relaproject.uid and relaproject.proid ");
        if (TextUtils.isEmpty(category)){
            onCondition.append("in(");
            for (int i=0;i<myProjects.size();i++){
                Project project =myProjects.get(i);
                onCondition.append(project.getProid());
                if (i!=myProjects.size()-1){
                    //最后一个不用
                    onCondition.append(",");
                }
            }
            onCondition.append(")");
        }else{
            onCondition.append(" = ");
            onCondition.append(category);
        }
        List<User> users=new Select()
                .from(User.class)
                .join(RelaProject.class)
                .on(onCondition.toString())
                .execute();
        if (users==null){
            users =new ArrayList<>();
        }
        ActiveAndroid.getDatabase().query()
        return users;
    }

}
