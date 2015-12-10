package com.scut.gof.coordinator.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.widget.CircleImageView;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/9.
 */
public class DetailInfoAdapter extends RecyclerView.Adapter {

    int[] iconRes;
    int[] titles;
    String[] contents;
    OnListitemClick listener;
    HashMap<Integer, Object> extraData;

    private DetailInfoAdapter() {
    }

    public DetailInfoAdapter(String[] contents, HashMap<Integer, Object> extraData, int[] iconRes, int[] titles, OnListitemClick listitemClick) {
        this.contents = contents;
        this.extraData = extraData;
        this.iconRes = iconRes;
        this.titles = titles;
        this.listener = listitemClick;
    }

    public void refreshData(String[] contents, HashMap<Integer, Object> extraData, int[] iconRes, int[] titles) {
        this.contents = contents;
        this.extraData = extraData;
        this.iconRes = iconRes;
        this.titles = titles;
        notifyDataSetChanged();
    }

    public void modifyContent(int title, String content) {
        for (int i = 0; i < titles.length; i++) {
            if (titles[i] == title) {
                contents[i] = content;
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listitem_detailinfo, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MHolder mHolder = (MHolder) holder;
        if (iconRes[position] != 0) {
            if (iconRes[position] == -1) {
                PicassoProxy.loadAvatar(mHolder.itemView.getContext()
                        , (String) extraData.get(iconRes[position]), mHolder.mIvicon);
            } else {
                mHolder.mIvicon.setImageResource(iconRes[position]);
            }
        } else {
            mHolder.mIvicon.setVisibility(View.INVISIBLE);
        }
        mHolder.mTvcaption.setText(titles[position]);
        mHolder.mTvcontent.setText(contents[position]);
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(mHolder.mTvcontent, titles[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.length;
    }

    public interface OnListitemClick {
        void onClick(TextView textView, int titleResId);
    }

    class MHolder extends RecyclerView.ViewHolder {
        CircleImageView mIvicon;
        TextView mTvcaption;
        TextView mTvcontent;

        public MHolder(View itemView) {
            super(itemView);
            this.mIvicon = (CircleImageView) itemView.findViewById(R.id.icon);
            this.mTvcaption = (TextView) itemView.findViewById(R.id.tv_caption);
            this.mTvcontent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

}
