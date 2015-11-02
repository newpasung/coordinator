package com.scut.gof.coordinator.main.widget.dialog.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2015/11/2.
 * 处理滑动时fab的动作
 */
public class FabBehavior extends FloatingActionButton.Behavior {

    public FabBehavior(Context context,AttributeSet attributeSet) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
        FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes== ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
    View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //正负可以判断滑动方向
        if(dyConsumed>0&&child.getVisibility()==View.VISIBLE){
            child.hide();
        }else if(dyConsumed<=0&&child.getVisibility()!=View.VISIBLE){
            child.show();
        }
    }
}
