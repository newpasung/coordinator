package com.scut.gof.coordinator.main.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.storage.XManager;
import com.scut.gof.coordinator.main.widget.Switcher;

/**
 * Created by Administrator on 2015/11/2.
 */
public class SettingFragment extends BaseFragment {

    Switcher mSwithcersync;
    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mSwithcersync = (Switcher) view.findViewById(R.id.switcher);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (XManager.isAutoSync(getActivity())) {
            mSwithcersync.setChecked(true);
        } else {
            mSwithcersync.setChecked(false);
        }
        mSwithcersync.setOncheckListener(new Switcher.OnCheckListener() {
            @Override
            public void onCheck(Switcher view, boolean check) {
                XManager.setAutoSync(getActivity(), check);
            }
        });
    }

}
