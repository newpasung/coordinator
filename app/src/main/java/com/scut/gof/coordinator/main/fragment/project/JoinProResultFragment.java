package com.scut.gof.coordinator.main.fragment.project;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.BaseFragment;

public class JoinProResultFragment extends BaseFragment {

    private TextView tv_invite_code;

    public JoinProResultFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_join_pro_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_invite_code = (TextView) view.findViewById(R.id.tv_invite_code);
    }
}
