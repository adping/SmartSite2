package com.isoftstone.smartsite.http;

/**
 * Created by gone on 2017/10/14.
 */

public class MessageListBean {
    private String title = "";
    private String detail = "";
    private int state = 0;    //1：视频；2：环境
    private String time = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
