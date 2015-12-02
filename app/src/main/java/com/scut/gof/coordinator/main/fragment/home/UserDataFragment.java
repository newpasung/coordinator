package com.scut.gof.coordinator.main.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.BaseinfoActivity;
import com.scut.gof.coordinator.main.activity.LoginActivity;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.storage.StorageHelper;

/**
 * Created by Administrator on 2015/10/31.
 * 用户数据--一些更细节操作的入口
 */
public class UserDataFragment extends BaseFragment {

    TableRow mRowbaseinfo;
    TableRow mRowexitapp;
    public UserDataFragment() {
    }

    public static UserDataFragment newInstance() {

        Bundle args = new Bundle();

        UserDataFragment fragment = new UserDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userdata,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRowbaseinfo = (TableRow) view.findViewById(R.id.row_baseinfo);
        mRowexitapp = (TableRow) view.findViewById(R.id.row_exitapp);
        mRowbaseinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BaseinfoActivity.class));
            }
        });
        mRowexitapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("将注销账号")
                        .content("我们会删除你在本应用内的配置文件和缓存文件")
                        .positiveText("清楚")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                //来清空数据吧！
                                StorageHelper.clearData(getActivity());
                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setClass(getActivity(), LoginActivity.class);
                                dialog.dismiss();
                                startActivity(intent);
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
        });
    }
}
