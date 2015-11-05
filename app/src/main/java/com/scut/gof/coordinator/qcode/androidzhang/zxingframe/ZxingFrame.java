package com.scut.gof.coordinator.qcode.androidzhang.zxingframe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.qcode.zxing.activity.CaptureActivity;

public class ZxingFrame extends Activity {

    protected int mScreenWidth;
    private EditText resultTextView;
    private Button scanBarCodeButton;
    private ImageView iv_qr_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_frame);

        resultTextView = (EditText) this.findViewById(R.id.scan_result);

        scanBarCodeButton = (Button) this.findViewById(R.id.bt_bigin_scan);

        iv_qr_image = (ImageView) findViewById(R.id.iv_qr_image);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;

        scanBarCodeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用ZXIng开源项目源码  扫描二维码
                Intent openCameraIntent = new Intent(ZxingFrame.this,
                        CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 取得返回信息
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            resultTextView.setText(scanResult);
        }
    }

    //调用浏览器打开，功能尚未完善、、、
    public void checkResult(View v) {
        String result = resultTextView.getText().toString();
//		Intent intent = new Intent(ZxingFrame.this,
//				CheckResult.class);
//		intent.putExtra("result", result);
//		startActivity(intent);

        Intent i = new Intent();
        i.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(result);
        i.setData(content_url);
        i.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        startActivity(i);
    }

    //生成二维码
    public void Create2QR(View v) {
        String uri = resultTextView.getText().toString();
//		Bitmap bitmap = BitmapUtil.create2DCoderBitmap(uri, mScreenWidth/2, mScreenWidth/2);
        Bitmap bitmap;
        try {
            bitmap = BitmapUtil.createQRCode(uri, mScreenWidth);

            if (bitmap != null) {
                iv_qr_image.setImageBitmap(bitmap);
            }

        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
