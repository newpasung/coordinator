package com.scut.gof.coordinator.main.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.utils.ApiUtil;

public class SideBar extends View {
    // 26个字母
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private int choose = -1;// 选中  
    private Paint paint = new Paint();
    private int commonColor;
    private int touchingColor;
    private TextView mTextDialog;
    //是不是由本身的touch控制
    private boolean touchingMe = false;

    private float itemHeight = -1;

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ini(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini(context);
    }

    public SideBar(Context context) {
        super(context);
        ini(context);
    }

    /**
     * 为SideBar设置显示字母的TextView
     *
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public boolean isTouching() {
        return this.touchingMe;
    }

    private void ini(Context context) {
        commonColor = ApiUtil.getColor(context, R.color.black_54);
        touchingColor = ApiUtil.getColor(context, R.color.colorAccent);
    }

    public void setChosenText(char c) {
        choose = c - 'a';
        invalidate();
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (itemHeight == -1) {
            itemHeight = getHeight() / b.length;
        }

        // 获取焦点改变背景颜色.  
        int height = getHeight();// 获取对应高度  
        int width = getWidth(); // 获取对应宽度  
        int singleHeight = height / b.length;// 获取每一个字母的高度  

        for (int i = 0; i < b.length; i++) {
            paint.setColor(commonColor);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(itemHeight - 10);
            // 选中的状态  
            if (i == choose) {
                paint.setColor(touchingColor);
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.  
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();// 重置画笔  
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标  
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        switch (action) {
            case MotionEvent.ACTION_UP:
                setVisibility(INVISIBLE);
                touchingMe = false;
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                touchingMe = true;
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}  