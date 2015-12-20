package com.scut.gof.coordinator.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.storage.model.User;
import com.scut.gof.coordinator.main.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class UserlistAdapter extends RecyclerView.Adapter implements SectionIndexer {

    private final int VIEWTYPE_USERITEM = 1;
    List<User> userData;
    OnUserClickListener listener;

    public UserlistAdapter(OnUserClickListener listener) {
        this.listener = listener;
    }

    /**
     * 必须传入已经排序的数据
     */
    public void setData(List<User> data) {
        userData = data;
    }

    public void updateData(List<User> data) {
        this.userData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_USERITEM) {
            return new ContentHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_userlist, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEWTYPE_USERITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserClicked(getItem(position).getUid());
            }
        });
        if (holder instanceof ContentHolder) {
            int section = getSectionForPosition(position);
            //如果是首位,显示首字母和分割线
            if (position == getPositionForSection(section)) {
                ((ContentHolder) holder).mDivider.setVisibility(View.VISIBLE);
                ((ContentHolder) holder).mTvinitial.setVisibility(View.VISIBLE);
                ((ContentHolder) holder).mTvinitial.setText(String.valueOf(getItem(position).getName_pinyin().charAt(0)));
                if (position == 0) {
                    ((ContentHolder) holder).mDivider.setVisibility(View.GONE);
                }
            } else {
                ((ContentHolder) holder).mDivider.setVisibility(View.GONE);
                ((ContentHolder) holder).mTvinitial.setVisibility(View.GONE);
            }
            ((ContentHolder) holder).mTvname.setText(getItem(position).getName());
            PicassoProxy.loadAvatar(((ContentHolder) holder).mCiravatar.getContext()
                    , getItem(position).getThumbnailavatar(), ((ContentHolder) holder).mCiravatar);
        }
    }

    public User getItem(int position) {
        return userData.get(position);
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    //获取首次出现某个首字母的位置,应该在这里提速一下
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getItemCount(); i++) {
            if (userData.get(i).getName_pinyin().charAt(0) == sectionIndex) {
                return i;
            }
        }
        return 0;
    }

    //使用首字母来的asc来区分section
    @Override
    public int getSectionForPosition(int position) {
        return userData.get(position).getName_pinyin().charAt(0);
    }

    class ContentHolder extends RecyclerView.ViewHolder {
        View mDivider;
        TextView mTvname;
        CircleImageView mCiravatar;
        TextView mTvinitial;

        public ContentHolder(View itemView) {
            super(itemView);
            mDivider = itemView.findViewById(R.id.line_divider);
            mTvname = (TextView) itemView.findViewById(R.id.tv_name);
            mTvinitial = (TextView) itemView.findViewById(R.id.tv_initial);
            mCiravatar = (CircleImageView) itemView.findViewById(R.id.cir_avatar);
        }
    }

    public interface  OnUserClickListener{
        void onUserClicked(long uid);
    }

}
