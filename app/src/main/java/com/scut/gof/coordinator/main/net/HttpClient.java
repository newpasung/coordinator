package com.scut.gof.coordinator.main.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.main.storage.XManager;

/**
 * Created by gjz on 11/2/15.
 */
public class HttpClient {
    private static final String BASE_URL = "http://121.42.160.104/coordinator/backend/web/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    private void get(Context context, String url, RequestParams params, JsonResponseHandler handler){
        params.put(RequestParamName.TOKEN, XManager.getToken(context));
        client.get(context, BASE_URL + url, params, handler);
    }
}
