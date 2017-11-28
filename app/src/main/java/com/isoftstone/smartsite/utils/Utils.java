package com.isoftstone.smartsite.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by zhang on 2017/11/17.
 */

public class Utils {

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(PackageManager pm, String packageName) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return null;
            }
        } catch (Exception e) {
            return "";
        }
        return versionName;
    }

    public static boolean isEmptyStr(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }
}
