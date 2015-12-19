package com.scut.gof.coordinator.main.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2015/10/2.
 */
public class HLinearLayoutManager extends LinearLayoutManager {

    public HLinearLayoutManager(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public HLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(HORIZONTAL);
    }

    public HLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        setOrientation(HORIZONTAL);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        /*View view =recycler.getViewForPosition(0);
        if(view!=null){
            measureChild(view, widthSpec, heightSpec);
                int measuredw= View.MeasureSpec.getSize(widthSpec);
                int measuredh = view.getMeasuredHeight();
                setMeasuredDimension(measuredw,measuredh);
        }*/
    }

}
