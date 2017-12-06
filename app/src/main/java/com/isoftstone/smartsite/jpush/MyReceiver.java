package com.isoftstone.smartsite.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.model.dirtcar.Service.RecognizeDirtCarService;
import com.isoftstone.smartsite.model.main.ui.AirMonitoringActivity;
import com.isoftstone.smartsite.model.main.ui.MainFragment;
import com.isoftstone.smartsite.model.main.ui.SplashActivity;
import com.isoftstone.smartsite.model.tripartite.activity.ReadReportActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    public static final String TURN_TO_PATROL_TASK = "app.cp.patrolTask";

    public static final String SEARCH_CODE_ENVIRON = "1|"; //环境检测
    public static final String SEARCH_CODE_PM10_EXCEED = "1|1|";//PM10指数超标
    public static final String SEARCH_CODE_PM10_UP_DOWN = "1|2|";//PM10上下线
    public static final String SEARCH_CODE_VEDIO = "2|";//视频监控
    public static final String SEARCH_CODE_OFFLINE = "2|1|";//视频监控离线
    public static final String SEARCH_CODE_THREE_PARTY = "3|"; //第三方协同工作
    public static final String SEARCH_CODE_THREE_PARTY_CHECK = "3|1|";//验收报告
    public static final String SEARCH_CODE_THREE_PARTY_CHECK_PASS = "3|2|";//验收报告通过
    public static final String SEARCH_CODE_THREE_PARTY_CHECK_REJECT = "3|3|";//报告退回
    public static final String SEARCH_CODE_THREE_PARTY_CHECK_REPLY = "3|4|"; //报告回复
    public static final String SEARCH_CODE_WATCH_CAR = "4|"; //渣土车监控
    public static final String SEARCH_CODE_WATCH_CAR_RECO = "4|1|";//人工识别
    public static final String SEARCH_CODE_WATCH_CAR_ZUIZONG = "4|2|";//渣土车追踪
    public static final String SEARCH_CODE_THREE_PARTH_WATCH = "7|"; //第三方巡查监控
    public static final String SEARCH_CODE_PLAN = "7|1|";//巡查任务
    public static final String SEARCH_CODE_THREE_PARTH_BUILD_WATCH = "110|";//第三方施工巡查
    public static final String SEARCH_CODE_PLAN_APPROVAL = "110|1|";//审批计划
    public static final String SEARCH_CODE_PAN_APPROVAL_PASS = "110|2|";//计划通过
    public static final String SEARCH_CODE_PAN_APPROVAL_REJECT = "110|3|";//计划退回


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            startDirtCarService(context);
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");

                //打开自定义的Activity
