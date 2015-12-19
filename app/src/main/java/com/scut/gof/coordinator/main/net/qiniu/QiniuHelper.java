package com.scut.gof.coordinator.main.net.qiniu;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Post;
import com.scut.gof.coordinator.main.utils.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public class QiniuHelper {

    public static UploadManager manager;
    public static boolean ISCANCELUPLOADPOSTPICS;

    /**
     * 用来向七牛上传文件
     *
     * @param option 上传不同类型文件选择不同option，在net.qiniu包下
     */
    public static void uploadFile(final Context mContext, UploadOption option, final byte[] file, final UpCompletionHandler handler) {
        if (manager == null) {
            manager = new UploadManager();
        }
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
                handler.complete(message, null, null);
            }
        });
    }

    /**
     * 发送post的图片，还有文字
     */
    public static void upLoadPicsPost(final Context context, String textContent, final List<String> picpaths, final MutiPicsLoadingListener listener) {
        ISCANCELUPLOADPOSTPICS = false;
        RequestParams params = new RequestParams();
        params.put("type", 1);
        if (!TextUtils.isEmpty(textContent)) {
            params.put("content", textContent);
        }
        params.put("piccount", picpaths.size());
        HttpClient.post(context, "post/new", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    String token = data.getString("qiniutoken");
                    String filename = data.getString("filename");
                    upLoadPostPic(context, picpaths, filename, token, listener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                Log.i("onFailure", message);
            }
        });
    }

    public static void upLoadPostPic(final Context context, final List<String> picpaths, String filename, String token, final MutiPicsLoadingListener listener) {
        if (manager == null) {
            manager = new UploadManager();
        }
        manager.put(ImageUtil.getResizedForUpload(picpaths.get(0), ImageUtil.COMPRESSEFFECT_BIGGER), filename, token, new UpCompletionHandler() {
            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                if (jsonObject == null) {
                    Log.e("QINIUERROR", "jsonobject is a null object");
                    return;
                }
                if (jsonObject.has("status")) {
                    try {
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            if (data.has("qiniutoken")) {
                                String token = data.getString("qiniutoken");
                                String filename = data.getString("filename");
                                picpaths.remove(0);
                                listener.progress(1 / picpaths.size());
                                upLoadPostPic(context, picpaths, filename, token, listener);
                            } else if (data.has("post")) {
                                Post.insertOrUpdate(data.getJSONObject("post"));
                                listener.complete();
                            }
                        } else {
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String s, double v) {
            }
        }, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return ISCANCELUPLOADPOSTPICS;
            }
        }));
    }

    public static void cancelUpLoadPostPics() {
        ISCANCELUPLOADPOSTPICS = true;
    }

    public interface MutiPicsLoadingListener {
        void progress(int progress);

        void complete();
    }

}
