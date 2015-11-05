package com.scut.gof.coordinator.qcode.androidzhang.zxingframe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.scut.gof.coordinator.R;

public class CheckResult extends Activity {

    ProgressDialog pd;
    private WebView myweb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_result);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String result = bundle.getString("result");

        // myweb.getSettings().setJavaScriptEnabled(true);
        // Log.i("jie",result);
        // myweb.loadData(result, "text/html/xml", "utf-8");
        // myweb.loadUrl("http://bbs.eoe.cn/forum.php?mod=guide&view=my");

        // init();// 执行初始化函数
        // loadurl(myweb, result);
        // loadurl(myweb,"http://www.pocketdigi.com");


        Intent i = new Intent();
        i.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(result);
        i.setData(content_url);
        i.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        startActivity(i);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myweb.canGoBack()) {
            myweb.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        } else {

            return super.onKeyDown(keyCode, event);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init() {// 初始化
        myweb = (WebView) findViewById(R.id.myweb);
        myweb.getSettings().setJavaScriptEnabled(true);// 可用JS
        myweb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        myweb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view,
                                                    final String url) {
                loadurl(view, url);// 载入网页
                return true;
            }// 重写点击动作,用webview载入

        });
        myweb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                super.onProgressChanged(view, progress);
            }
        });

        // pd=new ProgressDialog(LoginSuccess.this);
        // pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // pd.setMessage("数据载入中，请稍候！");
    }

    public void loadurl(final WebView view, final String url) {
        new Thread() {
            public void run() {
                view.loadUrl(url);// 载入网页
            }
        }.start();
    }

}
