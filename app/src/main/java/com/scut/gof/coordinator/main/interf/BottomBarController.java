package com.scut.gof.coordinator.main.interf;

import com.scut.gof.coordinator.main.widget.BottomToolBar;

/**
 * Created by Administrator on 2015/11/8.
 */
public interface BottomBarController {
    void refreshView(BottomToolBar bottomToolBar);

    void controllleft(BottomToolBar toolBar);

    void controllmiddle(BottomToolBar toolBar);

    void controllright(BottomToolBar toolBar);
}
