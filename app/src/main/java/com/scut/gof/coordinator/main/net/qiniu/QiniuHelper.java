package com.scut.gof.coordinator.main.net.qiniu;

import android.content.Context;
import android.widget.Toast;

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

    public static final int FILETYPE_PROLOGO = 0;
    public static final int FILETYPE_AVATAR = 1;

    /**
     * 用来向七牛上传文件
     *
     * @param option 上传不同类型文件选择不同option，在net.qiniu包下
     */
    public static void uploadFile(final Context mContext, UploadOption option, final byte[] file, final UpCompletionHandler handler) {
        final UploadManager manager = new UploadManager();
        RequestParams params = option.getParams();
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
                Toast.makeText(mContext, "upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
