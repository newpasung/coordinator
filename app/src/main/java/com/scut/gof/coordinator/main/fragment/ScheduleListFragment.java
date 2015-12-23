package com.scut.gof.coordinator.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.AddScheduleActivity;
import com.scut.gof.coordinator.main.activity.ScheduleDetailActivity;
import com.scut.gof.coordinator.main.adapter.PullupDecoratorAdapter;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.XManager;
import com.scut.gof.coordinator.main.storage.model.Schedule;
import com.scut.gof.coordinator.main.utils.DenstityUtil;
import com.scut.gof.coordinator.main.widget.BottomToolBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
public class ScheduleListFragment extends BaseFragment implements BottomBarController {

    RecyclerView mRecyclerview;
    SwipeRefreshLayout swipeRefreshLayout;
    List<Schedule> scheduleList;
    ScheduleListAdapter adapter;
    PullupDecoratorAdapter adapterDecorator;
    private int REQUESTCODE_DETAIL = 123;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedulelist, container, false);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        scheduleList = Schedule.get10Schedules();
        if (scheduleList == null) {
            scheduleList = new ArrayList<>(0);
        }
        adapter = new ScheduleListAdapter();
        adapterDecorator = new PullupDecoratorAdapter(adapter, new PullupDecoratorAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerview.setAdapter(adapter);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netGetSchedules();
            }
        });
    }

    @Override
    public void refreshView(BottomToolBar bottomToolBar) {
        bottomToolBar.setText(new String[]{"", "", "添加日程"});
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {

    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {

    }

    @Override
    public void controllright(BottomToolBar toolBar) {
        startActivity(new Intent(getActivity(), AddScheduleActivity.class));
    }

    //默认设置为剩下时间的倒数百分之二十处
    public void setAlertTime(int position) {
        Schedule schedule = scheduleList.get(position - 1);
        //新时间默认为开始时间前20%的时间点（现在离开始时间的间隔）
        long newtime = schedule.getStarttime()
                - (schedule.getStarttime() - System.currentTimeMillis()) / 5;
        if (schedule.getIsnetsync() == 1) {
            if (XManager.isAutoSync(getActivity())) {
                netAlerttime(newtime);
            }
        }
        schedule.setAlerttime(newtime);
    }

    public void netAlerttime(long time) {
    }

    public void netGetSchedules() {
        swipeRefreshLayout.setRefreshing(true);
        HttpClient.get(getActivity(), "schedule/myschedules", null, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Schedule.insertOrUpdate(response.getJSONObject("data").getJSONArray("schedules"));
                    scheduleList = Schedule.get10Schedules();
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                scheduleList = Schedule.get10Schedules();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void netLoadSchedules() {

        HttpClient.post(getActivity(), "", null, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String message, String for_param) {

            }
        });
    }

    class ScheduleListAdapter extends RecyclerView.Adapter {
        final int VIEWTYPE_EMPTYTIP = 0;
        final int VIEWTYPE_SCHEDULES = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEWTYPE_EMPTYTIP) {
                return new EmptyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_imageview, parent, false));
            } else {
                return new ScheduleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_schedule, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ScheduleHolder) {
                ((ScheduleHolder) holder).mIBtnclock
                        .setBackgroundResource(getItem(position).getAlerttime() == Schedule.ALERTTIME_NOALERT
                                ? R.drawable.clock_off : R.drawable.clock_on);
                ((ScheduleHolder) holder).mTvcontent.setText(getItem(position).getContent());
                ((ScheduleHolder) holder).mTvtime.setText(getItem(position).getDisplayStarttime());
                ((ScheduleHolder) holder).mIBtnclock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAlertTime(position);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScheduleDetailActivity.class);
                        intent.putExtra(ScheduleDetailActivity.EXTRA_LOCALSID, getItem(position).getId());
                        startActivityForResult(intent, REQUESTCODE_DETAIL);
                    }
                });
            }
            if (holder instanceof EmptyHolder) {
                EmptyHolder mHolder = (EmptyHolder) holder;
                if (scheduleList.size() == 0) {
                    ViewGroup.LayoutParams params = mHolder.itemView.getLayoutParams();
                    params.height = DenstityUtil.getScreenHeight(mHolder.itemView.getContext()) * 2 / 3;
                    mHolder.mContainer.setLayoutParams(params);
                    mHolder.mContainer.setVisibility(View.VISIBLE);
                    mHolder.mIvtip.setVisibility(View.VISIBLE);
                } else {
                    ViewGroup.LayoutParams params = mHolder.itemView.getLayoutParams();
                    params.height = 0;
                    mHolder.mContainer.setLayoutParams(params);
                    mHolder.mContainer.setVisibility(View.GONE);
                    mHolder.mIvtip.setVisibility(View.GONE);
                }
            }
        }

        public Schedule getItem(int position) {
            return scheduleList.get(position - 1);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return VIEWTYPE_EMPTYTIP;
            } else {
                return VIEWTYPE_SCHEDULES;
            }
        }

        @Override
        public int getItemCount() {
            return scheduleList.size();
        }

        class ScheduleHolder extends RecyclerView.ViewHolder {
            TextView mTvcontent;
            ImageButton mIBtnclock;
            TextView mTvtime;

            public ScheduleHolder(View itemView) {
                super(itemView);
                mTvtime = (TextView) itemView.findViewById(R.id.tv_time);
                mTvcontent = (TextView) itemView.findViewById(R.id.tv_content);
                mIBtnclock = (ImageButton) itemView.findViewById(R.id.iv_clock);
            }
        }

        class EmptyHolder extends RecyclerView.ViewHolder {
            ImageView mIvtip;
            LinearLayout mContainer;

            public EmptyHolder(View itemView) {
                super(itemView);
                mIvtip = (ImageView) itemView.findViewById(R.id.iv_nodatatip);
                mContainer = (LinearLayout) itemView.findViewById(R.id.pic_container);
            }
        }

    }

}
