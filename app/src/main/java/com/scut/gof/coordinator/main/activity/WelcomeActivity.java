package com.scut.gof.coordinator.main.activity;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.user.LoginActivity;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.WelcomeFragment.FirstWelcomeFragment;
import com.scut.gof.coordinator.main.fragment.WelcomeFragment.NormalWelcomeFragment;
import com.scut.gof.coordinator.main.storage.XManager;

public class WelcomeActivity extends BaseActivity{

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocalBrCast.PARAM_WELACT_TRANSACTIVITY)) {
                transAct();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initUI();
        LocalBrCast.register(this, LocalBrCast.PARAM_WELACT_TRANSACTIVITY, receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBrCast.unregisterReceiver(this, receiver);
    }

    private void transAct() {
        boolean isLogined = XManager.isLogined(WelcomeActivity.this);//通过是否已登录决定进入的下一个activity
        Intent transIntnet = new Intent(WelcomeActivity.this, isLogined ? HomeActivity.class : LoginActivity.class);
        transIntnet.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        transIntnet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(transIntnet);
    }

    private void initUI() {
        XManager.setLastOpenTime(WelcomeActivity.this, System.currentTimeMillis());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(XManager.hasOpened(WelcomeActivity.this)){
            //十五分钟内重复打开不显示欢迎界面
            if (System.currentTimeMillis() - XManager.getLastOpenTime(WelcomeActivity.this) < 15 * 60 * 1000) {
                transAct();
                return;
            }
            transaction.replace(R.id.welcome_content, new NormalWelcomeFragment());
        }else {
            transaction.replace(R.id.welcome_content, new FirstWelcomeFragment());
        }
        transaction.commit();
    }

}
