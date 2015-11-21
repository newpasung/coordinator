package com.scut.gof.coordinator.main.fragment.material;

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
 * Created by Administrator on 2015/11/7.
 */
public class IniMaterialFragment extends BaseFragment implements BottomBarController {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inimaterial, container, false);
    }

    @Override
    public void refreshView(BottomToolBar bottomToolBar) {
        bottomToolBar.setText(getString(R.string.action_cancel)
                , getString(R.string.action_commit)
                , getString(R.string.action_moreconfig));
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {

    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {

    }

    @Override
    public void controllright(BottomToolBar toolBar) {
        //TODO 暂时关闭掉
        getActivity().finish();
    }
}
