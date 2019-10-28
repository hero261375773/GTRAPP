package com.a8_hk.gtrapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
 * Created by think on 2019/06/18.
 */

public class QueryPatientInfoTask extends AsyncTask<String, Integer, String> {
    public static  String result;//患者数据
    public static JSONArray jsonArrayPatient;//患者数据

    @Override
    protected String doInBackground(String... params) {
        try {
            result =WebJSON.GetPatinetByDeptNo(params[0]);
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
            Toast.makeText((Context)MainActivity.activity, "无患者数据", Toast.LENGTH_SHORT).show();
        }
        else
        {
            jsonArrayPatient= JSON.parseArray(result);
            MainActivity.ModelsPatient=jsonArrayPatient.getJSONObject(0);
            List<Map<String,Object>> listitem=new ArrayList<Map<String,Object>>();
            for (int x=0;x<jsonArrayPatient.size();x++)
            {
                com.alibaba.fastjson.JSONObject js= jsonArrayPatient.getJSONObject(x);
                Map<String,Object> map=new HashMap<String, Object>() ;
                map.put("bed",js.get("BED_NO")+"床");
                map.put("name",js.get("NAME"));
                map.put("sex",js.get("SEX"));
                map.put("userinfo",js);
                switch (js.get("NURSE_CLASS").toString()){
                    case "Ⅰ级护理":
                        map.put("level",R.mipmap.level1);
                        break;
                    case "Ⅱ级护理":
                        map.put("level",R.mipmap.level2);
                        break;
                    case "Ⅲ级护理":
                        map.put("level",R.mipmap.level3);
                        break;
                    case "特级护理":
                        map.put("level",R.mipmap.level0);
                        break;
                    default:
                        break;
                }

                switch (js.get("FLAG").toString()){
                    case "new":
                        map.put("new",R.mipmap.levelnew);
                        break;
                    default:
                        break;
                }


                listitem.add(map);
            }
            //患者列表
            SimpleAdapter adapter=new SimpleAdapter(MainActivity.activity,listitem,R.layout.layout_list_patient_item,new String[]{"bed","name","sex","level","new"},new int[]{R.id.tv_patient_item_bed,R.id.tv_patient_item_name,R.id.tv_patient_item_sex,R.id.tv_patient_item_level,R.id.tv_patient_item_new});//上下文，数据，列表单项样式，数据字段，绑定UIID
            MainActivity.listviewPatient.setAdapter(adapter);
            MainActivity.listviewPatient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //此处添加弹窗事件
                    //Toast.makeText(getApplicationContext(),"选中了患者", Toast.LENGTH_SHORT).show();
                    int a= position;
                    Map<String,Object> map=(Map<String,Object>)MainActivity.listviewPatient.getItemAtPosition(position);
                    com.alibaba.fastjson.JSONObject JsonobjectOnePatient= (com.alibaba.fastjson.JSONObject)map.get("userinfo");
                    showDialog(JsonobjectOnePatient);
                }
            });

            //患者左滑列表
            PatientRightAdapter patientRightAdapter = new PatientRightAdapter(MainActivity.activity,R.layout.layout_list_patient_right_item,jsonArrayPatient);
            MainActivity.mListView_PatientInfo_right.setAdapter(patientRightAdapter);
        }
    }


    //初始化并弹出患者对话框方法
    private void showDialog(com.alibaba.fastjson.JSONObject JsonobjectOnePatient){
       // View view = LayoutInflater.from(MainActivity.activity).inflate(R.layout.dialog_patientinfo_layout,null,false);
        LayoutInflater inflater=LayoutInflater.from(MainActivity.activity);
        View view=inflater.inflate(R.layout.dialog_patientinfo_layout,null,false);
        final AlertDialog dialog = new AlertDialog.Builder((Context)MainActivity.activity).setView(view).create();
        Button mbutton=(Button) view.findViewById(R.id.Btn_PatientInfo_GB);
        TextView mTEV_patientinfo_bed=(TextView) view.findViewById(R.id.tev_bed_dialog_patientinfo_layout);//床号
        mTEV_patientinfo_bed.setText(JsonobjectOnePatient.getString("BED_NO"));
        TextView mTEV_patientinfo_name=(TextView)view.findViewById(R.id.tev_name_dialog_patientinfo_layout);//姓名
        mTEV_patientinfo_name.setText(JsonobjectOnePatient.getString("NAME"));
        TextView mTEV_patientinfo_age=(TextView) view.findViewById(R.id.tev_age_dialog_patientinfo_layout);//年龄
        mTEV_patientinfo_age.setText(JsonobjectOnePatient.getString("AGE"));
        TextView mTEV_patientinfo_sex=(TextView) view.findViewById(R.id.tev_sex_dialog_patientinfo_layout);//性别
        mTEV_patientinfo_sex.setText(JsonobjectOnePatient.getString("SEX"));
        TextView mTEV_patientinfo_patientid=(TextView)view.findViewById(R.id.tev_patientid_dialog_patientinfo_layout);//住院号
        mTEV_patientinfo_patientid.setText(JsonobjectOnePatient.getString("PATIENT_ID"));
        TextView mTEV_patientinfo_in_hos_time=(TextView) view.findViewById(R.id.tev_in_hos_time_dialog_patientinfo_layout);//入院时间
        mTEV_patientinfo_in_hos_time.setText(JsonobjectOnePatient.getString("IN_HOS_TIME"));
        TextView mTEV_patientinfo_tev_doctorname=(TextView) view.findViewById(R.id.tev_doctorname_dialog_patientinfo_layout);//责任医生
        mTEV_patientinfo_tev_doctorname.setText(JsonobjectOnePatient.getString("DOCTORNAME"));
        TextView mTEV_patientinfo_tev_disgnose=(TextView) view.findViewById(R.id.tev_disgnose_dialog_patientinfo_layout);//入院诊断
        mTEV_patientinfo_tev_disgnose.setText(JsonobjectOnePatient.getString("DISGNOSE"));
        TextView mTEV_patientinfo_tev_nurse_class=(TextView) view.findViewById(R.id.tev_nurse_class_dialog_patientinfo_layout);//护理等级
        mTEV_patientinfo_tev_nurse_class.setText(JsonobjectOnePatient.getString("NURSE_CLASS"));
        TextView mTEV_patientinfo_tev_charge_type=(TextView) view.findViewById(R.id.tev_charge_type_dialog_patientinfo_layout);//费别
        mTEV_patientinfo_tev_charge_type.setText(JsonobjectOnePatient.getString("CHARGE_TYPE"));
        TextView mTEV_patientinfo_tev_tel=(TextView) view.findViewById(R.id.tev_tel_dialog_patientinfo_layout);//电话
        mTEV_patientinfo_tev_tel.setText(JsonobjectOnePatient.getString("TEL"));
        TextView mTEV_patientinfo_tev_dept_name=(TextView) view.findViewById(R.id.tev_dept_name_dialog_patientinfo_layout);//病区
        mTEV_patientinfo_tev_dept_name.setText(JsonobjectOnePatient.getString("DEPT_NAME"));
        TextView mTEV_patientinfo_tev_total=(TextView) view.findViewById(R.id.tev_total_dialog_patientinfo_layout);//预交金
        mTEV_patientinfo_tev_total.setText(JsonobjectOnePatient.getString("TOTAL"));
        TextView mTEV_patientinfo_tev_deposit=(TextView)view.findViewById(R.id.tev_deposit_dialog_patientinfo_layout);//总费用
        mTEV_patientinfo_tev_deposit.setText(JsonobjectOnePatient.getString("DEPOSIT"));
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        //此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4  注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        dialog.getWindow().setLayout((PublicScreenUtils.getScreenWidth(MainActivity.activity)), LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
