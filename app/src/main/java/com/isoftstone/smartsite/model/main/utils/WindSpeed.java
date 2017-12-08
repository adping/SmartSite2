package com.isoftstone.smartsite.model.main.utils;

/**
 * Created by gone on 2017/12/7.
 */

public class WindSpeed {
    public  static  String getWindSpeed(String windDirection,double windspeed){
        String ws = "";
        if(windspeed < 1 ){
           ws = "0级";
        }else if(windspeed >=1 && windspeed < 6 ){
            ws = "1级";
        }else if(windspeed >=6 && windspeed < 12 ){
            ws = "2级";
        }else if(windspeed >=12 && windspeed < 20 ){
            ws = "3级";
        }else if(windspeed >=20 && windspeed < 29 ){
            ws = "4级";
        }else if(windspeed >=29 && windspeed < 39 ){
            ws = "5级";
        }else if(windspeed >=39 && windspeed < 50 ){
            ws = "6级";
        }else if(windspeed >=50 && windspeed < 62 ){
            ws = "7级";
        }else if(windspeed >=62 && windspeed < 75 ){
            ws = "8级";
        }else if(windspeed >=75 && windspeed < 89 ){
            ws = "9级";
        }else if(windspeed >=89 && windspeed < 103 ){
            ws = "10级";
        }else if(windspeed >=103 && windspeed < 118 ){
            ws = "11级";
        }else if(windspeed >=118){
            ws = "12级";
        }
        return  windDirection + "风"+ ws;
    }
}
