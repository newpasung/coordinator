package com.scut.gof.coordinator.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.lib.me.imid.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * Created by Administrator on 2015/11/3.
 */
public class ProinfoActivty extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proinfoactivity_frg_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.arrow_back);
    }

}
