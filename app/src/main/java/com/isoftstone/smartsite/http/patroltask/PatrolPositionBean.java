package com.isoftstone.smartsite.http.patroltask;

import com.isoftstone.smartsite.http.UserBean;

/**
 * Created by gone on 2017/11/16.
 */

/*
巡查任务的巡查点
 */
public class PatrolPositionBean {

    private static final long serialVersionUID = 0x0008L;

    private long id;  //主键
    private String position;    //巡查任务地点名
    private Double longitude;		//巡查任务经度
    private Double latitude;		//巡查任务纬度
    private int status;     //	巡查地点巡查状态
    private UserBean user;	//	巡查点实际巡查人
    private String executionTime;     //	巡查点巡查时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }
}
