package com.a8_hk.gtrapp;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by think on 2019/05/20.
 */

class QueryPatrolTask extends AsyncTask<String, Integer, String> {
    String resultPatrol;
    JSONArray jsonArrayPatrol;
    @Override
    protected String doInBackground(String... params) {
        try {
           resultPatrol =WebJSON.GetPatrolByPatientidVisitid(params[0],params[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将结果返回给onPostExecute方法
        return resultPatrol;
    }
    @Override
    protected void onPostExecute(String i) {
        if (resultPatrol.length()==9)
        {
            Toast.makeText((Context)MainActivity.applicationcontext, "无巡视数据", Toast.LENGTH_SHORT).show();
        }
        else
        {
            jsonArrayPatrol= JSON.parseArray(resultPatrol);
            List<Map<String,Object>> listitem=new ArrayList<Map<String,Object>>();
            for (int x=0;x<jsonArrayPatrol.size();x++)
            {
                com.alibaba.fastjson.JSONObject js= jsonArrayPatrol.getJSONObject(x);
                Map<String,Object> map=new HashMap<String, Object>() ;
                map.put("TIME_POINT",js.get("TIME_POINT"));
                map.put("OPERNAME",js.get("OPERNAME"));
                map.put("ITEM_VALUE",js.get("ITEM_VALUE"));
                listitem.add(map);
            }
            SimpleAdapter adapter=new SimpleAdapter((Context)MainActivity.applicationcontext,listitem,R.layout.layou_list_patrol_item,new String[]{"TIME_POINT","OPERNAME","ITEM_VALUE"},new int[]{R.id.tv_patrol_item_time,R.id.tv_patrol_item_username,R.id.tv_patrol_item_values});//上下文，数据，列表单项样式，数据字段，绑定UIID
            MainActivity.listviewPatrol.setAdapter(adapter);
        }
    }
}