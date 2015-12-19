package com.scut.gof.coordinator.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.libs.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.net.qiniu.QiniuHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/18.
 */
public class TestUploadPicsActivity extends BaseActivity {

    TextView mTextview;
    Button mBtnupload;
    ArrayList<String> paths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadtest);
        mTextview = (TextView) findViewById(R.id.textview);
        mBtnupload = (Button) findViewById(R.id.btn_upload);
        mBtnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPics();
            }
        });
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == MultiImageSelectorActivity.RESULT_OK) {
            paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < paths.size(); i++) {
                builder.append(paths.get(i));
                builder.append("\n");
            }
            mTextview.setText(builder.toString());
        }
    }

    public void uploadPics() {
        if (paths == null || paths.size() == 0) {
            return;
        }
        QiniuHelper.upLoadPicsPost(TestUploadPicsActivity.this, "", paths, new QiniuHelper.MutiPicsLoadingListener() {
            @Override
            public void progress(int progress) {

            }

            @Override
            public void complete() {

            }
        });
    }

}
