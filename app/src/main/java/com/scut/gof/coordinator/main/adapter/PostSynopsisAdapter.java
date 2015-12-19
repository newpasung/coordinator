package com.scut.gof.coordinator.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.storage.model.Post;
import com.scut.gof.coordinator.main.utils.DenstityUtil;
import com.scut.gof.coordinator.main.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/12/19.
 * 这个adapter把每一行的内容都细分了，所以有很多holder
 */
public class PostSynopsisAdapter extends RecyclerView.Adapter {
    private final int VIEWTYPE_HEADER = 0;
    private final int VIEWTYPE_SINGLEPIC = 1;//有且仅有1张图
    private final int VIEWTYPE_VPICS4 = 2;//有且仅有4张图
    private final int VIEWTYPE_HPICS4 = 3;//有且仅有4张图
    private final int VIEWTYPE_PICS6 = 4;//有且仅有6张图
    private final int VIEWTYPE_PICS9 = 5;//235789张图
    private final int VIEWTYPE_FOOTER = 6;
    List<Post> data;
    List<DisplayItem> items;
    Context mContext;
    int screenWidth;
    OnPostItemClick onPostItemClick;

    public PostSynopsisAdapter(Context context, OnPostItemClick onPostItemClick) {
        this.data = new ArrayList<>();
        this.items = new ArrayList<>();
        mContext = context;
        this.onPostItemClick = onPostItemClick;
        screenWidth = DenstityUtil.getScreenWidth(context);
    }

    public void setData(List<Post> data) {
        if (data == null) return;
        this.data = data;
        items = formateAllData(this.data);
    }

    public void updateData(List<Post> data) {
        if (data == null) return;
        this.data = data;
        items = formateAllData(data);
        notifyDataSetChanged();
    }

