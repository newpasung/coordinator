package com.scut.gof.coordinator.main.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.fragment.BaseFragment;

/**
 * Created by Administrator on 2015/11/1.
 */
public class FeedBackFragment extends BaseFragment {

    public FeedBackFragment() {
    }

    public static FeedBackFragment newInstance() {

        Bundle args = new Bundle();

        FeedBackFragment fragment = new FeedBackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button =(Button)view.findViewById(R.id.btn_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
