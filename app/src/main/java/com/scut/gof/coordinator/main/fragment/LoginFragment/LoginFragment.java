package com.scut.gof.coordinator.main.fragment.LoginFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.activity.HomeActivity;
import com.scut.gof.coordinator.main.animation.XRotationAnimation;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.net.RequestParamName;
import com.scut.gof.coordinator.main.storage.XManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends BaseFragment {

    private TextInputLayout phoneInputLayout;
    private TextInputLayout passwordInputLayout;
    private Button loginBtn;
    private Button registerBtn;
    //用于控制登陆按钮动画
    private boolean shouldTrans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initListener();
    }

    private void initListener() {
        final EditText phoneEditText = phoneInputLayout.getEditText();
        final EditText passwordEditText = passwordInputLayout.getEditText();

        loginBtn.setOnClickListener(new View.OnClickListener() {
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
                    XRotationAnimation animation = new XRotationAnimation();
                    animation.setRepeatMode(Animation.RESTART);
                    animation.setRepeatCount(Animation.INFINITE);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (shouldTrans) {
                                startActivity(new Intent(getActivity(), HomeActivity.class));
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            if (shouldTrans) {
                                startActivity(new Intent(getActivity(), HomeActivity.class));
                                getActivity().finish();
                            }
                        }
                    });
                    loginBtn.startAnimation(animation);
                    HttpClient.post(getActivity(), "user/login", params, new JsonResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                XManager.setLoginStatus(getActivity(), true);
                                UserManager.iniUserData(getActivity(), response.getJSONObject("data"));
                                shouldTrans = true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                            loginBtn.getAnimation().cancel();
                        }

                    });
                }
            }
        });

        //跳转到注册界面
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalBrCast.sendBroadcast(getActivity(), "show register fragment");
            }
        });
    }

    private void initUI(View view) {
        phoneInputLayout = (TextInputLayout) view.findViewById(R.id.phone);

        passwordInputLayout = (TextInputLayout) view.findViewById(R.id.password);

        loginBtn = (Button) view.findViewById(R.id.btn_login);

        registerBtn = (Button) view.findViewById(R.id.btn_register);
    }
}
