package com.scut.gof.coordinator.main.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.FragmentTransactionExtended;
import com.scut.gof.coordinator.main.fragment.LoginFragment.LoginFragment;
import com.scut.gof.coordinator.main.fragment.LoginFragment.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    private Fragment curFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private BroadcastReceiver showRegisterBroadcastReceiver;
    private BroadcastReceiver showLoginBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        initBroadcastReceiver();
    }

    private void initUI() {
        loginFragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.login_content);
        curFragment = loginFragment;
    }

    private void initBroadcastReceiver() {
        showRegisterBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (registerFragment == null){
                    registerFragment = new RegisterFragment();
                }
                replaceFragment(registerFragment);
            }
        };

        showLoginBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (loginFragment == null){
                    loginFragment = new LoginFragment();
                }
                replaceFragment(loginFragment);
            }
        };

        LocalBrCast.register(this, "show register fragment", showRegisterBroadcastReceiver);
        LocalBrCast.register(this, "show login fragment", showLoginBroadcastReceiver);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended
                = new FragmentTransactionExtended(this, fragmentTransaction, curFragment, fragment, R.id.login_content);
        fragmentTransactionExtended.addTransition(FragmentTransactionExtended.SLIDE_HORIZONTAL);
        fragmentTransactionExtended.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBrCast.unregisterReceiver(this, showRegisterBroadcastReceiver);
        LocalBrCast.unregisterReceiver(this, showLoginBroadcastReceiver);
    }
}
