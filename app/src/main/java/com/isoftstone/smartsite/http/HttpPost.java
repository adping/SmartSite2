package com.isoftstone.smartsite.http;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.isoftstone.smartsite.User;
import com.isoftstone.smartsite.common.App;
import com.isoftstone.smartsite.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by guowei on 2017/10/14.
 */

public class HttpPost {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient  mClient = null;
    //public static String URL = "http://111.47.21.51:19090";//生产
    public static String URL = "http://61.160.82.83:19090/ctess";//龙云

    private String LOGIN_URL = URL + "/login";                        //登录
    private String  GET_LOGIN_USER = URL + "/user/getLoginUser";      //获取登录用户信息
    private String  GET_LOGIN_USER_BYID = URL + "/user/get/";      //获取登录用户信息
    private String   USER_UPDATE = URL + "/user/update";              //用户信息更改
    private String   USER_UPLOAD = URL + "/user/upload";              //用户头像修改
    private String  GET_SYSTEM_CONFIG = URL + "/systemConfig/4";

    private String GET_VIDEO_CONFIG = URL + "/mobile/video/config";   //获取视频服务器参数
    private String MOBILE_HOME = URL + "/mobile/home";                //获取首页数据

    private String EQI_DATA_RANKING = URL + "/eqi/dataRanking";      //区域月度综合排名
    private String EQI_DATA_COMPARISON = URL + "/eqi/dataComparison";  //区域月度数据对比
    private String EQI_DAYS_PROPORTION = URL + "/eqi/daysProportion";  //优良天数占比
    private String EQI_WEATHER_LIVE = URL + "/eqi/weatherLive";        //获取实时天气情况
    private String EQI_LIST = URL + "/eqi/list";                       //单设备PM数据列表
    private String EQI_BYDEVICE_HISTORY = URL + "/eqi/byDevice/history/";  //获取一台设备历史参数
    private String EQI_BYDEVICE_DAYS = URL + "/eqi/byDevice/days";        //获取一台设备24小时数据


    private String ESS_DEVICE_LIST = URL + "/EssDevice/list";           //获取设备数据


    private String MESSAGE_LIST = URL + "/message/list";                //获取消息列表
    private String MESSAGE_ID_READ = URL + "/message/{id}/read";        //消息读取

    private String PATROL_LIST = URL + "/patrol/list";        //获取报告列表
    private String ADD_PATROL_REPORT  = URL + "/patrol";      //新增巡查报告
    private String GET_PATROL_REPORT = URL + "/patrol/";      //获取巡查报告
    private String ADD_REPORT  = URL + "/report";            //新增巡查报告回复 回访  验收
    private String IMAGE_UPLOAD  = URL + "/report/attach/mobile";  //图片上传
    private String DICTIONARY_LIST = URL + "/dictionary/list";   //获取报告类型
    private String GET_PATROL_ADDRESS  = URL+"/patrol/addresses";//获取巡查报告地点


    public static  boolean mVideoIsLogin = false;



    public static  LoginBean mLoginBean = null;
    public static CookiesManager mCookiesManager = null;
    public HttpPost(){
        if (mClient == null){
            mCookiesManager = new CookiesManager(App.getAppContext());
            mClient = new OkHttpClient.Builder()
                    .cookieJar(mCookiesManager)
                    .build();
        }
    }

