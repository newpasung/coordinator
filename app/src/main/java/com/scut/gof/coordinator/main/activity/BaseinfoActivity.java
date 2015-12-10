package com.scut.gof.coordinator.main.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.lib.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.adapter.UserinfoAdapter;
import com.scut.gof.coordinator.main.net.qiniu.AvatarOption;
import com.scut.gof.coordinator.main.net.qiniu.QiniuHelper;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.utils.ImageUtil;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/18.
 */
public class BaseinfoActivity extends BaseActivity {
    final int REQUESTCODE_GETAPIC = 1;
    RecyclerView mRecinfo;
    String picPath = "";
    long uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseinfo);
        uid = getIntent().getLongExtra("uid", 0);
        if (uid == 0) uid = UserManager.getUserid(this);
        iniUI();
        iniListener();
    }

    protected void iniUI() {
        mRecinfo = (RecyclerView) findViewById(R.id.recyclerview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(UserManager.getUserName(this));
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout
                = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(ApiUtil.getColor(this, R.color.colorPrimary));
        mRecinfo.setLayoutManager(new LinearLayoutManager(this));
        mRecinfo.setAdapter(new UserinfoAdapter(this, UserManager.getLocalUser(this)));
    }

    protected void iniListener() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_GETAPIC && resultCode == MultiImageSelectorActivity.RESULT_OK) {
            ArrayList<String> picPaths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            picPath = picPaths.get(0);
//            mBtnavatar.setImageBitmap(BitmapFactory.decodeFile(picPath));
        }
    }

    protected void uploadPic() {
        if (picPath.equals("")) {
            Toast.makeText(this, "no file chosen", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            QiniuHelper.uploadFile(this, new AvatarOption()
                    , ImageUtil.getResizedForUpload(picPath, ImageUtil.COMPRESSEFFECT_SMALLER),
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                            Log.i("complete", "successful");
                        }
                    });
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

    }

}
