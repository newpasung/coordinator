package com.scut.gof.coordinator.main.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.scut.gof.coordinator.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

/**
 * Created by Administrator on 2015/11/20.
 */
public class PicassoProxy {

    /**
     * 一般情况下的图片加载配置
     */
    public static void load(Context context, String url, ImageView view) {
        if (TextUtils.isEmpty(url)) return;
        Picasso.with(context).load(url)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .error(R.drawable.broken_image_black)
                .into(view);
    }

    public static void loadFile(Context context, File file, int maxLength, ImageView view) {
        if (!file.exists()) return;
        Picasso.with(context).load(file)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .resize(maxLength, maxLength)
                .centerCrop()
                .into(view);
    }

    public static void loadAvatar(Context context, String url, ImageView view) {
        if (TextUtils.isEmpty(url)) return;
        Picasso.with(context).load(url).fit()
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .error(R.drawable.holywhite)
                .into(view);
    }

    /**
     * 这个下载数据但是不会加载图片，可以自己处理数据哦，哼哼
     */
    public static void loadBigImg(Context context, String url, Target target) {
        if (TextUtils.isEmpty(url)) return;
        Picasso.with(context).load(url)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .tag("bigimage")
                .error(R.drawable.holywhite)
                .into(target);
    }

    public static void cancelLoadBigImg(Context context) {
        Picasso.with(context).cancelTag("bigimage");
    }

}
