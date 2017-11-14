package com.isoftstone.smartsite.common;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;


/**
 * Created by zw on 2017/10/11.
 */

public class App extends Application {

    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CrashReport.initCrashReport(getAppContext(),"5ba8f42531",false);
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }
}
