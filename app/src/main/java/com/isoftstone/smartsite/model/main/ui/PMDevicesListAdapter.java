package com.isoftstone.smartsite.model.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.DataQueryVoBean;
import com.isoftstone.smartsite.http.DevicesBean;
import com.isoftstone.smartsite.http.PMDevicesDataInfoBean;
import com.isoftstone.smartsite.http.VideoMonitorBean;
import com.isoftstone.smartsite.model.main.listener.OnConvertViewClickListener;
import com.isoftstone.smartsite.model.map.ui.VideoMonitorMapActivity;
import com.isoftstone.smartsite.model.video.VideoPlayActivity;
import com.isoftstone.smartsite.model.video.VideoRePlayActivity;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gone on 2017/10/16.
 * modifed by zhangyinfu on 2017/10/19
 * modifed by zhangyinfu on 2017/10/20
 * modifed by zhangyinfu on 2017/10/21
 */

public class PMDevicesListAdapter extends BaseAdapter {

    public static final String COLOR_OFFLINE = "#AAAAAA";
    public static final String COLOR_0= "#3464DD";
    public static final String COLOR_50 = "#01B663";
    public static final String COLOR_150 = "#FFD801";
    public static final String COLOR_250 = "#FD8200";
    public static final String COLOR_350 = "#FD0001";
    public static final String COLOR_420 = "#95014B";
    public static final String COLOR_600 = "#5C011B";


    private LayoutInflater mInflater;
    private  ArrayList<DataQueryVoBean> mData = new ArrayList<DataQueryVoBean>();
    private Context mContext = null;
    private final String IMAGE_TYPE = "image/*";

    public PMDevicesListAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        mContext = context;

    }

    public void setData(ArrayList<DataQueryVoBean> list){
        mData = list;
        for (int i = 0; i < list.size(); i++) {
            LogUtils.i("zw : PMDevicesListAdapter : ",list.get(i).toString());
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.pmdeviceslist_adapter, parent,false);
            holder.resName = (TextView)convertView.findViewById(R.id.textView1);
            holder.isOnline = (ImageView)convertView.findViewById(R.id.textView2);
            holder.installTime = (TextView)convertView.findViewById(R.id.textView3);
            holder.address = (TextView)convertView.findViewById(R.id.textView4);
            holder.PM10 = (TextView)convertView.findViewById(R.id.text_pm10);
            holder.PM25 = (TextView)convertView.findViewById(R.id.text_pm25);
            holder.SO2 = (TextView)convertView.findViewById(R.id.text_so2);
            holder.NO2 = (TextView)convertView.findViewById(R.id.text_no2);
            holder.button_1 = (LinearLayout)convertView.findViewById(R.id.button1);
            holder.button_2 = (LinearLayout)convertView.findViewById(R.id.button2);
            holder.gotomap = (LinearLayout) convertView.findViewById(R.id.gotomap);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        final  DataQueryVoBean devices = mData.get(position);
        holder.resName.setText(devices.getDeviceCoding());
        TextPaint paint = holder.resName.getPaint();
        paint.setFakeBoldText(true);
        if(devices.getDeviceStatus() == 0){
            holder.isOnline.setBackground(mContext.getResources().getDrawable(R.drawable.online));
        }else if(devices.getDeviceStatus() == 1){
            holder.isOnline.setBackground(mContext.getResources().getDrawable(R.drawable.offline));
        }else if(devices.getDeviceStatus() == 2){
            holder.isOnline.setBackground(mContext.getResources().getDrawable(R.drawable.breakdown));
        }
        holder.installTime.setText("安装时间: "+devices.getInstallTime().substring(0,10));
        holder.address.setText("设备名称"+devices.getDeviceName());

        String pm10 = "";

        double d_pm10 = devices.getPm10();
        int pm_10 = (int) d_pm10;

        double d_pm25 = devices.getPm2_5();
        int pm_25 = (int) d_pm25;

        double d_so2 = devices.getCo2();
        int pm_so2 = (int) d_so2;


        if(pm_10 < 50){
            pm10 = "PM10：<font color='" + COLOR_50 + "'>" + pm_10 + "</font>";
        } else if(pm_10 < 150){
            pm10 = "PM10：<font color='" + COLOR_150 + "'>" + pm_10 + "</font>";
        } else if(pm_10 < 250){
            pm10 = "PM10：<font color='" + COLOR_250 + "'>" + pm_10 + "</font>";
        } else if(pm_10 < 350){
            pm10 = "PM10：<font color='" + COLOR_350 + "'>" + pm_10 + "</font>";
        } else if(pm_10 < 420){
            pm10 = "PM10：<font color='" + COLOR_420 + "'>" + pm_10 + "</font>";
        } else {
            pm10 = "PM10：<font color='" + COLOR_600 + "'>" + pm_10 + "</font>";
        }

        holder.PM10.setText(Html.fromHtml(pm10));
        String pm25 = "PM2.5：<font color='" + COLOR_0 + "'>" + pm_25+ "</font>";
        holder.PM25.setText(Html.fromHtml(pm25));
        String so2 = "SO2：<font color='" + COLOR_0 + "'>" + pm_so2 + "</font>";
        holder.SO2.setText(Html.fromHtml(so2));
        String no2 = "NO2：<font color='" + COLOR_0 + "'>" + pm_so2 + "</font>";
        holder.NO2.setText(Html.fromHtml(no2));

        final int map_position = position;
        holder.button_1.setOnClickListener(new OnConvertViewClickListener(convertView, position) {

            @Override
            public void onClickCallBack(View registedView, View rootView, int position) {
                //Toast.makeText(mContext, "ViewHolder: " +  ((ViewHolder)rootView.getTag()).toString(), Toast.LENGTH_SHORT).show();
                ViewHolder viewHolder = (ViewHolder)rootView.getTag();
                if(null != viewHolder) {
                    //实时数据
                    Intent intent = new Intent();
                    intent.putExtra("devices",mData);
                    intent.putExtra("position",map_position);
                    intent.putExtra("id",devices.getDeviceId());
                    intent.putExtra("address","设备名称"+devices.getDeviceName());
                    intent.setClass(mContext, PMDataInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "errorException:  ViewHolder is null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.button_2.setOnClickListener(new OnConvertViewClickListener(convertView, position) {
            @Override
            public void onClickCallBack(View registedView, View rootView, int position) {
                ViewHolder viewHolder = (ViewHolder)rootView.getTag();

                if(null != viewHolder) {
                    //历史数据
                    Intent intent = new Intent();
                    intent.setClass(mContext, PMHistoryInfoActivity.class);
                    intent.putExtra("devices",mData);
                    intent.putExtra("position",map_position);
                    intent.putExtra("id",devices.getDeviceId());
                    intent.putExtra("address","设备名称"+devices.getDeviceName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "errorException:  ViewHolder is null", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到地图
                Intent intent = new Intent();
                intent.putExtra("devices",mData);
                intent.putExtra("type",VideoMonitorMapActivity.TYPE_ENVIRONMENT);
                intent.putExtra("position",map_position);
                intent.setClass(mContext,VideoMonitorMapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }


    public final class ViewHolder{
        public TextView resName;//资源名称
        public ImageView isOnline;//是否在线
        public TextView  installTime;//安装日期
        public TextView  address;//安装日期
        public TextView  PM10;//PM10
        public TextView  PM25;//PM2.5
        public TextView  SO2;//SO2
        public TextView  NO2;//NO2

        public LinearLayout button_1;//实时数据
        public LinearLayout button_2;//历史数据

        public LinearLayout gotomap ; //跳转到地图
    }


}
