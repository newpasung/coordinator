package com.scut.gof.coordinator.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.localmodels.HomeMessage;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 * 这个adapter用来呈现首页的项目消息公告入口，//TODO 这种逻辑更改起来麻烦，要修正
 */
public class HomeAdapter extends RecyclerView.Adapter {
    public static final String BROAD_ONPROCLICK = "clickonproject";
    public static final int TYPE_PROJECT = 0;
    public static final int TYPE_MSG = 1;
    private static final int DATATYPE_COUNT = 2;
    private static final int HEADLINE_PRO = 2;
    private static final int HEADLINE_MSG = 3;
    Context mContext;
    List<Project> prodata = new ArrayList<>();
    List<HomeMessage> msgdata = new ArrayList<>();
    MOnClick listener;
    public HomeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        LayoutInflater inflater =LayoutInflater.from(mContext);
        switch(viewType){
            case HEADLINE_PRO:
            case HEADLINE_MSG:{
                holder=new HeadHolder(inflater.inflate(R.layout.listitem_headline,parent,false));
            }break;
            case TYPE_PROJECT:{
                holder=new ProHolder(inflater.inflate(R.layout.listitem_projectpaper,parent,false));
            }break;
            case TYPE_MSG:{
                holder=new MsgHolder(inflater.inflate(R.layout.listitem_msgcard,parent,false));
            }break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch(getItemViewType(position)){
            case HEADLINE_PRO:{
                if (hasPro()) {
                    ((HeadHolder) holder).mTv.setText(mContext.getString(R.string.text_myproject));
                } else {
                    ((HeadHolder) holder).mTv.setText(mContext.getString(R.string.text_tip_hasnoproject));
                }
            }break;
            case HEADLINE_MSG:{
                ((HeadHolder) holder).mTv.setText(mContext.getString(R.string.text_importantmsg));
            }break;
            case TYPE_PROJECT:{
                PicassoProxy.loadAvatar(mContext, getProData(position).getThumbnailLogo(), ((ProHolder) holder).mCir);
                ((ProHolder) holder).mTvname.setText(getProData(position).getProname());
                ((ProHolder)holder).mRlcontainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClick(TYPE_PROJECT, getProData(position).getProid());
                        }
                    }
                });
            }break;
            case TYPE_MSG:{
                final HomeMessage message = getMsg(position);
                MsgHolder msgHolder = ((MsgHolder) holder);
                PicassoProxy.loadAvatar(mContext, message.getIconUrl(), msgHolder.mCir);
                msgHolder.mTvtype.setText(mContext.getString(R.string.test_type));
                msgHolder.mTvcontent.setText(message.getContent());
                msgHolder.mTvtype.setText(message.getDescription());
                msgHolder.mBtnpositive.setText(message.getPositiveTitle());
                msgHolder.mBtnnegative.setText(message.getNegativeTitle());
                msgHolder.mBtnpositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (message.getOnPositiveClick() != null) {
                            message.getOnPositiveClick().onClick(v);
                        }
                    }
                });
                msgHolder.mBtnnegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeMsg(position);
                        if (message.getOnNegativeClick() != null) {
                            message.getOnNegativeClick().onClick(v);
                        }
                    }
                });

            }break;
        }
    }

    @Override
    public int getItemCount() {
        return prodata.size() + msgdata.size() + DATATYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if(position<=0){
            return HEADLINE_PRO;
        }else if(position<=prodata.size()){
            return TYPE_PROJECT;
        }
        else if(position<=prodata.size()+1){
            return HEADLINE_MSG;
        }
        else if(position<=prodata.size()+msgdata.size()+1){
            return TYPE_MSG;
        }
        return HEADLINE_PRO;
    }

    public void setProData(List<Project> data) {
        this.prodata.clear();
        this.prodata = data;
    }

    public void setMsgData(List<HomeMessage> data) {
        this.msgdata.clear();
        this.msgdata = data;
    }

    //要转换出data的位置
    protected Project getProData(int position) {
        return prodata.get(position - 1);
    }

    protected HomeMessage getMsg(int position) {
        return msgdata.get(position - prodata.size() - 2);
    }

    protected void removeMsg(int position) {
        msgdata.remove(position - prodata.size() - 2);
        notifyItemRemoved(position);
    }

    protected boolean hasPro() {
        return prodata.size() != 0;
    }

    public void setListener(MOnClick listener) {
        this.listener = listener;
    }

    public interface MOnClick {
        void onClick(int type, long id);
    }

    class ProHolder extends RecyclerView.ViewHolder{
        CircleImageView mCir;
        TextView mTvname;
        RelativeLayout mRlcontainer;
        public ProHolder(View itemView) {
            super(itemView);
            mCir=(CircleImageView)itemView.findViewById(R.id.cir_avatar);
            mTvname=(TextView)itemView.findViewById(R.id.tv_name);
            mRlcontainer=(RelativeLayout)itemView.findViewById(R.id.rl_container);
        }
    }

    class MsgHolder extends RecyclerView.ViewHolder{
        CircleImageView mCir;
        TextView mTvtype;
        TextView mTvcontent;
        Button mBtnpositive;
        Button mBtnnegative;
        public MsgHolder(View itemView) {
            super(itemView);
            mCir=(CircleImageView)itemView.findViewById(R.id.cir_msgtype);
            mTvtype=(TextView)itemView.findViewById(R.id.tv_type);
            mTvcontent=(TextView)itemView.findViewById(R.id.tv_content);
            mBtnpositive = (Button) itemView.findViewById(R.id.btn_positive);
            mBtnnegative = (Button) itemView.findViewById(R.id.btn_negative);
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder{
        TextView mTv;
        public HeadHolder(View itemView) {
            super(itemView);
            mTv=(TextView)itemView.findViewById(R.id.tv_category);
        }
    }

}
