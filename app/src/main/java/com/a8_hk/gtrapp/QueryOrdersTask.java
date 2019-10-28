package com.a8_hk.gtrapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by think on 2019/07/11.
 */

public class QueryOrdersTask extends AsyncTask<String, Integer, String> {
    String result;
    public static JSONArray jsonArray;
    String edate="";
    String etime="";
    JSONArray jsonArray1111=new JSONArray();

    @Override
    protected String doInBackground(String... params) {
        try {
            result =WebJSON.GetOrderInfo(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将结果返回给onPostExecute方法
        return result;
    }
    @Override
    protected void onPostExecute(String i) {
        if (result.length()==9)
        {
            Toast.makeText((Context)MainActivity.applicationcontext, "无数据", Toast.LENGTH_SHORT).show();
        }
        else
        {
            JSONObject lsjsonobject;//临时用于插入array中
            String plan_time="";   //记录时间
            jsonArray= JSON.parseArray(result);//结果集合

            for (int c=0;c<jsonArray.size();c++)
            {
                //根据数据创建tag
                lsjsonobject=  jsonArray.getJSONObject(c);
                if (!lsjsonobject.getString("PLAN_TIME").equals(plan_time) &&c+1<jsonArray.size())//时间不一样，并且没有超出界限
                {
                    //插入抬头
                    plan_time=lsjsonobject.getString("PLAN_TIME");
                    String jsonString="{'PLAN_TIME':'"+plan_time+"','PLAN_DATE':'"+lsjsonobject.getString("PLAN_DATE")+"'}";
                    JSONObject jsonObject = JSONObject.parseObject(jsonString);
                    jsonArray1111.add(jsonObject);

                    //插入对象（下一条医嘱号不一样）
                    if (!lsjsonobject.getString("ORDER_NO").equals(jsonArray.getJSONObject(c+1).getString("ORDER_NO")))//如果医嘱号不一致
                    {
                        jsonArray1111.add(lsjsonobject);//插入数据
                    }
                    //插入对象（医嘱号一致,需要整理信息）
                    else if (lsjsonobject.getString("PLAN_TIME").equals(jsonArray.getJSONObject(c+1).getString("PLAN_TIME")))
                    {
                        String ORDER_TEXT="";
                        int e=1;
                        for (int d=c;d<jsonArray.size();d++)
                        {
                            if (jsonArray.getJSONObject(d).getString("ORDER_NO").equals(lsjsonobject.getString("ORDER_NO")))
                            {
                                ORDER_TEXT=ORDER_TEXT+"("+e+")"+jsonArray.getJSONObject(d).getString("ORDER_TEXT")+"\r\n";
                                e++;
                                if (d==jsonArray.size()-1)
                                {
                                    c=d-1;
                                    ORDER_TEXT=ORDER_TEXT.substring(0,ORDER_TEXT.length()-3);
                                    lsjsonobject.put("ORDER_TEXT",ORDER_TEXT);
                                    jsonArray1111.add(lsjsonobject);
                                    break;
                                }
                            }
                            else
                            {
                                c=d-1;
                                ORDER_TEXT=ORDER_TEXT.substring(0,ORDER_TEXT.length()-3);
                                lsjsonobject.put("ORDER_TEXT",ORDER_TEXT);
                                jsonArray1111.add(lsjsonobject);
                                break;
                            }
                        }
                    }
                    else
                    {
                        jsonArray1111.add(lsjsonobject);//插入数据
                    }

                }


                else if ((c+1<jsonArray.size()))
                {
                    //插入对象（下一条医嘱号不一样）
                    if (!lsjsonobject.getString("ORDER_NO").equals(jsonArray.getJSONObject(c+1).getString("ORDER_NO")))//如果医嘱号不一致
                    {
                        jsonArray1111.add(lsjsonobject);//插入数据
                    }
                    //插入对象（医嘱号一致,需要整理信息）
                    else
                    {
                        String ORDER_TEXT="";
                        int e=1;
                        for (int d=c;d<jsonArray.size();d++)
                        {
                            if (jsonArray.getJSONObject(d).getString("ORDER_NO").equals(lsjsonobject.getString("ORDER_NO")) && jsonArray.getJSONObject(d).getString("PLAN_TIME").equals(plan_time))
                            {
                                ORDER_TEXT=ORDER_TEXT+"("+e+")"+jsonArray.getJSONObject(d).getString("ORDER_TEXT")+"\r\n";
                                e++;
                                if (d==jsonArray.size()-1)
                                {
                                    c=d-1;
                                    ORDER_TEXT=ORDER_TEXT.substring(0,ORDER_TEXT.length()-3);
                                    lsjsonobject.put("ORDER_TEXT",ORDER_TEXT);
                                    jsonArray1111.add(lsjsonobject);
                                    break;
                                }
                            }
                            else
                            {
                                c=d-1;
                                ORDER_TEXT=ORDER_TEXT.substring(0,ORDER_TEXT.length()-3);
                                lsjsonobject.put("ORDER_TEXT",ORDER_TEXT);
                                jsonArray1111.add(lsjsonobject);
                                break;
                            }
                        }
                    }
                }

                else if (c+1==jsonArray.size() && !jsonArray.getJSONObject(c).getString("ORDER_NO").equals(jsonArray.getJSONObject(c-1).getString("ORDER_NO")))
                {
                    if (jsonArray.getJSONObject(c).getString("PLAN_TIME").equals(plan_time))
                    {
                        jsonArray1111.add(lsjsonobject);//插入数据
                    }
                    else
                    {
                        //插入抬头
                        plan_time=lsjsonobject.getString("PLAN_TIME");
                        String jsonString="{'PLAN_TIME':'"+plan_time+"','PLAN_DATE':'"+lsjsonobject.getString("PLAN_DATE")+"'}";
                        JSONObject jsonObject = JSONObject.parseObject(jsonString);
                        jsonArray1111.add(jsonObject);
                        jsonArray1111.add(lsjsonobject);//插入数据
                    }
                }

            }

            ListView listView=(ListView) MainActivity.include_exe_orders.findViewById(R.id.orders_exe_listView_list);
            OrdersExeAdapter ordersExeAdapter=new OrdersExeAdapter();
            listView.setAdapter(ordersExeAdapter);

        }
    }





    private class OrdersExeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return jsonArray1111.size();
        }

        @Override
        public Object getItem(int position) {
            return jsonArray1111.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=convertView;
            JSONObject js=(JSONObject) getItem(position);
            if (js.size()==2)
            {
                view= LayoutInflater.from(MainActivity.activity).inflate(R.layout.orders_exe_list_item_tag, null);
                TextView mtev_date=(TextView) view.findViewById(R.id.tev_ordersinfo_tag_date_list_item);//确定用哪个view后   给相应的view进行数据展现
                mtev_date.setText(js.getString("PLAN_DATE"));
                TextView mtev_time=(TextView) view.findViewById(R.id.tev_ordersinfo_tag_time_list_item);//确定用哪个view后   给相应的view进行数据展现
                mtev_time.setText(js.getString("PLAN_TIME"));
            }
            else
            {
                view= LayoutInflater.from(MainActivity.activity).inflate(R.layout.orders_exe_list_item, null);
                TextView text1=(TextView) view.findViewById(R.id.addexam_list_item_text);//确定用哪个view后   给相应的view进行数据展现
                text1.setText(js.getString("ORDER_TEXT"));//医嘱内容
                if (js.getString("OPERCODE")!=null)
                {
                    view.findViewById(R.id.orders_exe_list_item_left).setBackgroundColor(MainActivity.activity.getResources().getColor(R.color.执行后));//左侧颜色变绿
                    view.findViewById(R.id.orders_exe_list_item_left_1_1).setBackgroundColor(MainActivity.activity.getResources().getColor(R.color.执行后));//左侧颜色变绿
                }
                else if(js.getString("CHECKID")!=null)
                {
                    view.findViewById(R.id.orders_exe_list_item_left).setBackgroundColor(MainActivity.activity.getResources().getColor(R.color.核对后));//左侧颜色变绿
                    view.findViewById(R.id.orders_exe_list_item_left_1_1).setBackgroundColor(MainActivity.activity.getResources().getColor(R.color.核对后));//左侧颜色变绿
                }

                ((TextView)view.findViewById(R.id.tev_orders_exe_list_item_ORDER_TYPE)).setText(js.getString("ORDER_TYPE"));
                ((TextView)view.findViewById(R.id.tev_orders_exe_list_item_EXEC_TIME)).setText(js.getString("EXEC_TIME"));
                ((TextView)view.findViewById(R.id.tev_orders_exe_list_item_OPERATING_DATE)).setText(js.getString("OPERATING_DATE"));
                ((TextView)view.findViewById(R.id.tev_orders_exe_list_item_OPERNAME)).setText(js.getString("OPERNAME"));
            }
            return view;
        }
    }


}



