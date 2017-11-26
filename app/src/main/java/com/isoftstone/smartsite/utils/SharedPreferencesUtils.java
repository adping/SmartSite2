package com.isoftstone.smartsite.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gone on 2017/11/19.
 */

public class SharedPreferencesUtils {
    private static final String SHARED_PREFERENCES_NAME = "isoftstone";
    private static final String SAVE_PASSWD = "save_passwd";
    private static final String BASE_WIDTH = "base_width";

    public static void updateSavePasswd(Activity activity, boolean isSave) {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SAVE_PASSWD, isSave);
        editor.commit();
    }

    public static boolean getSavePasswd(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        boolean isSave = settings.getBoolean(SAVE_PASSWD, true);
        return isSave;
    }

    public static void saveBaseWidth(Context context, int w) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(BASE_WIDTH, w);
        editor.commit();
    }


    public static int getBaseWidth(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        return settings.getInt(BASE_WIDTH, 0);
    }

}
