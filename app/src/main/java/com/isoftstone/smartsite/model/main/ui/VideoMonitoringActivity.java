package com.isoftstone.smartsite.model.main.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.video.DevicesBean;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.model.main.adapter.VideoMonitorAdapter;
import com.isoftstone.smartsite.model.video.VideoPlayActivity;
import com.isoftstone.smartsite.model.video.VideoRePlayActivity;
import com.isoftstone.smartsite.model.video.bean.AlbumInfo;
import com.isoftstone.smartsite.model.video.bean.PhotoInfo;
import com.isoftstone.smartsite.model.video.bean.PhotoList;
import com.isoftstone.smartsite.model.video.utils.ThumbnailsUtil;
import com.isoftstone.smartsite.utils.FilesUtils;
import com.isoftstone.smartsite.model.video.SnapPicturesActivity;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gone on 2017/10/17.
 * modifed by zhangyinfu on 2017/10/19
 */

public class VideoMonitoringActivity extends BaseActivity implements VideoMonitorAdapter.AdapterViewOnClickListener, View.OnClickListener{
    private static final String TAG = "VideoMonitoringActivity";
    public HttpPost mHttpPost = new HttpPost();

    /*请求识别码 实时视频*/
    public static final int REQUEST_FOR_ONE_TYPE_CODE = 1;
    /* 请求识别码 历史监控*/
    public static final int REQUEST_FOR_TWO_TYPE_CODE = 2;
    /* 请求识别码 抓拍记录*/
    public static final int REQUEST_FOR_THREE_TYPE_CODE = 3;
	
    private List<PhotoInfo> mlistPhotoInfo = new ArrayList<PhotoInfo>();
    private List<AlbumInfo> mListImageInfo = new ArrayList<AlbumInfo>();

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
            closeDlg();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mContext = getApplicationContext();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_videomonitoring;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        init();
        //查询设备列表,并填充ListView数据
        //setResourceDate();
        showDlg("正在获取设备列表");
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
    public void viewOnClickListener(DevicesBean devicesBean, int requestType) {
        mDevicesBean = devicesBean;

        if (requestType == REQUEST_FOR_ONE_TYPE_CODE) {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("resCode", devicesBean.getDeviceCoding());
            bundle.putInt("resSubType", devicesBean.getCameraType());
            intent.putExtras(bundle);
            intent.setClass(mContext, VideoPlayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (requestType == REQUEST_FOR_TWO_TYPE_CODE) {

            //进入历史摄像界面
            startRePlayListActivity();

        } else if (requestType == REQUEST_FOR_THREE_TYPE_CODE) {
            mListImageInfo.clear();
            mlistPhotoInfo.clear();
            new ImageAsyncTask().execute();
        }

    }



    private void  startRePlayListActivity () {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = formatter.format(now) + " 00:00:00";
        String endTime = formatter2.format(now);

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

    private class ImageAsyncTask extends AsyncTask<Void, Void, Object> {

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            //Toast.makeText(context,"开始执行",Toast.LENGTH_SHORT).show();
            showDlg("正在获取" + mDevicesBean.getDeviceCoding() + "设备抓拍记录");
        }

        @Override
        protected Object doInBackground(Void... params) {
            //获取缩略图
            ThumbnailsUtil.clear();
            ContentResolver sContentResolver = mContext.getContentResolver();
            String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA };
            Cursor cur = sContentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

            if (cur!=null&&cur.moveToFirst()) {
                int image_id;
                String image_path;
                int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                do {
                    image_id = cur.getInt(image_idColumn);
                    image_path = cur.getString(dataColumn);
                    ThumbnailsUtil.put(image_id, "file://"+image_path);
                } while (cur.moveToNext());
            }
            //获取原图
            Cursor cursor = sContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_modified DESC");
            String _path="_data";
            String _album="bucket_display_name";
            HashMap<String,AlbumInfo> myhash = new HashMap<String, AlbumInfo>();
            AlbumInfo albumInfo = null;
            PhotoInfo photoInfo = null;
            if (cursor!=null&&cursor.moveToFirst())
            {
                do{
                    int index = 0;
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String path = cursor.getString(cursor.getColumnIndex(_path));
                    String album = cursor.getString(cursor.getColumnIndex(_album));
                    List<PhotoInfo> stringList = new ArrayList<PhotoInfo>();
                    photoInfo = new PhotoInfo();
                    if(myhash.containsKey(album)){
                        albumInfo = myhash.remove(album);
                        if(mListImageInfo.contains(albumInfo)) {
                            index = mListImageInfo.indexOf(albumInfo);
                        }
                        photoInfo.setImage_id(_id);
                        photoInfo.setPath_file("file://"+path);
                        photoInfo.setPath_absolute(path);
                        albumInfo.getList().add(photoInfo);
                        mListImageInfo.set(index, albumInfo);
                        myhash.put(album, albumInfo);
                        //Log.i("zyf", albumInfo.toString() + "\n" + album);
                    }else{
                        albumInfo = new AlbumInfo();
                        stringList.clear();
                        photoInfo.setImage_id(_id);
                        photoInfo.setPath_file("file://"+path);
                        photoInfo.setPath_absolute(path);
                        stringList.add(photoInfo);
                        albumInfo.setImage_id(_id);
                        albumInfo.setPath_file("file://"+path);
                        albumInfo.setPath_absolute(path);
                        albumInfo.setName_album(album);
                        albumInfo.setList(stringList);
                        mListImageInfo.add(albumInfo);
                        myhash.put(album, albumInfo);
                        //Log.i("zyf", albumInfo.toString() + "\n" + album);
                    }
                }while (cursor.moveToNext());
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            //Log.i("zzz", FilesUtils.SNATCH_PATH + mDevicesBean.getDeviceCoding() + "/");
            for (int i=0; i<mListImageInfo.size(); i++) {
                if (mListImageInfo.get(i).getPath_absolute().contains(FilesUtils.SNATCH_PATH + mDevicesBean.getDeviceCoding() + "/")) {
                    mlistPhotoInfo.addAll(mListImageInfo.get(i).getList());
                }
            }

            closeDlg();

            if (mlistPhotoInfo.size() <= 0 ) {
                ToastUtils.showShort(getText(R.string.snatch_photo_size_0_toast).toString());
                return;
            } else {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                PhotoList photo = new PhotoList();
                photo.setList(mlistPhotoInfo);
                bundle.putSerializable("list", photo);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(mContext, SnapPicturesActivity.class);
                mContext.startActivity(intent);
            }
        }
    }

}
