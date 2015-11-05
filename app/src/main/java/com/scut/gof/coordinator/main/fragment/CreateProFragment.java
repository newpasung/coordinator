package com.scut.gof.coordinator.main.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.scut.gof.coordinator.R;
import com.scut.gof.coordinator.main.widget.dialog.ChoiceDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/11/3.
 * 用来创建project
 */
public class CreateProFragment extends BaseFragment {

    Button mBtnstarttime;
    Button mBtnendtime;
    Button mBtncategory;
    ImageButton mBtnpic;
    int which = 0;

    public CreateProFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_createpro, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnstarttime = (Button) view.findViewById(R.id.btn_starttime);
        mBtnendtime = (Button) view.findViewById(R.id.btn_endtime);
        mBtncategory = (Button) view.findViewById(R.id.btn_category);
        mBtnpic = (ImageButton) view.findViewById(R.id.btn_img);
        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                StringBuilder builder = new StringBuilder();
                builder.append(year);
                builder.append("-");
                builder.append(monthOfYear + 1);
                builder.append("-");
                builder.append(dayOfMonth);
                if (which == 0)
                    mBtnstarttime.setText(builder.toString());
                else mBtnendtime.setText(builder.toString());
            }
        }, calendar.YEAR, calendar.MONTH, calendar.DAY_OF_MONTH);
        mBtnstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = 0;
                dialog.show(getFragmentManager(), "startdatepicker");
            }
        });
        mBtnendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = 1;
                dialog.show(getFragmentManager(), "enddatepicker");
            }
        });
        final ChoiceDialog choiceDialog = new ChoiceDialog(getActivity());
        choiceDialog.addItem("校园体育活动", new ChoiceDialog.OnClickListener() {
            @Override
            public void didClick(ChoiceDialog dialog, String itemTitle) {
                mBtncategory.setText(itemTitle);
                dialog.dismiss();
            }
        })
                .addItem("校园歌唱比赛", new ChoiceDialog.OnClickListener() {
                    @Override
                    public void didClick(ChoiceDialog dialog, String itemTitle) {
                        mBtncategory.setText(itemTitle);
                        dialog.dismiss();
                    }
                })
                .addItem("建筑", new ChoiceDialog.OnClickListener() {
                    @Override
                    public void didClick(ChoiceDialog dialog, String itemTitle) {
                        mBtncategory.setText(itemTitle);
                        dialog.dismiss();
                    }
                });
        mBtncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDialog.show();
            }
        });
        mBtnpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAPic();
            }
        });
    }

    protected void getAPic() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            Uri uri = data.getData();
//            String []column ={MediaStore.Images.Media.DATA};
//            Cursor cursor=getActivity().getContentResolver()
//                    .query(uri,column,null,null,null);
//            cursor.moveToFirst();
//            int index =cursor.getColumnIndex(column[0]);
//            String path =cursor.getString(index);
//            cursor.close();
//            mBtnpic.setImageBitmap(BitmapFactory.decodeFile(path));
            mBtnpic.setImageURI(uri);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
