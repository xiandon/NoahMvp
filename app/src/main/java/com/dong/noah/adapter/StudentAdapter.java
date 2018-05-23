package com.dong.noah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.noah.R;
import com.dong.noah.entity.Student;

import java.util.List;

public class StudentAdapter extends BaseAdapter {

    private List<Student> studentList;
    private Context mContext;
    private LayoutInflater inflater;

    public StudentAdapter(List<Student> studentList, Context mContext) {
        this.studentList = studentList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return studentList == null ? 0 : studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_lv_item_student, null);
        }

        StudentViewHolder viewHolder = getViewHolder(convertView);

        viewHolder.tv_name.setText(studentList.get(position).getName());
        return convertView;
    }

    private StudentViewHolder getViewHolder(View view) {
        StudentViewHolder viewHolder = (StudentViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new StudentViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }

    class StudentViewHolder {
        ImageView iv_icon;
        TextView tv_name;

        private StudentViewHolder(View view) {
            iv_icon = view.findViewById(R.id.iv_lv_item_student_image);
            tv_name = view.findViewById(R.id.tv_lv_item_student_name);
        }


    }
}
