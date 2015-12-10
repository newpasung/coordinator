package com.scut.gof.coordinator.main.fragment.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

/**
 * Created by Administrator on 2015/12/6.
 */
public class CreateAdvancedTaskFragment extends BaseFragment implements BottomBarController {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advantask, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void refreshView(BottomToolBar bottomToolBar) {
        /*bottomToolBar.setText(new String [] {getString(R.string.action_laststep)
        ,"",getString(R.string.action_commit)});*/
        bottomToolBar.setText("上一步", "", "完成");
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {

    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {

    }

    @Override
    public void controllright(BottomToolBar toolBar) {

    }
}
