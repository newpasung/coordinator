package com.scut.gof.coordinator.main.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.BaseinfoActivity;
import com.scut.gof.coordinator.main.fragment.BaseFragment;

/**
 * Created by Administrator on 2015/10/31.
 * 用户数据--一些更细节操作的入口
 */
public class UserDataFragment extends BaseFragment {

    TableRow mRowbaseinfo;
    public UserDataFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userdata,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRowbaseinfo = (TableRow) view.findViewById(R.id.row_baseinfo);
        mRowbaseinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BaseinfoActivity.class));
            }
        });
    }
}
