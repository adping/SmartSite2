package com.isoftstone.smartsite.http;

/**
 * Created by gone on 2017/11/1.
 */

public class DeviceBean {
    private Integer deviceId;		//设备ID
    private String deviceCoding	;	//设备编码
    private String deviceName;		//设备名称
    private Integer deviceType;		//设备类型（0为PM10，1为监控）
    private String installTime;	             //Date(yyyy-MM-dd HH:mm:ss)	安装时间
    private Long archId;		        //所属区域ID
    private Integer deviceStatus;		  //设备状态（0在线，1离线，2故障）
    private String longitude;	      //设备精度
    private String  latitude;		//设备纬度

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCoding() {
        return deviceCoding;
    }

    public void setDeviceCoding(String deviceCoding) {
        this.deviceCoding = deviceCoding;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public Long getArchId() {
        return archId;
    }

    public void setArchId(Long archId) {
        this.archId = archId;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
