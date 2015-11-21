package com.scut.gof.coordinator.main.fragment.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.lib.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.net.RequestParamName;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.widget.BottomToolBar;
import com.scut.gof.coordinator.main.widget.dialog.ChoiceDialog;
import com.scut.gof.coordinator.main.widget.dialog.InputDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/11/3.
 * 用来创建project
 */
public class CreateProFragment extends BaseFragment implements BottomBarController {

    Button mBtnstarttime;
    Button mBtnendtime;
    TextView mTvCategory;
    TextView mTvname;
    TextView mTvaffiliation;
    TextView mTvprincipal;
    TextView mTvdesc;
    ImageButton mBtnpic;
    int which = 0;
    DatePickerDialog pickerDialog;
    ChoiceDialog choiceDialog;
    InputDialog inputDialog;

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
        return inflater.inflate(R.layout.fragment_createpro, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnstarttime = (Button) view.findViewById(R.id.btn_starttime);
        mBtnendtime = (Button) view.findViewById(R.id.btn_endtime);
        mBtnpic = (ImageButton) view.findViewById(R.id.btn_img);
        mTvCategory = (TextView) view.findViewById(R.id.tv_category);
        mTvaffiliation = (TextView) view.findViewById(R.id.tv_affiliation);
        mTvdesc = (TextView) view.findViewById(R.id.tv_desc);
        mTvname = (TextView) view.findViewById(R.id.tv_name);
        mTvprincipal = (TextView) view.findViewById(R.id.tv_principal);
        iniDialog();
        iniListener();
    }

    protected void iniDialog() {
        Calendar calendar = Calendar.getInstance();
        pickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                StringBuilder builder = new StringBuilder();
                builder.append(year);
                builder.append("-");
                builder.append(monthOfYear + 1);
                builder.append("-");
                builder.append(dayOfMonth);
                if (which == 0)
                    mBtnstarttime.setText(builder.toString());
                else mBtnendtime.setText(builder.toString());
            }
        }, calendar.YEAR, calendar.MONTH, calendar.DAY_OF_MONTH);

        choiceDialog = new ChoiceDialog(getActivity());
        choiceDialog.addItem("校园体育活动", new ChoiceDialog.OnClickListener() {
            @Override
            public void didClick(ChoiceDialog dialog, String itemTitle) {
                mTvCategory.setText(itemTitle);
                dialog.dismiss();
            }
        })
                .addItem("校园歌唱比赛", new ChoiceDialog.OnClickListener() {
                    @Override
                    public void didClick(ChoiceDialog dialog, String itemTitle) {
                        mTvCategory.setText(itemTitle);
                        dialog.dismiss();
                    }
                })
                .addItem("建筑", new ChoiceDialog.OnClickListener() {
                    @Override
                    public void didClick(ChoiceDialog dialog, String itemTitle) {
                        mTvCategory.setText(itemTitle);
                        dialog.dismiss();
                    }
                })
                .addItem("abc", new ChoiceDialog.OnClickListener() {
                    @Override
                    public void didClick(ChoiceDialog dialog, String itemTitle) {
                        mTvCategory.setText(itemTitle);
                        dialog.dismiss();
                    }
                });
        inputDialog = new InputDialog(getActivity());
    }

    //一些关于dialog和表单的点击关系
    protected void iniListener() {
        mTvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDialog.show();
            }
        });
        mBtnpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAPic();
            }
        });
        mBtnstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = 0;
                pickerDialog.show(getFragmentManager(), "startdatepicker");
            }
        });
        mBtnendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = 1;
                pickerDialog.show(getFragmentManager(), "enddatepicker");
            }
        });
        mTvname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.show();
                inputDialog.onEnsure(mTvname);
            }
        });
        mTvprincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.show();
                inputDialog.onEnsure(mTvprincipal);
            }
        });
        mTvaffiliation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.show();
                inputDialog.onEnsure(mTvaffiliation);
            }
        });
        mTvdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.show();
                inputDialog.onEnsure(mTvdesc);
            }
        });

    }

    protected void getAPic() {
        Intent intent = new Intent(getActivity(), MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            Log.i("picdata", data.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void uploadInfo() {
        String name = mTvname.getText().toString();
        String affiliation = mTvaffiliation.getText().toString();
        String starttime = mBtnstarttime.getText().toString();
        String endtime = mBtnendtime.getText().toString();
        String category = mTvCategory.getText().toString();
        String desc = mTvdesc.getText().toString();
        String principal = mTvprincipal.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toastWarn(getString(R.string.warn_inputproname));
        }
        if (TextUtils.isEmpty(starttime) || TextUtils.isEmpty(endtime)) {
            toastWarn(getString(R.string.warn_chooseprotime));
        }
        RequestParams params = new RequestParams();
        params.put(RequestParamName.PROJECT_PRINCIPALID, principal);
        params.put(RequestParamName.PROJECT_AFFILIATION, affiliation);
        params.put(RequestParamName.PROJECT_CATEGORY, category);
        params.put(RequestParamName.PROJECT_DESCRIPTION, desc);
        params.put(RequestParamName.PROJECT_PLANENDTIME, endtime);
        params.put(RequestParamName.PROJECT_PLANSTARTTIME, starttime);
        params.put(RequestParamName.PROJECT_NAME, name);
        HttpClient.post(getActivity(), "project/newproject", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    Project.joinProject(getActivity(), data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                toast("failure");
            }
        });
    }

    @Override
    public void refreshView(BottomToolBar bottomToolBar) {
        bottomToolBar.setText(new String[]{"取消", "", "高级设置"});
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {
        getActivity().finish();
    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {

    }

    @Override
    public void controllright(BottomToolBar toolBar) {

    }
}
