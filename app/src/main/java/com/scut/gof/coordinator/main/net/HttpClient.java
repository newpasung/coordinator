package com.scut.gof.coordinator.main.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.scut.gof.coordinator.main.UserManager;

import java.lang.ref.WeakReference;

/**
 * Created by gjz on 11/2/15.
 */
public class HttpClient {
    private static final String BASE_URL = "http://121.42.160.104/coordinator/backend/web/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static SyncHttpClient syncClient;

    public static void get(Context context, String url, RequestParams params, JsonResponseHandler handler){
        if (params == null) {
            params = new RequestParams();
        }
        params.put(RequestParamName.TOKEN, UserManager.getToken(context));
        handler.setWeakReference(new WeakReference<>(context));
        client.get(context, BASE_URL + url, params, handler);
    }

    public static void post(Context context, String url, RequestParams params, JsonResponseHandler handler){
        if (params == null) {
            params = new RequestParams();
        }
        params.put(RequestParamName.TOKEN, UserManager.getToken(context));
        handler.setWeakReference(new WeakReference<>(context));
        client.post(context, BASE_URL + url, params, handler);
    }

    public static void getByte(Context context, String url, AsyncHandler handler) {
        handler.setWeakReference(new WeakReference<>(context));
        client.get(context, url, handler);
    }

    public static void syncPost(Context context, String url, RequestParams params, JsonResponseHandler handler) {
        if (syncClient == null) {
            syncClient = new SyncHttpClient();
        }
        if (params == null) {
            params = new RequestParams();
        }
        handler.setWeakReference(new WeakReference<>(context));
        params.put(RequestParamName.TOKEN, UserManager.getToken(context));
        syncClient.post(context, url, params, handler);
    }

}