//				Intent i = new Intent(context, TestActivity.class);
//				i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//				context.startActivity(i);
                Log.e(TAG, "yanlog bundle:" + bundle.toString());
                HashMap<String, String> result = getBundle(bundle);
                String searchCode = result.get("searchCode");
                Logger.d(TAG, "[MyReceiver] turnTo:" + searchCode);
                if (HttpPost.mLoginBean == null || !HttpPost.mLoginBean.isLoginSuccess()) {
                    Intent i = new Intent(context, SplashActivity.class);
                    context.startActivity(i);
                } else if (SEARCH_CODE_ENVIRON.equals(searchCode)) {
                    //环境检测 //TODO please check
                    Intent i = new Intent(context, AirMonitoringActivity.class);
                    context.startActivity(i);
                } else if (SEARCH_CODE_PM10_EXCEED.equals(searchCode)) {
                    //PM10指数超标 //TODO please check
                    Intent i = new Intent(context, AirMonitoringActivity.class);
                    context.startActivity(i);
                } else if (SEARCH_CODE_PM10_UP_DOWN.equals(searchCode)) {
                    //PM10上下线  //TODO please check
                    Intent i = new Intent(context, AirMonitoringActivity.class);
                    context.startActivity(i);
                } else if (SEARCH_CODE_VEDIO.equals(searchCode)) {
                    //视频监控//TODO please check
                    MainFragment.enterVideoMonitoring(context);
                } else if (SEARCH_CODE_OFFLINE.equals(searchCode)) {
                    //视频监控离线//TODO please check
                    MainFragment.enterVideoMonitoring(context);
                } else if (SEARCH_CODE_THREE_PARTY.equals(searchCode)) {
                    //第三方协同工作
                    MainFragment.enterThirdPartReport(context);
                } else if (SEARCH_CODE_THREE_PARTY_CHECK.equals(searchCode)) {
                    //验收报告
                    Intent i = new Intent(context, ReadReportActivity.class);
                    int id = -1;
                    try {
                        id = Integer.parseInt(result.get(id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i.putExtra("_id", id);
                    context.startActivity(i);
                } else if (SEARCH_CODE_THREE_PARTY_CHECK_PASS.equals(searchCode)) {
                    //验收报告通过
                    Intent i = new Intent(context, ReadReportActivity.class);
                    int id = -1;
                    try {
                        id = Integer.parseInt(result.get(id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i.putExtra("_id", id);
                    context.startActivity(i);
                } else if (SEARCH_CODE_THREE_PARTY_CHECK_REJECT.equals(searchCode)) {
                    //验收报告退回
                    Intent i = new Intent(context, ReadReportActivity.class);
                    int id = -1;
                    try {
                        id = Integer.parseInt(result.get(id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i.putExtra("_id", id);
                    context.startActivity(i);
                } else if (SEARCH_CODE_THREE_PARTY_CHECK_REPLY.equals(searchCode)) {
                    //报告回复
                    Intent i = new Intent(context, ReadReportActivity.class);
                    int id = -1;
                    try {
                        id = Integer.parseInt(result.get(id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i.putExtra("_id", id);
                    context.startActivity(i);
                } else if (SEARCH_CODE_WATCH_CAR.equals(searchCode)) {
                    //渣土车监控 //TODO please check
                    MainFragment.enterDircar(context);
                } else if (SEARCH_CODE_WATCH_CAR_RECO.equals(searchCode)) {
                    //人工识别
                    //忽略
                } else if (SEARCH_CODE_WATCH_CAR_ZUIZONG.equals(searchCode)) {
                    //渣土车追踪  //TODO please check
                    MainFragment.enterDircar(context);
                } else if (SEARCH_CODE_THREE_PARTH_WATCH.equals(searchCode)) {
                    //第三方巡查监控//TODO please check
                    MainFragment.enterConstructionMonitor(context);
                } else if (SEARCH_CODE_PLAN.equals(searchCode)) {
                    //巡查任务//TODO please check
                    MainFragment.enterPatrolMission(context);
                } else if (SEARCH_CODE_THREE_PARTH_BUILD_WATCH.equals(searchCode)) {
                    //第三方施工巡查//TODO please check
                    Intent i = new Intent(com.isoftstone.smartsite.MainActivity.ACTION_CHANGE_TAB);
                    i.setPackage("com.isoftstone.smartsite");
                    i.putExtra("tab", 2);
                    context.sendBroadcast(i);
                } else if (SEARCH_CODE_PLAN_APPROVAL.equals(searchCode)) {
                    //审批计划//TODO please check
                    Intent i = new Intent(com.isoftstone.smartsite.MainActivity.ACTION_CHANGE_TAB);
                    i.setPackage("com.isoftstone.smartsite");
                    i.putExtra("tab", 2);
                    context.sendBroadcast(i);
                } else if (SEARCH_CODE_PAN_APPROVAL_PASS.equals(searchCode)) {
                    //计划通过//TODO please check
                    Intent i = new Intent(com.isoftstone.smartsite.MainActivity.ACTION_CHANGE_TAB);
                    i.setPackage("com.isoftstone.smartsite");
                    i.putExtra("tab", 2);
                    context.sendBroadcast(i);
                } else if (SEARCH_CODE_PAN_APPROVAL_REJECT.equals(searchCode)) {
                    //计划退回//TODO please check
                    Intent i = new Intent(com.isoftstone.smartsite.MainActivity.ACTION_CHANGE_TAB);
                    i.setPackage("com.isoftstone.smartsite");
                    i.putExtra("tab", 2);
                    context.sendBroadcast(i);
                }


            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }

        } catch (
                Exception e)

        {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private static HashMap<String, String> getBundle(Bundle bundle) {
        HashMap<String, String> result = new HashMap<>();
        if (bundle == null) {
            return result;
        }
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                //sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
                result.put(key, bundle.getInt(key) + "");
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                // sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
                result.put(key, "" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
//                        sb.append("\nkey:" + key + ", value: [" +
//                                myKey + " - " + json.optString(myKey) + "]");
                        result.put(myKey, "" + json.optString(myKey));
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
//                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
                result.put(key, bundle.getString(key));
            }
        }
        return result;
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }
    }

    private void startDirtCarService(Context context) {
        try {
            Intent i = new Intent(context, RecognizeDirtCarService.class);
            i.putExtra("sync", true);
            context.startService(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
