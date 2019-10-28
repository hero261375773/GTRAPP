package com.a8_hk.gtrapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PatientInfoActivity extends AppCompatActivity {
    private ListView mLvPatientInfo;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        mLvPatientInfo=(ListView) findViewById(R.id.lv_content_main);
        mButton=(Button) findViewById(R.id.btn_patient_info);
        mLvPatientInfo.setAdapter(new MyListAdapter(PatientInfoActivity.this));
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PatientInfoActivity.this, "正在刷新患者", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
