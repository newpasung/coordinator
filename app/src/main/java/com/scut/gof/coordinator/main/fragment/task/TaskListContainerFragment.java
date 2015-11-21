package com.scut.gof.coordinator.main.fragment.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.BaseSupportFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/7.
 */
public class TaskListContainerFragment extends BaseSupportFragment {

    ViewPager viewPager;
    MyAdapter pagerAdapter;
    List<Fragment> fragments;
    TabLayout tabLayout;
    String[] tabTitles;
    long proid = -1;

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
        tabTitles = getActivity().getResources().getStringArray(R.array.tasklist_tabtitles);
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
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter = new MyAdapter(getFragmentManager(), fragments);
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
        viewPager.setAdapter(pagerAdapter);
    }

    protected void iniData() {
        if (getArguments() != null) {
            proid = getArguments().getLong("proid");
        }
        fragments = new ArrayList<>();
        fragments.add(TaskListFragment.newInstance(proid));
        fragments.add(TaskListFragment.newInstance(proid));
        fragments.add(TaskListFragment.newInstance(proid));
    }

    class MyAdapter extends FragmentPagerAdapter {
        List<Fragment> list;

        public MyAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.list = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
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
            if (position < tabTitles.length) {
                return tabTitles[position];
            } else {
                return "";
            }
        }
    }

}
