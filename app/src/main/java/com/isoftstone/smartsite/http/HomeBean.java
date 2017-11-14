package com.isoftstone.smartsite.http;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/14.
 */

public class HomeBean {
    private String errorinfo = "";
    private int errorcode = 0;
    private String date = "";
    private String city = "";
    private String weather = "";
    private String weather_image = "";
    private String wind = "";
    private String wind_image = "";
    private String temperature = "";
    private int message = 0;
    private int report = 0;
    private String video_device = "";
    private String environment_device = "";
    private ArrayList<MessageListBean> messagelist = new ArrayList<MessageListBean>();

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeather_image() {
        return weather_image;
    }

    public void setWeather_image(String weather_image) {
        this.weather_image = weather_image;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWind_image() {
        return wind_image;
    }

    public void setWind_image(String wind_image) {
        this.wind_image = wind_image;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getVideo_device() {
        return video_device;
    }

    public void setVideo_device(String video_device) {
        this.video_device = video_device;
    }

    public ArrayList<MessageListBean> getMessagelist() {
        return messagelist;
    }

    public void setMessagelist(ArrayList<MessageListBean> messagelist) {
        this.messagelist = messagelist;
    }

    public String getEnvironment_device() {
        return environment_device;
    }

    public void setEnvironment_device(String environment_device) {
        this.environment_device = environment_device;
    }
}
