package com.isoftstone.smartsite.model.main.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.common.widget.AlertView;
import com.isoftstone.smartsite.http.DevicesBean;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.VideoMonitorBean;
import com.isoftstone.smartsite.model.video.VideoRePlayActivity;
import com.isoftstone.smartsite.model.video.VideoRePlayListActivity;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.uniview.airimos.listener.OnQueryReplayListener;
import com.uniview.airimos.listener.OnQueryResourceListener;
import com.uniview.airimos.manager.ServiceManager;
import com.uniview.airimos.obj.QueryCondition;
import com.uniview.airimos.obj.RecordInfo;
import com.uniview.airimos.obj.ResourceInfo;
import com.uniview.airimos.parameter.QueryReplayParam;
import com.uniview.airimos.parameter.QueryResourceParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by gone on 2017/10/17.
 * modifed by zhangyinfu on 2017/10/19
 */

public class VideoMonitoringActivity extends Activity implements VideoMonitorAdapter.AdapterViewOnClickListener,View.OnClickListener{
    private static final String TAG = "VideoMonitoringActivity";
    public HttpPost mHttpPost = new HttpPost();
    private ListView mListView = null;
    private Context mContext;
    private DevicesBean mDevicesBean;

    private ImageButton mImageView_back = null;
    private ImageButton mImageView_serch = null;
    private View oneIconLayout = null;
    private View searchLayout = null;
    private ImageButton mSearch_back = null;
    private TextView mSearch_cancel = null;
    private TextView mtitleTextView = null;
    private ImageButton search_btn_search = null;
    private EditText search_edit_text = null;

    private static final int  HANDLER_GETDIVICES_START = 1;
    private static  final int  HANDLER_GETDIVICES_END = 2;
    private ArrayList<DevicesBean> list = null;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_GETDIVICES_START: {
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            list =  mHttpPost.getDevices("1","","","");
                            mHandler.sendEmptyMessage(HANDLER_GETDIVICES_END);
                        }
                    };
                    thread.start();
                }
                break;
                case HANDLER_GETDIVICES_END:{
                     setListViewData();
                }
                break;
            }
        }
    };
    private void setListViewData(){
        if( list!=null ){
            VideoMonitorAdapter adapter = new VideoMonitorAdapter(VideoMonitoringActivity.this);
            adapter.setData(list);
            mListView.setAdapter(adapter);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mContext = getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videomonitoring);
        init();
        //查询设备列表,并填充ListView数据
        //setResourceDate();
        mHandler.sendEmptyMessage(HANDLER_GETDIVICES_START);
    }

    private void init(){

        mImageView_back = (ImageButton)findViewById(R.id.btn_back);
        mImageView_serch = (ImageButton)findViewById(R.id.btn_icon);
        oneIconLayout = (View)findViewById(R.id.one_icon);
        searchLayout = (View)findViewById(R.id.serch);
        mSearch_back = (ImageButton)findViewById(R.id.search_btn_back);
        mSearch_cancel = (TextView)findViewById(R.id.search_btn_icon_right);
        mtitleTextView = (TextView) findViewById(R.id.toolbar_title);
        search_btn_search = (ImageButton)findViewById(R.id.search_btn_search);
        search_edit_text = (EditText)findViewById(R.id.search_edit_text);
        mtitleTextView.setText("视频监控");
        mImageView_serch.setImageResource(R.drawable.search);
        mImageView_back.setOnClickListener(this);
        mImageView_serch.setOnClickListener(this);
        mSearch_back.setOnClickListener(this);
        mSearch_cancel.setOnClickListener(this);
        search_btn_search.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.list);

        mImageView_serch.setVisibility(View.INVISIBLE);
    }

    @Override
    public void viewOnClickListener(DevicesBean devicesBean, boolean isFormOneType) {
        mDevicesBean = devicesBean;
        if (isFormOneType) {
            //进入历史摄像界面
            startRePlayListActivity();
        } else {
            //打开系统相册浏览照片  
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }



    private void  startRePlayListActivity () {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = formatter.format(now) + " 00:00:00";
        String endTime = formatter2.format(now);

        /*Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putString("resCode", mDevicesBean.getDeviceCoding());
        bundle.putInt("resSubType", mDevicesBean.getCameraType());
        bundle.putString("resName", mDevicesBean.getDeviceName());
        boolean isOnLine = mDevicesBean.getDeviceStatus().equals("0");
        bundle.putBoolean("isOnline", isOnLine);

        bundle.putString("beginTime", beginTime);
        bundle.putString("endTime", endTime);
        //Toast.makeText(mContext, "ViewHolder: " +  ((ViewHolder)rootView.getTag()).name.getText().toString(), Toast.LENGTH_SHORT).show();
        intent.putExtras(bundle);
        intent.setClass(mContext, VideoRePlayListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);*/
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("resCode", mDevicesBean.getDeviceCoding());
        bundle.putString("beginTime", beginTime);
        bundle.putString("endTime", endTime);
        bundle.putString("resSubType", mDevicesBean.getCameraType()+"");
        bundle.putString("resName", mDevicesBean.getDeviceName());
        bundle.putBoolean("isOnline", mDevicesBean.getDeviceStatus().equals("0"));
        bundle.putInt("position", 0);
        intent.putExtras(bundle);
        intent.setClass(mContext, VideoRePlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_back:{
                finish();
            }
            break;
            case R.id.btn_icon:{
                oneIconLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
            }
            break;
            case R.id.search_btn_back:{
                finish();
            }
            break;
            case R.id.search_btn_icon_right:{
                oneIconLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
            }
            break;
            case R.id.search_btn_search:{
                String serch = search_edit_text.getText().toString();
                Toast.makeText(getBaseContext(),"搜索内容为:"+serch,Toast.LENGTH_LONG).show();
            }
            break;
        }
    }
}
