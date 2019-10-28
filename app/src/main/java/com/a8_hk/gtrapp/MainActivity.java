package com.a8_hk.gtrapp;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        public static View include_patient_info,include_patient_patrol,include_check_orders,include_exe_orders;
        public static Activity activity;
        public static com.alibaba.fastjson.JSONObject ModelsPatient;//选中患者
        public static String Title;//选中标题
        public static Dialog progressDialog;//等待窗体
        TextView mNav_Header_UserName;//用户名
        TextView mNav_Header_WardName;//科室名
        public static com.alibaba.fastjson.JSONObject jsonObjectUser;//用户数据
//////////////////////////患者信息////////////////////////////////////////
        Button mbtn_patient_info;//刷新患者按钮
        public static ListView listviewPatient;//患者列表
        public static ListView mListView_PatientInfo_right;//患者右侧列表
        public static Object applicationcontext;//当前页面文件

//////////////////////////巡视页面////////////////////////////////////////
        public static  ListView listviewPatrol; //巡视VIEW
        Button mbtn_Patrol;//巡视确认
        CheckBox cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9,cb10,cb11,cb12;//巡视种类
        public static ArrayList<CheckBox> checkBoxArrayList=new ArrayList<CheckBox>();
///////////////////////////配液核对/////////////////////////////////////////

///////////////////////////医嘱执行/////////////////////////////////////////
        public static Spinner msp1,msp2,msp3,msp4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity=this;


        //内容页切换
           include_patient_info=findViewById(R.id.include_patient_info);//患者信息页面
           include_patient_patrol=findViewById(R.id.include_patient_patrol);//护理巡视
           include_check_orders=findViewById(R.id.include_check_orders);//医嘱审核
           include_exe_orders=findViewById(R.id.include_exe_orders);//医嘱执行


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



