package com.scut.gof.coordinator.main.widget;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.utils.DenstityUtil;
import com.scut.gof.coordinator.main.utils.MathUtil;
import com.scut.gof.coordinator.main.utils.ViewUtil;

/**
 * Created by Administrator on 2015/10/26.
 * 一个与fab密切挂钩的底部工具栏
 */
public class BottomToolBar extends RelativeLayout {

    final int INIWIDTH = 20;
    final int INIHEIGHT = 20;
    View animView;
    LinearLayout btnContainer;
    Button[] buttons;
    FloatingActionButton mFloatingButton;
    ShapeDrawable shapeDrawable;
    BottomBarController barController;
    //是否需要初始化
    boolean needIni = true;
    //是否进行动画中
    boolean isAnimating = false;

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

    public void setButtonListener(BottomBarController listener) {
        this.barController = listener;
    }

    public void attachFloatingButton(FloatingActionButton button) {
        this.mFloatingButton = button;
    }

    /**
     * 用于设置三个button显示的字，可以传入1到3个字符串，一个字符串只设置右边的btn
     */
    public void setText(String... text) {
        if (text == null) {
            buttons[0].setText("");
            buttons[1].setText("");
            buttons[2].setText("");
            return;
        }
        if (text.length > 3) return;
        if (text.length == 1) {
            buttons[0].setText("");
            buttons[1].setText("");
            buttons[2].setText(text[0]);
        } else if (text.length == 2) {
            buttons[0].setText(text[0]);
            buttons[1].setText("");
            buttons[2].setText(text[1]);
        } else if (text.length == 3) {
            buttons[0].setText(text[0]);
            buttons[1].setText(text[1]);
            buttons[2].setText(text[2]);
        }
    }

