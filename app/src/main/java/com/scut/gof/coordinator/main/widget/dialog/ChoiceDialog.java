package com.scut.gof.coordinator.main.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.utils.ApiUtil;


/**
 * Created by admin on 14/11/13.
 */
public class ChoiceDialog extends Dialog {
    private ListView listView;
    private ActionListAdapter adapter;
    private TextView titleView;
    private String title;
    private TextView textView;
    public ChoiceDialog(Context context) {
        super(context, R.style.commondialog);
        adapter = new ActionListAdapter(context,R.layout.textview);
        setCanceledOnTouchOutside(true);
    }

    public ChoiceDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ChoiceDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choicedialog_layout);
        listView = (ListView)findViewById(R.id.listView);
        listView.setDividerHeight(3);
        listView.setDivider(new ColorDrawable(ApiUtil.getColor(getContext(),R.color.black_24)));
        listView.setAdapter(adapter);

        titleView = (TextView)findViewById(R.id.title_view);
        if (title != null && title.length() > 0) {
            titleView.setText(title);
            titleView.setVisibility(View.VISIBLE);
        } else {
            titleView.setVisibility(View.GONE);
        }
    }

    public ChoiceDialog addTitle(String title) {
        this.title = title;
        return this;
    }

    public ChoiceDialog addItem(String title, OnClickListener listener) {
        ActionItem item = new ActionItem(title, listener);
        adapter.add(item);
        return this;
    }

    public  interface OnClickListener {
        void didClick(ChoiceDialog dialog, String itemTitle);
    }

    public static class ActionItem {
        private String title;
        private OnClickListener listener;

        private ActionItem(String title, OnClickListener listener) {
            this.title = title;
            this.listener = listener;
        }
    }

    class ActionListAdapter extends ArrayAdapter<ActionItem> {
        LayoutInflater inflater;

        ActionListAdapter(Context context, int resource) {
            super(context, resource);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.choicedialog_item_layout,null);
            }
            textView=(TextView)convertView.findViewById(R.id.tv_text);
            ActionItem item = getItem(position);
            textView.setText(item.title);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActionItem item = getItem(position);
                    if (item.listener != null)
                        item.listener.didClick(ChoiceDialog.this, item.title);
                }
            });
            return convertView;
        }
    }

}
