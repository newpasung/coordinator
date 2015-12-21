package com.scut.gof.coordinator.main.fragment.project;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.widget.CircleImageView;

import org.json.JSONObject;


public class JoinProResultFragment extends BaseFragment {

    private CircleImageView iv_prologo;
    private TextView tv_proname;
    private Button btn_join_pro;

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

        iv_prologo = (CircleImageView) view.findViewById(R.id.pro_logo);
        tv_proname = (TextView) view.findViewById(R.id.tv_proname);
        btn_join_pro = (Button) view.findViewById(R.id.btn_join_pro);
        PicassoProxy.loadAvatar(getActivity(), getArguments().getString("prologo"), iv_prologo);
        tv_proname.setText(getArguments().getString("proname"));

        btn_join_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams("invite_code", getArguments().getString("invite_code"));
                HttpClient.post(getActivity(), "project/join_with_invite_code", params, new JsonResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        toast("加入成功");
                    }

                    @Override
                    public void onFailure(String message, String for_param) {
                        toast("加入失败");
                    }
                });
            }
        });
    }

}
