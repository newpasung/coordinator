package com.scut.gof.coordinator.main.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.storage.model.Task;
import com.scut.gof.coordinator.main.utils.DenstityUtil;
import com.scut.gof.coordinator.main.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/5.
 */
public class TaskSynopsisAdapter extends RecyclerView.Adapter {
    static int index = 0;
    final int VIEWTYPE_EMPTYTIP = 0;
    final int VIEWTYPE_TASKS = 1;
    List<Task> taskArrayList;
    MActionListener listener;

    public TaskSynopsisAdapter(MActionListener listener) {
        this.listener = listener;
        this.taskArrayList = new ArrayList<>();
    }

    public void setTaskArrayList(List<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    public List<Task> addTaskOnBottom(Task task) {
        this.taskArrayList.add(task);
        notifyItemInserted(taskArrayList.size());
        return this.taskArrayList;
    }

    private int randomBackground() {
        /*int [] res=new int[]{R.drawable.frame4_blue_radius,R.drawable.frame4_green_radius
        ,R.drawable.frame4_orange_radius,R.drawable.frame4_purple_radius};
        index =index>res.length-1?0:index;
        return res[index++];*/
        return R.drawable.frame4_lightgreen_radius;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_EMPTYTIP) {
            return new EmptyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_imageview, parent, false));
        } else {
            return new MHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_tasksynopsis, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!isDataEmpty()) {
            if (holder instanceof MHolder) {
                MHolder mHolder = (MHolder) holder;
                final Task task = getTask(position);
                Context context = mHolder.mTvname.getContext();
                mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemViewClick(task.getTid());
                    }
                });
                mHolder.mTvname.setText(task.getTname());
                mHolder.mTvtime.setText(task.getDisplayTimeDuration());
                //显示任务的状态
                if (TextUtils.isEmpty(task.getDisplayTaskstatus(context))) {
                    mHolder.mTvtaskstatus.setVisibility(View.GONE);
                } else {
                    mHolder.mTvtaskstatus.setText(task.getDisplayTaskstatus(context));
                    mHolder.mTvtaskstatus.setVisibility(View.VISIBLE);
                }
                //设置是否显示接受任务和肌肉
                mHolder.mCirmuscle.setVisibility(View.GONE);
                mHolder.mBtnright_container.setVisibility(View.VISIBLE);
                mHolder.mBtnright_container.setCardElevation(mHolder.itemView.getContext()
                        .getResources().getDimension(R.dimen.evelation_l1));
                if (task.getPeopleneedcount() - task.getPeoplecount() > 0 && task.getStatus() == 0 && task.getRole() != 2) {
                    //准备中的任务,且还没接受任务,且人数不够
                    mHolder.mBtnaction_right.setText(context.getString(R.string.action_accepttask));
                } else if (task.getRole() == 2) {
                    //我是这个任务的成员
                    mHolder.mCirmuscle.setVisibility(View.VISIBLE);
                    mHolder.mBtnright_container.setCardElevation(0);
                    mHolder.mBtnaction_right.setText(context.getString(R.string.action_quittask));
                } else {
                    mHolder.mBtnright_container.setVisibility(View.GONE);
                }
                mHolder.mBtnaction_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onBtnright(task);
                    }
                });
                //显示右上角的标记
                if (task.getPriority() != 1) {
                    mHolder.mIvconnermark.setVisibility(View.GONE);
                } else {
                    mHolder.mIvconnermark.setBackgroundResource(R.drawable.mark_emergency);
                    mHolder.mIvconnermark.setVisibility(View.VISIBLE);
                }
                //显示任务分类
                mHolder.mTvcategory.setText(task.getCategory());
                mHolder.mTvcategory.setBackgroundResource(randomBackground());
                //显示标签
                if (!task.getTag().equals("")) {
                    mHolder.mTvtag.setText(task.getTag());
                    mHolder.mTvtag.setVisibility(View.VISIBLE);
                    mHolder.mTvtag.setBackgroundResource(randomBackground());
                } else {
                    mHolder.mTvtag.setVisibility(View.GONE);
                }
                //显示人数
                if (!TextUtils.isEmpty(task.getDisplayPeopleCount())) {
                    mHolder.mTvpeople.setText(task.getDisplayPeopleCount());
                    mHolder.mTvpeople.setVisibility(View.VISIBLE);
                } else {
                    mHolder.mTvpeople.setVisibility(View.GONE);
                }
            }
        }
        if (holder instanceof EmptyHolder) {
            EmptyHolder mHolder = (EmptyHolder) holder;
            if (isDataEmpty()) {
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

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEWTYPE_EMPTYTIP;
        } else {
            return VIEWTYPE_TASKS;
        }
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size() + 1;
    }

    public Task getTask(int position) {
        return this.taskArrayList.get(position - 1);
    }

    public boolean isDataEmpty() {
        return this.taskArrayList.size() == 0;
    }

    public interface MActionListener {
        void onBtnright(Task task);

        void onItemViewClick(long tid);
    }

    class MHolder extends RecyclerView.ViewHolder {
        TextView mTvname;
        TextView mTvtime;
        TextView mTvtaskstatus;
        ImageView mIvconnermark;
        Button mBtnaction_right;
        CardView mBtnright_container;
        CircleImageView mCirmuscle;
        //三个可能存在的标签
        TextView mTvcategory;
        TextView mTvtag;
        TextView mTvpeople;

        public MHolder(View itemView) {
            super(itemView);
            this.mTvname = (TextView) itemView.findViewById(R.id.tv_name);
            this.mTvtime = (TextView) itemView.findViewById(R.id.tv_time);
            this.mTvtaskstatus = (TextView) itemView.findViewById(R.id.tv_taskstatus);
            this.mIvconnermark = (ImageView) itemView.findViewById(R.id.iv_connermark);
            this.mBtnaction_right = (Button) itemView.findViewById(R.id.btn_action_right);
            this.mBtnright_container = (CardView) itemView.findViewById(R.id.btn_right_container);
            this.mTvcategory = (TextView) itemView.findViewById(R.id.tv_tag_category);
            this.mTvtag = (TextView) itemView.findViewById(R.id.tv_tag);
            this.mTvpeople = (TextView) itemView.findViewById(R.id.tv_people);
            mCirmuscle = (CircleImageView) itemView.findViewById(R.id.cir_muscle);
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
