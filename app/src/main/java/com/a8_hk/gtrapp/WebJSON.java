package com.a8_hk.gtrapp;

import android.os.Looper;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by think on 2019/04/23.
 */

public  class WebJSON {
    static String WSDL_URI = "http://192.168.43.33/webService.asmx?WSDL";//web服务地址，以后只需要改这一个地方就可以
   // static String WSDL_URI = "http://172.16.24.233/webService.asmx?WSDL";//web服务地址，以后只需要改这一个地方就可以

    static String namespace = "http://tempuri.org/";//namespaceweb服务命名空间


    public static String View_User_Info_Manager(String userid,String pwd) throws Exception{
        String methodName = "View_User_Info_Manager";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);//设置参数
        // 设置需调用WebService接口需要传入的两个参数工号和密码
        request.addProperty("userid", userid);
        request.addProperty("pwd", pwd);
        return JSONBase(methodName,request);//调用基本类
    }//验证用户

    public static String GetPatinetByDeptNo(String wardcode) throws Exception{
        String methodName = "GetPatinetByDeptNo";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);//设置参数
        // 设置需调用WebService接口需要传入的参数 病区编号
        request.addProperty("dept_code", wardcode);
        return JSONBase(methodName,request);//调用基本类
    } //根据病区号取患者

    public static String GetPatrolByPatientidVisitid(String patient_id,String visit_id) throws Exception{
        String methodName = "GetPatrolByPatientidVisitid";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);//设置参数
        // 设置需调用WebService接口需要传入的参数 病区编号
        request.addProperty("patient_id", patient_id);
        request.addProperty("visit_id", visit_id);
        return JSONBase(methodName,request);//调用基本类
    }//取护理巡视

    public static String AddPatrol(String contents) throws  Exception {
        String methodName = "AddPatrol";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);//设置参数
        // 设置需调用WebService接口需要传入的参数 病区编号
        request.addProperty("contents", contents);
        return JSONBase(methodName,request);//调用基本类
    }//添加护理巡视

    public static String GetOrderCheckQueryOne(String oid) throws Exception{
        String methodName = "OrderCheckQueryOne";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);//设置参数
        // 设置需调用WebService接口需要传入的参数 病区编号
        request.addProperty("oid", oid);
        return JSONBase(methodName,request);//调用基本类
    }//根据OID取一条医嘱信息

    public static  String UpdateCheckOrder(String checkid,String checkname,String oid)  throws  Exception//更配液审核者
    {
        String methodName = "UpdateOrderCheckOne";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);//设置参数
        // 设置需调用WebService接口需要传入的参数 病区编号
        request.addProperty("checkid", checkid);
        request.addProperty("checkname", checkname);
        request.addProperty("oid", oid);
        return JSONBase(methodName,request);//调用基本类
    }

    public static  String UpdateOrderExeOne(String opercode,String opername,String oid)  throws  Exception//执行医嘱
    {
        String methodName = "UpdateOrderExeOne";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);//设置参数
        // 设置需调用WebService接口需要传入的参数 病区编号
        request.addProperty("opercode", opercode);
        request.addProperty("opername", opername);
        request.addProperty("oid", oid);
        return JSONBase(methodName,request);//调用基本类
    }

    public static  String GetOrderInfo(String tj) throws Exception
    {
        //MainActivity.progressDialog.show();
        String methodName = "GetOrderInfo";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);//设置参数
        // 设置需调用WebService接口需要传入的参数 病区编号
        request.addProperty("TJ", tj);
        return JSONBase(methodName,request);//调用基本类
    }//取出来患者的所有医嘱任务用于显示


    public static  String App_Update_Info()throws  Exception
    {
        String methodName = "App_Update_Info";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);//设置参数
        return JSONBase(methodName,request);//调用基本类
    }//APP更新信息查询



    public static String JSONBase(String methodName,SoapObject request ) throws Exception
    {
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        httpTransportSE.call(null, envelope);//调用

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        String result = object.getProperty(0).toString();
        Log.d("debug",result);
        return result;
    }
}
