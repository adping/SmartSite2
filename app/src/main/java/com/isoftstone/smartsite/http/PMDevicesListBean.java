package com.isoftstone.smartsite.http;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/14.
 */

public class PMDevicesListBean {
    private String errorinfo = "";
    private int errorcode = 0;
    private String date = "";
    private ArrayList<PMDevicesBean> lsit = new ArrayList<PMDevicesBean>();

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<PMDevicesBean> getLsit() {
        return lsit;
    }

    public void setLsit(ArrayList<PMDevicesBean> lsit) {
        this.lsit = lsit;
    }
}
