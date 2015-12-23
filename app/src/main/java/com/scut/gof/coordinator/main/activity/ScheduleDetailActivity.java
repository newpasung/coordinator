package com.scut.gof.coordinator.main.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.libs.nereo.multi_image_selector.utils.TimeUtils;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.storage.model.Schedule;

/**
 * Created by Administrator on 2015/12/22.
 */
public class ScheduleDetailActivity extends BaseActivity {

    public static String EXTRA_LOCALSID = "localsid";
    Schedule schedule;

    TextView mTvstarttime;
    TextView mTvendtime;
    TextView mTvalarmtime;
    TextView mTvcontent;
    TextView mTvdesc;

    String content = "";
    String description = "";
    long starttime = 0;
    long endtime = 0;
    long alarmtime = Schedule.ALERTTIME_NOALERT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduledetail);
        long id = getIntent().getLongExtra(EXTRA_LOCALSID, 0);
        schedule = Schedule.getSchedleByLocalid(id);
        iniUI();
    }

    private void iniUI() {
        mTvcontent = (TextView) findViewById(R.id.tv_content);
        mTvdesc = (TextView) findViewById(R.id.tv_desc);
        mTvalarmtime = (TextView) findViewById(R.id.tv_alarmtime);
        mTvstarttime = (TextView) findViewById(R.id.tv_starttime);
        mTvendtime = (TextView) findViewById(R.id.tv_endtime);
        starttime = schedule.getStarttime();
        endtime = schedule.getEndtime();
        alarmtime = schedule.getAlerttime();
        content = schedule.getContent();
        description = schedule.getDescription();
        mTvstarttime.setText(TimeUtils.displayDetailTime(starttime));
        mTvendtime.setText(TimeUtils.displayDetailTime(endtime));
        if (alarmtime == Schedule.ALERTTIME_NOALERT) {
            mTvalarmtime.setText("不需要提醒");
        } else {
            mTvalarmtime.setText("在 " + TimeUtils.displayDetailTime(alarmtime) + " 提醒");
        }
        mTvcontent.setText(content);
        if (TextUtils.isEmpty(description)) {
            mTvdesc.setText("无");
        } else {
            mTvdesc.setText(description);
        }
    }

}
