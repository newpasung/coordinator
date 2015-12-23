package com.scut.gof.coordinator.main.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;

import org.json.JSONObject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadFileService extends IntentService {
    private static final String ACTION_BYTE = "uploadbytedata";
    private static final String ACTION_LOCALFILE = "uploadlocalfile";
    private static final String EXTRA_BYTEDATA = "bytedata";
    private static final String EXTRA_LOCALFILEPATH = "localfilepath";

    public UploadFileService() {
        super("UploadFileService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UploadFileService.class);
        intent.setAction(ACTION_BYTE);
        intent.putExtra(EXTRA_BYTEDATA, param1);
        intent.putExtra(EXTRA_LOCALFILEPATH, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void upLoadLocalFile(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UploadFileService.class);
        intent.setAction(ACTION_LOCALFILE);
        intent.putExtra(EXTRA_BYTEDATA, param1);
        intent.putExtra(EXTRA_LOCALFILEPATH, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_BYTE.equals(action)) {
                final byte[] param1 = intent.getByteArrayExtra(EXTRA_BYTEDATA);
                handleActionByte(param1);
            } else if (ACTION_LOCALFILE.equals(action)) {
                final String param2 = intent.getStringExtra(EXTRA_LOCALFILEPATH);
                handleLocalFile(param2);
            }
        }
    }

    private void handleActionByte(byte[] param1) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleLocalFile(String filepath) {
        RequestParams params = new RequestParams();

    }

    private void uploadFile(RequestParams params) {
        HttpClient.syncPost(UploadFileService.this, "", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
            }

            @Override
            public void onFailure(String message, String for_param) {
            }
        });
    }

}
