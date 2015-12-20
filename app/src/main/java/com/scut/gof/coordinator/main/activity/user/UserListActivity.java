package com.scut.gof.coordinator.main.activity.user;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.adapter.UserlistAdapter;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.storage.model.RelaProject;
import com.scut.gof.coordinator.main.storage.model.User;
import com.scut.gof.coordinator.main.thread.TaskExecutor;
import com.scut.gof.coordinator.main.utils.CharacterParser;
import com.scut.gof.coordinator.main.utils.PinyinComparator;
import com.scut.gof.coordinator.main.widget.SideBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    SideBar mSidebar;
    List<Project> myProjects;
    boolean canSelect = false;//因为spinner默认回调一下
    int spin_lastselectindex;
    LinearLayoutManager linearLayoutManager;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SEARCH_SUCCESS) {
                adapter.updateData((List<User>) msg.obj);
            }
            else if (msg.what==MSG_NET_DATASUCCESS){
                contactData = localGetUsers(mSpinnerFilter.getSelectedItemPosition());
                adapter.setData(contactData);
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
        linearLayoutManager = new LinearLayoutManager(this);
        mSidebar = (SideBar) findViewById(R.id.sidebar);
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
        mRecyclerview.setLayoutManager(linearLayoutManager);
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
        mSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                mRecyclerview.scrollToPosition(adapter.getPositionForSection(s.toLowerCase().charAt(0)));
            }
        });
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mSidebar.setChosenText((char) adapter.getSectionForPosition(linearLayoutManager.findFirstVisibleItemPosition()));
                if (Math.abs(dy) > 2) {
                    if (mSidebar.getVisibility() == View.INVISIBLE) {
                        mSidebar.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, final int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mSidebar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (newState == RecyclerView.SCROLL_STATE_IDLE && !mSidebar.isTouching()) {
                                mSidebar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }, 300);
                }
            }
        });
        mSpinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!canSelect) {
                    spin_lastselectindex = position;
                    canSelect = true;
                    return;
                }
                if (position == spin_lastselectindex) {
                    return;
                }
                if (position == 0) {
                    contactData = localGetUsers(0);
                } else {
                    contactData = localGetUsers(myProjects.get(position - 1).getProid());
                }
                adapter.updateData(contactData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void iniData() {
        myProjects=getMyProjects();
        contactData = new ArrayList<>();
        contactData = localGetUsers(0);
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
        if (myProjects != null) {
            for (int i = 0; i < myProjects.size(); i++) {
                arrayAdapter.add(myProjects.get(i).getProname());
            }
        }
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
                                if (searchTarget.matches("[A-Za-z#]")) {
                                    if (contactData.get(i).getName_pinyin().contains(searchTarget)) {
                                        contacts.add(contactData.get(i));
                                    }
                                } else {
                                    if (contactData.get(i).getName().contains(searchTarget)) {
                                        contacts.add(contactData.get(i));
                                    }
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

    /**
     * @category 可以输入一个项目的id来筛选，或者输入0获取全部
     */
    private List<User> localGetUsers(long proid) {
        if (myProjects == null) ;
        String whereCause = proid == 0 ? "" : "relaproject.proid =" + proid + " and relaproject.uid = user.uid";
        List<User> users = new ArrayList<>();
        Cursor cursor = ActiveAndroid.getDatabase().query(true, "user,relaproject ", new String[]{"user.uid"}
                , whereCause, null, null, null, null, null);
        while (cursor.moveToNext()) {
            users.add(User.getUserById(cursor.getLong(cursor.getColumnIndex("uid"))));
        }
        if (users==null){
            users =new ArrayList<>();
        }
        return users;
    }


}
