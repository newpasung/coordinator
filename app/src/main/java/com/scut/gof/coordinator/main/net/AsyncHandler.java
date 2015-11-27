package com.scut.gof.coordinator.main.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2015/11/26.
 */
public abstract class AsyncHandler extends AsyncHttpResponseHandler {

    WeakReference<Context> weakReference = null;

    public void setWeakReference(WeakReference<Context> weakReference) {
        this.weakReference = weakReference;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        onSuccess(responseBody);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        onFailure(statusCode);
    }

    public abstract void onSuccess(byte[] responseBody);

    public abstract void onFailure(int statusCode);
}
