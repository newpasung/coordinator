package com.scut.gof.coordinator.main.widget;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.utils.DenstityUtil;
import com.scut.gof.coordinator.main.utils.MathUtil;
import com.scut.gof.coordinator.main.utils.ViewUtil;

/**
 * Created by Administrator on 2015/10/26.
 * 一个与fab密切挂钩的底部工具栏
 */
public class BottomToolBar extends RelativeLayout {

    View animView;
    ShapeDrawable shapeDrawable;
    final int INIWIDTH=10;
    final int INIHEIGHT=10;
    boolean needIni=true;
    public BottomToolBar(Context context) {
        super(context);
        init(context);
    }

    public BottomToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        needIni=true;
        animView = new View(context);
        addView(animView);
        RelativeLayout.LayoutParams params=new LayoutParams(INIWIDTH,INIHEIGHT);
        animView.setLayoutParams(params);
        animView.setVisibility(INVISIBLE);
        shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.colorPrimary));
        ViewUtil.setBackground(animView, shapeDrawable);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        animView.layout(l, t, l + INIWIDTH, t + INIHEIGHT);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w=MeasureSpec.makeMeasureSpec(INIWIDTH,MeasureSpec.EXACTLY);
        int h=MeasureSpec.makeMeasureSpec(INIHEIGHT,MeasureSpec.EXACTLY);
        animView.measure(w, h);
        if(needIni){
            hideMainBuz();
            needIni=false;
        }
    }

    public void reveal() {
        animView.setScaleX(1f);
        animView.setScaleY(1f);
        //先移到中点后缩放
        animView.setTranslationX(getWidth() / 2 - animView.getWidth() / 2);
        animView.setTranslationY(getHeight() / 2 - animView.getHeight() / 2);
        animView.setVisibility(VISIBLE);
        int scale=calScale(animView.getWidth() / 2);
        animView.animate()
                .scaleX(scale).scaleY(scale).setDuration(250)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showMainBuz();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    public void reveal(Context context, final FloatingActionButton button) {
        int transX=ViewUtil.getScreenX(button) - DenstityUtil.getScreenWidth(context) /2+ button.getWidth() / 2;;
        int transY=DenstityUtil.getScreenHeight(context)-
                ViewUtil.getScreenY(button)-button.getHeight();
        button.animate()
                .translationX(-transX)
                .translationY(transY)
                .setDuration(100)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        button.hide();
                        reveal();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .start();
    }

    public void reset(final Animator.AnimatorListener listener) {
        hideMainBuz();
        animView.animate().scaleY(1f).scaleX(1f).
                setInterpolator(new DecelerateInterpolator()).setDuration(200).
                setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animView.setScaleY(1f);
                        animView.setScaleX(1f);
                        animView.setVisibility(INVISIBLE);
                        if (listener != null) {
                            listener.onAnimationEnd(animation);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    public void reset(final FloatingActionButton button) {
        hideMainBuz();
        animView.animate().scaleY(1f).scaleX(1f).
                setInterpolator(new DecelerateInterpolator()).setDuration(200).
                setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animView.setScaleY(1f);
                        animView.setScaleX(1f);
                        animView.setVisibility(INVISIBLE);
                        button.show();
                        button.animate().translationX(0)
                                .translationY(0)
                                .setDuration(250)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    //计算完全覆盖的scale
    protected int calScale(int radius){
        Rect barRect =new Rect();
        Rect viewRect =new Rect();
        getGlobalVisibleRect(barRect);
        animView.getGlobalVisibleRect(viewRect);
        int dis1=MathUtil.calDis(viewRect.centerX(),viewRect.centerY(),barRect.left,barRect.top);
        int dis2=MathUtil.calDis(viewRect.centerX(),viewRect.centerY(),barRect.left,barRect.bottom);
        int dis3=MathUtil.calDis(viewRect.centerX(),viewRect.centerY(),barRect.right,barRect.top);
        int dis4=MathUtil.calDis(viewRect.centerX(),viewRect.centerY(),barRect.right,barRect.bottom);
        dis1 =dis1>dis2?dis1:dis2;
        dis1 =dis1>dis3?dis1:dis3;
        dis1 =dis1>dis4?dis1:dis4;
//        return dis1/radius+1;
        //TODO 还不知道怎么确定一个合适的scale
        return 80;
    }

    //隐藏除动画view外的view
    protected void hideMainBuz(){
        for (int i=1;i<getChildCount();i++){
            getChildAt(i).setVisibility(INVISIBLE);
        }
    }

    protected void showMainBuz(){
        for (int i=1;i<getChildCount();i++){
            getChildAt(i).setVisibility(VISIBLE);
        }
    }

}
