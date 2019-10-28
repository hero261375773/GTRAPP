package com.a8_hk.gtrapp;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by think on 2019/06/20.
 */

public class UpdateCheckOrderTask extends AsyncTask<String, Integer, String> {
    public static String result="";

    @Override
    protected String doInBackground(String... params) {
        try {
            result =WebJSON.UpdateCheckOrder(params[0],params[1],params[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将结果返回给onPostExecute方法
        return result;
    }
    @Override
    protected void onPostExecute(String i) {
        if (Integer.valueOf(result)==0)
        {
            Toast toast = Toast.makeText((Context)MainActivity.applicationcontext, "执行失败", Toast.LENGTH_LONG);
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
            Toast toast = Toast.makeText((Context)MainActivity.applicationcontext, "执行成功", Toast.LENGTH_LONG);
            ImageView img = new ImageView((Context)MainActivity.applicationcontext);
            img.setImageResource(R.drawable.toast_check);
            //得到toast的布局对象
            LinearLayout toast_layout = (LinearLayout) toast.getView();
            //为toast添加图片资源,第二个参数，0表示图片在上
            toast_layout.addView(img,1);
            toast.setGravity(Gravity.CENTER,0,100);
            toast.show();
        }
    }
}
