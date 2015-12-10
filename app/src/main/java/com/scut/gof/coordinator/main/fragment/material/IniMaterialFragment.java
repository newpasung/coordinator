package com.scut.gof.coordinator.main.fragment.material;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
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
        bottomToolBar.setText(getString(R.string.action_laststep)
                , ""
                , getString(R.string.action_commit));
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {
        //在act返回上一个frg
    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {
    }

    @Override
    public void controllright(BottomToolBar toolBar) {
        if (acceptParam()) {
            LocalBrCast.sendBroadcast(getActivity(), LocalBrCast.PARAM_NEWPROJECT);
        }
    }

    /**
     * 判断输入是否可用，和设置act里的参数
     */
    private boolean acceptParam() {
        return true;
    }
}
