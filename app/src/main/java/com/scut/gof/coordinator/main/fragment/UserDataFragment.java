package com.scut.gof.coordinator.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;

/**
 * Created by Administrator on 2015/10/31.
 */
public class UserDataFragment extends BaseFragment {

    public UserDataFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userdata,container,false);
    }
}
