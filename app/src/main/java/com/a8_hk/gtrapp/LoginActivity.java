package com.a8_hk.gtrapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a8_hk.gtrapp.utils.Tools;
import com.a8_hk.gtrapp.view.CommonProgressDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {
    Button button_login;
    public static  String result;
    EditText mEditText_username;
    EditText mEditText_userpwd;
    private CommonProgressDialog pBar;
    public String App_Update_Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button_login=(Button) findViewById(R.id.btn_login);
        mEditText_username=(EditText)findViewById(R.id.et_userName);
        mEditText_userpwd=(EditText)findViewById(R.id.et_password);
        getSupportActionBar().hide();//继承 AppCompatActivity的页面隐藏标题栏



        QueryApp_Update_InfoTask queryApp_update_infoTask=new QueryApp_Update_InfoTask();
        queryApp_update_infoTask.execute();

    }


    ///登录按钮
    public void login_button_onClick(View view)  {

       // startActivity(new Intent(LoginActivity.this,MainActivity.class));
        //启动后台任务
        if (mEditText_username != null && mEditText_userpwd!=null){
            String username=mEditText_username.getText().toString();
            String userpwd=mEditText_userpwd.getText().toString();
            if (username.equals("")| userpwd.equals(""))
            {
                Toast.makeText(getApplicationContext(), "请输入用户名或密码", Toast.LENGTH_SHORT).show();
            }
            else
            {
                    QueryAddressTask queryAddressTask = new QueryAddressTask();
                    queryAddressTask.execute(username,userpwd);//启用访问webservice线程返回到result中
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "程序崩溃请联系管理员", Toast.LENGTH_SHORT).show();
        }
    }
    ///请求线程
    class QueryAddressTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                result =WebJSON.View_User_Info_Manager(params[0],params[1]);
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
                Toast.makeText(getApplicationContext(), "用户名密码错误", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
                //startActivity(new Intent(LoginActivity.this,MainActivity.class));
                //Toast.makeText(getApplicationContext(), jsonObject.get("NAME").toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userinfo",result);
                startActivity(intent);
            }
        }
    }


    // 下载存储的文件名
    private static final String DOWNLOAD_NAME = "channelWe";




    //获取版本信息
    class QueryApp_Update_InfoTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {
            try {
                App_Update_Info =WebJSON.App_Update_Info();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return  App_Update_Info;
        }

        @Override
        protected void onPostExecute(String i) {
            // 获取本版本号，是否更新
            int vision = Tools.getVersion(getApplicationContext());
            getVersion(vision,App_Update_Info);
        }

    }





















    // 获取更新版本号
    private void getVersion(final int vision,String App_Update_Info) {
        JSONArray jsonArray=  JSON.parseArray(App_Update_Info);
        JSONObject jsonObject=jsonArray.getJSONObject(0);

        String C_newversion = jsonObject.getString("NEWVERSION");//更新新的版本号
        String C_content =jsonObject.getString("CONTENT").replace("\\n", "\n");//更新内容
        String C_url = jsonObject.getString("URL");//安装包下载地址

        double newversioncode = Double
                .parseDouble(C_newversion);
        int cc = (int) (newversioncode);

        System.out.println(C_newversion + "v" + vision + ",,"
                + cc);
        if (cc != vision) {
            if (vision < cc) {
                System.out.println(C_newversion + "v"
                        + vision);
                // 版本号不同
                ShowDialog(vision, C_newversion, C_content, C_url);
            }
        }
    }

    /**
     * 升级系统
     *
     * @param content
     * @param url
     */
    private void ShowDialog(int vision, String newversion, String content,
                            final String url) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pBar = new CommonProgressDialog(LoginActivity.this);
                        pBar.setCanceledOnTouchOutside(false);
                        pBar.setTitle("正在下载");
                        pBar.setCustomTitle(LayoutInflater.from(
                                LoginActivity.this).inflate(
                                R.layout.title_dialog, null));
                        pBar.setMessage("正在下载");
                        pBar.setIndeterminate(true);
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setCancelable(true);
                        //downFile(URLData.DOWNLOAD_URL);
                        final DownloadTask downloadTask = new DownloadTask(
                                LoginActivity.this);
                        downloadTask.execute(url);
                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(),
                            DOWNLOAD_NAME);

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "sd卡未挂载",
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            pBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            pBar.setIndeterminate(false);
            pBar.setMax(100);
            pBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            pBar.dismiss();
            if (result != null) {
                Toast.makeText(context, "您未打开SD卡权限" + result, Toast.LENGTH_LONG).show();
            } else {
                update();
            }

        }
    }

    private void update() {
        //安装应用
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), DOWNLOAD_NAME)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }


}
