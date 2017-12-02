package com.isoftstone.smartsite.model.message.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.message.MessageBean;
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

public class EnvironActivity extends BaseActivity {
    //listview中各项的名称
    public static final String ITEM_DATE = "lab_time";
    public static final String ITEM_TITLE = "lab_title";
    public static final String ITEM_DETAILS = "lab_details";

    private Activity mActivity = null;
    private ListView mListView = null;
    private ArrayList<MsgData> mDatas = new ArrayList<>();

    private HttpPost mHttpPost = null;
    private QueryMsgTask mTask = new QueryMsgTask();
    private BaseAdapter mAdapter = null;

    private static final boolean isDebug = false;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_msg_environ;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        mActivity = this;
        init();
    }

    private void init() {
        mListView = (ListView) mActivity.findViewById(R.id.listview_frag_environ);
        mAdapter = new MsgListAdapter(mActivity, mDatas);
        mListView.setAdapter(mAdapter);
        mHttpPost = new HttpPost();
    }

    private class QueryMsgTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            ArrayList<MessageBean> msgs = mHttpPost.getMessage("", "", "", "1");
            if(msgs != null){
                if (isDebug) {
                    insertDebugData(msgs);
                }
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

    @Override
    protected void onResume() {
        super.onResume();

        mTask.execute();
        new ReadMsgTask().execute();
    }

    private class ReadMsgTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (MsgData temp : mDatas) {
                if(temp.getStatus() == MsgData.STATUS_UNREAD) {
                    mHttpPost.readMessage(temp.getId());
                }
            }
            return null;
        }
    }

    private void insertDebugData(ArrayList<MessageBean> datas) {
        MessageBean data = new MessageBean();
        data.setInfoId("1");
        data.setUpdateTime("2017-10-3 13:35:00");
        data.setTitle("这是测试消息1");
        data.setContent("这是测试消息的内容");
        data.setStatus(MsgData.STATUS_READ);
        datas.add(data);

        data = new MessageBean();
        data.setInfoId("2");
        data.setUpdateTime("2017-10-3 13:35:00");
        data.setTitle("这是测试消息2");
        data.setContent("这是测试消息的内容");
        data.setStatus(MsgData.STATUS_READ);
        datas.add(data);

        data = new MessageBean();
        data.setInfoId("3");
        data.setUpdateTime("2016-10-3 13:35:00");
        data.setTitle("这是测试消息3");
        data.setContent("这是测试消息的内容");
        datas.add(data);

        data = new MessageBean();
        data.setInfoId("4");
        data.setUpdateTime("2017-11-3 13:35:00");
        data.setTitle("这是测试消息4");
        data.setContent("这是测试消息的内容");
        datas.add(data);

        data = new MessageBean();
        data.setInfoId("5");
        data.setUpdateTime("2017-11-2 13:35:00");
        data.setTitle("这是测试消息5");
        data.setContent("这是测试消息的内容");
        datas.add(data);

        data = new MessageBean();
        data.setInfoId("6");
        data.setUpdateTime("2005-10-3 13:35:00");
        data.setTitle("这是测试消息6");
        data.setContent("这是测试消息的内容");
        data.setStatus(MsgData.STATUS_UNREAD);
        datas.add(data);

        data = new MessageBean();
        data.setInfoId("7");
        data.setUpdateTime("2017-10-2 11:35:00");
        data.setTitle("这是测试消息7");
        data.setContent("这是测试消息的内容");
        datas.add(data);

        data = new MessageBean();
        data.setInfoId("8");
        data.setUpdateTime("2015-9-3 13:35:00");
        data.setTitle("这是测试消息8");
        data.setContent("这是测试消息的内容");
        datas.add(data);

        data = new MessageBean();
        data.setInfoId("9");
        data.setUpdateTime("2014-10-3 13:35:00");
        data.setTitle("这是测试消息9");
        data.setContent("这是测试消息的内容");
        datas.add(data);

        data = new MessageBean();
        data.setInfoId("10");
        data.setUpdateTime("2014-10-1 13:35:00");
        data.setTitle("这是测试消息10");
        data.setContent("这是测试消息的内容");
        datas.add(data);
    }
}
