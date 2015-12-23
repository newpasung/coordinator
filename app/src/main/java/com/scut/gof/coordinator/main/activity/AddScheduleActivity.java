package com.scut.gof.coordinator.main.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.libs.nereo.multi_image_selector.utils.TimeUtils;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.XManager;
import com.scut.gof.coordinator.main.storage.model.Schedule;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2015/12/22.
 */
public class AddScheduleActivity extends BaseActivity {

    TextView mTvstarttime;
    TextView mTvendtime;
    TextView mTvalarmtime;
    EditText mEtcontent;
    EditText mEtdesc;

    String content = "";
    String description = "";
    long starttime = 0;
    long endtime = 0;
    long alarmtime = Schedule.ALERTTIME_NOALERT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addschedule);
        iniUI();
        iniListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (acceptParam()) {
            localAddSchedule();
            if (XManager.isAutoSync(AddScheduleActivity.this)) {
                netAddSchedule();
            }
        }
        return true;
    }

    private void iniUI() {
        mEtcontent = (EditText) findViewById(R.id.et_content);
        mEtdesc = (EditText) findViewById(R.id.et_desc);
        mTvalarmtime = (TextView) findViewById(R.id.tv_alarmtime);
        mTvstarttime = (TextView) findViewById(R.id.tv_starttime);
        mTvendtime = (TextView) findViewById(R.id.tv_endtime);
        starttime = System.currentTimeMillis();
        endtime = System.currentTimeMillis() + 3600 * 1000 * 24;
        mTvstarttime.setText(TimeUtils.displayDetailTime(starttime));
        mTvendtime.setText(TimeUtils.displayDetailTime(endtime));
        mTvstarttime.setTag(starttime);
        mTvendtime.setTag(endtime);
        mTvalarmtime.setTag(0l);
        mTvalarmtime.setText("不需要提醒");
    }

    private void iniListener() {
        View.OnClickListener getTimeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(v);
            }
        };
        mTvstarttime.setOnClickListener(getTimeListener);
        mTvendtime.setOnClickListener(getTimeListener);
        mTvalarmtime.setOnClickListener(getTimeListener);
    }

    public boolean acceptParam() {
        content = mEtcontent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mEtcontent.setError("做点什么呢？");
            return false;
        }
        starttime = (long) mTvstarttime.getTag();
        endtime = (long) mTvendtime.getTag();
        if (endtime < starttime) {
            toastWarn("结束时间不能比开始时间早");
            return false;
        }
        alarmtime = (long) mTvalarmtime.getTag();
        description = mEtdesc.getText().toString();
        return true;
    }

    public void getTime(View view) {
        final GregorianCalendar selectedCalendar = new GregorianCalendar();
        GregorianCalendar curCalendar = (GregorianCalendar) GregorianCalendar.getInstance(Locale.CHINA);
        curCalendar.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog dateDialog = new DatePickerDialog();
        dateDialog.vibrate(false);
        dateDialog.setYearRange(2010, 2030);
        final TimePickerDialog timeDialog = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedCalendar.set(Calendar.MINUTE, minute);
                view.setTag(selectedCalendar.getTimeInMillis());
            }
        }, curCalendar.get(Calendar.HOUR_OF_DAY), curCalendar.get(Calendar.MINUTE), true);
        timeDialog.setCanceledOnTouchOutside(false);
        dateDialog.initialize(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                selectedCalendar.set(year, monthOfYear, dayOfMonth);
                timeDialog.show();
            }
        }, curCalendar.get(Calendar.YEAR), curCalendar.get(Calendar.MONTH), curCalendar.get(Calendar.DAY_OF_MONTH));

        dateDialog.show(getFragmentManager(), "date");
    }

    public void localAddSchedule() {
        Schedule.addLocalSchedule(content, starttime, endtime, alarmtime, description);
    }

    public void netAddSchedule() {
        RequestParams params = new RequestParams();
        params.put("content", content);
        params.put("starttime", starttime);
        params.put("endtime", endtime);
        if (!TextUtils.isEmpty(description)) {
            params.put("description", description);
        }
        params.put("alerttime", alarmtime);
        HttpClient.post(AddScheduleActivity.this, "schedule/new", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Schedule.insertOrUpdate(response.getJSONObject("data").getJSONObject("schedule"));
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
