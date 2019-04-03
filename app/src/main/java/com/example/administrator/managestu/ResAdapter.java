package com.example.administrator.managestu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ResAdapter extends ArrayAdapter {
    private int resourceId;

    public ResAdapter(Context context, int textViewResourceId, List<Result> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Result res = (Result) getItem(position);//获当前项的Result的实例

        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);//实例化一个对象
        TextView viewNumber = (TextView) view.findViewById(R.id.res_number);
        TextView viewTime = (TextView) view.findViewById(R.id.res_time);
        TextView viewSchool = (TextView) view.findViewById(R.id.res_school);
        TextView viewConfirmname = (TextView) view.findViewById(R.id.res_confirmname);

        viewNumber.setText(res.getTheNumber());//为文本设置文本内容
        viewTime.setText(res.getTheTime());
        viewSchool.setText(res.getTheSchool());
        viewConfirmname.setText(res.getTheConfirmName());

        return view;
    }
}
