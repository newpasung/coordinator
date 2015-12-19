package com.scut.gof.coordinator.main.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.fragment.PostListFragment;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.widget.PagerSlidingTabStrip;

/**
 * Created by Administrator on 2015/12/18.
 */
public class PostListActivity extends BaseActivity {

    public final int REQUESTCODE_NEWPOST = 10;
    ViewPager viewPager;
    PagerSlidingTabStrip pagerTabStrip;
    String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);
        titles = new String[]{"任务记录", "吐槽"};
        iniUi();
    }

    private void iniUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(ApiUtil.getDrawable(getResources(), R.mipmap.plus));
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        pagerTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerslidingtabstrip);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getFragmentManager()));
        pagerTabStrip.setViewPager(viewPager);//为slidingpager设置一个viewpager这个viewpager在布局中位于其下方
        pagerTabStrip.setDividerPadding(0);
        pagerTabStrip.setTextColorResource(R.color.white);
        pagerTabStrip.setTextSize(getResources().getDimensionPixelSize(R.dimen.textsize_l3));
        pagerTabStrip.setUnderlineColorResource(R.color.transparent);//滑动栏下方的分隔线
        pagerTabStrip.setIndicatorColorResource(R.color.colorAccent);
        pagerTabStrip.setIndicatorHeight(5);
        pagerTabStrip.setDividerColorResource(R.color.transparent);//标题之间的分割线
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, NewPostActivity.class);
        intent.putExtra("title", item.getTitle());
        startActivityForResult(intent, REQUESTCODE_NEWPOST);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for (int i = 0; i < titles.length; i++) {
            menu.add(titles[i]);
        }
        return true;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PostListFragment.newInstance(titles[position]);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
