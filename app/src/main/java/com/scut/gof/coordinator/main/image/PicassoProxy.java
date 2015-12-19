package com.scut.gof.coordinator.main.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.scut.gof.coordinator.R;
import com.squareup.picasso.Callback;
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
                .fit()
                .error(R.drawable.holywhite)
                .placeholder(R.drawable.holywhite)
                .into(view);
    }

    public static void loadFile(Context context, File file, ImageView view) {
        if (!file.exists()) return;
        Picasso.with(context).load(file)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .fit()
                .into(view);
    }

    public static void loadUri(Context context, Uri uri, ImageView view) {
        Picasso.with(context).load(uri)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .error(R.drawable.holywhite)
                .fit()
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

    public static void load(Context context, String url, ImageView view, int height, int width) {
        Picasso.with(context).load(url)
                .config(Bitmap.Config.RGB_565)
                .error(R.drawable.holywhite)
                .placeholder(R.drawable.holywhite)
                .resize(width, height)
                .into(view);
    }

    /**
     * 这个下载数据但是不会加载图片，可以自己处理数据哦，哼哼
     */
    public static void loadBigImg(Context context, String url, String tag, Target target) {
        if (TextUtils.isEmpty(url)) return;
        Picasso.with(context).load(url)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .tag(tag)
                .error(R.drawable.brokenimg_accent)
                .into(target);
    }

    public static void loadBigImg2(final Context context, final String url, String tag, final Target target) {
        Picasso.with(context).load(url)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.holywhite)
                .tag(tag)
                .error(R.drawable.brokenimg_accent)
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        loadBigImg(context, url, "no", target);
                    }

                    @Override
                    public void onError() {
                        target.onBitmapFailed(null);
                    }
                });
    }

    public static void cancelLoad(Context context, String tag) {
        Picasso.with(context).cancelTag(tag);
    }

}
