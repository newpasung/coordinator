package com.scut.gof.coordinator.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.activity.base.ImageBrowserActivity;
import com.scut.gof.coordinator.main.activity.user.UserinfoActivity;
import com.scut.gof.coordinator.main.adapter.PostSynopsisAdapter;
import com.scut.gof.coordinator.main.net.HttpClient;
import com.scut.gof.coordinator.main.net.JsonResponseHandler;
import com.scut.gof.coordinator.main.storage.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/12/18.
 */
public class PostListFragment extends BaseFragment {

    String title;
    RecyclerView mRecyclerview;
    PostSynopsisAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    PostSynopsisAdapter.OnPostItemClick onPostItemClick = new PostSynopsisAdapter.OnPostItemClick() {
        @Override
        public void onImageClick(String url) {
            Intent intent = new Intent(getActivity(), ImageBrowserActivity.class);
            intent.putExtra(ImageBrowserActivity.EXTRA_PARAMETER_PICURL, 1);
            intent.putExtra(ImageBrowserActivity.EXTRA_PARAMETER_PICURL, url);
            startActivity(intent);
        }

        @Override
        public void onUserClick(long uid) {
            Intent intent = new Intent(getActivity(), UserinfoActivity.class);
            intent.putExtra(UserinfoActivity.EXTRA_UID, uid);
            startActivity(intent);
        }
    };

    public static PostListFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        PostListFragment fragment = new PostListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        title = getArguments().getString("title");
        return inflater.inflate(R.layout.fragment_postlist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new PostSynopsisAdapter(getActivity(), onPostItemClick);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netMyPosts();
            }
        });
        List<Post> oldPosts = Post.get10MyPosts();
        if (oldPosts == null || oldPosts.size() == 0) {
            netMyPosts();
        } else {
            adapter.setData(oldPosts);
        }
    }

    public void netMyPosts() {
        refreshLayout.setRefreshing(true);
        HttpClient.post(getActivity(), "post/mypostlist", null, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                refreshLayout.setRefreshing(false);
                try {
                    adapter.setData(Post.insertOrUpdate(response.getJSONObject("data").getJSONArray("posts")));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

}
