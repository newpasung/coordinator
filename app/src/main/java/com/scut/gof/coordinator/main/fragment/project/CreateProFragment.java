package com.scut.gof.coordinator.main.fragment.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.CreateProActivity;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.net.RequestParamName;
import com.scut.gof.coordinator.main.utils.ViewUtil;
import com.scut.gof.coordinator.main.widget.BottomToolBar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2015/11/3.
 * 用来创建project
 */
public class CreateProFragment extends BaseFragment implements BottomBarController {

    Button mBtnstarttime;
    Button mBtnendtime;
    Spinner mSpcategory;
    EditText mEtname;
    EditText mEtdesc;
    DatePickerDialog pickerDialog;
    String[] categories;
    public CreateProFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categories = container.getContext().getResources().getStringArray(R.array.project_category);
        return inflater.inflate(R.layout.fragment_createpro, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnstarttime = (Button) view.findViewById(R.id.btn_starttime);
        mBtnendtime = (Button) view.findViewById(R.id.btn_endtime);
        mBtnstarttime.setTag(Long.MIN_VALUE);
        mBtnendtime.setTag(Long.MAX_VALUE);
        mSpcategory = (Spinner) view.findViewById(R.id.spinner_type);
        mEtdesc = (EditText) view.findViewById(R.id.et_desc);
        mEtname = (EditText) view.findViewById(R.id.et_name);
        iniDialog();
        iniListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categories = null;
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
                    if (time > (long) mBtnendtime.getTag()) {
                        toastWarn("结束时间应该更早");
                        return;
                    }
                    mBtnstarttime.setText(builder.toString());
                    mBtnstarttime.setTag(new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime().getTime());
                } else {
                    if (time < (long) mBtnstarttime.getTag()) {
                        toastWarn("结束时间应该更久");
                        mBtnendtime.setText(mBtnstarttime.getText().toString());
                        return;
                    }
                    mBtnendtime.setText(builder.toString());
                    mBtnendtime.setTag(time);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.setYearRange(2010, 2030);
    }

    //一些关于dialog和表单的点击关系
    protected void iniListener() {
        mBtnstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog.show(getFragmentManager(), "startdatepicker");
            }
        });
        mBtnendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog.show(getFragmentManager(), "enddatepicker");
            }
        });
        mSpcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpcategory.setTag(categories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void refreshView(BottomToolBar bottomToolBar) {
        bottomToolBar.setText(new String[]{getString(R.string.action_cancel)
                , getString(R.string.action_commit)
                , getString(R.string.action_moreconfig)});
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {
        getActivity().finish();
    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {
        if (acceptParam()) {
            LocalBrCast.sendBroadcast(getActivity(), CreateProActivity.BRCAST_KEY_NEWPRO);
        }
    }

    @Override
    public void controllright(BottomToolBar toolBar) {
        acceptParam();
    }

    /**
     * 判断输入是否可用，和设置act里的参数
     */
    private boolean acceptParam() {
        if (ViewUtil.isAllTextEmpty(mEtdesc, mEtname)) {
            toast("资料未填写完整");
            return false;
        }
        if ((long) mBtnendtime.getTag() == Long.MAX_VALUE) {
            toast("你需要选择结束时间");
            return false;
        }
        if ((long) mBtnstarttime.getTag() == Long.MIN_VALUE) {
            toast("你需要选择开始时间");
            return false;
        }
        ((CreateProActivity) getActivity()).addReqParams(RequestParamName.PROJECT_NAME, mEtname.getText().toString());
        ((CreateProActivity) getActivity()).addReqParams(RequestParamName.PROJECT_PLANSTARTTIME, mBtnstarttime.getText().toString());
        ((CreateProActivity) getActivity()).addReqParams(RequestParamName.PROJECT_PLANENDTIME, mBtnendtime.getText().toString());
        ((CreateProActivity) getActivity()).addReqParams(RequestParamName.PROJECT_DESCRIPTION, mEtdesc.getText().toString());
        ((CreateProActivity) getActivity()).addReqParams(RequestParamName.PROJECT_CATEGORY, (String) mSpcategory.getTag());
        return true;
    }

}
