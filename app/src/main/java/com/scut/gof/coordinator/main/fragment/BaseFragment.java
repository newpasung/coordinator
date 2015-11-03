package com.scut.gof.coordinator.main.fragment;

import android.app.Fragment;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/10/31.
 */
public class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    protected void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

}
