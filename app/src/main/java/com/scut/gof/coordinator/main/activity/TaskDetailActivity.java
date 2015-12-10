package com.scut.gof.coordinator.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.query.Update;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.adapter.DetailInfoAdapter;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Task;
import com.scut.gof.coordinator.main.storage.model.User;
import com.scut.gof.coordinator.main.utils.ViewUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Administrator on 2015/12/7.
 */
public class TaskDetailActivity extends BaseActivity {

    public static final int RESULTCODE_DELETE = 0;
    public static final int REQUESTCODE_GETCATEGORY = 1;
    public static final int REQUESTCODE_CHECKCHILDTASK = 2;
    Task mTask;
    int iconRes[];
    int titles[];
    String contents[];
    RecyclerView mRecinfo;
    TextView mTvname;
    TextView mTvcontent;
    FloatingActionButton mBtnmark;
    DetailInfoAdapter adapter;
    WeakReference<Context> contextWeakReference;
    DetailInfoAdapter.OnListitemClick onListitem = new DetailInfoAdapter.OnListitemClick() {
        @Override
        public void onClick(final TextView textView, int titleResId) {
            switch (titleResId) {
                case R.string.text_creator: {
                    checkUserInfo();
                }
                break;
                case R.string.text_taskstatus: {
                    modifyStatus(textView);
                }
                break;
                case R.string.text_latest_starttime: {
                    if (!isEditable()) return;
                    modifyStarttime(textView);
                }
                break;
                case R.string.text_endtime: {
                    if (!isEditable()) return;
                    modifyEndtime(textView);
                }
                break;
                case R.string.text_taskpriority: {
                    if (!isEditable()) return;
                    modifyPriority(textView);
                }
                break;
                case R.string.text_numofpeople: {
                    new MaterialDialog.Builder(TaskDetailActivity.this)
                            .items("修改人数", "查看成员")
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                    if (which == 0) {
                                        modifyNumOfPeople(textView);
                                    } else if (which == 1) {
                                        checkPartners();
                                    }
                                }
                            })
                            .show();
                }
                break;
                case R.string.text_childtask: {
                    checkChildTask();
                }
                break;
                case R.string.text_taskcategory: {
                    if (!isEditable()) return;
                    modifyCategory();
                }
                break;
                case R.string.text_tag: {
                    if (!isEditable()) return;
                    modifyTag(textView);
                }
                break;
                case R.string.text_taskdesc: {
                    if (!isEditable()) return;
                    modifyDesc(textView);
                }
                break;
                default:
                    return;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetail);
        iniData();
        iniUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detailinfo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: {
                deleteTask();
            }
            break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_GETCATEGORY && resultCode == RESULT_OK) {
            String category = data.getStringExtra("category");
            adapter.modifyContent(R.string.text_taskcategory, category);
            netModifyBaseinfo("category", category);
        }
    }

    private void iniData() {
        long tid = getIntent().getLongExtra("tid", 0);
        if (tid == 0) finish();
        mTask = Task.getTaskById(tid);
        User creator = User.getUserById(mTask.getCreator());
        final int displaycount = 10;
        iconRes = new int[displaycount];
        titles = new int[displaycount];
        contents = new String[displaycount];
        int index = 0;
        int extraDataIndex = -1;
        HashMap<Integer, Object> extraData = new HashMap<>();
        titles[index] = R.string.text_creator;
        iconRes[index] = extraDataIndex;
        extraData.put(extraDataIndex--, creator.getThumbnailavatar());
        contents[index++] = creator.getName();
        titles[index] = R.string.text_taskstatus;
        iconRes[index] = R.drawable.stairs;
        contents[index++] = mTask.getStrStatus();
        titles[index] = R.string.text_latest_starttime;
        iconRes[index] = R.drawable.starttime;
        contents[index++] = mTask.getStrStarttime();
        titles[index] = R.string.text_endtime;
        iconRes[index] = R.drawable.endtime;
        contents[index++] = mTask.getStrEndtime();
        titles[index] = R.string.text_taskpriority;
        iconRes[index] = R.drawable.flag_color;
        contents[index++] = mTask.getStrPriority();
        titles[index] = R.string.text_numofpeople;
        iconRes[index] = R.drawable.group_color;
        contents[index++] = mTask.getPeoplecount() + " / " + mTask.getPeopleneedcount();
        titles[index] = R.string.text_childtask;
        iconRes[index] = R.drawable.assignment_color;
        contents[index++] = mTask.getDisplayChildtaskStatus();
        titles[index] = R.string.text_taskdesc;
        iconRes[index] = R.drawable.attach_color;
        contents[index++] = mTask.getDescription().equals("") ? "无" : mTask.getDescription();
        titles[index] = R.string.text_taskcategory;
        iconRes[index] = R.drawable.category;
        contents[index++] = mTask.getCategory();
        titles[index] = R.string.text_tag;
        iconRes[index] = R.drawable.tag;
        contents[index++] = mTask.getTag().equals("") ? "无" : mTask.getTag();
        adapter = new DetailInfoAdapter(contents, extraData, iconRes, titles, onListitem);
    }

    private boolean isEditable() {
        return mTask.getStatus() == 1 || mTask.getStatus() == 0;
    }

    private void checkPartners() {
        Intent intent = new Intent(this, UserSimpleListActivity.class);
        startActivity(intent);
    }

    private void checkChildTask() {
        Intent intent = new Intent(this, TaskHierarchyActivity.class);
        intent.putExtra("tid", mTask.getTid());
        startActivityForResult(intent, REQUESTCODE_CHECKCHILDTASK);
    }

    private void iniUI() {
        mRecinfo = (RecyclerView) findViewById(R.id.recyclerview);
        mTvname = (TextView) findViewById(R.id.tv_name);
        mTvcontent = (TextView) findViewById(R.id.tv_content);
        mBtnmark = (FloatingActionButton) findViewById(R.id.btn_fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout
                = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        toolbar.setTitle(null);
        collapsingToolbarLayout.setTitle(null);
        mRecinfo.setLayoutManager(new LinearLayoutManager(this));
        mRecinfo.setAdapter(adapter);
        setSupportActionBar(toolbar);
        mTvcontent.setText(mTask.getContent().equals("") ? "无" : mTask.getContent());
        mTvname.setText(mTask.getTname());
        if (mTask.isMark()) {
            mBtnmark.setImageResource(R.drawable.star_highlight);
        } else {
            mBtnmark.setImageResource(R.drawable.star);
        }
        mTvname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyTaskName();
            }
        });
        mTvcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyContent();
            }
        });
        mBtnmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netMarkorCancel();
            }
        });
    }

    private void modifyTaskName() {
        new MaterialDialog.Builder(this)
                .title("修改任务名")
                .input(null, mTask.getTname(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if ((input + "").equals(mTask.getTname())) {
                            return;
                        } else {
                            netModifyBaseinfo("tname", input + "");
                            mTvname.setText(input);
                        }
                    }
                })
                .inputRange(1, 20)
                .positiveText("确认")
                .negativeText("取消")
                .show();
    }

    private void modifyContent() {
        new MaterialDialog.Builder(this)
                .title("修改任务详情")
                .input(null, mTask.getContent(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if ((input + "").equals(mTask.getContent())) {
                            return;
                        } else {
                            netModifyBaseinfo("content", input + "");
                            mTvcontent.setText(input);
                        }
                    }
                })
                .inputRange(0, 100)
                .positiveText("确认")
                .negativeText("取消")
                .show();
    }

    private void modifyDesc(final TextView textView) {
        new MaterialDialog.Builder(this)
                .title("修改备注")
                .inputRange(0, 100)
                .input(null, mTask.getDescription(), true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if ((input + "").equals(mTask.getDescription())) {
                            return;
                        } else {
                            adapter.modifyContent(R.string.text_taskdesc, input + "");
                            netModifyBaseinfo("description", input);
                        }
                    }
                })
                .positiveText("确认")
                .negativeText("取消")
                .show();
    }

    private void deleteTask() {
        new MaterialDialog.Builder(this)
                .title("删除任务")
                .content("将不能找回任务的相关数据并同删除子任务和去除任务链关系")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        netChangeStatus(Task.TASKSTATUS_DELETED);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void checkUserInfo() {
        Intent intent = new Intent(this, BaseinfoActivity.class);
        intent.putExtra("uid", mTask.getCreator());
        startActivity(intent);
    }

    private void modifyTag(final TextView textView) {
        new MaterialDialog.Builder(TaskDetailActivity.this)
                .title("更改标签")
                .input(null, mTask.getTag(), true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if ((input + "").equals(mTask.getTag())) {
                            return;
                        } else {
                            adapter.modifyContent(R.string.text_tag, input + "");
                            netModifyBaseinfo("tag", input + "");
                        }
                    }
                })
                .inputRange(0, 8)
                .positiveText("确认")
                .negativeText("取消")
                .show();
    }

    private void modifyStatus(final TextView textView) {
        new MaterialDialog.Builder(TaskDetailActivity.this)
                .title("移至一阶段")
                .items(R.array.tasklist_tabtitles)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == mTask.getStatus()) return;
                        netChangeStatus(which);
                        adapter.modifyContent(R.string.text_taskstatus
                                , getResources().getStringArray(R.array.tasklist_tabtitles)[which]);
                        dialog.dismiss();
                    }
                })
                .show().setSelectedIndex(mTask.getStatus());
    }

    private void modifyStarttime(final TextView textView) {
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance(Locale.CHINA);
        DatePickerDialog dialog = new DatePickerDialog().newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar selectCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                long time = selectCalendar.getTimeInMillis();
                if (time > mTask.getPlanendtime()) {
                    toastWarn("比结束时间晚");
                    return;
                } else {
                    adapter.modifyContent(R.string.text_latest_starttime, ViewUtil.displayTime(time, 1));
                    netModifyBaseinfo("planstarttime", time);
                }
                view.dismiss();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setYearRange(2010, 2030);
        dialog.show(getFragmentManager(), "starttimedialog");
        dialog.vibrate(false);
    }

    private void modifyEndtime(final TextView textView) {
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance(Locale.CHINA);
        DatePickerDialog dialog = new DatePickerDialog().newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar selectCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                long time = selectCalendar.getTimeInMillis();
                if (time < mTask.getPlanstarttime()) {
                    toastWarn("比开始时间早");
                    return;
                } else {
                    adapter.modifyContent(R.string.text_endtime, ViewUtil.displayTime(time, 1));
                    netModifyBaseinfo("planendtime", time);
                }
                view.dismiss();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setYearRange(2010, 2030);
        dialog.vibrate(false);
        dialog.show(getFragmentManager(), "endtimedialog");
    }

    private void modifyPriority(final TextView textView) {
        new MaterialDialog.Builder(TaskDetailActivity.this)
                .title("选择优先级")
                .items(R.array.task_priority_order)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which + 1 == mTask.getPriority()) return;
                        netModifyBaseinfo("priority", which + 1);
                        adapter.modifyContent(R.string.text_taskpriority,
                                getResources().getStringArray(R.array.task_priority_order)[which]);
                        dialog.dismiss();
                    }
                })
                .show().setSelectedIndex(mTask.getPriority() - 1);
    }

    private void modifyNumOfPeople(final TextView textView) {
        new MaterialDialog.Builder(TaskDetailActivity.this)
                .title("任务人数")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("请输入1~999之间的整数", mTask.getPeopleneedcount() + "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        try {
                            int number = Integer.parseInt(input + "");
                            if (number < 1 || number > 999) {
                                toastWarn("请输入1~999之间的整数");
                                return;
                            }
                            if (number < mTask.getPeoplecount()) {
                                toastWarn("请先联系额外的成员退出任务");
                                return;
                            } else {
                                adapter.modifyContent(R.string.text_numofpeople,
                                        mTask.getPeoplecount() + " / " + number);
                                netModifyBaseinfo("peopleneedcount", number);
                            }
                        } catch (NumberFormatException e) {
                            toastWarn("请输入1~999之间的整数");
                        }
                    }
                })
                .inputRange(1, 3)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                    }
                })
                .show();
    }

    private void modifyCategory() {
        Intent intent = new Intent(this, TaskCategorySelectorActivity.class);
        intent.putExtra("tid", mTask.getTid());
        startActivityForResult(intent, REQUESTCODE_GETCATEGORY);
    }

    private void netMarkorCancel() {
        RequestParams params = new RequestParams();
        params.put("tid", mTask.getTid());
        params.put("mark", mTask.isMark() ? 0 : 1);
        contextWeakReference = new WeakReference<Context>(TaskDetailActivity.this);
        HttpClient.post(TaskDetailActivity.this, "task/markorcancel", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Context context = contextWeakReference.get();
                    if (context != null) {
                        mBtnmark.setImageResource(mTask.isMark() ? R.drawable.star : R.drawable.star_highlight);
                    }
                    JSONObject data = response.getJSONObject("data").getJSONObject("task");
                    new Update(Task.class).set("mark =" + data.getInt("mark"))
                            .where("tid=" + data.getLong("tid")).execute();
                    mTask = Task.getTaskById(data.getLong("tid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                toast("关注任务失败");
            }
        });
    }

    private void netChangeStatus(final int status) {
        RequestParams params = new RequestParams();
        params.put("tid", mTask.getTid());
        params.put("status", status);
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .progress(true, 0, false).build();
        dialog.setCanceledOnTouchOutside(false);
        if (status == 3) {
            dialog.show();
        }
        HttpClient.post(TaskDetailActivity.this, "task/changestatus", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Task.updateTaskStatus(response.getJSONObject("data").getJSONArray("tasks"));
                    if (status == 3) {
                        Intent intent = new Intent();
                        intent.putExtra("tid", mTask.getTid());
                        setResult(RESULTCODE_DELETE, intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void netModifyBaseinfo(String key, Object value) {
        RequestParams params = new RequestParams();
        params.put("tid", mTask.getTid());
        params.put(key, value);
        HttpClient.post(TaskDetailActivity.this, "task/modifytask", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Task task = Task.insertOrUpdate(response.getJSONObject("data").getJSONObject("task"));
                    if (task != null) mTask = task;
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
