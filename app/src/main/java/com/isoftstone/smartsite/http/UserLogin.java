package com.isoftstone.smartsite.http;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.muckcar.MapMarkersVoBean;
import com.isoftstone.smartsite.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gone on 2017/10/29.
 */

public class UserLogin {
    private static String TAG = "UserLogin";

    public static LoginBean login(String strurl, OkHttpClient mClient, String username, String password, String mobileDeviceId){
        LoginBean loginBean = null;
        FormBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("registerId",mobileDeviceId)
                .build();
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,"login response code "+response.code());
            if(response.isSuccessful()){
                loginBean = new LoginBean();
                String responsebody = response.body().string();
                LogUtils.i(TAG,"login response "+responsebody);
                JSONObject json = new JSONObject(responsebody);
                boolean success = json.getBoolean("success");
                if(success){
                    loginBean.setmName(username);
                    loginBean.setmPassword(password);
                    loginBean.setLoginSuccess(true);
                }else{
                    int errorinfo = json.getInt("reason");
                    loginBean.setmErrorCode(errorinfo);
                    loginBean.setLoginSuccess(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginBean;
    }

    public static LoginBean.VideoParameter getVideoConfig(String strurl, OkHttpClient mClient){
        LoginBean.VideoParameter videoParameter = null;
        String funName = "getVideoConfig";
        Request request = new Request.Builder()
                .url(strurl)
                .get()
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                Gson gson = new Gson();
                videoParameter = gson.fromJson(responsebody,LoginBean.VideoParameter.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoParameter;
    }


    public static UserBean getLoginUser(String strurl, OkHttpClient mClient,UserBean userBean){
        UserBean userBeanReturn = null;
        UserBean.Permission permission = null;
        String funName = "getLoginUser";
        try {
            Gson gson = new Gson();
            String json = gson.toJson(userBean);
            RequestBody body = RequestBody.create(HttpPost.JSON, json);

            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                JSONObject object = new JSONObject(responsebody);
                String loginUser = object.getString("loginUser");
                String privilegeCode = object.getString("privilegeCode");
                userBeanReturn = gson.fromJson(loginUser,UserBean.class);
                permission = gson.fromJson(privilegeCode,UserBean.Permission.class);
                userBeanReturn.setmPermission(permission);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  userBeanReturn;
    }

    public static UserBean getLoginUserById(String strurl, OkHttpClient mClient){
        UserBean userBeanReturn = null;
        UserBean.Permission permission = null;
        String funName = "getLoginUserById";
        try {
            Request request = new Request.Builder()
                    .url(strurl)
                    .get()
                    .build();
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                Gson gson = new Gson();
                userBeanReturn = gson.fromJson(responsebody,UserBean.class);
                userBeanReturn.setmPermission(HttpPost.mLoginBean.getmUserBean().getmPermission());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userBeanReturn;
    }

    public static  void  userUpdate(String strurl, OkHttpClient mClient,UserBean userBean){

        String funName = "userUpdate";
        try {
            Gson gson = new Gson();
            String json = gson.toJson(userBean);
            RequestBody body = RequestBody.create(HttpPost.JSON, json);

            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  MobileHomeBean getMobileHomeData(String strurl, OkHttpClient mClient)
    {
        MobileHomeBean mobileHomeBean = null;
        String funName = "getMobileHomeData";
        try {
            Request request = new Request.Builder()
                    .url(strurl)
                    .get()
                    .build();
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                Gson gson = new Gson();
                mobileHomeBean = gson.fromJson(responsebody,MobileHomeBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  mobileHomeBean;
    }


    public static void  userImageUpload(String strurl, OkHttpClient mClient,Bitmap bit,Bitmap.CompressFormat format){
        String funName = "userImageUpload";
        try {
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            bit.compress(format, 100, bos);//参数100表示不压缩
            byte[] bytes=bos.toByteArray();
            String strBase64 =  Base64.encodeToString(bytes, Base64.DEFAULT);
            JSONObject json = new JSONObject();
            json.put("base64",strBase64);
            RequestBody body = RequestBody.create(HttpPost.JSON, json.toString());

            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static InstallBean getSystemConifg(String strurl, OkHttpClient mClient){

        InstallBean installBean = null;
        String funName = "getSystemConifg";
        try{
            Request request = new Request.Builder()
                    .url(strurl)
                    .get()
                    .build();
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                JSONArray jsonArray = new JSONArray(responsebody);
                installBean = new InstallBean();
                for (int i = 0; i <jsonArray.length(); i ++){
                      JSONObject object = (JSONObject) jsonArray.get(i);
                      String key = object.getString("key");
                      if(key.equals("android_type")){
                          installBean.setAndroid_type(object.getInt("value"));
                      }
                     if(key.equals("android_url")){
                          installBean.setAndroid_url(object.getString("value"));
                     }
                    if(key.equals("android_version")){
                        installBean.setAndroid_version(object.getString("value"));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return installBean;
    }

    public  static  void feedback(String strurl, OkHttpClient mClient,long userId,String content){
        String funName = "feedback";
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            object.put("content", content);
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
