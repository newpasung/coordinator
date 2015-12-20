package com.scut.gof.coordinator.main.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.RequestParams;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.libs.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.scut.gof.coordinator.main.UserManager;
import com.scut.gof.coordinator.main.activity.base.BaseActivity;
import com.scut.gof.coordinator.main.activity.base.ImageBrowserActivity;
import com.scut.gof.coordinator.main.adapter.UserinfoAdapter;
import com.scut.gof.coordinator.main.image.PicassoProxy;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.net.qiniu.AvatarOption;
import com.scut.gof.coordinator.main.net.qiniu.QiniuHelper;
import com.scut.gof.coordinator.main.storage.model.User;
import com.scut.gof.coordinator.main.utils.ApiUtil;
import com.scut.gof.coordinator.main.utils.ImageUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2015/11/18.
 */
public class UserinfoActivity extends BaseActivity {
    public static final String EXTRA_UID = "uid";
    final int REQUESTCODE_GETAPIC = 1;
    RecyclerView mRecinfo;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView mIvtoolbar;
    String picPath = "";
    long uid;
    User mUser;
    UserinfoAdapter adapter;
    WeakReference<Context> contextWeakReference;
    RequestParams modifyParams;
    boolean isEditable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseinfo);
        uid = getIntent().getLongExtra(EXTRA_UID, 0);
        if (uid == 0) uid = UserManager.getUserid(this);
        mUser = User.getUserById(uid);
        adapter = new UserinfoAdapter(this, mUser, new UserinfoAdapter.OnListitemClick() {
            @Override
            public void onClick(int title, final String content) {
                switch (title) {
                    case R.string.text_workphone: {
                        inputModifyLocally(content, title, "workphone");
                    }
                    break;
                    case R.string.text_email: {
                        inputModifyLocally(content, title, "email");
                    }
                    break;
                    case R.string.text_locale: {
                        inputModifyLocally(content, title, "locale");
                    }
                    break;
                    case R.string.text_signature: {
                        inputModifyLocally(content, title, "signature");
                    }
                    break;
                    case R.string.text_gender: {
                        inputGenderLocally(title, content);
                    }
                    break;
                    case R.string.text_birthday: {
                        inputBirthday(title, content);
                    }
                    break;
                }
            }
        });
        modifyParams = new RequestParams();
        iniUI();
        iniListener();
        netUserInfo();
    }

    private void inputModifyLocally(final String prefill, final int title, final String netKey) {
        new MaterialDialog.Builder(UserinfoActivity.this)
                .input(null, prefill, true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!input.equals(prefill)) {
                            modifyParams.put(netKey, input);
                            adapter.modifyAData(title, String.valueOf(input));
                        }
                    }
                })
                .title("修改" + getString(title))
                .show();
    }

    private void inputGenderLocally(final int title, final String content) {
        new MaterialDialog.Builder(this)
                .title("选择性别")
                .items("男", "女")
                .itemsCallbackSingleChoice(content.equals("男") ? 0 : 1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (!text.equals(content)) {
                            modifyParams.put("gender", "男".equals(text) ? 1 : 0);
                            adapter.modifyAData(title, String.valueOf(text));
                        }
                        return true;
                    }
                })
                .show();
    }

    private void inputBirthday(final int title, final String primitive) {
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance(Locale.CHINA);
        DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                StringBuilder builder = new StringBuilder();
                builder.append(year);
                builder.append("-");
                builder.append(monthOfYear + 1);
                builder.append("-");
                builder.append(dayOfMonth);
                if (!primitive.equals(builder.toString())) {
                    modifyParams.put("birthday", builder.toString());
                    adapter.modifyAData(title, builder.toString());
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show(getFragmentManager(), "birthday");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEditable) {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_edit: {
                if (item.getTitle().equals(getString(R.string.action_edit))) {
                    adapter.setIsEditable(true);
                    item.setIcon(R.mipmap.done);
                    item.setTitle(R.string.action_commit);
                    isEditable = true;
                } else {
                    adapter.setIsEditable(false);
                    item.setIcon(R.drawable.edit_white);
                    item.setTitle(R.string.action_edit);
                    netModify();
                    uploadPic();
                    isEditable = false;
                }
            }
            break;
        }
        return true;
    }

    protected void iniUI() {
        mRecinfo = (RecyclerView) findViewById(R.id.recyclerview);
        mIvtoolbar = (ImageView) findViewById(R.id.iv_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbarLayout
                = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(ApiUtil.getColor(this, R.color.colorPrimary));
        collapsingToolbarLayout.setTitle(mUser.getName());
        mRecinfo.setLayoutManager(new LinearLayoutManager(this));
        mRecinfo.setAdapter(adapter);
        PicassoProxy.load(UserinfoActivity.this, mUser.getAvatar(), mIvtoolbar);
    }

    protected void iniListener() {
        collapsingToolbarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditable) {
                    new MaterialDialog.Builder(UserinfoActivity.this)
                            .items("修改名称", "修改头像")
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                    dialog.dismiss();
                                    if (which == 0) {
                                        new MaterialDialog.Builder(UserinfoActivity.this)
                                                .input(null, mUser.getName(), true, new MaterialDialog.InputCallback() {
                                                    @Override
                                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                                        if (!input.equals(mUser.getName())) {
                                                            modifyParams.put("name", input);
                                                            collapsingToolbarLayout.setTitle(input);
                                                        }
                                                    }
                                                })
                                                .title("修改名称")
                                                .show();
                                    } else {
                                        Intent intent = new Intent(UserinfoActivity.this, MultiImageSelectorActivity.class);
                                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                                        startActivityForResult(intent, REQUESTCODE_GETAPIC);
                                    }
                                }
                            })
                            .show();
                } else {
                    Intent intent = new Intent(UserinfoActivity.this, ImageBrowserActivity.class);
                    intent.putExtra(ImageBrowserActivity.EXTRA_PARAMETER_PICCOUNT, 1);
                    intent.putExtra(ImageBrowserActivity.EXTRA_PARAMETER_PICURL, mUser.getAvatar());
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_GETAPIC && resultCode == MultiImageSelectorActivity.RESULT_OK) {
            ArrayList<String> picPaths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            picPath = picPaths.get(0);
            PicassoProxy.loadFile(UserinfoActivity.this, new File(picPath), mIvtoolbar);
        }
    }

    protected void uploadPic() {
        if (picPath.equals("")) {
            return;
        }
        try {
            QiniuHelper.uploadFile(this, new AvatarOption()
                    , ImageUtil.getResizedForUpload(picPath, ImageUtil.COMPRESSEFFECT_BIGGER),
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                            try {
                                User.insertOrUpdate(jsonObject.getJSONObject("data").getJSONObject("user"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    private void netUserInfo() {
        contextWeakReference = new WeakReference<Context>(this);
        RequestParams params = new RequestParams();
        params.put("checkuserid", uid);
        HttpClient.get(UserinfoActivity.this, "user/infomation", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    mUser = User.insertOrUpdate(response.getJSONObject("data").getJSONObject("user"));
                    UserManager.notifyDataChanged();
                    adapter.updateData(mUser);
                    collapsingToolbarLayout.setTitle(mUser.getName());
                    Context context = contextWeakReference.get();
                    if (context != null) {
                        PicassoProxy.load(context, mUser.getAvatar(), mIvtoolbar);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {

            }
        });
    }

    private void netModify() {
        HttpClient.post(UserinfoActivity.this, "user/modifyinfo", modifyParams, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    mUser = User.insertOrUpdate(response.getJSONObject("data").getJSONObject("user"));
                    modifyParams = new RequestParams();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                modifyParams = new RequestParams();
            }
        });
    }

}
