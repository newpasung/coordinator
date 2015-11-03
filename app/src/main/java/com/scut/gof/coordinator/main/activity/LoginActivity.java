package com.scut.gof.coordinator.main.activity;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.LoginFragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.login_content, new LoginFragment());
        transaction.commit();
    }
}
