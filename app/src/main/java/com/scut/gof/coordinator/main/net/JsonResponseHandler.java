package com.scut.gof.coordinator.main.net;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gjz on 11/2/15.
 */
public class JsonResponseHandler extends JsonHttpResponseHandler{
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int status = response.getInt("status");
            if (status == 1) {
                onSuccess(response);
            }else{
                String message = response.getString("message");
                onFailure(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        onFailure("请求异常");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        onFailure("请求异常");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        if (throwable != null && throwable.getCause() != null) {
            if (throwable.getCause().toString().contains("Network is unreachable")) {
                onFailure("请检查网络是否正常!");
                return;
            }
        }

        onFailure("请求异常");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        onFailure("请求异常");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        onFailure("请求异常");
    }

    public void onSuccess(JSONObject response){

    }
    public void onFailure(String error){

    }
}