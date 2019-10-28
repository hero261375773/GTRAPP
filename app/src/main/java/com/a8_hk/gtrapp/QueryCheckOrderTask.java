package com.a8_hk.gtrapp;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by think on 2019/06/16.
 */

 class QueryCheckOrderTask extends AsyncTask <String, Integer, String> {
    public  static String result;
    public static JSONArray jsonArray;//护理巡视数据
    @Override
    protected String doInBackground(String... params) {
        try {
            result =WebJSON.GetOrderCheckQueryOne(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将结果返回给onPostExecute方法
        return result;
    }
    @Override
    protected void onPostExecute(String i) {
        jsonArray= JSON.parseArray(result);
        TextView tv_checkorders_order_type=(TextView)MainActivity.include_check_orders.findViewById(R.id.tv_checkorders_order_type);
        TextView tv_checkorders_exec_time=(TextView)MainActivity.include_check_orders.findViewById(R.id.tv_checkorders_exec_time);
        TextView tv_checkorders_bedname=(TextView)MainActivity.include_check_orders.findViewById(R.id.tv_checkorders_bedname);
        TextView tv_checkorders_order_text=(TextView)MainActivity.include_check_orders.findViewById(R.id.tv_checkorders_order_text);
        TextView tv_checkorders_checkidname=(TextView)MainActivity.include_check_orders.findViewById(R.id.tv_checkorders_checkidname);
        tv_checkorders_order_type.setText("");
        tv_checkorders_exec_time.setText("");
        tv_checkorders_bedname.setText("");
        tv_checkorders_order_text.setText("");
        tv_checkorders_checkidname.setText("");


        if (jsonArray.size()==0)
        {
            Toast toast = Toast.makeText((Context)MainActivity.applicationcontext, "查无此液体", Toast.LENGTH_LONG);
            ImageView img = new ImageView((Context)MainActivity.applicationcontext);
            img.setImageResource(R.drawable.toast_error);
            //得到toast的布局对象
            LinearLayout toast_layout = (LinearLayout) toast.getView();
            //为toast添加图片资源,第二个参数，0表示图片在上
            toast_layout.addView(img,1);
            toast.setGravity(Gravity.CENTER,0,100);
            toast.show();
        }
        else
        {
            com.alibaba.fastjson.JSONObject js= jsonArray.getJSONObject(0);
            //如果有人核对过
            Object a=js.get("CHECKNAME");
            if(a!=null)
            {
                tv_checkorders_order_text.setText("此瓶液体已由【"+ js.get("CHECKNAME").toString()+ "】核对");
            }
            else
            {
                UpdateCheckOrderTask updateCheckOrderTask=new UpdateCheckOrderTask();
                updateCheckOrderTask.execute(MainActivity.jsonObjectUser.getString("ID"),MainActivity.jsonObjectUser.getString("NAME"),js.getString("OID"));



                String ORDER_TYPE=js.get("ORDER_TYPE").toString();
                String EXEC_TIME=js.get("EXEC_TIME").toString();
                String BEDNAME="【"+js.get("BED_NO").toString()+"】"+js.get("PAT_NAME").toString();
                String ORDERS="";
                for (int x=0;x<jsonArray.size();x++)
                {
                    com.alibaba.fastjson.JSONObject jss= jsonArray.getJSONObject(x);
                    ORDERS=ORDERS+jss.get("ORDER_TEXT").toString()+"\n";
                }
                tv_checkorders_order_type.setText(ORDER_TYPE);
                tv_checkorders_exec_time.setText(EXEC_TIME);
                tv_checkorders_bedname.setText(BEDNAME);
                tv_checkorders_order_text.setText(ORDERS);
                tv_checkorders_checkidname.setText("签字护士："+MainActivity.jsonObjectUser.getString("NAME"));
            }
        }
    }
}
