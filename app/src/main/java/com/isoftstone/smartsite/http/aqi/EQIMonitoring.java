package com.isoftstone.smartsite.http.aqi;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.video.DevicesBean;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gone on 2017/10/29.
 */

public class EQIMonitoring {
    private static  String TAG = "EQIMonitoring";
    public static EQIRankingBean eqiDataRanking(String strurl, OkHttpClient mClient, String archId, String time){
        EQIRankingBean eqiRankingBean = null;
        String funName = "EQIMonitoring";
        try {
            JSONObject object = new JSONObject();
            object.put("archId",archId);
            object.put("time",time);

            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                responsebody = responsebody.replaceAll("PM2.5","PM2_5");
                Gson gson = new Gson();
                eqiRankingBean = gson.fromJson(responsebody,EQIRankingBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  eqiRankingBean;
    }

    public static MonthlyComparisonBean carchMonthlyComparison(String strurl, OkHttpClient mClient, String archId, String time, String type){
        MonthlyComparisonBean monthlyComparisonBean = null;
        try {
            //区域月度数据对比
            String funName = "carchMonthlyComparison";
            JSONObject object = new JSONObject();
            object.put("archId",archId);
            object.put("time",time);
            object.put("type",type);
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                //
                Gson gson = new Gson();
                if(type.equals("0")){
                    ArrayList<MonthlyComparisonBean.AirQualityBean> list = new ArrayList<MonthlyComparisonBean.AirQualityBean>();
                    list = HttpPost.stringToList(responsebody,MonthlyComparisonBean.AirQualityBean.class);
                    monthlyComparisonBean = new MonthlyComparisonBean();
                    monthlyComparisonBean.setCurrentMonth(list);
                }else {
                    monthlyComparisonBean = gson.fromJson(responsebody,MonthlyComparisonBean.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return monthlyComparisonBean;
    }

    public static ArrayList<WeatherConditionBean> getWeatherConditionDay(String strurl, OkHttpClient mClient, String archId, String time){
        ArrayList<WeatherConditionBean> list = null;
        String funName = "getWeatherConditionDay";
        try {
            //优良天数占比
            JSONObject object = new JSONObject();
            object.put("archId",archId);
            object.put("time",time);
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();


            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                //
                list = HttpPost.stringToList(responsebody,WeatherConditionBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static  ArrayList<DataQueryVoBean> onePMDevicesDataList(String strurl, OkHttpClient mClient, String deviceIdsStr, String dataType, String beginTime, String endTime){
        //2.2	单设备PM数据列表
        ArrayList<DataQueryVoBean> list = null;
        String funName = "onePMDevicesDataList";
        FormBody body = new FormBody.Builder()
                .add("size",300+"")
                .add("deviceIdsStr", deviceIdsStr)
                .add("dataType",dataType)
                .add("beginTime",beginTime)
                .add("endTime",endTime)
                .build();
        Request request = new Request.Builder()
                .addHeader("X-Requested-With","X-Requested-With")
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                String content = new JSONObject(responsebody).getString("content");
                list = HttpPost.stringToList(content,DataQueryVoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  list;
    }

    public static ArrayList<DataQueryVoBean> getOneDevicesHistoryData(String strurl, OkHttpClient mClient,String id){
        //2.3	单设备PM历史数据
        ArrayList<DataQueryVoBean> list = null;
        String funName = "getOneDevicesHistoryData";
        Request request = new Request.Builder()
                .url(strurl+id)
                .get()
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                list = HttpPost.stringToList(responsebody,DataQueryVoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static  ArrayList<DataQueryVoBean> onePMDevices24Data(String strurl, OkHttpClient mClient,String deviceId,String pushTime){
        //2.5	单设备某天24小时数据
        ArrayList<DataQueryVoBean> list = null;
        String funName = "onePMDevices24Data";
        FormBody body = new FormBody.Builder()
                .add("deviceId", deviceId)
                .add("pushTime",pushTime)
                .build();
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                list = HttpPost.stringToList(responsebody,DataQueryVoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  list;
    }


    public static ArrayList<DevicesBean> getDevicesList(String strurl, OkHttpClient mClient, String deviceType, String deviceName, String archId, String deviceStatus){
        //获取设备列表—文昊炅  ESS_DEVICE_LIST
        ArrayList<DevicesBean> list = null;
        String funName = "getDevicesList";
        FormBody  body = new FormBody.Builder()
                .add("deviceType", deviceType)
                .add("deviceName", deviceName)
                .add("archId", archId)
                .add("size",300+"")
                .add("deviceStatus", deviceStatus)
                .build();
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                String content = new JSONObject(responsebody).getString("content");
                list = HttpPost.stringToList(content,DevicesBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static WeatherLiveBean getWeatherLive(String strurl, OkHttpClient mClient, String archId, String time){

        WeatherLiveBean weatherLiveBean = null;
        String funName = "getWeatherLive";
        try {
            //天气实况
            JSONObject object = new JSONObject();
            object.put("archId",archId);
            object.put("time",time);
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                Gson gson = new Gson();
                weatherLiveBean = gson.fromJson(responsebody,WeatherLiveBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return  weatherLiveBean;
    }
}
