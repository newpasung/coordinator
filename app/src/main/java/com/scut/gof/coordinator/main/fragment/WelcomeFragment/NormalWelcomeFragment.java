package com.scut.gof.coordinator.main.fragment.WelcomeFragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.HomeActivity;
import com.scut.gof.coordinator.main.fragment.BaseFragment;

import java.util.Timer;
import java.util.TimerTask;

public class NormalWelcomeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_normal_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //延时2秒后进入登录界面或主页（取决于是否已经登录）
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }, 2000);

    }
}
