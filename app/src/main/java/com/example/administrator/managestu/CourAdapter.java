package com.example.administrator.managestu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CourAdapter extends ArrayAdapter {
    private int resourceId;

    public CourAdapter(Context context, int textViewResourceId, List<Course> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Course cour = (Course) getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);//实例化一个对象
        TextView viewName = (TextView) view.findViewById(R.id.cour_name);
        TextView viewGrade = (TextView) view.findViewById(R.id.cour_grade);

        viewName.setText(cour.getCourseName());//为文本设置文本内容
        viewGrade.setText(cour.getCoursegrade());

        return view;
    }
}
