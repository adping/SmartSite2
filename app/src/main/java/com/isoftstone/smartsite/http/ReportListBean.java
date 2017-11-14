package com.isoftstone.smartsite.http;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/15.
 */

public class ReportListBean {
    private String errorinfo = "";
    private int errorcode = 0;
    private String date = "";
    private ArrayList<ReportBeanBak> patrolRepoList = new ArrayList<ReportBeanBak>();  //巡查报告
    private ArrayList<ReportBeanBak> checkRepoList = new ArrayList<ReportBeanBak>();   //验收报告

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

    public ArrayList<ReportBeanBak> getPatrolRepoList() {
        return patrolRepoList;
    }

    public void setPatrolRepoList(ArrayList<ReportBeanBak> patrolRepoList) {
        this.patrolRepoList = patrolRepoList;
    }

    public ArrayList<ReportBeanBak> getCheckRepoList() {
        return checkRepoList;
    }

    public void setCheckRepoList(ArrayList<ReportBeanBak> checkRepoList) {
        this.checkRepoList = checkRepoList;
    }
}
