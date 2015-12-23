package com.scut.gof.coordinator.main.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.project.JoinProFragment;
import com.scut.gof.coordinator.main.fragment.project.JoinProResultFragment;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Project;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinProActivity extends BaseActivity {

    public String invite_code;
    private JoinProFragment joinProFragment;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocalBrCast.PARAM_JOINPROJECT)) {
                sendRequest();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_pro);

        Uri intentData = getIntent().getData();
        if (intentData != null && intentData.getScheme().equals("coordinator") && intentData.getHost().equals("invide_code")) {
            invite_code = intentData.getQueryParameter("code");
            sendRequest();
        }
        iniUI();
        LocalBrCast.register(this, LocalBrCast.PARAM_JOINPROJECT, receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBrCast.unregisterReceiver(this, receiver);
    }

    private void iniUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.arrow_back);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getString(R.string.action_joinpro));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        joinProFragment = new JoinProFragment();

        getFragmentManager().beginTransaction().add(R.id.fragment_container, joinProFragment, "default").commit();
    }

    private void sendRequest() {
        RequestParams params = new RequestParams();
        params.put("invite_code", invite_code);
        HttpClient.get(this, "project/get_info_from_invite_code", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    Project project = Project.insertOrUpdate(data.getJSONObject("project"));
                    Bundle bundle = new Bundle();
                    bundle.putString("proname", project.getProname());
                    bundle.putString("prologo", project.getThumbnailLogo());
                    bundle.putString("invite_code", invite_code);
                    JoinProResultFragment joinProResultFragment = new JoinProResultFragment();
                    joinProResultFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, joinProResultFragment, "joinProResult").commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                toast(message);
            }
        });
    }
}
