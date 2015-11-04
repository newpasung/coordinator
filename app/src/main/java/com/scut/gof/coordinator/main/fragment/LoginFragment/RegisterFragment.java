package com.scut.gof.coordinator.main.fragment.LoginFragment;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.animation.XRotationAnimation;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.net.RequestParamName;

import org.json.JSONObject;

public class RegisterFragment extends BaseFragment {

    private TextInputLayout phoneInputLayout;
    private TextInputLayout passwordInputLayout;
    private Button registerBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initListener();
    }

    private void initUI(View view) {

        phoneInputLayout = (TextInputLayout) view.findViewById(R.id.phone);

        passwordInputLayout = (TextInputLayout) view.findViewById(R.id.password);

        registerBtn = (Button) view.findViewById(R.id.btn_register);
    }

    private void initListener() {
        final EditText phoneEditText = phoneInputLayout.getEditText();
        final EditText passwordEditText = passwordInputLayout.getEditText();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (phone.length() != 11) {
                    phoneInputLayout.setErrorEnabled(true);
                    phoneInputLayout.setError("请输入11位手机号码");
                } else if (password.length() == 0) {
                    passwordInputLayout.setErrorEnabled(true);
                    passwordInputLayout.setError("请输入密码");
                } else {
                    RequestParams params = new RequestParams();
                    params.put(RequestParamName.PHONE, phone);
                    params.put(RequestParamName.PASSWORD, password);
                    HttpClient.post(getActivity(), "user/register", params, new JsonResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            XRotationAnimation animation = new XRotationAnimation();
                            animation.setRepeatMode(Animation.INFINITE);
                            registerBtn.startAnimation(animation);
                            toast("注册成功");
                            LocalBrCast.sendBroadcast(getActivity(), "show login fragment");
                        }

                        @Override
                        public void onFailure(String message, String for_param) {
                            if (for_param.equals(RequestParamName.PHONE)) {
                                phoneInputLayout.setErrorEnabled(true);
                                phoneInputLayout.setError(message);
                            } else if (for_param.equals(RequestParamName.PASSWORD)) {
                                passwordInputLayout.setErrorEnabled(true);
                                passwordInputLayout.setError(message);
                            } else {
                                toast(message);
                            }

                            Log.i("login", message + "  " + for_param);
                        }

                    });
                }
            }
        });
    }
}
