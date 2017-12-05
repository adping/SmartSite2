package com.isoftstone.smartsite.model.main.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.aqi.DataQueryVoBean;
import com.isoftstone.smartsite.http.HttpPost;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/21.
 */

public class PMDevicesListActivity extends Activity implements View.OnClickListener {

    private ListView mListView = null;
    public static  final  int HANDLER_GET_DATA_START = 1;
    public static  final  int HANDLER_GET_DATA_END = 2;
    private HttpPost mHttpPost = new HttpPost();
    private ArrayList<DataQueryVoBean> mList = null;

    private ImageButton mImageView_back = null;
    private ImageButton mImageView_serch = null;
    private View oneIconLayout = null;
    private View searchLayout = null;
    private ImageButton mSearch_back = null;
    private TextView mSearch_cancel = null;
    private TextView mtitleTextView = null;
    private ImageButton search_btn_search = null;
    private EditText search_edit_text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmdeviceslist);

        init();
        mHandler.sendEmptyMessage(HANDLER_GET_DATA_START);
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
        mtitleTextView.setText("设备列表");
        mImageView_serch.setImageResource(R.drawable.search);
        mImageView_back.setOnClickListener(this);
        mImageView_serch.setOnClickListener(this);
        mSearch_back.setOnClickListener(this);
        mSearch_cancel.setOnClickListener(this);
        search_btn_search.setOnClickListener(this);


        mListView = (ListView)findViewById(R.id.listview);

        mImageView_serch.setVisibility(View.INVISIBLE);
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_GET_DATA_START:{
                     Thread thread = new Thread(){
                         @Override
                         public void run() {
                             getDevices();
                         }
                     };
                     thread.start();
                }
                break;
                case  HANDLER_GET_DATA_END:{
                    setmListViewData();
                }
                break;
            }
        }
    };

    private  void getDevices(){
//        mList = mHttpPost.getDevices("0","","","");
        mList = mHttpPost.onePMDevicesDataList("","0","","");
        mHandler.sendEmptyMessage(HANDLER_GET_DATA_END);
    }

    private void setmListViewData(){
        PMDevicesListAdapter adapter = new PMDevicesListAdapter(getBaseContext());
        adapter.setData(mList);
        mListView.setAdapter(adapter);
    }

    @Override
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
                Toast.makeText(getBaseContext(),"搜索内容为:"+serch,2000).show();
            }
               break;
        }
    }
}
