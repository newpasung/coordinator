package com.scut.gof.coordinator.main.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.WelcomeFragment.FirstWelcomeFragment;
import com.scut.gof.coordinator.main.fragment.WelcomeFragment.NormalWelcomeFragment;
import com.scut.gof.coordinator.main.storage.XManager;

public class WelcomeActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initUI();
    }

    private void initUI() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(XManager.hasOpened(WelcomeActivity.this)){
            transaction.replace(R.id.welcome_content, new NormalWelcomeFragment());
        }else {
            transaction.replace(R.id.welcome_content, new FirstWelcomeFragment());
        }
        transaction.commit();
    }

}