    private List<DisplayItem> formateAllData(List<Post> posts) {
        List<DisplayItem> displayItems = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            displayItems.add(new HeaderItem().acceptData(post));
            switch (post.getPiccount()) {
                case 0:
                    break;
                case 2:
                case 3:
                case 5:
                case 7:
                case 8:
                case 9:
                    displayItems.add(new Pic9Item().acceptData(post));
                    break;
                case 6:
                    displayItems.add(new Pics6Item().acceptData(post));
                    break;
                case 4: {
                    //无法得知图片大小，先随机吧
                    if (0 == random.nextInt(2)) {
                        displayItems.add(new HorizontalPics4Item().acceptData(post));
                    } else {
                        displayItems.add(new VerticalPics4Item().acceptData(post));
                    }
                }
                break;
                case 1:
                    displayItems.add(new SinglePicItem().acceptData(post));
                    break;
                default:
                    break;
            }
            displayItems.add(new FooterItem().acceptData(post));
        }
        return displayItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEWTYPE_HEADER: {
                return new HeaderHolder(inflater.inflate(R.layout.listitem_postsynopsis_header, parent, false));
            }
            case VIEWTYPE_SINGLEPIC: {
                return new SinglePicHolder(inflater.inflate(R.layout.listitem_singlepic, parent, false));
            }
            case VIEWTYPE_VPICS4: {
                return new VerticalPics4Holder(inflater.inflate(R.layout.listitem_4pics_vertical, parent, false));
            }
            case VIEWTYPE_HPICS4: {
                return new HorizontalPics4Holder(inflater.inflate(R.layout.listitem_4pics_horizontal, parent, false));
            }
            case VIEWTYPE_PICS6: {
                return new Pics6Holder(inflater.inflate(R.layout.listitem_6pics, parent, false));
            }
            case VIEWTYPE_PICS9: {
                return new Pics9Holder(inflater.inflate(R.layout.listitem_9pics, parent, false));
            }
            case VIEWTYPE_FOOTER: {
                return new FooterHolder(inflater.inflate(R.layout.listitem_postsynopsis_footer, parent, false));
            }
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //下面是对每一红holder进行不同的设置
        Context context = holder.itemView.getContext();
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).mTvname.setText(getPost(position).getCreator().getName());
            if (TextUtils.isEmpty(getPost(position).getContent())) {
                ((HeaderHolder) holder).mTvcontent.setVisibility(View.GONE);
            } else {
                ((HeaderHolder) holder).mTvcontent.setText(getPost(position).getContent());
                ((HeaderHolder) holder).mTvcontent.setVisibility(View.VISIBLE);
            }
            PicassoProxy.loadAvatar(holder.itemView.getContext(),
                    getPost(position).getCreator().getThumbnailavatar(),
                    ((HeaderHolder) holder).mCiravatar);
            addUserClick(((HeaderHolder) holder).mTvname, getPost(position).getCreatorid());
            addUserClick(((HeaderHolder) holder).mCiravatar, getPost(position).getCreatorid());
        } else if (holder instanceof SinglePicHolder) {
            PicassoProxy.load(context, getPost(position).getPreviewBitPic0(), ((SinglePicHolder) holder).mIvpic0);
            addImageClick(((SinglePicHolder) holder).mIvpic0, getPost(position).getPic0());
        } else if (holder instanceof VerticalPics4Holder) {
            PicassoProxy.load(context, getPost(position).getVPreviewBigPic0(), ((VerticalPics4Holder) holder).mIvpic0);
            PicassoProxy.load(context, getPost(position).getPicthumbnail1(), ((VerticalPics4Holder) holder).mIvpic1);
            PicassoProxy.load(context, getPost(position).getPicthumbnail2(), ((VerticalPics4Holder) holder).mIvpic2);
            PicassoProxy.load(context, getPost(position).getPicthumbnail3(), ((VerticalPics4Holder) holder).mIvpic3);
            addImageClick(((VerticalPics4Holder) holder).mIvpic0, getPost(position).getPic0());
            addImageClick(((VerticalPics4Holder) holder).mIvpic1, getPost(position).getPic1());
            addImageClick(((VerticalPics4Holder) holder).mIvpic2, getPost(position).getPic2());
            addImageClick(((VerticalPics4Holder) holder).mIvpic3, getPost(position).getPic3());
        } else if (holder instanceof HorizontalPics4Holder) {
            PicassoProxy.load(context, getPost(position).getHPreviewBigPic0(), ((HorizontalPics4Holder) holder).mIvpic0);
            PicassoProxy.load(context, getPost(position).getPicthumbnail1(), ((HorizontalPics4Holder) holder).mIvpic1);
            PicassoProxy.load(context, getPost(position).getPicthumbnail2(), ((HorizontalPics4Holder) holder).mIvpic2);
            PicassoProxy.load(context, getPost(position).getPicthumbnail3(), ((HorizontalPics4Holder) holder).mIvpic3);
            addImageClick(((HorizontalPics4Holder) holder).mIvpic0, getPost(position).getPic0());
            addImageClick(((HorizontalPics4Holder) holder).mIvpic1, getPost(position).getPic1());
            addImageClick(((HorizontalPics4Holder) holder).mIvpic2, getPost(position).getPic2());
            addImageClick(((HorizontalPics4Holder) holder).mIvpic3, getPost(position).getPic3());
        } else if (holder instanceof Pics6Holder) {
            PicassoProxy.load(context, getPost(position).getPreviewBitPic0(), ((Pics6Holder) holder).mIvpic0);
            PicassoProxy.load(context, getPost(position).getPicthumbnail1(), ((Pics6Holder) holder).mIvpic1);
            PicassoProxy.load(context, getPost(position).getPicthumbnail2(), ((Pics6Holder) holder).mIvpic2);
            PicassoProxy.load(context, getPost(position).getPicthumbnail3(), ((Pics6Holder) holder).mIvpic3);
            PicassoProxy.load(context, getPost(position).getPicthumbnail4(), ((Pics6Holder) holder).mIvpic4);
            PicassoProxy.load(context, getPost(position).getPicthumbnail5(), ((Pics6Holder) holder).mIvpic5);
            addImageClick(((Pics6Holder) holder).mIvpic0, getPost(position).getPic0());
            addImageClick(((Pics6Holder) holder).mIvpic1, getPost(position).getPic1());
            addImageClick(((Pics6Holder) holder).mIvpic2, getPost(position).getPic2());
            addImageClick(((Pics6Holder) holder).mIvpic3, getPost(position).getPic3());
            addImageClick(((Pics6Holder) holder).mIvpic4, getPost(position).getPic4());
            addImageClick(((Pics6Holder) holder).mIvpic5, getPost(position).getPic5());
        } else if (holder instanceof Pics9Holder) {
            //初始化部分为不可见,到这里肯定是有图片的
            ((Pics9Holder) holder).mLlrow2.setVisibility(View.GONE);
            ((Pics9Holder) holder).mLlrow3.setVisibility(View.GONE);
            ((Pics9Holder) holder).mIvpic2.setVisibility(View.INVISIBLE);
            ((Pics9Holder) holder).mIvpic5.setVisibility(View.INVISIBLE);
            ((Pics9Holder) holder).mIvpic7.setVisibility(View.INVISIBLE);
            ((Pics9Holder) holder).mIvpic8.setVisibility(View.INVISIBLE);
            switch (getPost(position).getPiccount()) {
                case 9:
                    PicassoProxy.load(context, getPost(position).getPicthumbnail8(), ((Pics9Holder) holder).mIvpic8);
                    ((Pics9Holder) holder).mIvpic8.setVisibility(View.VISIBLE);
                    addImageClick(((Pics9Holder) holder).mIvpic8, getPost(position).getPic8());
                case 8:
                    PicassoProxy.load(context, getPost(position).getPicthumbnail7(), ((Pics9Holder) holder).mIvpic7);
                    ((Pics9Holder) holder).mIvpic7.setVisibility(View.VISIBLE);
                    addImageClick(((Pics9Holder) holder).mIvpic7, getPost(position).getPic7());
                case 7:
                    //第三行可见
                    ((Pics9Holder) holder).mLlrow3.setVisibility(View.VISIBLE);
                    ((Pics9Holder) holder).mIvpic5.setVisibility(View.VISIBLE);
                    PicassoProxy.load(context, getPost(position).getPicthumbnail5(), ((Pics9Holder) holder).mIvpic5);
                    PicassoProxy.load(context, getPost(position).getPicthumbnail6(), ((Pics9Holder) holder).mIvpic6);
                    addImageClick(((Pics9Holder) holder).mIvpic5, getPost(position).getPic5());
                    addImageClick(((Pics9Holder) holder).mIvpic6, getPost(position).getPic6());
                case 5:
                    //第二行可见
                    ((Pics9Holder) holder).mLlrow2.setVisibility(View.VISIBLE);
                    PicassoProxy.load(context, getPost(position).getPicthumbnail3(), ((Pics9Holder) holder).mIvpic3);
                    PicassoProxy.load(context, getPost(position).getPicthumbnail4(), ((Pics9Holder) holder).mIvpic4);
                    addImageClick(((Pics9Holder) holder).mIvpic3, getPost(position).getPic3());
                    addImageClick(((Pics9Holder) holder).mIvpic4, getPost(position).getPic4());
                case 3:
                    PicassoProxy.load(context, getPost(position).getPicthumbnail3(), ((Pics9Holder) holder).mIvpic2);
                    ((Pics9Holder) holder).mIvpic2.setVisibility(View.VISIBLE);
                    addImageClick(((Pics9Holder) holder).mIvpic2, getPost(position).getPic2());
                case 2:
                    PicassoProxy.load(context, getPost(position).getPicthumbnail0(), ((Pics9Holder) holder).mIvpic0);
                    PicassoProxy.load(context, getPost(position).getPicthumbnail1(), ((Pics9Holder) holder).mIvpic1);
                    addImageClick(((Pics9Holder) holder).mIvpic0, getPost(position).getPic0());
                    addImageClick(((Pics9Holder) holder).mIvpic1, getPost(position).getPic1());
                    ((Pics9Holder) holder).mIvpic1.setVisibility(View.VISIBLE);
            }
        } else if (holder instanceof FooterHolder) {
            ((FooterHolder) holder).mTextview.setText(getPost(position).getDisplayTime());
        }
    }

    public Post getPost(int position) {
        return items.get(position).getPost();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void addImageClick(ImageView view, final String url) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostItemClick.onImageClick(url);
            }
        });
    }

    private void addUserClick(View view, final long uid) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostItemClick.onUserClick(uid);
            }
        });
    }

    public interface OnPostItemClick {
        void onImageClick(String url);

        void onUserClick(long uid);
    }

    //高能警告！下面是一大堆类
    abstract class DisplayItem {
        Post post;

        public DisplayItem acceptData(Post post) {
            this.post = post;
            return this;
        }

        public Post getPost() {
            return this.post;
        }

        abstract int type();
    }

    class HeaderItem extends DisplayItem {
        @Override
        int type() {
            return VIEWTYPE_HEADER;
        }
    }

    class SinglePicItem extends DisplayItem {
        @Override
        int type() {
            return VIEWTYPE_SINGLEPIC;
        }
    }

    class HorizontalPics4Item extends DisplayItem {
        @Override
        int type() {
            return VIEWTYPE_HPICS4;
        }
    }

    class VerticalPics4Item extends DisplayItem {
        @Override
        int type() {
            return VIEWTYPE_VPICS4;
        }
    }

    class Pics6Item extends DisplayItem {
        @Override
        int type() {
            return VIEWTYPE_PICS6;
        }
    }

    class Pic9Item extends DisplayItem {
        @Override
        int type() {
            return VIEWTYPE_PICS9;
        }
    }

    class FooterItem extends DisplayItem {
        @Override
        int type() {
            return VIEWTYPE_FOOTER;
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        CircleImageView mCiravatar;
        TextView mTvname;
        TextView mTvcontent;

        public HeaderHolder(View itemView) {
            super(itemView);
            mCiravatar = (CircleImageView) itemView.findViewById(R.id.cir_avatar);
            mTvname = (TextView) itemView.findViewById(R.id.tv_name);
            mTvcontent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    class SinglePicHolder extends RecyclerView.ViewHolder {
        ImageView mIvpic0;

        public SinglePicHolder(View itemView) {
            super(itemView);
            mIvpic0 = (ImageView) itemView.findViewById(R.id.iv_pic0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth * 3 / 5, screenWidth * 3 / 5);
            mIvpic0.setLayoutParams(layoutParams);
        }
    }

    class HorizontalPics4Holder extends RecyclerView.ViewHolder {
        ImageView mIvpic0;
        ImageView mIvpic1;
        ImageView mIvpic2;
        ImageView mIvpic3;

        public HorizontalPics4Holder(View itemView) {
            super(itemView);
            mIvpic0 = (ImageView) itemView.findViewById(R.id.iv_pic0);
            mIvpic1 = (ImageView) itemView.findViewById(R.id.iv_pic1);
            mIvpic2 = (ImageView) itemView.findViewById(R.id.iv_pic2);
            mIvpic3 = (ImageView) itemView.findViewById(R.id.iv_pic3);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth));
        }
    }

    class VerticalPics4Holder extends RecyclerView.ViewHolder {
        ImageView mIvpic0;
        ImageView mIvpic1;
        ImageView mIvpic2;
        ImageView mIvpic3;

        public VerticalPics4Holder(View itemView) {
            super(itemView);
            mIvpic0 = (ImageView) itemView.findViewById(R.id.iv_pic0);
            mIvpic1 = (ImageView) itemView.findViewById(R.id.iv_pic1);
            mIvpic2 = (ImageView) itemView.findViewById(R.id.iv_pic2);
            mIvpic3 = (ImageView) itemView.findViewById(R.id.iv_pic3);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth));
        }
    }

    class Pics6Holder extends RecyclerView.ViewHolder {
        ImageView mIvpic0;
        ImageView mIvpic1;
        ImageView mIvpic2;
        ImageView mIvpic3;
        ImageView mIvpic4;
        ImageView mIvpic5;

        public Pics6Holder(View itemView) {
            super(itemView);
            mIvpic0 = (ImageView) itemView.findViewById(R.id.iv_pic0);
            mIvpic1 = (ImageView) itemView.findViewById(R.id.iv_pic1);
            mIvpic2 = (ImageView) itemView.findViewById(R.id.iv_pic2);
            mIvpic3 = (ImageView) itemView.findViewById(R.id.iv_pic3);
            mIvpic4 = (ImageView) itemView.findViewById(R.id.iv_pic4);
            mIvpic5 = (ImageView) itemView.findViewById(R.id.iv_pic5);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth * 6 / 5));
        }
    }

    class Pics9Holder extends RecyclerView.ViewHolder {
        ImageView mIvpic0;
        ImageView mIvpic1;
        ImageView mIvpic2;
        ImageView mIvpic3;
        ImageView mIvpic4;
        ImageView mIvpic5;
        ImageView mIvpic6;
        ImageView mIvpic7;
        ImageView mIvpic8;
        LinearLayout mLlrow1;
        LinearLayout mLlrow2;
        LinearLayout mLlrow3;

        public Pics9Holder(View itemView) {
            super(itemView);
            LinearLayout.LayoutParams multiPicParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth * 1 / 3);
            mIvpic0 = (ImageView) itemView.findViewById(R.id.iv_pic0);
            mIvpic1 = (ImageView) itemView.findViewById(R.id.iv_pic1);
            mIvpic2 = (ImageView) itemView.findViewById(R.id.iv_pic2);
            mIvpic3 = (ImageView) itemView.findViewById(R.id.iv_pic3);
            mIvpic4 = (ImageView) itemView.findViewById(R.id.iv_pic4);
            mIvpic5 = (ImageView) itemView.findViewById(R.id.iv_pic5);
            mIvpic6 = (ImageView) itemView.findViewById(R.id.iv_pic6);
            mIvpic7 = (ImageView) itemView.findViewById(R.id.iv_pic7);
            mIvpic8 = (ImageView) itemView.findViewById(R.id.iv_pic8);
            mLlrow1 = (LinearLayout) itemView.findViewById(R.id.ll_row1);
            mLlrow2 = (LinearLayout) itemView.findViewById(R.id.ll_row2);
            mLlrow3 = (LinearLayout) itemView.findViewById(R.id.ll_row3);
            mLlrow1.setLayoutParams(multiPicParam);
            mLlrow3.setLayoutParams(multiPicParam);
            mLlrow2.setLayoutParams(multiPicParam);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        TextView mTextview;

        public FooterHolder(View itemView) {
            super(itemView);
            mTextview = (TextView) itemView.findViewById(R.id.textview);
        }
    }

}
