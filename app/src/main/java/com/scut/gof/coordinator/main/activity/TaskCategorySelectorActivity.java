package com.scut.gof.coordinator.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.RequestParams;
import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Task;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/10.
 */
public class TaskCategorySelectorActivity extends BaseActivity {

    long proid;
    RecyclerView mRec;
    MAdapter adapter;
    String[] categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskcategorylist);
        categories = new String[0];
        adapter = new MAdapter();
        mRec = (RecyclerView) findViewById(R.id.recyclerview);
        mRec.addItemDecoration(new HorizontalDividerItemDecoration.Builder(TaskCategorySelectorActivity.this).build());
        mRec.setAdapter(adapter);
        mRec.setLayoutManager(new LinearLayoutManager(TaskCategorySelectorActivity.this));
        proid = getIntent().getLongExtra("proid", 0);
        netRefreshCategories(proid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_taskcategories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add: {
                addCategory();
            }
            break;
        }
        return true;
    }

    private void addCategory() {
        new MaterialDialog.Builder(TaskCategorySelectorActivity.this)
                .title("新增分类")
                .input(null, null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        for (int i = 0; i < categories.length; i++) {
                            if (categories[i].equals(input)) {
                                return;
                            }
                        }
                        netAddCategory(input + "");
                    }
                })
                .positiveText(R.string.text_ensure)
                .negativeText(R.string.action_cancel)
                .show();
    }

    private void netAddCategory(String category) {
        RequestParams params = new RequestParams();
        params.put("proid", proid);
        params.put("category", category);
        HttpClient.post(TaskCategorySelectorActivity.this, "task/addcategory", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray categoriesData = response.getJSONObject("data")
                            .getJSONArray("categories");
                    String[] newdata = new String[categoriesData.length()];
                    for (int i = 0; i < newdata.length; i++) {
                        newdata[i] = ((JSONObject) categoriesData.get(i)).getString("category");
                    }
                    categories = newdata;
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {

            }
        });
    }

    private void netDeleteCategory(String category) {
        RequestParams params = new RequestParams();
        params.put("proid", proid);
        params.put("category", category);
        HttpClient.post(TaskCategorySelectorActivity.this, "task/removecategory", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray categoriesData = data.getJSONArray("categories");
                    String[] newdata = new String[categoriesData.length()];
                    for (int i = 0; i < newdata.length; i++) {
                        newdata[i] = ((JSONObject) categoriesData.get(i)).getString("category");
                    }
                    categories = newdata;
                    adapter.notifyDataSetChanged();
                    Task.insertOrUpdate(data.getJSONArray("tasks"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {

            }
        });
    }

    private void netRefreshCategories(long proid) {
        final MaterialDialog dialog = new MaterialDialog.Builder(TaskCategorySelectorActivity.this)
                .progress(true, 0, false)
                .show();
        RequestParams params = new RequestParams();
        params.put("proid", proid);
        HttpClient.post(this, "task/categories", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    dialog.dismiss();
                    String data = response.getJSONObject("data").getString("categories");
                    categories = data.split(";");
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                dialog.dismiss();
            }
        });
    }

    class MAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_textview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((MHolder) holder).mTvtext.setText(categories[position]);
            ((MHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!categories[position].equals("默认")) {
                        new MaterialDialog.Builder(TaskCategorySelectorActivity.this)
                                .items("选择", "删除")
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                        if (which == 0) {
                                            Intent intent = new Intent();
                                            intent.putExtra("category", categories[position]);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        } else if (which == 1) {
                                            netDeleteCategory(categories[position]);
                                        }
                                    }
                                })
                                .show();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("category", categories[position]);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return categories.length;
        }

        class MHolder extends RecyclerView.ViewHolder {
            TextView mTvtext;

            public MHolder(View itemView) {
                super(itemView);
                mTvtext = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
}
