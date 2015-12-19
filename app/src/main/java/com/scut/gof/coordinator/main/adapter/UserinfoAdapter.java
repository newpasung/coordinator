package com.scut.gof.coordinator.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.storage.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/20.
 */
public class UserinfoAdapter extends RecyclerView.Adapter {

    User mUser;
    Context mContext;
    List<Integer> captionList;
    List<String> contentList;
    OnListitemClick listitemClick;
    private boolean isLocalUser = false;
    private boolean isEditable = false;

    public UserinfoAdapter(Context context, User mUser, OnListitemClick listener) {
        this.mUser = mUser;
        this.mContext = context;
        setData(mUser);
        isLocalUser = mUser.getUid() == UserManager.getUserid(context);
        isEditable = false;
        listitemClick = listener;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
        notifyDataSetChanged();
    }

    public void updateData(User user) {
        transDataSource(user);
        notifyDataSetChanged();
    }

    protected void setData(User user) {
        captionList = new ArrayList<>();
        contentList = new ArrayList<>();
        transDataSource(user);
    }

    public void modifyAData(int title, String content) {
        int index = 0;
        for (int i = 0; i < captionList.size(); i++) {
            if (captionList.get(i).equals(title)) {
                index = i;
                break;
            }
        }
        contentList.add(index, content);
        contentList.remove(index + 1);
        notifyItemChanged(index);
    }

    //把数据源转为adapter的数据结构
    protected void transDataSource(User user) {
        if (!isLocalUser && !TextUtils.isEmpty(user.getWorkphone())) {
            contentList.add(user.getWorkphone());
            captionList.add(R.string.text_workphone);
        }
        if (!isLocalUser && !TextUtils.isEmpty(user.getEmail())) {
            contentList.add(user.getEmail());
            captionList.add(R.string.text_email);
        }
        if (!isLocalUser && !TextUtils.isEmpty(user.getGender())) {
            contentList.add(user.getGender());
            captionList.add(R.string.text_gender);
        }
        if (!isLocalUser && !TextUtils.isEmpty(user.getSignature())) {
            contentList.add(user.getSignature());
            captionList.add(R.string.text_signature);
        }
        if (!isLocalUser && !TextUtils.isEmpty(user.getLocale())) {
            contentList.add(user.getLocale());
            captionList.add(R.string.text_locale);
        }
        if (!isLocalUser && !TextUtils.isEmpty(user.getBirthday())) {
            contentList.add(user.getBirthday());
            captionList.add(R.string.text_birthday);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MHolder(inflater.inflate(R.layout.listitem_userinfo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MHolder mHolder = (MHolder) holder;
        if (isEditable()) {
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listitemClick.onClick(captionList.get(position), contentList.get(position));
                }
            });
        } else {
            mHolder.itemView.setOnClickListener(null);
        }
        mHolder.mTvcontent.setText(contentList.get(position));
        mHolder.mTvcaption.setText(captionList.get(position));
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public interface OnListitemClick {
        void onClick(int title, String content);
    }

    class MHolder extends RecyclerView.ViewHolder {
        TextView mTvcontent;
        TextView mTvcaption;
        View mViewline;

        public MHolder(View itemView) {
            super(itemView);
            mTvcaption = (TextView) itemView.findViewById(R.id.tv_caption);
            mTvcontent = (TextView) itemView.findViewById(R.id.et_content);
            mViewline = itemView.findViewById(R.id.line_divider);
        }
    }

}
