package com.a8_hk.gtrapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by think on 2019/04/28.
 */

public class MyListAdapter extends BaseAdapter {
    private Context mContext;//声明引用
    private LayoutInflater mLayoutInflater;

    //新建一个构造函数，把Context对象传递进来  也就是把ListView所在的对象传递进来
    public MyListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //实例化ViewHolder
        ViewHolder holder = null;
        //如果视图为空
        if (convertView == null) {
            //此处需要导入包，填写ListView的图标和标题等控件的来源，来自于layout_list_item这个布局文件
            convertView = mLayoutInflater.inflate(R.layout.layout_list_patient_item, null);
            //生成一个ViewHolder的对象
            holder = new ViewHolder();
            //把layout_list_item对象转移过来，以便修改和赋值
            holder.tv_patient_item_bed = (TextView) convertView.findViewById(R.id.tv_patient_item_bed);
            holder.tv_patient_item_name = (TextView) convertView.findViewById(R.id.tv_patient_item_name);
            holder.tv_patient_item_sex = (TextView) convertView.findViewById(R.id.tv_patient_item_sex);
            //把这个holder传递进去
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //给控件赋值
        holder.tv_patient_item_bed.setText("床号");
        holder.tv_patient_item_name.setText("姓名");
        holder.tv_patient_item_sex.setText("性别");
        return convertView;
    }


    //写一个静态的class,把layout_list_item的控件转移过来使用
    static class ViewHolder {
        //声明引用
        public TextView tv_patient_item_bed, tv_patient_item_name, tv_patient_item_sex;
    }
}