package com.scut.gof.coordinator.main.fragment.project;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.RequestParams;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.lib.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.fragment.BaseFragment;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.interf.BottomBarController;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.net.qiniu.PrologoOption;
import com.scut.gof.coordinator.main.net.qiniu.QiniuHelper;
import com.scut.gof.coordinator.main.storage.model.Project;
import com.scut.gof.coordinator.main.storage.model.User;
import com.scut.gof.coordinator.main.utils.ImageUtil;
import com.scut.gof.coordinator.main.widget.BottomToolBar;
import com.scut.gof.coordinator.main.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/25.
 */
public class ProDetailFragment extends BaseFragment implements BottomBarController {
    public static final String ARGUMENT_KEY = "proid";
    final int REQUESTCODE_GETAPIC = 1;
    final int TAG_NOTEDITED = 0;
    final int TAG_EDITED = 1;
    String logoFilePath = "";
    CircleImageView mCirlogo;
    CircleImageView mCirprinava;
    TextView mTvname;
    TextView mTvtime;
    TextView mTvdesc;
    TextView mTvprincipal;
    TextView mTvcategory;
    TextView mTvaffiliation;
    TextView mTvedittip;
    //以下用来点击的部分
    LinearLayout mLlprincipal;
    boolean canEdit = true;
    boolean inEditMode = false;
    long proid;
    long newPrincipalid = 0;
    Project project;

    public static ProDetailFragment newInstance(long proid) {
        Bundle args = new Bundle();
        args.putLong(ARGUMENT_KEY, proid);
        ProDetailFragment fragment = new ProDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prodetail, container, false);
        mCirlogo = (CircleImageView) view.findViewById(R.id.cir_prologo);
        mTvname = (TextView) view.findViewById(R.id.tv_pronama);
        mTvdesc = (TextView) view.findViewById(R.id.tv_desc);
        mTvtime = (TextView) view.findViewById(R.id.tv_time);
        mTvprincipal = (TextView) view.findViewById(R.id.tv_principal);
        mCirprinava = (CircleImageView) view.findViewById(R.id.cir_principalavatar);
        mTvcategory = (TextView) view.findViewById(R.id.tv_category);
        mTvaffiliation = (TextView) view.findViewById(R.id.tv_affliation);
        mLlprincipal = (LinearLayout) view.findViewById(R.id.llcontainer_principal);
        mTvedittip = (TextView) view.findViewById(R.id.tv_edittip);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        iniData();
        PicassoProxy.loadAvatar(getActivity(), project.getThumbnailLogo(), mCirlogo);
        PicassoProxy.loadAvatar(getActivity(), User.getUserById(project.getPrincipalid()).getThumbnailavatar(), mCirprinava);
        mTvname.setText(project.getProname());
        mTvaffiliation.setText(project.getAffiliation());
        mTvcategory.setText(project.getCategory());
        mTvdesc.setText(project.getDescription());
        mTvprincipal.setText(User.getUserById(project.getPrincipalid()).getName());
        String str1 = "自  ";
        String str2 = "  到  ";
        SpannableString spannableString = new SpannableString(str1 + project.getPlanstarttime() + str2 + project.getPlanendtime());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), str1.length(), project.getPlanstarttime().length() + str1.length()
                , SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), str1.length() + str2.length() + project.getPlanstarttime().length()
                , spannableString.length()
                , SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        mTvtime.setText(spannableString);
        if (project.getProid() == UserManager.getUserid(getActivity())) {
            mTvedittip.setVisibility(View.VISIBLE);
            canEdit = true;
        }
        iniListener();
    }

    private void iniListener() {
        //tag用来判断view是不是修改过
        mTvname.setTag(TAG_NOTEDITED);
        mTvdesc.setTag(TAG_NOTEDITED);
        mTvname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inEditMode) {
                    new MaterialDialog.Builder(getActivity())
                            .title("修改项目名")
                            .input(null, null, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                    mTvname.setText(input);
                                    mTvname.setTag(TAG_EDITED);
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });
        mTvdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("修改项目名")
                        .input(null, null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                mTvdesc.setText(input);
                                mTvdesc.setTag(TAG_EDITED);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        mLlprincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mCirlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_GETAPIC && resultCode == MultiImageSelectorActivity.RESULT_OK) {
            ArrayList<String> logoFilePaths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            logoFilePath = logoFilePaths.get(0);
        }
    }

    private void iniData() {
        logoFilePath = "";
        newPrincipalid = 0;
        if (getArguments() != null) {
            proid = getArguments().getLong(ARGUMENT_KEY);
            project = Project.getProById(proid);
            assert project != null : "proid invalid";
        }
    }

    @Override
    public void refreshView(BottomToolBar bottomToolBar) {
        if (canEdit) {
            if (inEditMode) {
                bottomToolBar.setText(getString(R.string.action_cancel), "", getString(R.string.action_commit));
            } else {
                bottomToolBar.setText("修改", "转交项目", "归档项目");
            }
        }
    }

    @Override
    public void controllleft(BottomToolBar toolBar) {
        if (canEdit) {
            if (inEditMode) {
                //这个是取消操作
                inEditMode = false;
                mTvedittip.setVisibility(View.GONE);
                toolBar.resetImmediate();
            } else {
                //点击修改按钮
                mTvedittip.setVisibility(View.VISIBLE);
                inEditMode = true;
            }
        }
    }

    @Override
    public void controllmiddle(BottomToolBar toolBar) {
        if (canEdit) {
            if (inEditMode) {
            } else {
                //转交项目
            }
        }
    }

    @Override
    public void controllright(BottomToolBar toolBar) {
        if (canEdit) {
            if (inEditMode) {
                if (inEditMode) {
                    //确认修改东西
                    uploadPrologo(proid);
                    modifyPrincipal();
                    modifyData();
                    toolBar.resetImmediate();
                    mTvedittip.setVisibility(View.GONE);
                    inEditMode = false;
                } else {
                    //点击归档项目
                }
            }
        }
    }

    /**
     * 会判断数据是不是修改过，没有修改不会联网
     */
    private void modifyData() {
        boolean shouldUplaod = false;
        RequestParams params = new RequestParams();
        params.put("proid", project.getProid());
        if ((int) mTvname.getTag() == TAG_EDITED) {
            params.put("proname", mTvname.getText().toString());
            shouldUplaod = true;
        }
        if ((int) mTvdesc.getTag() == TAG_EDITED) {
            params.put("description", mTvdesc.getText().toString());
            shouldUplaod = true;
        }
        if (!shouldUplaod) {
            return;
        }
        HttpClient.post(getActivity(), "project/modify", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    Project.insertOrUpdate(data.getJSONObject("project"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                Log.i("onFailure", "hahaha");
            }
        });
    }

    /**
     * 如果没有修改过不会联网
     */
    private void modifyPrincipal() {
        if (newPrincipalid == 0) {
            return;
        }
        RequestParams params = new RequestParams();
        params.put("proid", project.getProid());
        params.put("principalid", newPrincipalid);
        HttpClient.post(getActivity(), "project/modifyprincipal", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
            }

            @Override
            public void onFailure(String message, String for_param) {

            }
        });
    }

    /**
     * 还是那句话，没修改不会联网上传图片的
     */
    private void uploadPrologo(long proid) {
        if (logoFilePath.equals("")) {
            return;
        }
        try {
            QiniuHelper.uploadFile(getActivity(), new PrologoOption(proid)
                    , ImageUtil.getResizedForUpload(logoFilePath, ImageUtil.COMPRESSEFFECT_SMALLER),
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                        }
                    });
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }
}