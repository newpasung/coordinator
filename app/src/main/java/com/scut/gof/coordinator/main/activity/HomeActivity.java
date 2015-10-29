package com.scut.gof.coordinator.main.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

public class HomeActivity extends BaseAcitivy {
    DrawerLayout mDrwer;
    BottomToolBar mBar;
    FloatingActionButton mBtnfab;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBar.reset(mBtnfab);
            }
        });
        mDrwer = (DrawerLayout) findViewById(R.id.drawerlayout);
        mDrwer.setScrimColor(getResources().getColor(R.color.black_54));
        mBar = (BottomToolBar) findViewById(R.id.bottombar);
        mBtnfab = (FloatingActionButton) findViewById(R.id.btn_fab);
        mBtnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBar.reveal(HomeActivity.this, mBtnfab);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
