package com.scut.gof.coordinator.main.fragment.task;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.widget.BottomToolBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/7.
 */
public class TaskListContainerFragment extends BaseFragment implements BottomBarController {
    ViewPager viewPager;
    MyAdapter pagerAdapter;
    List<Fragment> fragments;
    TabLayout tabLayout;
    String[] tabTitles;
    long proid = -1;

    public TaskListContainerFragment() {

    }

    public static TaskListContainerFragment newInstance(long proid) {
        Bundle args = new Bundle();
        args.putLong("proid", proid);
        TaskListContainerFragment fragment = new TaskListContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            proid = getArguments().getLong("proid");
        }
        return inflater.inflate(R.layout.fragment_tasklistcontainer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        iniData();
        iniUI(view);
        iniAdapter();
    }

    protected void iniUI(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
    }

    protected void iniAdapter() {
        if (pagerAdapter == null) {
            pagerAdapter = new MyAdapter(getChildFragmentManager());
        }
        pagerAdapter.setList(fragments);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override

            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        TabLayout.TabLayoutOnPageChangeListener listener
                = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        viewPager.addOnPageChangeListener(listener);
    }

    protected void iniData() {
        if (fragments == null || fragments.size() == 0) {
            fragments = new ArrayList<>();
            fragments.add(TaskListFragment.newInstance(1));
            fragments.add(TaskListFragment.newInstance(2));
            fragments.add(TaskListFragment.newInstance(3));
            tabTitles = getActivity().getResources().getStringArray(R.array.tasklist_tabtitles);
        }
    }

    @Override
    public void refreshView(BottomToolBar bottomToolBar) {
        bottomToolBar.setText(getString(R.string.action_newtask));
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

    class MyAdapter extends android.support.v13.app.FragmentStatePagerAdapter {
        List<Fragment> list;
        FragmentManager manager;

        public MyAdapter(android.app.FragmentManager fm) {
            super(fm);
            this.manager = fm;
        }

        /**
         * 清除adapter的数据和fragmentmanager绑定的fragment
         */
        public void clearData() {
            list.clear();
        }

        public void setList(List<Fragment> list) {
            this.list = list;
        }

        @Override
        public android.app.Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}