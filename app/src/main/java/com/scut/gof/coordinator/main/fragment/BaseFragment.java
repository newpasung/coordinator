package com.scut.gof.coordinator.main.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.utils.DenstityUtil;

/**
 * Created by Administrator on 2015/10/31.
 */
public class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    protected void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    protected void toastWarn(String text) {
        Toast toast = new Toast(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(ApiUtil.getColor(getActivity(), R.color.colorAccent));
        toast.setGravity(Gravity.CENTER, 0, (int) ((float) DenstityUtil.getScreenHeight(getActivity()) * 0.382f));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(textView);
        toast.show();
    }

}
