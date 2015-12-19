package com.scut.gof.coordinator.main.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ImageUtil {
    /**
     * 用来描述压缩的效果，smaller即压缩出来更小，适合头像一类
     */
    public static final int COMPRESSEFFECT_SMALLER = 0;
    /**
     * 用来描述压缩的效果，middle即压缩出来大小中等，需要清晰度，又省点流量
     */
    public static final int COMPRESSEFFECT_MIDDLE = 1;
    /**
     * 用来描述压缩的效果，bigger即压缩出来更大，适合较大的图
     */
    public static final int COMPRESSEFFECT_BIGGER = 2;

    /**
     * 这是用来获取用于上传的压缩过的图片,只能进行倍数按比例缩小，不能保证长宽大小
     *
     * @param picPath    文件所在路径
     * @param resizeMode COMPRESSEFFECT_SMALLER等变量，用来选择压缩效果
     * @return 返回压缩过的图片的byte[]数据
     */
    public static byte[] getResizedForUpload(String picPath, int resizeMode) {
        byte[] data = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
        options.inJustDecodeBounds = false;
        int scale = 0;
        int targetSize = 0;
        int shorterOne = 0;
        if (options.outHeight <= options.outWidth) {
            shorterOne = options.outHeight;
        } else {
            shorterOne = options.outWidth;
        }
        //下面给出不同策略，测试阶段，不一定有区别
        switch (resizeMode) {
            case COMPRESSEFFECT_BIGGER: {
                if (shorterOne > 4000) {
                    scale = 4;
                } else if (shorterOne > 1000) {
                    scale = 2;
                }
                targetSize = 50;
            }
            break;
            case COMPRESSEFFECT_MIDDLE: {
                if (shorterOne > 4000) {
                    scale = 4;
                } else if (shorterOne > 1000) {
                    scale = 2;
                }
                targetSize = 80;
            }
            break;
            case COMPRESSEFFECT_SMALLER: {
                if (shorterOne > 3800) {
                    scale = 8;
                }
                if (shorterOne > 2400) {
                    scale = 4;
                } else if (shorterOne > 800) {
                    scale = 2;
                }
                targetSize = 120;
            }
            break;
        }
        options.inSampleSize = scale < 1 ? 1 : scale;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(picPath, options);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        while (outputStream.toByteArray().length > targetSize * 128) {
            quality -= 10;
            outputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            if (quality < 40) {
                break;
            }
        }
        data = outputStream.toByteArray();
        try {
            outputStream.flush();
            outputStream.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}
