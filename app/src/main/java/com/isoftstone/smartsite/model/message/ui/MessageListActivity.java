package com.isoftstone.smartsite.model.message.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.message.MessageBean;
import com.isoftstone.smartsite.jpush.MyReceiver;
import com.isoftstone.smartsite.model.message.adapter.MsgListAdapter;
import com.isoftstone.smartsite.model.message.data.MsgData;
import com.isoftstone.smartsite.utils.MsgUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by yanyongjun on 2017/10/15.
 */

public class MessageListActivity extends BaseActivity {
    //listview中各item项的名称
    public static final String ITEM_DATE = "lab_time";
    public static final String ITEM_TITLE = "lab_title";
    public static final String ITEM_DETAILS = "lab_details";

    private Activity mActivity = null;
    private ListView mListView = null;
    private ArrayList<MsgData> mDatas = new ArrayList<>();

    private HttpPost mHttpPost = null;
    private BaseAdapter mAdapter = null;

    private String mQueryMsgType = "1|";

    private static final boolean isDebug = false;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_msg_vcr;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        mActivity = this;
        init();
    }

    private void init() {
        mListView = (ListView) mActivity.findViewById(R.id.listview_frag_vcr);
        mAdapter = new MsgListAdapter(mActivity, mDatas);
        mListView.setAdapter(mAdapter);
        mHttpPost = new HttpPost();

        Intent intent = getIntent();
        try {
            mQueryMsgType = intent.getStringExtra("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initTitleName();
    }

    private void initTitleName() {
        TextView title = (TextView) findViewById(R.id.lab_title_name);
        if (mQueryMsgType.equals(MyReceiver.SEARCH_CODE_ENVIRON)) {
            title.setText("环境监控消息");
        } else if (mQueryMsgType.equals(MyReceiver.SEARCH_CODE_VEDIO)) {
            title.setText("视频监控消息");
        } else if (mQueryMsgType.equals(MyReceiver.SEARCH_CODE_THREE_PARTY)) {
            title.setText("三方协同消息");
        } else if (mQueryMsgType.equals(MyReceiver.SEARCH_CODE_DIRTCAR)) {
            title.setText("渣土车监控消息");
        } else if (mQueryMsgType.equals(MyReceiver.SEARCH_CODE_TASK)) {
            title.setText("巡查任务消息");
        } else if (mQueryMsgType.equals(MyReceiver.SEARCH_CODE_PLAN)) {
            title.setText("巡查计划消息");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new QueryMsgTask().execute();
        new ReadMsgTask().execute();
    }


    private class QueryMsgTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            ArrayList<MessageBean> msgs = mHttpPost.getMessage("", "", "", mQueryMsgType);
            if (msgs != null) {
                Collections.sort(msgs, new Comparator<MessageBean>() {
                    @Override
                    public int compare(MessageBean o1, MessageBean o2) {
                        try {
                            Date date1 = MsgData.format5.parse(o1.getUpdateTime());
                            Date date2 = MsgData.format5.parse(o2.getUpdateTime());
                            return date2.compareTo(date1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                MsgUtils.toMsgData(mDatas, msgs);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "defernotifyDatasetChanged");
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ReadMsgTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (MsgData temp : mDatas) {
                if (temp.getStatus() == MsgData.STATUS_UNREAD) {
                    mHttpPost.readMessage(temp.getId());
                }
            }
            return null;
        }
    }
}
