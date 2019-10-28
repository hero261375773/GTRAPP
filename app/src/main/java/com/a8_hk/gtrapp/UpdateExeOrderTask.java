package com.a8_hk.gtrapp;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by think on 2019/07/19.
 */

public class UpdateExeOrderTask  extends AsyncTask<String, Integer, String> {
    public  String result="";

    @Override
    protected String doInBackground(String... params) {
        try {
            result =WebJSON.UpdateOrderExeOne(params[0],params[1],params[2]);
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
            Toast toast = Toast.makeText((Context)MainActivity.applicationcontext, "查无此液体", Toast.LENGTH_LONG);
            ImageView img = new ImageView((Context)MainActivity.applicationcontext);
            img.setImageResource(R.drawable.toast_error);
            //得到toast的布局对象
            LinearLayout toast_layout = (LinearLayout) toast.getView();
            //为toast添加图片资源,第二个参数，0表示图片在上
            toast_layout.addView(img,1);
            toast.setGravity(Gravity.CENTER,0,100);
            toast.show();
            openMusic_error();
            Vibrator vibrator = (Vibrator) MainActivity.activity.getSystemService(Context.VIBRATOR_SERVICE);//调用震动服务
            vibrator.vibrate(new long[]{300,1000,200,1000},-1);//开始震动
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
            openMusic_check();
        }
    }

    public  void  openMusic_check(){
        RingtoneManager rm=new RingtoneManager(MainActivity.activity);//初始化 系统声音
        Uri uri = rm.getDefaultUri(rm.TYPE_NOTIFICATION);//获取系统声音路径
        Ringtone mRingtone = rm.getRingtone(MainActivity.activity, uri);//通过Uri 来获取提示音的实例对象
        mRingtone.play();//播放:
    }

    public  void  openMusic_error() {
        RingtoneManager rm = new RingtoneManager(MainActivity.activity);//初始化 系统声音
        Uri uri = rm.getDefaultUri(rm.TYPE_ALARM);//获取系统声音路径
        Ringtone mRingtone = rm.getRingtone(MainActivity.activity, uri);//通过Uri 来获取提示音的实例对象
        mRingtone.play();//播放:
    }
}
