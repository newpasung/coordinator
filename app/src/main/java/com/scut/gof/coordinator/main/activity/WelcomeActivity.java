package com.scut.gof.coordinator.main.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.scut.gof.coordinator.R;

public class WelcomeActivity extends BaseActivity implements ViewSwitcher.ViewFactory, View.OnTouchListener{
    private ImageSwitcher imgSwitcher;
    //欢迎页的图片资源id
    private int[] imgIds;
    //当前页的序号
    private int currentPosition;
    //按下时的x坐标
    private float downX;
    //抬起时的x坐标
    private float upX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initData();
        initUI();
    }

    private void initData() {
        imgIds = new int[]{R.drawable.welcome_img1, R.drawable.welcome_img2, R.drawable.welcome_img3};
        currentPosition  = 0;
    }

    private void initUI() {
        imgSwitcher = (ImageSwitcher) findViewById(R.id.imgswitcher_welcome);
        //设置背景颜色

        imgSwitcher.setBackgroundColor(Color.BLACK);
        //设置factory
        imgSwitcher.setFactory(this);

        //设置OnTouchListener，用于监听滑动，以改变当前图片
        imgSwitcher.setOnTouchListener(this);

        //设置改变图片时的效果
        imgSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slow_fade_in));
        imgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fast_fade_out));

        //设置第一张图片
        imgSwitcher.setImageResource(imgIds[0]);
    }


    @Override
    public View makeView() {
        ImageView iv = new ImageView(this);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return iv;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                //从右往左拖动，显示下一张
                if (downX - upX > 0) {
                    if (currentPosition < imgIds.length - 1) {
                        imgSwitcher.setImageResource(imgIds[++currentPosition]);
                        return true;
                    }
                    //已经是最后一张，进入下一个activity
                    else {
                        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }

                //从左往右拖动，回到前一张
                if (upX - downX > 0) {
                    if (currentPosition > 0){
                        imgSwitcher.setImageResource(imgIds[--currentPosition]);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
        }
        return true;
    }
}
