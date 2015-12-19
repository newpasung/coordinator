package com.scut.gof.coordinator.main.activity.base;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.libs.com.davemorrissey.labs.subscaleview.ImageSource;
import com.scut.gof.coordinator.libs.com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * Created by Administrator on 2015/10/3.
 * 浏览图片，传入url
 */
public class ImageBrowserActivity extends BaseActivity {
    public final static String EXTRA_PARAMETER_PICCOUNT = "piccount";
    public final static String EXTRA_PARAMETER_PICURL = "picurl";
    SubsamplingScaleImageView scaleView;
    int piccount = 0;
    String url;
    String tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagebrowser);
        tag = String.valueOf(System.currentTimeMillis());
        scaleView = (SubsamplingScaleImageView) findViewById(R.id.subscaleview);
        scaleView.setMinimumDpi(80);
        if (getIntent() != null) {
            piccount = getIntent().getIntExtra(EXTRA_PARAMETER_PICCOUNT, 0);
            url = getIntent().getStringExtra(EXTRA_PARAMETER_PICURL);
        }
        if (url != null) {
            final MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .content("图片加载中")
                    .progress(true, 0).show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            PicassoProxy.loadBigImg2(this, url, tag, new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    scaleView.setImage(ImageSource.bitmap(bitmap));
                    dialog.dismiss();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    dialog.dismiss();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
            iniListener();
        } else {
            toastWarn("当你看到这个说明出bug了");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        PicassoProxy.cancelLoad(this, tag);
        super.onPause();
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
