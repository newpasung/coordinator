package com.scut.gof.coordinator.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.models.SimpleContact;
import com.scut.gof.coordinator.main.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class UserlistAdapter extends RecyclerView.Adapter implements SectionIndexer {

    private final int VIEWTYPE_USERITEM = 1;
    List<SimpleContact> simpleContacts;

    /**
     * 必须传入已经排序的数据
     */
    public void setData(List<SimpleContact> data) {
        simpleContacts = data;
    }

    public void updateData(List<SimpleContact> data) {
        this.simpleContacts = data;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentHolder) {
            int section = getSectionForPosition(position);
            //如果是首位,显示首字母和分割线
            if (position == getPositionForSection(section)) {
                ((ContentHolder) holder).mDivider.setVisibility(View.VISIBLE);
                ((ContentHolder) holder).mTvinitial.setVisibility(View.VISIBLE);
                ((ContentHolder) holder).mTvinitial.setText(String.valueOf(getItem(position).getPinyin().charAt(0)));
                if (position == 0) {
                    ((ContentHolder) holder).mDivider.setVisibility(View.GONE);
                }
            } else {
                ((ContentHolder) holder).mDivider.setVisibility(View.GONE);
                ((ContentHolder) holder).mTvinitial.setVisibility(View.GONE);
            }
            ((ContentHolder) holder).mTvname.setText(getItem(position).getName());
            PicassoProxy.loadAvatar(((ContentHolder) holder).mCiravatar.getContext()
                    , getItem(position).getAvaterUrl(), ((ContentHolder) holder).mCiravatar);
        }
    }

    public SimpleContact getItem(int position) {
        return simpleContacts.get(position);
    }

    @Override
    public int getItemCount() {
        return simpleContacts.size();
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    //获取首次出现某个首字母的位置,应该在这里提速一下
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getItemCount(); i++) {
            if (simpleContacts.get(i).getPinyin().charAt(0) == sectionIndex) {
                return i;
            }
        }
        return 0;
    }

    //使用首字母来的asc来区分section
    @Override
    public int getSectionForPosition(int position) {
        return simpleContacts.get(position).getPinyin().charAt(0);
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

}
