package com.scut.gof.coordinator.main.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.lib.com.davemorrissey.labs.subscaleview.ImageSource;
import com.scut.gof.coordinator.lib.com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.widget.dialog.WaitingDialog;

import org.apache.http.Header;

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
            final WaitingDialog dialog = new WaitingDialog(this);
            dialog.show();
            HttpClient.getByte(ImageBrowserActivity.this, url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    scaleView.setImage(ImageSource.bitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
                    source = bytes;
                    dialog.dismiss();
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
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
