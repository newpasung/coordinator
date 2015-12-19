package com.scut.gof.coordinator.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.scut.gof.coordinator.main.storage.model.Post;

import java.util.List;

/**
 * Created by Administrator on 2015/12/18.
 */
public class PostListAdapter extends RecyclerView.Adapter {
    List<Post> postArrayList;

    public void setData(List<Post> data) {
        this.postArrayList = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
