package com.scut.gof.coordinator.main.fragment.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.JoinProActivity;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.BaseFragment;

public class JoinProFragment extends BaseFragment {
    private Button btn_scan;
    private Button btn_invite_code;
    private EditText et_invite_code;

    public JoinProFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_join_project, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_scan = (Button) view.findViewById(R.id.btn_scan);
        btn_invite_code = (Button) view.findViewById(R.id.btn_invite_code);
        et_invite_code = (EditText) view.findViewById(R.id.et_invite_code);
        initLinstener();
    }

    private void initLinstener() {
        btn_invite_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_invite_code.getText().toString())) {
                    toast("请输入邀请码");
                } else {
                    ((JoinProActivity) getActivity()).invite_code = et_invite_code.getText().toString();
                    LocalBrCast.sendBroadcast(getActivity(), LocalBrCast.PARAM_JOINPROJECT);
                }
            }
        });
    }
}
