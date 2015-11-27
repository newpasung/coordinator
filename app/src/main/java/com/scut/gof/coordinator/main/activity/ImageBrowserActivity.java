package com.scut.gof.coordinator.main.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.lib.com.davemorrissey.labs.subscaleview.ImageSource;
import com.scut.gof.coordinator.lib.com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.scut.gof.coordinator.main.net.AsyncHandler;
import com.scut.gof.coordinator.main.net.HttpClient;


/**
 * Created by Administrator on 2015/10/3.
 */
public class ImageBrowserActivity extends BaseActivity {
    public final static String EXTRA_PARAMETER_PICCOUNT = "piccount";
    public final static String EXTRA_PARAMETER_PICURL = "picurl";
    SubsamplingScaleImageView scaleView;
    int piccount = 0;
    String url;
    byte[] source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagebrowser);
        scaleView = (SubsamplingScaleImageView) findViewById(R.id.subscaleview);
        scaleView.setMinimumDpi(80);
        if (getIntent() != null) {
            piccount = getIntent().getIntExtra(EXTRA_PARAMETER_PICCOUNT, 0);
            url = getIntent().getStringExtra(EXTRA_PARAMETER_PICURL);
        }
        if (url != null) {
            final MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .progress(true, 0).show();
            HttpClient.getByte(ImageBrowserActivity.this, url, new AsyncHandler() {
                @Override
                public void onSuccess(byte[] responseBody) {
                    scaleView.setImage(ImageSource.bitmap(BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length)));
                    source = responseBody;
                    dialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode) {
                    dialog.dismiss();
                }
            });
        }
        iniListener();
    }

    protected void iniListener() {
        scaleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*if(source!=null){
                    new ChoiceDialog(ImageBrowserActivity.this)
                            .addItem("保存图片", new ChoiceDialog.OnClickListener() {
                                @Override
                                public void didClick(ChoiceDialog dialog, String itemTitle) {
                                    FileStorage.saveImage(ImageBrowserActivity.this,source);
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }*/
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

}
