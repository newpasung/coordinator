package com.scut.gof.coordinator.main.widget;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.utils.DenstityUtil;
import com.scut.gof.coordinator.main.utils.MathUtil;
import com.scut.gof.coordinator.main.utils.ViewUtil;

/**
 * Created by Administrator on 2015/10/26.
 * 一个与fab密切挂钩的底部工具栏
 */
public class BottomToolBar extends RelativeLayout {

    final int INIWIDTH=20;
    final int INIHEIGHT=20;
    View animView;
    ShapeDrawable shapeDrawable;
    //是否需要初始化
    boolean needIni=true;
    //是否进行动画中
    boolean isAnimating=false;
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
        ApiUtil.setBackground(animView, shapeDrawable);
        setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        animView.layout(l, t, l + INIWIDTH, t + INIHEIGHT);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w=MeasureSpec.makeMeasureSpec(INIWIDTH, MeasureSpec.EXACTLY);
        int h=MeasureSpec.makeMeasureSpec(INIHEIGHT, MeasureSpec.EXACTLY);
        animView.measure(w, h);
        if(needIni){
            hideMainBuz();
            needIni=false;
        }
    }

    //显示bottombar
    public void reveal() {
        preProcess();
        setVisibility(VISIBLE);
        //根据宽度决定scale
        float radius=calScale(animView.getWidth()/2);
        animView.setVisibility(VISIBLE);
        animView.animate()
                .scaleX(radius)
                .scaleY(radius)
                .setDuration(120)
                .setInterpolator(new LinearInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showMainBuz();
                        isAnimating = false;
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

    protected void preProcess(){
        animView.setScaleX(1f);
        animView.setScaleY(1f);
        animView.setTranslationX(getWidth()/2-animView.getWidth()/2);
        animView.setTranslationY(getHeight() / 2 - animView.getHeight() / 2);
    }

    //协同显示bottombar和隐藏fab
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
                setInterpolator(new DecelerateInterpolator()).setDuration(120).
                setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animView.setScaleY(1f);
                        animView.setScaleX(1f);
                        animView.setVisibility(INVISIBLE);
                        setVisibility(INVISIBLE);
                        isAnimating = false;
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
                setInterpolator(new DecelerateInterpolator()).setDuration(120).
                setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimating=true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animView.setScaleY(1f);
                        animView.setScaleX(1f);
                        animView.setVisibility(INVISIBLE);
                        button.show();
                        isAnimating=false;
                        setVisibility(INVISIBLE);
                        button.animate()
                                .translationX(0)
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

    protected float calScale(float radius){
        Rect barRect =new Rect();
        Rect viewRect =new Rect();
        getGlobalVisibleRect(barRect);
        animView.getGlobalVisibleRect(viewRect);
        long dis1=MathUtil.calDis(viewRect.centerX(),viewRect.centerY(),barRect.left,viewRect.centerY());
        long dis2=MathUtil.calDis(viewRect.centerX(),viewRect.centerY(),barRect.right,viewRect.centerY());
        dis1 =dis1>dis2?dis1:dis2;
        return dis1/radius+1;
    }

    //隐藏除动画view外的view
    protected void hideMainBuz(){
        for (int j = 1; j < getChildCount(); j++) {
            getChildAt(j).setVisibility(INVISIBLE);
        }
    }

    protected void showMainBuz(){
        for (int i=1;i<getChildCount();i++){
            final int j = i;
            getChildAt(i).setVisibility(VISIBLE);
        }
    }

    public boolean isAnimating(){
        return isAnimating;
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
