package com.scut.gof.coordinator.main.net;

import android.content.Context;
import android.content.Intent;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.scut.gof.coordinator.main.activity.LoginActivity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;


/**
 * Created by gjz on 11/2/15.
 */
public abstract class JsonResponseHandler extends JsonHttpResponseHandler{
    WeakReference<Context> weakReference = null;

    public void setWeakReference(WeakReference<Context> weakReference) {
        this.weakReference = weakReference;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int status = response.getInt("status");
            if (status == 1) {
                onSuccess(response);
            }else{
                if (response.getInt("errorCode") == RequestParamName.ERRORCODE_TOKENINVALID) {
                    final Context context = weakReference.get();
                    if (context != null) {
                        new MaterialDialog.Builder(context)
                                .positiveText("了解")
                                .title("异地登录警告")
                                .content("你需要重新登录")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        Intent intent = new Intent();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.setClass(context, LoginActivity.class);
                                        context.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                    return;
                }
                String message = response.getString("message");
                String for_param = response.getString("for_param");
                onFailure(message, for_param);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        onFailure("请求异常", "common");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        onFailure("请求异常", "common");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        if (throwable != null && throwable.getCause() != null) {
            if (throwable.getCause().toString().contains("Network is unreachable")) {
                onFailure("请检查网络是否正常!", "common");
                return;
            }
        }
        onFailure("请求异常", "common");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        onFailure("请求异常", "common");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        onFailure("请求异常", "common");
    }

    public abstract void onSuccess(JSONObject response);
    public abstract void onFailure(String message, String for_param);
}
