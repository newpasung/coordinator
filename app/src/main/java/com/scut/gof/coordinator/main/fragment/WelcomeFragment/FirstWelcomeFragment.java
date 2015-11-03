package com.scut.gof.coordinator.main.fragment.WelcomeFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.HomeActivity;
import com.scut.gof.coordinator.main.activity.LoginActivity;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.storage.XManager;

public class FirstWelcomeFragment extends BaseFragment implements View.OnTouchListener{

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
        initUI(view);
    }

    private void initData() {
        imgIds = new int[]{R.drawable.welcome_img1, R.drawable.welcome_img2, R.drawable.welcome_img3};
        currentPosition  = 0;
    }

    private void initUI(View view) {
        imgSwitcher = (ImageSwitcher) view.findViewById(R.id.imgswitcher_first_welcome);

        //设置背景颜色
        imgSwitcher.setBackgroundColor(Color.BLACK);
        //设置factory
        imgSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView iv = new ImageView(getActivity());
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return iv;
            }
        });

        //设置OnTouchListener，用于监听滑动，以改变当前图片
        imgSwitcher.setOnTouchListener(this);

        //设置改变图片时的效果
        imgSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slow_fade_in));
        imgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fast_fade_out));

        //设置第一张图片
        imgSwitcher.setImageResource(imgIds[0]);
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
                        XManager.setOpenedStatus(getActivity(), true);//设置已经打开过app
                        boolean isLogined = XManager.isLogined(getActivity());//通过是否已登录决定进入的下一个activity
                        Intent intent = new Intent(getActivity(), isLogined ? HomeActivity.class : LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
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