    public static <T>  ArrayList<T> stringToList(String json ,Class<T> cls  ){
        Gson gson = new Gson();
        ArrayList<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            list.add(gson.fromJson(elem, cls));
        }
        return list ;
    }


    public static  boolean isConnected(){
        return NetworkUtils.isConnected();
    }


    /*
    登录专用接口，传入用户名，密码，以及设备ID号
     */
    public LoginBean login(String username, String password, String mobileDeviceId){

        mCookiesManager.removeAll();

        if (mLoginBean == null)
        {
            mLoginBean = new LoginBean();
        }

        LoginBean loginBean = UserLogin.login(LOGIN_URL,mClient,username,password,mobileDeviceId);
        if(loginBean != null){
            if(loginBean.isLoginSuccess()){
                mLoginBean.setLoginSuccess(true);
                mLoginBean.setmName(username);
                mLoginBean.setmPassword(password);
            }else{
                mLoginBean.setmErrorCode(loginBean.getmErrorCode());
            }
        }
        return  mLoginBean;
    }




    /*
     区域月度综合排名  测试object.put("archId",21); object.put("time","2017-10");
   */
    public EQIRankingBean  eqiDataRanking(String archId,String time){
        return EQIMonitoring.eqiDataRanking(EQI_DATA_RANKING,mClient,archId,time);
    }


    /*
     区域月度数据对比 测试数据 "29","2017-10","1"
     */
    public MonthlyComparisonBean carchMonthlyComparison(String archId,String time,String type){
        return  EQIMonitoring.carchMonthlyComparison(EQI_DATA_COMPARISON,mClient,archId,time,type);
    }

    /*
    优良天数占比
    "29","2017-10"
     */
    public ArrayList<WeatherConditionBean> getWeatherConditionDay(String archId,String time){
        return  EQIMonitoring.getWeatherConditionDay(EQI_DAYS_PROPORTION,mClient,archId,time);
    }

    /*
    2.2	单设备PM数据列表  "[1,2]","0","2017-10-01 00:00:00","2017-10-11 00:00:00"
    */
    public  ArrayList<DataQueryVoBean> onePMDevicesDataList(String deviceIdsStr,String dataType,String beginTime,String endTime) {
          return EQIMonitoring.onePMDevicesDataList(EQI_LIST,mClient,deviceIdsStr,dataType,beginTime,endTime);
    }


   /*
   2.3	单设备PM历史数据    测试数据 "1"
    */
    public  ArrayList<DataQueryVoBean> getOneDevicesHistoryData(String id){
       return EQIMonitoring.getOneDevicesHistoryData(EQI_BYDEVICE_HISTORY,mClient,id);
    }


    /*
    2.5	单设备某天24小时数据  测试数据 "2","2017-10-10 10:10:10"
     */

    public ArrayList<DataQueryVoBean> onePMDevices24Data(String deviceIdsStr,String pushTime){
        return  EQIMonitoring.onePMDevices24Data(EQI_BYDEVICE_DAYS,mClient,deviceIdsStr,pushTime);
    }

    /*
    //获取设备列表—文昊炅  测试 "","","",""
     */
    public  ArrayList<DevicesBean>  getDevices(String deviceType,String deviceName,String archId,String deviceStatus){
        return EQIMonitoring.getDevicesList(ESS_DEVICE_LIST,mClient,deviceType,deviceName,archId,deviceStatus);
    }

    /*
     //获取消息列表   测试"","","","2"
     */
    public ArrayList<MessageBean> getMessage(String title, String type, String status, String module) {

        return  MessageOperation.getMessage(MESSAGE_LIST,mClient,title,type,status,module);
    }

    /*
    消息阅读已经完成
     */
    public void readMessage(String id){
        MessageOperation.readMessage(MESSAGE_ID_READ,mClient,id);
    }

    /*
     2.2	获取视频配置
     */
    public boolean getVideoConfig(){
        boolean flag = false;
        LoginBean.VideoParameter videoParameter = UserLogin.getVideoConfig(GET_VIDEO_CONFIG,mClient);
        if(videoParameter != null){
            mLoginBean.setmVideoParameter(videoParameter);
            Log.i("zyf", videoParameter.toString());
            flag = true;
        }
        return flag;
    }


    /*
    天气实况  测试数据"47","2017-10"
     */
    public WeatherLiveBean getWeatherLive(String archId,String time){
        return  EQIMonitoring.getWeatherLive(EQI_WEATHER_LIVE,mClient,archId,time);
    }


    /*
     获取巡查报告列表  测试数据 1
     */
    public ArrayList<PatrolBean>  getPatrolReportList(String status){
        return  ReportOperation.getPatrolReportList(PATROL_LIST,mClient,status);
    }

    /*
    新增巡查报告
     */
    public PatrolBean addPatrolReport(PatrolBean reportBean){
        return ReportOperation.addPatrolReport(ADD_PATROL_REPORT,mClient,reportBean);
    }


    /*
    获取一个巡查报告   测试"75"
     */
    public PatrolBean getPatrolReport(String id){
        return  ReportOperation.getPatrolReport(GET_PATROL_REPORT,mClient,id);
    }

    /*
    新增巡查回访
     */
    public  void addPatrolVisit(ReportBean reportBean){
         ReportOperation.addPatrolVisit(ADD_REPORT,mClient,reportBean);
    }

    /*
    新增巡查回复
     */
    public void addPatrolReply(ReportBean reportBean){
        ReportOperation.addPatrolReply(ADD_REPORT,mClient,reportBean);
    }

    /*
    新增巡查验收
     */
    public void addPatrolCheck(ReportBean reportBean){
        ReportOperation.addPatrolCheck(ADD_REPORT,mClient,reportBean);
    }
    /*
    报告上传文件
     */
    public void reportFileUpload(String filepath,int id){
        ReportOperation.reportFileUpload(IMAGE_UPLOAD,mClient,filepath,id);
    }


    //下载报告图片，需要传入id和服务器获取的到路径
    public void downloadReportFile(int id,String filename){

        String url = getFileUrl(filename);
        String name = getFileName(filename);
        String sdpath = Environment.getExternalStorageDirectory().getPath();
        String storagePath = "/isoftstone/"+mLoginBean.getmName()+"/report/"+id;
        File file = new File(storagePath);
        if(!file.exists()){
            file.mkdirs();
        }
        if (file.exists())Log.i("Test","url  "+url+"  storagePath  "+storagePath+" name "+name+"   "+getReportPath(id,filename));
        ReportOperation.downloadfile(url,storagePath,name);
    }

    /*
    获取用户信息
    */
    public UserBean getLoginUser(){
        UserBean userBean = null;
        if(mLoginBean != null) {
            if (mLoginBean.getmUserBean() != null) {
                userBean = UserLogin.getLoginUserById(GET_LOGIN_USER_BYID + mLoginBean.getmUserBean().getId(), mClient);
            } else {
                UserBean user = new UserBean();
                user.setAccount(mLoginBean.getmName());
                user.setPassword(mLoginBean.getmPassword());
                userBean =UserLogin.getLoginUser(GET_LOGIN_USER, mClient, user);
            }
        }
        mLoginBean.setmUserBean(userBean);
        return userBean;
    }
    //更改用户信息
    public void userUpdate(UserBean userBean){
        UserLogin.userUpdate(USER_UPDATE,mClient,userBean);
    }

    //上传用户头像
    public void userImageUpload(Bitmap bit,Bitmap.CompressFormat format){
        UserLogin.userImageUpload(USER_UPLOAD,mClient,bit,format);
    }
    //下载用户图片  服务器获取的到路径
    public void downloadUserImage(String filename){

        String url = getFileUrl(filename);
        String name = getFileName(filename);
        String sdpath = Environment.getExternalStorageDirectory().getPath();
        String storagePath = "/isoftstone/"+mLoginBean.getmName()+"/usericon";
        File file = new File(storagePath);
        if(!file.exists()){
            file.mkdirs();
        }

        if (file.exists())Log.i("Test","url  "+url+"  storagePath  "+storagePath+" name "+name+"   "+getImagePath(filename));
        ReportOperation.downloadfile(url,storagePath,name);
    }
    /*
    获取主界面数据
     */
    public  MobileHomeBean getMobileHomeData(){
       return UserLogin.getMobileHomeData(MOBILE_HOME,mClient);
    }



    //获取下载文件、图片的URL
    public  String  getFileUrl(String filename){
        if(filename != null){
            filename = filename.replaceAll("\\\\","/");
        }
        return URL + "/"+filename;
    }

    //获取用户图片本地保存绝对路径
    public String getImagePath(String imageName){

        String sdpath = Environment.getExternalStorageDirectory().getPath();
        String storagePath = sdpath + "/isoftstone/"+mLoginBean.getmName()+"/usericon/"+getFileName(imageName);
        return  storagePath;
    }

    //获取报告图片、文件本地保存绝对路径
    public String getReportPath(int id,String imageName){

        String sdpath = Environment.getExternalStorageDirectory().getPath();
        String storagePath = sdpath + "/isoftstone/"+mLoginBean.getmName()+"/report/"+id+"/"+getFileName(imageName); ;
        return  storagePath;
    }


    private String  getFileName(String filename){
        int index = filename.lastIndexOf("\\");
        if(index > 0){
            return  filename.substring(index+1);
        }
        return  filename;
    }

    /*
    获取巡查类型列表
     */
    public  ArrayList<DictionaryBean> getDictionaryList(String lang){
        return  ReportOperation.getDictionaryList(DICTIONARY_LIST,mClient,lang);
    }

    /*
    获取巡查地址
     */
    public ArrayList<String>   getPatrolAddress(){
        return  ReportOperation.getPatrolAddress(GET_PATROL_ADDRESS,mClient);
    }
    /*
    获取版本信息，包含版本号、版本下载绝对路径、是否强制更新
     */
    public InstallBean getSystemConifg(){
        return  UserLogin.getSystemConifg(GET_SYSTEM_CONFIG,mClient);
    }
}