package com.scut.gof.coordinator.main.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.scut.gof.coordinator.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2015/11/20.
 */
public class PicassoProxy {

    /**
     * 一般情况下的图片加载配置
     */
    public static void load(Context context, String url, ImageView view) {
        Picasso.with(context).load(url)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .error(R.drawable.broken_image_black)
                .into(view);
    }

    public static void loadAvatar(Context context, String url, ImageView view) {
        Picasso.with(context).load(url).fit()
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .error(R.drawable.holywhite)
                .into(view);
    }

    public static void loadBigImg(Context context, String url, ImageView view, Callback callback) {
        Picasso.with(context).load(url).fit()
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .error(R.drawable.holywhite)
                .into(view, callback);
    }

}
