package com.scut.gof.coordinator.main.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scut.gof.coordinator.R;

/**
 * Created by Administrator on 2015/11/2.
 */
public class InputDialog extends Dialog {
    private TextInputLayout inputLayout ;
    private TextView mTvtip;
    private Button  mBtnensure;
    private Button mBtncancel;
    public InputDialog(Context context) {
        super(context, R.style.commondialog);
    }

    protected InputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public InputDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputdialog_layout);
        inputLayout=(TextInputLayout)findViewById(R.id.textinput);
        mTvtip=(TextView)findViewById(R.id.text_tip);
        mBtncancel=(Button)findViewById(R.id.btn_cancel);
        mBtnensure=(Button)findViewById(R.id.btn_ensure);
    }

    public void onCancel(View.OnClickListener listener){
        mBtncancel.setOnClickListener(listener);
    }

    //点击确认按键
    public void onEnsure(View.OnClickListener listener){
        mBtnensure.setOnClickListener(listener);
    }

    /**显示一个dialog信息*/
    public void setTip(String text){
        mTvtip.setText(text);
    }

    /**显示输入提示*/
    public void setHint(String text){
        inputLayout.setHint(text);
    }

    /**设置最大字数提示*/
    public void setMaxCount(int count){
        inputLayout.setCounterMaxLength(count);
        inputLayout.setCounterEnabled(true);
    }

    public String getText(){
        return inputLayout.getEditText().getText().toString();
    }

    public void setError(String error){
        inputLayout.setError(error);
        inputLayout.setErrorEnabled(true);
    }

}
