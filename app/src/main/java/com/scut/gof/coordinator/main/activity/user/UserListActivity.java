package com.scut.gof.coordinator.main.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.adapter.UserlistAdapter;
import com.scut.gof.coordinator.main.models.SimpleContact;
import com.scut.gof.coordinator.main.thread.TaskExecutor;
import com.scut.gof.coordinator.main.utils.CharacterParser;
import com.scut.gof.coordinator.main.utils.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class UserListActivity extends BaseActivity {

    final CharacterParser characterParser = CharacterParser.getInstance();
    private final int MSG_SEARCH_SUCCESS = 1;
    RecyclerView mRecyclerview;
    List<SimpleContact> imitationData;
    UserlistAdapter adapter;
    Spinner mSpinnerFilter;
    EditText mEtsearch;
    String searchTarget;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SEARCH_SUCCESS) {
                adapter.updateData((List<SimpleContact>) msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        iniData();
        iniUi();
    }

    private void iniUi() {
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
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
        adapter = new UserlistAdapter();
        adapter.setData(imitationData);
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
                    adapter.updateData(imitationData);
                } else {
                    searching(characterParser.getSpelling(String.valueOf(s)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void iniData() {
        String avatar = UserManager.getLocalUser(UserListActivity.this).getThumbnailavatar();
        String[] testdata = getResources().getStringArray(R.array.test_userlist);
        imitationData = new ArrayList<>();
        for (int i = 0; i < testdata.length; i++) {
            SimpleContact contact = new SimpleContact();
            contact.setAvaterUrl(avatar);
            contact.setName(testdata[i]);
            String pinyin = characterParser.getSpelling(testdata[i]);
            String initial = pinyin.substring(0, 1).toUpperCase();
            if (initial.matches("[A-Za-z]")) {
                contact.setPinyin(pinyin);
            } else {
                contact.setPinyin("#" + pinyin);
            }
            imitationData.add(contact);
        }
        Collections.sort(imitationData, new PinyinComparator());
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
        arrayAdapter.add("All");
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
                        List<SimpleContact> contacts = new ArrayList<>();
                        String targetCopy = searchTarget;
                        for (int i = 0; i < imitationData.size(); i++) {
                            //就是，用户木有新输入
                            if (targetCopy.equals(searchTarget)) {
                                if (imitationData.get(i).getPinyin().contains(searchTarget)) {
                                    contacts.add(imitationData.get(i));
                                }
                            } else {
                                break;
                            }
                            if (i == imitationData.size() - 1) {
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

}
