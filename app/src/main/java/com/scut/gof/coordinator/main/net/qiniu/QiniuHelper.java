package com.scut.gof.coordinator.main.net.qiniu;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/9/21.
 */
public class QiniuHelper {

    public static final int FILETYPE_DEFAULT = 1;

    public static void uploadFile(Context mContext, int filetype, final byte[] file, final UpCompletionHandler handler) {
        final UploadManager manager = new UploadManager();
        RequestParams params = new RequestParams();
        params.put("category", filetype);
        HttpClient.get(mContext, "qiniu/token", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONObject data = object.getJSONObject("data");
                    String token = data.getString("qiniutoken");
                    String name = data.getString("filename");
                    manager.put(file, name, token, handler, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                Log.i("he", "d");
            }
        });
    }

}
