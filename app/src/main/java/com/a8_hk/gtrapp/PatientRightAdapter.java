package com.a8_hk.gtrapp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.odps.udf.CodecCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 2019/05/15.
 */

public class PatientRightAdapter extends BaseAdapter{
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    protected JSONArray list;
    public Activity activity;
    public PatientRightAdapter(Context context, int resource, JSONArray list){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        if(list==null){
            this.list=new JSONArray();
        }else{
            this.list = list;
        }
         activity=(Activity)context;
    }
    @Override
    public int getCount() {
        if(list.size()%2>0) {
            return list.size()/2+1;
        } else {
            return list.size()/2;
        }
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null ) {
            convertView = inflater.inflate(resource, null);
            vh = new ViewHolder();
            vh.tv1=(TextView)convertView.findViewById(R.id.btn_layou_list_patient_right_item1);
            vh.tv2=(TextView)convertView.findViewById(R.id.btn_layou_list_patient_right_item2);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        int distance =  list.size() - position*2;
        int cellCount = distance >= 2? 2:distance;
        final List<Object> itemList = list.subList(position*2,position*2+cellCount);
        if (itemList.size() >0) {
            vh.tv1.setText(((JSONObject) itemList.get(0)).getString("BED_NO").toString()+"-"+((JSONObject) itemList.get(0)).getString("NAME").toString());
            vh.tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //设置选中住院号+住院次数
                    MainActivity.ModelsPatient=(JSONObject) itemList.get(0);
                    //改变标题
                    activity.setTitle(MainActivity.Title+ " "+MainActivity.ModelsPatient.getString("BED_NO").toString()+"-"+MainActivity.ModelsPatient.getString("NAME").toString());
                    //改变页面内容
                    if (MainActivity.Title=="护理巡视")
                    {
                        QueryPatrolTask queryPatrolTask = new QueryPatrolTask();
                        queryPatrolTask.execute(MainActivity.ModelsPatient.getString("PATIENT_ID").toString(),MainActivity.ModelsPatient.getString("VISIT_ID").toString());//启用访问webservice线程返回到result中
                        for (int i=0;i<MainActivity.checkBoxArrayList.size();i++)
                        {
                            if (MainActivity.checkBoxArrayList.get(i).isChecked())
                            {
                                MainActivity.checkBoxArrayList.get(i).setChecked(false);
                            }
                        }
                    }
                    if (MainActivity.Title=="医嘱执行")
                    {
                        QueryOrdersTask queryOrdersTask=new QueryOrdersTask();
                        queryOrdersTask.execute(MainActivity.ModelsPatient.getString("PATIENT_ID")+MainActivity.ModelsPatient.getString("VISIT_ID")+"-"+MainActivity.msp4.getSelectedItem().toString()+"-"+MainActivity.msp1.getSelectedItem().toString()+"-"+MainActivity.msp2.getSelectedItem().toString()+"-"+MainActivity.msp3.getSelectedItem().toString());
                    }

                    //选中患者后隐藏侧滑页面
                    DrawerLayout  drawer = (DrawerLayout)activity.findViewById(R.id.drawer_layout);
                    drawer.closeDrawers();
                }
            });
            if (itemList.size() >1){
                vh.tv2.setVisibility(View.VISIBLE);
                vh.tv2.setText(((JSONObject) itemList.get(1)).getString("BED_NO").toString()+"-"+((JSONObject) itemList.get(1)).getString("NAME").toString());
                vh.tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设置选中住院号+住院次数
                        MainActivity.ModelsPatient=(JSONObject) itemList.get(1);
                        //改变标题
                        activity.setTitle(MainActivity.Title+ " "+MainActivity.ModelsPatient.getString("BED_NO").toString()+"-"+MainActivity.ModelsPatient.getString("NAME").toString());
                        //改变页面内容
                        if (MainActivity.Title=="护理巡视")
                        {
                            QueryPatrolTask queryPatrolTask = new QueryPatrolTask();
                            queryPatrolTask.execute(MainActivity.ModelsPatient.getString("PATIENT_ID").toString(),MainActivity.ModelsPatient.getString("VISIT_ID").toString());//启用访问webservice线程返回到result中
                            for (int i=0;i<MainActivity.checkBoxArrayList.size();i++)
                            {
                                if (MainActivity.checkBoxArrayList.get(i).isChecked())
                                {
                                    MainActivity.checkBoxArrayList.get(i).setChecked(false);
                                }
                            }
                        }
                        if (MainActivity.Title=="医嘱执行")
                        {
                            QueryOrdersTask queryOrdersTask=new QueryOrdersTask();
                            queryOrdersTask.execute(MainActivity.ModelsPatient.getString("PATIENT_ID")+MainActivity.ModelsPatient.getString("VISIT_ID")+"-"+MainActivity.msp4.getSelectedItem().toString()+"-"+MainActivity.msp1.getSelectedItem().toString()+"-"+MainActivity.msp2.getSelectedItem().toString()+"-"+MainActivity.msp3.getSelectedItem().toString());
                        }

                        DrawerLayout  drawer = (DrawerLayout)activity.findViewById(R.id.drawer_layout);
                        drawer.closeDrawers();
                    }
                });
            }else{
                vh.tv2.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }
    /**
     * 封装ListView中item控件以优化ListView
     * @author tongleer
     *
     */
    public static class ViewHolder{
        TextView tv1;
        TextView tv2;
    }
}