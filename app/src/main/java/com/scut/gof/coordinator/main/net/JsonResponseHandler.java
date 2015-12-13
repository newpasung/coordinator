package com.scut.gof.coordinator.main.net;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.scut.gof.coordinator.main.activity.user.LoginActivity;

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
                final Context context = weakReference.get();
                if (response.getInt("errorCode") == RequestParamName.ERRORCODE_TOKENINVALID) {
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
                    weakReference.clear();
                }
                if (response.getInt("errorCode") == RequestParamName.ERRORCODE_DATABASEEXECPTION) {
                    if (context != null) {
                        Toast.makeText(context, "后台bug", Toast.LENGTH_SHORT).show();
                    }
                    weakReference.clear();
                }
                if (response.getInt("errorCode") == RequestParamName.ERRORCODE_ILLEGLESTATE) {
                    onFailure(response.getString("message"), RequestParamName.ERRORCODE_ILLEGLESTATE + "");
                }
                String message = response.getString("message");
                String for_param = response.getString("for_param");
                Log.i("httpclient_onFail", message);
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
            if (throwable.getCause().toString().contains("Network is unreachable")
                    || throwable.getCause().toString().contains("timed out")) {
                final Context context = weakReference.get();
                if (context != null) {
                    new MaterialDialog.Builder(context)
                            .positiveText("好的")
                            .title("网络环境错误")
                            .content("请检查你的手机能否正常上网")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                weakReference.clear();
                onFailure("请检查网络是否正常!", "common");
                return;
            }
        }
        onFailure("请求异常", "common");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        if (statusCode == 500) {
            final Context context = weakReference.get();
            if (context != null) {
                new MaterialDialog.Builder(context)
                        .positiveText("了解")
                        .title("深感抱歉")
                        .content("服务器维护中")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                weakReference.clear();
            }
        }
        onFailure("请求异常", "common");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        onFailure("请求异常", "common");
    }

    public abstract void onSuccess(JSONObject response);
    public abstract void onFailure(String message, String for_param);
}
