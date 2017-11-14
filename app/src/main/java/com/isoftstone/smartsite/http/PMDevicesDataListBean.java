package com.isoftstone.smartsite.http;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/15.
 */

public class PMDevicesDataListBean {
    private String errorinfo = "";
    private int errorcode = 0;
    private String date = "";
    private String time = "";
    private String address = "";
    private int state = 0;
    private String name = "";
    private ArrayList<PMDevicesDataInfoBean> lsit = new ArrayList<PMDevicesDataInfoBean>();

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PMDevicesDataInfoBean> getLsit() {
        return lsit;
    }

    public void setLsit(ArrayList<PMDevicesDataInfoBean> lsit) {
        this.lsit = lsit;
    }
}
