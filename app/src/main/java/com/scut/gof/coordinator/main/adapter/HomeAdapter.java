package com.scut.gof.coordinator.main.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 * 这个adapter用来呈现首页的项目消息公告入口，//TODO 这种逻辑更改起来麻烦，要修正
 */
public class HomeAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<String> prodata=new ArrayList<>();
    List<String> msgdata =new ArrayList<>();
    private final int TYPECOUNT=2;
    private final int TYPE_PROJECT=0;
    private final int TYPE_MSG=1;
    private final int HEADLINE_PRO=2;
    private final int HEADLINE_MSG=3;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)){
            case HEADLINE_PRO:{
                ((HeadHolder) holder).mTv.setText(mContext.getString(R.string.text_myproject));
            }break;
            case HEADLINE_MSG:{
                ((HeadHolder) holder).mTv.setText(mContext.getString(R.string.text_message));
            }break;
            case TYPE_PROJECT:{
                ((ProHolder)holder).mCir.setImageResource(R.drawable.tencent);
                ((ProHolder)holder).mTvname.setText(getProData(position));
                ((ProHolder)holder).mRlcontainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                    }
                });
            }break;
            case TYPE_MSG:{
                ((MsgHolder)holder).mCir.setImageResource(R.drawable.tencent);
                ((MsgHolder)holder).mTvtype.setText(mContext.getString(R.string.test_type));
                ((MsgHolder)holder).mTvcontent.setText(getMsgData(position));
            }break;
        }
    }

    @Override
    public int getItemCount() {
        return prodata.size()+msgdata.size()+TYPECOUNT;
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
        public MsgHolder(View itemView) {
            super(itemView);
            mCir=(CircleImageView)itemView.findViewById(R.id.cir_msgtype);
            mTvtype=(TextView)itemView.findViewById(R.id.tv_type);
            mTvcontent=(TextView)itemView.findViewById(R.id.tv_content);
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder{
        TextView mTv;
        public HeadHolder(View itemView) {
            super(itemView);
            mTv=(TextView)itemView.findViewById(R.id.tv_category);
        }
    }

    public void setProData(List<String > data){
        this.prodata=data;
    }

    public void setMsgData(List<String> data){
        this.msgdata=data;
    }

    //要转换出data的位置
    protected String getProData(int position){
        return prodata.get(position-1);
    }

    protected String getMsgData(int position){
        return msgdata.get(position-prodata.size()-2);
    }

}
