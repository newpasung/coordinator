package com.scut.gof.coordinator.main.storage;

import android.content.Context;
import android.os.Environment;

import com.activeandroid.ActiveAndroid;
import com.scut.gof.coordinator.main.storage.model.Material;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.storage.model.RelaProject;
import com.scut.gof.coordinator.main.storage.model.RelaTask;
import com.scut.gof.coordinator.main.storage.model.User;

import java.io.File;

/**
 * Created by Administrator on 2015/12/2.
 */
public class StorageHelper {

    /**
     * 清空应用的所有数据，以后有新的数据源要记得加
     */
    public static void clearData(Context context) {
        XManager.clearData(context);
        ActiveAndroid.clearCache();
        RelaProject.clearData();
        RelaTask.clearData();
        Project.clearData();
        Material.clearData();
        User.clearData();
        //删除内部缓存文件
        deleteFilesByDirectory(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 删除一个目录底下所有文件
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }
    }

}
