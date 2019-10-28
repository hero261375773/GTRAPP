package com.a8_hk.gtrapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by think on 2019/06/13.
 */

public class CodeReceiver extends BroadcastReceiver {
    //private static final String action="android.provider.Telephony.SMS_RECEIVED";
    public static final String BAR_READ_ACTION = "SYSTEM_BAR_READ";//条码广播
    public String action;

    @Override
    public void onReceive(Context context, Intent intent) {

        action = intent.getAction();
        if (action.equals(BAR_READ_ACTION)) {
            String BAR_value = intent.getStringExtra("BAR_VALUE");//获取扫描的二维码
            //判断二维码格式
            if (BAR_value.subSequence(0, 1).equals("Z"))//如果扫描的是腕带
            {
                String zyh = BAR_value.split("\\&")[1];
                //Toast.makeText(context,zyh,Toast.LENGTH_LONG).show();
                JSONObject js = new JSONObject();
                for (int i = 0; i < QueryPatientInfoTask.jsonArrayPatient.size(); i++) {
                    js = (JSONObject) QueryPatientInfoTask.jsonArrayPatient.get(i);
                    String newzyh = js.get("PATIENT_ID").toString() + js.get("VISIT_ID").toString();
                    if (newzyh.equals(zyh)) {
                        MainActivity.ModelsPatient = js;
                        MainActivity.activity.setTitle(MainActivity.Title + " " + MainActivity.ModelsPatient.getString("BED_NO").toString() + "-" + MainActivity.ModelsPatient.getString("NAME").toString());
                        //改变页面内容
                        if (MainActivity.Title == "护理巡视") {
                            QueryPatrolTask queryPatrolTask = new QueryPatrolTask();
                            queryPatrolTask.execute(MainActivity.ModelsPatient.getString("PATIENT_ID").toString(), MainActivity.ModelsPatient.getString("VISIT_ID").toString());//启用访问webservice线程返回到result中
                            for (int x = 0; x < MainActivity.checkBoxArrayList.size(); i++) {
                                if (MainActivity.checkBoxArrayList.get(x).isChecked()) {
                                    MainActivity.checkBoxArrayList.get(x).setChecked(false);
                                }
                            }
                        }
                        else if (MainActivity.Title == "医嘱执行") {
                            QueryOrdersTask queryOrdersTask=new QueryOrdersTask();
                            queryOrdersTask.execute(MainActivity.ModelsPatient.getString("PATIENT_ID")+MainActivity.ModelsPatient.getString("VISIT_ID")+"-"+MainActivity.msp4.getSelectedItem().toString()+"-"+MainActivity.msp1.getSelectedItem().toString()+"-"+MainActivity.msp2.getSelectedItem().toString()+"-"+MainActivity.msp3.getSelectedItem().toString());
                       }
                    }
                }
            }

            else if (BAR_value.subSequence(0, 1).equals("H"))//如果扫描的是医嘱
            {
                if (MainActivity.Title.equals("配液核对")) {
                    QueryCheckOrderTask queryCheckOrderTask = new QueryCheckOrderTask();
                    queryCheckOrderTask.execute(BAR_value);
                }
                else if (MainActivity.Title.equals("医嘱执行")) {
                    JSONObject jsonObject=new JSONObject();
                    int bl=0;
                    for (int i=0;i<QueryOrdersTask.jsonArray.size();i++)
                    {
                        jsonObject=QueryOrdersTask.jsonArray.getJSONObject(i);
                        if (jsonObject.getString("OID").equals(BAR_value))
                        {
                            bl=1;
                        }
                    }
                    if (bl==1)
                    {
                        UpdateExeOrderTask updateExeOrderTask=new UpdateExeOrderTask();
                        updateExeOrderTask.execute(MainActivity.jsonObjectUser.getString("ID"),MainActivity.jsonObjectUser.getString("NAME"),BAR_value);
                    }
                    else
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
                }
            }
        }









    }



    public  void  openMusic_error() {
        RingtoneManager rm = new RingtoneManager(MainActivity.activity);//初始化 系统声音
        Uri uri = rm.getDefaultUri(rm.TYPE_ALARM);//获取系统声音路径
        Ringtone mRingtone = rm.getRingtone(MainActivity.activity, uri);//通过Uri 来获取提示音的实例对象
        mRingtone.play();//播放:
    }
}
