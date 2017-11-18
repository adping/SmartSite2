package com.isoftstone.smartsite.model.inspectplan.bean;

import java.util.Date;

/**
 * Created by zhang on 2017/11/18.
 */

public class InspectPlanBean {
    private int userId;//根据userId查询任务
    private String taskName;//任务名称
    private String address;//巡查点名称
    private Date taskTimeStart;//开始时间
    private Date taskTimeEnd;//结束时间
    private int taskStatus;//巡查任务状态(未执行:0、正在执行：1、已执行:2、已延期:3)
    private String users;//任务巡查人员
    private String patrolPositions;//任务巡查点位
    private String userName;//用户名称
    private String userCompany;//用户公司

    public InspectPlanBean() {

    }

    public InspectPlanBean(int userId, String taskName, String address, Date taskTimeStart, Date taskTimeEnd, int taskStatus) {
        this.userId = userId;
        this.taskName = taskName;
        this.address = address;
        this.taskTimeStart = taskTimeStart;
        this.taskTimeEnd = taskTimeEnd;
        this.taskStatus = taskStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getTaskTimeStart() {
        return taskTimeStart;
    }

    public void setTaskTimeStart(Date taskTimeStart) {
        this.taskTimeStart = taskTimeStart;
    }

    public Date getTaskTimeEnd() {
        return taskTimeEnd;
    }

    public void setTaskTimeEnd(Date taskTimeEnd) {
        this.taskTimeEnd = taskTimeEnd;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    @Override
    public String toString() {
        return "InspectPlanBean{" +
                "userId=" + userId +
                ", taskName='" + taskName + '\'' +
                ", address='" + address + '\'' +
                ", taskTimeStart=" + taskTimeStart +
                ", taskTimeEnd=" + taskTimeEnd +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
