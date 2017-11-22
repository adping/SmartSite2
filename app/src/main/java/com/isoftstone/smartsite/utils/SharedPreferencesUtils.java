package com.isoftstone.smartsite.utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by gone on 2017/11/19.
 */

public class SharedPreferencesUtils {
    private static  final  String SHARED_PREFERENCES_NAME = "isoftstone";
    private static  final  String SAVE_PASSWD = "save_passwd";

    public static void updateSavePasswd(Activity activity,boolean isSave){
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SAVE_PASSWD, isSave);
        editor.commit();
    }

    public static  boolean getSavePasswd(Activity activity){
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        boolean isSave = settings.getBoolean(SAVE_PASSWD,true);
        return  isSave;
    }
}
