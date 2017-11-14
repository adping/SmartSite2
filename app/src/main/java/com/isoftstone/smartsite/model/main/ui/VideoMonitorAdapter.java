package com.isoftstone.smartsite.model.main.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.isoftstone.smartsite.http.DevicesBean;
import com.isoftstone.smartsite.http.VideoMonitorBean;
import com.isoftstone.smartsite.model.main.listener.OnConvertViewClickListener;
import com.isoftstone.smartsite.model.map.ui.VideoMonitorMapActivity;
import com.isoftstone.smartsite.model.video.VideoRePlayActivity;
import com.isoftstone.smartsite.model.video.VideoPlayActivity;
import com.isoftstone.smartsite.utils.ToastUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.isoftstone.smartsite.model.map.ui.VideoMonitorMapActivity.TYPE_CAMERA;

/**
 * Created by gone on 2017/10/16.
 * modifed by zhangyinfu on 2017/10/19
 * modifed by zhangyinfu on 2017/10/20
 * modifed by zhangyinfu on 2017/10/21
 */

public class VideoMonitorAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private ArrayList<DevicesBean> mData = new ArrayList<DevicesBean>();
    private final String IMAGE_TYPE = "image/*";
    private Context mContext = null;
    private AdapterViewOnClickListener listener;

    public VideoMonitorAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
        listener = (AdapterViewOnClickListener)context;
    }

    public interface AdapterViewOnClickListener {
        //postionType means   true ? onclick button 2 : onclick button 3
        public void viewOnClickListener(DevicesBean devicesBean, boolean isFormOneType);
    }

    public void setData(ArrayList<DevicesBean> list){
        mData = list;
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
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.videomonitor_adapter, null);
            holder.resCodeTv = (TextView)convertView.findViewById(R.id.textView_1);
            holder.installTime = (TextView)convertView.findViewById(R.id.textView_2);
            holder.resSubTypeTv = (TextView)convertView.findViewById(R.id.textView_subType);
            holder.resNameTv = (TextView)convertView.findViewById(R.id.textView_3);
            holder.isOnlineTv = (ImageView)convertView.findViewById(R.id.textView_4);
            holder.button_1 = (LinearLayout)convertView.findViewById(R.id.button1);
            holder.button_2 = (LinearLayout)convertView.findViewById(R.id.button2);
            holder.button_3 = (LinearLayout)convertView.findViewById(R.id.button3);
            holder.gotoMap = (LinearLayout)convertView.findViewById(R.id.gotomap);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        final  DevicesBean devicesBean = mData.get(position);
        Paint paint = holder.resCodeTv.getPaint();
        paint.setFakeBoldText(true);
        if(devicesBean.getDeviceCoding().length() > 10){
            holder.resCodeTv.setText(devicesBean.getDeviceCoding().substring(0,10));
        }else {
            holder.resCodeTv.setText(devicesBean.getDeviceCoding());
        }
        holder.resNameTv.setText(devicesBean.getDeviceName());
        //setCameraType(holder.resSubTypeTv, mData.get(position).getResSubType());
        String installTime = devicesBean.getInstallTime();
        holder.installTime.setText(installTime.split(" ")[0]);
        Log.i("zzzzz",  devicesBean.getDeviceName() +  "  &   " + devicesBean.getCameraType()  +  "  &  "  + devicesBean.getDeviceType());
        setCameraType(holder.resSubTypeTv, devicesBean.getCameraType());
        holder.resType = devicesBean.getDeviceType();
        //holder.resSubType = mData.get(position).getResSubType();
        holder.isOnline = devicesBean.getDeviceStatus().equals("0");
        if(devicesBean.getDeviceStatus().equals("0")){
            holder.isOnlineTv.setImageResource(R.drawable.online);
            holder.button_1.setEnabled(true);
        }else if(devicesBean.getDeviceStatus().equals("1")){
            holder.isOnlineTv.setImageResource(R.drawable.offline);
            holder.button_1.setEnabled(false);
        }else if(devicesBean.getDeviceStatus().equals("2")){
            holder.isOnlineTv.setImageResource(R.drawable.breakdown);
            holder.button_1.setEnabled(false);
        }
        holder.isShared = false;

        holder.button_1.setOnClickListener(new OnConvertViewClickListener(convertView, position) {

            @Override
            public void onClickCallBack(View registedView, View rootView,int position) {
                //Toast.makeText(mContext, "ViewHolder: " +  ((ViewHolder)rootView.getTag()).toString(), Toast.LENGTH_SHORT).show();
                ViewHolder viewHolder = (ViewHolder)rootView.getTag();
                if(null != viewHolder) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("resCode", devicesBean.getDeviceCoding());
                    bundle.putInt("resSubType", devicesBean.getCameraType());
                    intent.putExtras(bundle);
                    intent.setClass(mContext, VideoPlayActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "errorException:  ViewHolder is null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.button_2.setOnClickListener(new OnConvertViewClickListener(convertView, position) {
            @Override
            public void onClickCallBack(View registedView, View rootView,int position) {

                ViewHolder viewHolder = (ViewHolder)rootView.getTag();
                if (null != viewHolder) {
                    listener.viewOnClickListener(devicesBean,true);
                }
            }
        });

        final Calendar sCalendar = Calendar.getInstance();
        holder.button_3.setOnClickListener(new OnConvertViewClickListener(convertView, position)  {

            @Override
            public void onClickCallBack(View registedView, View rootView,int position) {
                ViewHolder viewHolder = (ViewHolder)rootView.getTag();
                if (null != viewHolder) {
                    listener.viewOnClickListener(devicesBean, false);
                }
            }
        });

        holder.gotoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到地图
                Intent intent = new Intent();
                intent.putExtra("type",TYPE_CAMERA);
                intent.putExtra("devices",mData);
                intent.putExtra("position",position);
                intent.setClass(mContext,VideoMonitorMapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    /**
     * 固定摄像机：1; 云台摄像机：2; 高清固定摄像机：3; 高清云台摄像机：4; 车载摄像机：5; 不可控标清摄像机：6; 不可控高清摄像机：7;
     * @param textView
     * @param resSubType
     */
    private void setCameraType(TextView textView,int resSubType) {
        if (1 == resSubType) {
            textView.setText(R.string.camera_type_1);
        } else if (2 == resSubType) {
            textView.setText(R.string.camera_type_2);
        } else if (3 == resSubType) {
            textView.setText(R.string.camera_type_3);
        } else if (4 == resSubType) {
            textView.setText(R.string.camera_type_4);
        } else if (5 == resSubType) {
            textView.setText(R.string.camera_type_5);
        } else if (6 == resSubType) {
            textView.setText(R.string.camera_type_6);
        } else if (7 == resSubType) {
            textView.setText(R.string.camera_type_7);
        } else {
            textView.setText("");
        }
    }


    public final class ViewHolder{
        public TextView resCodeTv;//资源编码
        public TextView installTime;//资源子类型
        public TextView resSubTypeTv;//资源子类型
        public TextView resNameTv;//资源名称
        public ImageView isOnlineTv;//是否在线
        public int resType;
        public int resSubType;
        public Boolean isOnline;
        public Boolean isShared;

        public LinearLayout gotoMap ;

        public LinearLayout button_1;//视频监控Btn
        public LinearLayout button_2;//环境监控
        public LinearLayout button_3 ;//三方协同

    }


}