    protected void init(final Context context) {
        setVisibility(View.INVISIBLE);
        needIni = true;
        animView = new View(context);
        RelativeLayout.LayoutParams params = new LayoutParams(INIWIDTH, INIHEIGHT);
        animView.setLayoutParams(params);
        animView.setVisibility(INVISIBLE);
        shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(ApiUtil.getColor(context, R.color.colorAccent));
        ApiUtil.setBackground(animView, shapeDrawable);
        btnContainer = new LinearLayout(getContext());
        RelativeLayout.LayoutParams containerParam
                = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        btnContainer.setLayoutParams(containerParam);
        btnContainer.setWeightSum(3f);
        btnContainer.setOrientation(LinearLayout.HORIZONTAL);
        buttons = new Button[3];
        LinearLayout.LayoutParams childParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        childParams.weight = 1f;
        int padding = DenstityUtil.dp2px(8f, getResources());
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(getContext());
            buttons[i].setLayoutParams(childParams);
            buttons[i].getPaint().setFakeBoldText(true);//设置加粗
            buttons[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            buttons[i].setTextColor(ApiUtil.getColor(getContext(), R.color.white));
            buttons[i].setBackgroundResource(R.drawable.bg_flat_btn);
            buttons[i].setGravity(Gravity.CENTER);
            buttons[i].setPadding(padding, padding, padding, padding);
            btnContainer.addView(buttons[i]);
        }
        buttons[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                barController.controllleft(BottomToolBar.this);
            }
        });
        buttons[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                barController.controllmiddle(BottomToolBar.this);
            }
        });
        buttons[2].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                barController.controllright(BottomToolBar.this);
            }
        });
        addView(animView);
        addView(btnContainer);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        animView.layout(l, t, l + INIWIDTH, t + INIHEIGHT);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.makeMeasureSpec(INIWIDTH, MeasureSpec.EXACTLY);
        int h = MeasureSpec.makeMeasureSpec(INIHEIGHT, MeasureSpec.EXACTLY);
        animView.measure(w, h);
        if (needIni) {
            hideMainBuz();
            needIni = false;
        }
        //因为onDraw没反应，所以放在这里吧
        if (barController != null) {
            barController.refreshView(this);
        }
    }

    /**
     * 带动画显示bottombar,这个不关联fab
     */
    public void reveal() {
        preProcess();
        setVisibility(VISIBLE);
        //根据宽度决定scale
        float radius = calScale(animView.getWidth() / 2);
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

    protected void preProcess() {
        animView.setScaleX(1f);
        animView.setScaleY(1f);
        animView.setTranslationX(getWidth() / 2 - animView.getWidth() / 2);
        animView.setTranslationY(getHeight() / 2 - animView.getHeight() / 2);
    }

    /**
     * 带动画协同显示bottombar和隐藏fab，没有fab使用无参reveal
     */
    public void reveal(Context context) {
        if (mFloatingButton == null) {
            Log.e("BottomToolbar", "reveal error ,you should call attachFloatingButton() first");
            return;
        }
        int transX = ViewUtil.getScreenX(mFloatingButton) - DenstityUtil.getScreenWidth(context) / 2 + mFloatingButton.getWidth() / 2;
        int transY = DenstityUtil.getScreenHeight(context) -
                ViewUtil.getScreenY(mFloatingButton) - mFloatingButton.getHeight();
        mFloatingButton.animate()
                .translationX(-transX)
                .translationY(transY)
                .setDuration(100)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mFloatingButton.hide();
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
        if (mFloatingButton == null) {
            Log.e("BottomToolbar", "reveal error ,you should call attachFloatingButton() first");
            return;
        }
        hideMainBuz();
        animView.animate().scaleY(1f).scaleX(1f).
                setInterpolator(new DecelerateInterpolator()).setDuration(120).
                setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        listener.onAnimationStart(animation);
                        isAnimating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listener.onAnimationEnd(animation);
                        animView.setScaleY(1f);
                        animView.setScaleX(1f);
                        animView.setVisibility(INVISIBLE);
                        mFloatingButton.show();
                        isAnimating = false;
                        setVisibility(INVISIBLE);
                        mFloatingButton.animate()
                                .translationX(0)
                                .translationY(0)
                                .setDuration(250)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        listener.onAnimationCancel(animation);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        listener.onAnimationRepeat(animation);
                    }
                })
                .start();
    }

    /**
     * 这个用来动态地重置view和恢复fab，所以必须先执行attachFloatingButton
     */
    public void reset() {
        if (mFloatingButton == null) {
            Log.e("BottomToolbar", "reveal error ,you should call attachFloatingButton() first");
            return;
        }
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
                        mFloatingButton.show();
                        isAnimating = false;
                        setVisibility(INVISIBLE);
                        mFloatingButton.animate()
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

    /**
     * 马上静态地隐藏bottombar和显示出fab
     */
    public void resetImmediate() {
        if (mFloatingButton != null) {
            mFloatingButton.setTranslationX(0f);
            mFloatingButton.setTranslationY(0f);
            mFloatingButton.show();
        }
        hideMainBuz();
        setVisibility(INVISIBLE);
    }

    protected float calScale(float radius) {
        Rect barRect = new Rect();
        Rect viewRect = new Rect();
        getGlobalVisibleRect(barRect);
        animView.getGlobalVisibleRect(viewRect);
        long dis1 = MathUtil.calDis(viewRect.centerX(), viewRect.centerY(), barRect.left, viewRect.centerY());
        long dis2 = MathUtil.calDis(viewRect.centerX(), viewRect.centerY(), barRect.right, viewRect.centerY());
        dis1 = dis1 > dis2 ? dis1 : dis2;
        return dis1 / radius + 1;
    }

    //隐藏除动画view外的view
    protected void hideMainBuz() {
        for (int j = 1; j < getChildCount(); j++) {
            getChildAt(j).setVisibility(INVISIBLE);
        }
    }

    protected void showMainBuz() {
        for (int i = 1; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(VISIBLE);
        }
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    /**
     * 因为这个view默认隐藏，可以使用这个来设置可见,设置一个静态的bar
     */
    public void iniStaticBar() {
        this.setVisibility(VISIBLE);
        showMainBuz();
        setBackgroundColor(ApiUtil.getColor(getContext(), R.color.colorAccent));
        needIni = false;
    }

    public void hideFab() {
        if (mFloatingButton != null) {
            mFloatingButton.hide();
        }
    }

}
