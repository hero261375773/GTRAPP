package com.a8_hk.gtrapp;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by think on 2019/05/23.
 */

class AddPatrolTask extends AsyncTask<String, Integer, String> {
    public static String resultAddPatrol="";
    public static String resultPatrol="";


    @Override
    protected String doInBackground(String... params) {
        try {
            resultPatrol =WebJSON.AddPatrol(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将结果返回给onPostExecute方法
        return resultAddPatrol;
    }
    @Override
    protected void onPostExecute(String i) {
        if (resultPatrol.length()==9)
        {
            Toast.makeText((Context)MainActivity.applicationcontext, "无巡视数据", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast toast = Toast.makeText((Context)MainActivity.applicationcontext, "执行成功", Toast.LENGTH_LONG);
            ImageView img = new ImageView((Context)MainActivity.applicationcontext);
            img.setImageResource(R.drawable.toast_check);
            //得到toast的布局对象
            LinearLayout toast_layout = (LinearLayout) toast.getView();
            //为toast添加图片资源,第二个参数，0表示图片在上
            toast_layout.addView(img,1);
            toast.setGravity(Gravity.CENTER,0,100);
            toast.show();
            QueryPatrolTask queryPatrolTask = new QueryPatrolTask();//刷新
            queryPatrolTask.execute(MainActivity.ModelsPatient.getString("PATIENT_ID"),MainActivity.ModelsPatient.getString("VISIT_ID"));//启用访问webservice线程返回到result中
        }
    }
}