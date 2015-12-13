package com.scut.gof.coordinator.main.fragment.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.task.CreateTaskActivity;
import com.scut.gof.coordinator.main.activity.task.TaskCategorySelectorActivity;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.widget.BottomToolBar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2015/12/6.
 */
public class CreateBaseTaskFragment extends BaseFragment implements BottomBarController {

    final int REQUESTCODE_GETCATEGORY = 1;
    DatePickerDialog pickerDialog;
    //用来点击的控件
    RelativeLayout mRlstatime;
    RelativeLayout mRlendtime;
    RelativeLayout mRlcategory;
    long proid;
    private EditText mEtname;
    private EditText mEtcontent;
    private EditText mEtdesc;
    private EditText mEttag;
    private TextView mTvstarttime;
    private TextView mTvendtime;
    private TextView mTvcategory;
    private Spinner mSppriority;
    private boolean canNetUpload;

    public static CreateBaseTaskFragment newInstance(long proid) {
        Bundle args = new Bundle();
        args.putLong("proid", proid);
        CreateBaseTaskFragment fragment = new CreateBaseTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createbasetask, container, false);
        mEtname = (EditText) view.findViewById(R.id.et_name);
        mEtcontent = (EditText) view.findViewById(R.id.et_content);
        mEtdesc = (EditText) view.findViewById(R.id.et_desc);
        mEttag = (EditText) view.findViewById(R.id.et_tag);
        mSppriority = (Spinner) view.findViewById(R.id.spinner_priority);
        mTvstarttime = (TextView) view.findViewById(R.id.tv_starttime);
        mTvendtime = (TextView) view.findViewById(R.id.tv_endtime);
        mTvcategory = (TextView) view.findViewById(R.id.tv_category);
        mRlstatime = (RelativeLayout) view.findViewById(R.id.rl_starttime_selector);
        mRlendtime = (RelativeLayout) view.findViewById(R.id.rl_endtime_selector);
        mRlcategory = (RelativeLayout) view.findViewById(R.id.rl_category_selector);
        mTvstarttime.setTag(Long.MIN_VALUE);
        mTvendtime.setTag(Long.MAX_VALUE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        proid = getArguments().getLong("proid");
        canNetUpload = false;
        iniDialog();
        iniListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_GETCATEGORY && resultCode == Activity.RESULT_OK) {
            mTvcategory.setText(data.getStringExtra("category"));
        }
    }

    private void iniListener() {
        mEtname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtname.setError(null);
            }
        });
        mRlendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pickerDialog.show(getFragmentManager(), "enddatepicker");
                    }
                }, 80);
            }
        });
        mRlstatime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pickerDialog.show(getFragmentManager(), "startdatepicker");
                    }
                }, 80);
            }
        });
        mRlcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TaskCategorySelectorActivity.class);
                intent.putExtra("proid", proid);
                startActivityForResult(intent, REQUESTCODE_GETCATEGORY);
            }
        });
        mSppriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mSppriority.setTag(2);
                }
                //紧急
                if (position == 1) {
                    mSppriority.setTag(1);
                }
                //低优先级
                if (position == 2) {
                    mSppriority.setTag(3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    protected void iniDialog() {
        final GregorianCalendar calendar = new GregorianCalendar(Locale.CHINA);
        pickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar endCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                long time = endCalendar.getTime().getTime();
                StringBuilder builder = new StringBuilder();
                builder.append(year);
                builder.append("-");
                builder.append(monthOfYear + 1);
                builder.append("-");
                builder.append(dayOfMonth);
                //下面包括了简单的辨别"起始时间"的逻辑
                if (view.getTag().equals("startdatepicker")) {
                    if (time > (long) mTvendtime.getTag()) {
                        mTvstarttime.setText(mTvendtime.getText().toString());
                        mTvstarttime.setTag(mTvendtime.getTag());
                        toastWarn("开始时间应该更早");
                        return;
                    }
                    mTvstarttime.setText(builder.toString());
                    mTvstarttime.setTag(new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime().getTime());
                } else {
                    if (time < (long) mTvstarttime.getTag()) {
                        toastWarn("结束时间应该更久");
                        mTvendtime.setText(mTvstarttime.getText().toString());
                        mTvendtime.setTag(mTvstarttime.getTag());
                        return;
                    }
                    mTvendtime.setText(builder.toString());
                    mTvendtime.setTag(time);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.setYearRange(2010, 2030);
    }

    @Override
    public void refreshView(BottomToolBar bottomToolBar) {
        bottomToolBar.setText(new String[]{getString(R.string.action_cancel)
                , getString(R.string.action_commit), getString(R.string.action_moreconfig)});
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {
        getActivity().finish();
    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {
        if (acceptParams()) {
            canNetUpload = true;
            LocalBrCast.sendBroadcast(getActivity(), LocalBrCast.PARAM_NEWTASK);
        }
    }

    @Override
    public void controllright(BottomToolBar toolBar) {
        canNetUpload = acceptParams();
    }

    private boolean acceptParams() {
        if (TextUtils.isEmpty(mEtname.getText().toString())) {
            mEtname.setError("必填");
            mEtname.requestFocus();
            return false;
        } else {
            ((CreateTaskActivity) getActivity()).addReqParams("tname", mEtname.getText().toString());
        }
        if ((long) mTvstarttime.getTag() == Long.MIN_VALUE) {
            toastWarn("必须选择开始时间");
            return false;
        } else {
            ((CreateTaskActivity) getActivity()).addReqParams("planstarttime", String.valueOf((long) mTvstarttime.getTag()));
        }
        if ((long) mTvendtime.getTag() == Long.MAX_VALUE) {
            toastWarn("必须选择开始时间");
            return false;
        } else {
            ((CreateTaskActivity) getActivity()).addReqParams("planendtime", String.valueOf((long) mTvendtime.getTag()));
        }
        String content = mEtcontent.getText().toString();
        content = TextUtils.isEmpty(content) ? "" : content;
        String desc = mEtdesc.getText().toString();
        desc = TextUtils.isEmpty(desc) ? "" : desc;
        String priority = String.valueOf((int) mSppriority.getTag());
        String category = mTvcategory.getText().toString();
        String tag = TextUtils.isEmpty(mEttag.getText().toString()) ? "" : mEttag.getText().toString();
        ((CreateTaskActivity) getActivity()).addReqParams("content", content);
        ((CreateTaskActivity) getActivity()).addReqParams("priority", priority);
        ((CreateTaskActivity) getActivity()).addReqParams("description", desc);
        ((CreateTaskActivity) getActivity()).addReqParams("category", category);
        ((CreateTaskActivity) getActivity()).addReqParams("tag", tag);
        return true;
    }

    public boolean canUpload() {
        return this.canNetUpload;
    }

}
