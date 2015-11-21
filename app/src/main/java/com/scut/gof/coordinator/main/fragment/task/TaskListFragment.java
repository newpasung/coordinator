package com.scut.gof.coordinator.main.fragment.task;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.BaseSupportFragment;

/**
 * Created by Administrator on 2015/11/7.
 */
public class TaskListFragment extends BaseSupportFragment {

    TextView mTvtest;
    long proid = -1;

    public TaskListFragment() {
    }

    public static TaskListFragment newInstance(long proid) {
        Bundle args = new Bundle();
        args.putLong("proid", proid);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasklist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            proid = bundle.getLong("proid");
        }
        mTvtest = (TextView) view.findViewById(R.id.tv_test);
        mTvtest.setText(proid + "");
    }

}
