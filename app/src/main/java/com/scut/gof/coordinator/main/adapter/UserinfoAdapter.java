package com.scut.gof.coordinator.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.storage.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/20.
 */
public class UserinfoAdapter extends RecyclerView.Adapter {

    User mUser;
    Context mContext;
    List<String> captionList;
    List<String> contentList;

    public UserinfoAdapter(Context context, User mUser) {
        this.mUser = mUser;
        this.mContext = context;
        setData(mUser);
    }

    protected void setData(User user) {
        captionList = new ArrayList<>();
        contentList = new ArrayList<>();

        captionList.add(mContext.getString(R.string.text_workphone));
        captionList.add(mContext.getString(R.string.text_email));
        captionList.add(mContext.getString(R.string.text_gender));
        captionList.add(mContext.getString(R.string.text_signature));
        captionList.add(mContext.getString(R.string.text_locale));
        captionList.add(mContext.getString(R.string.text_birthday));

        contentList.add(user.getWorkphone());
        contentList.add(user.getEmail());
        contentList.add(user.getGender());
        contentList.add(user.getSignature());
        contentList.add(user.getLocale());
        contentList.add(user.getBirthday());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MHolder(inflater.inflate(R.layout.listitem_userinfo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MHolder mHolder = (MHolder) holder;
        mHolder.mTvcontent.setText(contentList.get(position));
        mHolder.mTvcaption.setText(captionList.get(position));
        if (position == getItemCount() - 1) {
            mHolder.mViewline.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return captionList.size();
    }

    class MHolder extends RecyclerView.ViewHolder {
        TextView mTvcontent;
        TextView mTvcaption;
        View mViewline;

        public MHolder(View itemView) {
            super(itemView);
            mTvcaption = (TextView) itemView.findViewById(R.id.tv_caption);
            mTvcontent = (TextView) itemView.findViewById(R.id.tv_content);
            mViewline = itemView.findViewById(R.id.line_divider);
        }
    }

}
