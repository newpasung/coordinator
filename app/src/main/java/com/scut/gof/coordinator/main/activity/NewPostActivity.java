package com.scut.gof.coordinator.main.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.libs.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.net.qiniu.QiniuHelper;
import com.scut.gof.coordinator.main.storage.model.Post;
import com.scut.gof.coordinator.main.widget.HLinearLayoutManager;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/18.
 */
public class NewPostActivity extends BaseActivity {

    private final int MAX_PICCOUNT = 9;
    private final int REQUESTCODE_PICKPICS = 10;
    RecyclerView recyclerView;
    List<String> picpaths;
    PicPreviewAdapter adapter;
    EditText mEtcontent;
    MaterialDialog uploadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        picpaths = new ArrayList<>();
        adapter = new PicPreviewAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mEtcontent = (EditText) findViewById(R.id.et_text);
        recyclerView.setLayoutManager(new HLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(NewPostActivity.this).build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_commit: {
                if (picpaths.size() > 0) {
                    uploadPics();
                } else {
                    uploadOnlyContent();
                }
            }
            break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == MultiImageSelectorActivity.RESULT_OK) {
            if (picpaths.size() > 0) {
                int index = picpaths.size();
                picpaths.addAll(data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
                adapter.notifyDataSetChanged();
            } else {
                picpaths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                adapter.notifyDataSetChanged();
            }

        }
    }

    public void uploadPics() {
        if (picpaths == null || picpaths.size() == 0) {
            return;
        }
        uploadingDialog = new MaterialDialog.Builder(this)
                .progress(false, 1, false).title("上传图片").show();
        uploadingDialog.setCanceledOnTouchOutside(false);
        uploadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                QiniuHelper.cancelUpLoadPostPics();
            }
        });
        QiniuHelper.upLoadPicsPost(NewPostActivity.this, mEtcontent.getText().toString(), picpaths, new QiniuHelper.MutiPicsLoadingListener() {
            @Override
            public void progress(int progress) {
                uploadingDialog.setProgress(progress);
            }

            @Override
            public void complete() {
                uploadingDialog.dismiss();
                finish();
            }
        });
    }

    public void uploadOnlyContent() {
        if (TextUtils.isEmpty(mEtcontent.getText().toString())) {
            mEtcontent.setError("写点什么吧");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("type", 1);
        params.put("content", mEtcontent.getText().toString());
        params.put("piccount", 0);
        HttpClient.post(NewPostActivity.this, "post/new", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Post.insertOrUpdate(response.getJSONObject("data").getJSONObject("post"));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {

            }
        });
    }

    class PicPreviewAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImgHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_previewpic, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            Context context = holder.itemView.getContext();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast("应该展示图片");
                }
            });
            ((ImgHolder) holder).mBtndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (picpaths.size() - 1 >= position) {
                        picpaths.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
            if (position == getItemCount() - 1) {
                if (!isReachMaxCount()) {
                    ((ImgHolder) holder).mIvpic.setImageResource(R.drawable.add_tophoto);
                    ((ImgHolder) holder).mBtndelete.setVisibility(View.INVISIBLE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(NewPostActivity.this, MultiImageSelectorActivity.class);
                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, MAX_PICCOUNT - picpaths.size());
                            startActivityForResult(intent, REQUESTCODE_PICKPICS);
                        }
                    });
                    ((ImgHolder) holder).mBtndelete.setOnClickListener(null);
                } else {
                    PicassoProxy.loadFile(context, new File(picpaths.get(position)), ((ImgHolder) holder).mIvpic);
                    ((ImgHolder) holder).mBtndelete.setVisibility(View.VISIBLE);
                }
            } else {
                PicassoProxy.loadFile(context, new File(picpaths.get(position)), ((ImgHolder) holder).mIvpic);
                ((ImgHolder) holder).mBtndelete.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return picpaths.size() < MAX_PICCOUNT - 1 ? picpaths.size() + 1 : picpaths.size();
        }

        public boolean isReachMaxCount() {
            return picpaths.size() >= MAX_PICCOUNT;
        }

        class ImgHolder extends RecyclerView.ViewHolder {
            ImageView mIvpic;
            Button mBtndelete;

            public ImgHolder(View itemView) {
                super(itemView);
                mIvpic = (ImageView) itemView.findViewById(R.id.imageview);
                mBtndelete = (Button) itemView.findViewById(R.id.btn_delete);
            }
        }

    }

}
