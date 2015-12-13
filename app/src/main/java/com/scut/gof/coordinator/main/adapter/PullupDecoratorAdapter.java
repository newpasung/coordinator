package com.scut.gof.coordinator.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.utils.DenstityUtil;
import com.scut.gof.coordinator.main.widget.CirclePrgbar;

/**
 * Created by Administrator on 2015/12/13.
 */
public class PullupDecoratorAdapter extends RecyclerView.Adapter {

    final int VIEWTYPE_LOADMORE = 88;
    boolean canShowLoadMoreView;
    boolean loadingMore;
    RecyclerView.Adapter contentAdapter;
    OnLoadMoreListener listener;

    private PullupDecoratorAdapter() {
    }

    public PullupDecoratorAdapter(RecyclerView.Adapter adapter, OnLoadMoreListener listener) {
        this.contentAdapter = adapter;
        canShowLoadMoreView = false;
        loadingMore = false;
        this.listener = listener;
    }

    public void notifyChanged() {
//        contentAdapter.notifyDataSetChanged();
        notifyDataSetChanged();
    }

    public void notifyRemoved(int location) {
        contentAdapter.notifyItemRemoved(location);
        notifyItemRemoved(location);
    }

    public void setLoadMoreFinished() {
        if (canShowLoadMoreView == false) return;
        canShowLoadMoreView = false;
        loadingMore = false;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_LOADMORE) {
            return new LoadViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_loadmoretip, parent, false));
        } else {
            return contentAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadViewHolder) {
            if (!canShowLoadMoreView) {
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                params.height = 0;
                holder.itemView.setLayoutParams(params);
            } else {
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                params.height = DenstityUtil.dp2px(46, holder.itemView.getResources());
                holder.itemView.setLayoutParams(params);
                if (!loadingMore) {
                    listener.onLoadMore();
                    loadingMore = true;
                }
            }
        } else {
            contentAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return contentAdapter.getItemCount() + (canShowLoadMoreView ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (canShowLoadMoreView) {
            if (contentAdapter.getItemCount() == position) {
                return VIEWTYPE_LOADMORE;
            } else {
                return contentAdapter.getItemViewType(position);
            }
        } else {
            return contentAdapter.getItemViewType(position);
        }

    }

    public void canShowLoadMoreView(boolean ifOk) {
        canShowLoadMoreView = ifOk;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {
        CirclePrgbar circlePrgbar;

        public LoadViewHolder(View itemView) {
            super(itemView);
            circlePrgbar = (CirclePrgbar) itemView.findViewById(R.id.circleprgbar);
        }
    }

}
