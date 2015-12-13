package com.scut.gof.coordinator.main.fragment.task;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.task.CreateTaskActivity;
import com.scut.gof.coordinator.main.activity.task.TaskHierarchyActivity;
import com.scut.gof.coordinator.main.communication.LocalBrCast;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.storage.model.Task;
import com.scut.gof.coordinator.main.widget.BottomToolBar;
import com.scut.gof.coordinator.main.widget.CirclePrgbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/7.
 */
public class TaskListContainerFragment extends BaseFragment implements BottomBarController {
    final int REQUESTCODE_NEWTASK = 1;
    ViewPager viewPager;
    MyAdapter pagerAdapter;
    List<Fragment> fragments;
    TabLayout tabLayout;
    String[] tabTitles;
    Project project;
    //下面是taskfilter的组件
    LinearLayout mLltaskfilter;
    Button mBtncancel;
    Button mBtncommit;
    CirclePrgbar circlePrgbar;
    RadioGroup mBtngroup;
    View maskView;
    BottomToolBar mBottomtoolbar;
    String[] categories;
    boolean animating = false;
    long proid = -1;
    public TaskListContainerFragment() {
        categories = new String[0];
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
            project = Project.getProById(proid);
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
        mLltaskfilter = (LinearLayout) view.findViewById(R.id.ll_taskfilter);
        mBtncancel = (Button) view.findViewById(R.id.btn_cancel);
        circlePrgbar = (CirclePrgbar) view.findViewById(R.id.circleprgbar);
        mBtngroup = (RadioGroup) view.findViewById(R.id.category_group);
        mBtncommit = (Button) view.findViewById(R.id.btn_commit);
        mLltaskfilter.setVisibility(View.INVISIBLE);
        maskView = view.findViewById(R.id.maskview);
        mBtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideTaskFilter();
            }
        });
        mBtncommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = "";
                if (mBtngroup.getCheckedRadioButtonId() != -1) {
                    category = categories[mBtngroup.getCheckedRadioButtonId()];
                }
                modifyFilter(category);
                hideTaskFilter();
            }
        });
        maskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideTaskFilter();
            }
        });
    }

    public void showTaskFilter() {
        if (animating) return;
        if (mLltaskfilter.getVisibility() == View.VISIBLE) return;
        if (categories.length == 0) {
            netRefreshCategories(proid);
            circlePrgbar.setVisibility(View.VISIBLE);
        }
        int height = mLltaskfilter.getHeight();
        mLltaskfilter.setTranslationX(0);
        TranslateAnimation animation = new TranslateAnimation(0, 0, -height, 0);
        animation.setInterpolator(new LinearOutSlowInInterpolator());
        animation.setFillAfter(false);
        animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mLltaskfilter.setVisibility(View.VISIBLE);
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mLltaskfilter.startAnimation(animation);
    }

    public void hideTaskFilter() {
        if (animating) return;
        if (mLltaskfilter.getVisibility() == View.GONE) return;
        final int height = mLltaskfilter.getHeight();
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -height);
        animation.setInterpolator(new LinearOutSlowInInterpolator());
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLltaskfilter.setVisibility(View.GONE);
                animating = false;
                if (mBottomtoolbar != null) {
                    mBottomtoolbar.showFab();
                }
                mLltaskfilter.setTranslationX(-height);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLltaskfilter.startAnimation(animation);
    }

    private void netRefreshCategories(long proid) {
        RequestParams params = new RequestParams();
        params.put("proid", proid);
        HttpClient.post(getActivity(), "task/categories", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String data = response.getJSONObject("data").getString("categories");
                    categories = data.split(";");
                    if (categories != null && categories.length > 0) {
                        mBtngroup.removeAllViews();
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        for (int i = 0; i < categories.length; i++) {
                            RadioButton button = new RadioButton(getActivity());
                            button.setLayoutParams(layoutParams);
                            button.setText(categories[i]);
                            button.setId(i);
                            mBtngroup.addView(button);
                        }
                    }

                    circlePrgbar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
            }
        });
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
            fragments.add(TaskListFragment.newInstance(proid, 0));
            fragments.add(TaskListFragment.newInstance(proid, 1));
            fragments.add(TaskListFragment.newInstance(proid, 2));
            tabTitles = getActivity().getResources().getStringArray(R.array.tasklist_tabtitles);
        }
    }

    public void modifyFilter(String category) {
        for (int i = 0; i < fragments.size(); i++) {
            ((TaskListFragment) fragments.get(i)).modifyNetFilter(category);
        }
        LocalBrCast.sendBroadcast(getActivity(), LocalBrCast.PARAM_NETREFRESHTASK);
    }

    @Override
    public void refreshView(BottomToolBar bottomToolBar) {
        if (project == null) return;
        if (project.getStatus() == 1) {
            bottomToolBar.setText("任务层级视图", getString(R.string.action_filtercondition), getString(R.string.action_newtask));
        } else {
            bottomToolBar.hideFab();
        }
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {
        Intent intent = new Intent(getActivity(), TaskHierarchyActivity.class);
        intent.putExtra("proid", proid);
        startActivity(intent);
    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {
        if (mBottomtoolbar == null) {
            mBottomtoolbar = toolBar;
        }
        toolBar.hideAll();
        showTaskFilter();
    }

    @Override
    public void controllright(BottomToolBar toolBar) {
        Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
        intent.putExtra("proid", proid);
        startActivityForResult(intent, REQUESTCODE_NEWTASK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_NEWTASK && resultCode == Activity.RESULT_OK) {
            ((TaskListFragment) fragments.get(0))
                    .addTaskOnBottom(Task.getTaskById(data.getLongExtra("tid", 0)));
        }
    }

    class MyAdapter extends android.support.v13.app.FragmentStatePagerAdapter {
        List<Fragment> list;
        FragmentManager manager;

        public MyAdapter(android.app.FragmentManager fm) {
            super(fm);
            this.manager = fm;
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