///等待框--------不会做
//        progressDialog=new Dialog(MainActivity.this,R.style.progress_dialog);
//        progressDialog.setContentView(R.layout.loading_dialog);
//        progressDialog.setCancelable(true);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
//        msg.setText("正在加载数据");



        //用户信息获取
        View view = navigationView.getHeaderView(0);
        LinearLayout LinearLayout_nav_header = (LinearLayout) view.findViewById(R.id.LinearLayout_nav_herder_main);
        mNav_Header_UserName=(TextView)LinearLayout_nav_header.findViewById(R.id.tv_nav_header_username);
        mNav_Header_WardName=(TextView)LinearLayout_nav_header.findViewById(R.id.tv_nav_header_wardname);
        Intent intent = getIntent();
        String userinfo =intent.getStringExtra("userinfo");
        jsonObjectUser= JSON.parseObject(userinfo);
        mNav_Header_UserName.setText(jsonObjectUser.get("NAME").toString());
        mNav_Header_WardName.setText(jsonObjectUser.get("DEPT_NAME").toString());


        //患者页面事件
        listviewPatient=(ListView)findViewById(R.id.lv_content_main);
        mListView_PatientInfo_right= (ListView)findViewById(R.id.lv_PatientInfo_right);

        mbtn_patient_info=(Button) include_patient_info.findViewById(R.id.btn_patient_info);
        mbtn_patient_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "正在刷新患者", Toast.LENGTH_SHORT).show();
                QueryPatientInfoTask queryPatientInfoTask = new QueryPatientInfoTask();
                queryPatientInfoTask.execute(jsonObjectUser.get("DEPT_CODE").toString());//启用访问webservice线程返回到result中
            }
        });
        QueryPatientInfoTask queryPatientInfoTask = new QueryPatientInfoTask();
        queryPatientInfoTask.execute(jsonObjectUser.get("DEPT_CODE").toString());//启用访问webservice线程返回到result中

        applicationcontext=  this.getApplicationContext();
        listviewPatrol=(ListView) findViewById(R.id.lv_patrol);

        //巡视页面事件
        mbtn_Patrol=(Button) include_patient_patrol.findViewById(R.id.btn_activity_patrol);
        mbtn_Patrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PatrolisChecked="";
                cb1=(CheckBox) findViewById(R.id.cb_activity_patrol_1);checkBoxArrayList.add(cb1);
                cb2=(CheckBox) findViewById(R.id.cb_activity_patrol_2);checkBoxArrayList.add(cb2);
                cb3=(CheckBox) findViewById(R.id.cb_activity_patrol_3);checkBoxArrayList.add(cb3);
                cb4=(CheckBox) findViewById(R.id.cb_activity_patrol_4);checkBoxArrayList.add(cb4);
                cb5=(CheckBox) findViewById(R.id.cb_activity_patrol_5);checkBoxArrayList.add(cb5);
                cb6=(CheckBox) findViewById(R.id.cb_activity_patrol_6);checkBoxArrayList.add(cb6);
                cb7=(CheckBox) findViewById(R.id.cb_activity_patrol_7);checkBoxArrayList.add(cb7);
                cb8=(CheckBox) findViewById(R.id.cb_activity_patrol_8);checkBoxArrayList.add(cb8);
                cb9=(CheckBox) findViewById(R.id.cb_activity_patrol_9);checkBoxArrayList.add(cb9);
                cb10=(CheckBox) findViewById(R.id.cb_activity_patrol_10);checkBoxArrayList.add(cb10);
                cb11=(CheckBox) findViewById(R.id.cb_activity_patrol_11);checkBoxArrayList.add(cb11);
                cb12=(CheckBox) findViewById(R.id.cb_activity_patrol_12);checkBoxArrayList.add(cb12);
                for (int i=0;i<checkBoxArrayList.size();i++)
                {
                   if (checkBoxArrayList.get(i).isChecked())
                   {
                       PatrolisChecked=PatrolisChecked+checkBoxArrayList.get(i).getText()+" ";
                       checkBoxArrayList.get(i).setChecked(false);
                   }
                }
                String addPatrolTaskcontents=ModelsPatient.getString("PATIENT_ID")+"$"+ModelsPatient.get("VISIT_ID")+"$"+ModelsPatient.getString("PATIENT_ID")+ModelsPatient.get("VISIT_ID") +"$"+"服务器日期"+"$"+"服务器时间"+"$"+PatrolisChecked+"$"+jsonObjectUser.get("ID").toString()+"$"+jsonObjectUser.get("NAME").toString()+"$"+ModelsPatient.getString("NAME");
                AddPatrolTask addPatrolTask=new AddPatrolTask();//插入
                addPatrolTask.execute(addPatrolTaskcontents);
            }
        });
        ///配液核对

        ///医嘱执行
        msp1=(Spinner) findViewById(R.id.spinner_order_type);String[] msp1mItems = getResources().getStringArray(R.array.order_type);ArrayAdapter<String> msp1adapter=new ArrayAdapter<String>(MainActivity.activity,android.R.layout.simple_spinner_item, msp1mItems);msp1adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);msp1 .setAdapter(msp1adapter);
        msp2=(Spinner) findViewById(R.id.spinner_order_administration);String[] msp2mItems = getResources().getStringArray(R.array.administration);ArrayAdapter<String> msp2adapter=new ArrayAdapter<String>(MainActivity.activity,android.R.layout.simple_spinner_item, msp2mItems);msp2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);msp2 .setAdapter(msp2adapter);
        msp3=(Spinner) findViewById(R.id.spinner_order_zt);String[] msp3mItems = getResources().getStringArray(R.array.zt);ArrayAdapter<String> msp3adapter=new ArrayAdapter<String>(MainActivity.activity,android.R.layout.simple_spinner_item, msp3mItems);msp3adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);msp3 .setAdapter(msp3adapter);
        msp4=(Spinner) findViewById(R.id.spinner_order_plan_date);String[] msp4mItems = getResources().getStringArray(R.array.order_plan_date);ArrayAdapter<String> msp4adapter=new ArrayAdapter<String>(MainActivity.activity,android.R.layout.simple_spinner_item, msp4mItems);msp4adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);msp4 .setAdapter(msp4adapter);

        msp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QueryOrdersTask queryOrdersTask=new QueryOrdersTask();
                queryOrdersTask.execute(ModelsPatient.getString("PATIENT_ID")+ModelsPatient.getString("VISIT_ID")+"-"+msp4.getSelectedItem().toString()+"-"+msp1.getSelectedItem().toString()+"-"+msp2.getSelectedItem().toString()+"-"+msp3.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        msp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QueryOrdersTask queryOrdersTask=new QueryOrdersTask();
                queryOrdersTask.execute(ModelsPatient.getString("PATIENT_ID")+ModelsPatient.getString("VISIT_ID")+"-"+msp4.getSelectedItem().toString()+"-"+msp1.getSelectedItem().toString()+"-"+msp2.getSelectedItem().toString()+"-"+msp3.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        msp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QueryOrdersTask queryOrdersTask=new QueryOrdersTask();
                queryOrdersTask.execute(ModelsPatient.getString("PATIENT_ID")+ModelsPatient.getString("VISIT_ID")+"-"+msp4.getSelectedItem().toString()+"-"+msp1.getSelectedItem().toString()+"-"+msp2.getSelectedItem().toString()+"-"+msp3.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        msp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QueryOrdersTask queryOrdersTask=new QueryOrdersTask();
                queryOrdersTask.execute(ModelsPatient.getString("PATIENT_ID")+ModelsPatient.getString("VISIT_ID")+"-"+msp4.getSelectedItem().toString()+"-"+msp1.getSelectedItem().toString()+"-"+msp2.getSelectedItem().toString()+"-"+msp3.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Title="患者总览";
            setTitle(Title+ " "+ModelsPatient.getString("BED_NO").toString()+"-"+ModelsPatient.getString("NAME").toString());
            include_patient_info.setVisibility(View.VISIBLE);
            include_patient_patrol.setVisibility(View.GONE);
            include_check_orders.setVisibility(View.GONE);
            include_exe_orders.setVisibility(View.GONE);
        } else if (id == R.id.nav_gallery) {
            Title="护理巡视";
            setTitle(Title+ " "+ModelsPatient.getString("BED_NO").toString()+"-"+ModelsPatient.getString("NAME").toString());
            include_patient_patrol.setVisibility(View.VISIBLE);
            include_patient_info.setVisibility(View.GONE);
            include_check_orders.setVisibility(View.GONE);
            include_exe_orders.setVisibility(View.GONE);
            QueryPatrolTask queryPatrolTask = new QueryPatrolTask();
            queryPatrolTask.execute(ModelsPatient.getString("PATIENT_ID"),ModelsPatient.getString("VISIT_ID"));//启用访问webservice线程返回到result中
        } else if (id == R.id.nav_slideshow) {
            Title="配液核对";
            setTitle(Title+ " "+ModelsPatient.getString("BED_NO").toString()+"-"+ModelsPatient.getString("NAME").toString());
            include_patient_patrol.setVisibility(View.GONE);
            include_patient_info.setVisibility(View.GONE);
            include_check_orders.setVisibility(View.VISIBLE);
            include_exe_orders.setVisibility(View.GONE);
        } else if (id == R.id.nav_manage) {
            Title="医嘱执行";
            setTitle(Title+ " "+ModelsPatient.getString("BED_NO").toString()+"-"+ModelsPatient.getString("NAME").toString());
            include_patient_patrol.setVisibility(View.GONE);
            include_patient_info.setVisibility(View.GONE);
            include_check_orders.setVisibility(View.GONE);
            include_exe_orders.setVisibility(View.VISIBLE);
            QueryOrdersTask queryOrdersTask=new QueryOrdersTask();
            queryOrdersTask.execute(ModelsPatient.getString("PATIENT_ID")+ModelsPatient.getString("VISIT_ID")+"-"+msp4.getSelectedItem().toString()+"-"+msp1.getSelectedItem().toString()+"-"+msp2.getSelectedItem().toString()+"-"+msp3.getSelectedItem().toString());
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//隐藏右侧弹出框
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }














}








