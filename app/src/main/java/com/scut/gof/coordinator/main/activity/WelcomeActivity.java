package com.scut.gof.coordinator.main.activity;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.FirstWelcomeFragment;
import com.scut.gof.coordinator.main.storage.XManager;

public class WelcomeActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initUI();
    }

    private void initUI() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(!XManager.hasOpened(WelcomeActivity.this)){
            transaction.replace(R.id.welcome_content, new FirstWelcomeFragment());
        }
        transaction.commit();
    }


}
