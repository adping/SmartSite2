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
import com.isoftstone.smartsite.common.widget.PullToRefreshListView;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.message.MessageBean;
import com.isoftstone.smartsite.http.message.MessageBeanPage;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.patrolreport.PatrolBean;
import com.isoftstone.smartsite.jpush.MyReceiver;
import com.isoftstone.smartsite.model.message.adapter.MsgListAdapter;
import com.isoftstone.smartsite.model.message.data.MsgData;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;
import com.isoftstone.smartsite.model.tripartite.fragment.InspectReportMainFragment;
import com.isoftstone.smartsite.utils.MsgUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by yanyongjun on 2017/10/15.
 */

public class MessageListActivity extends BaseActivity {
    private Activity mActivity = null;
    private PullToRefreshListView mListView = null;
    private ArrayList<MsgData> mDatas = new ArrayList<>();

    private HttpPost mHttpPost = null;
    private BaseAdapter mAdapter = null;

    private String mQueryMsgType = MyReceiver.SEARCH_CODE_ENVIRON;
    //分页开始
    private int mCurPageNum = -1;
    public boolean isLoading = false;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_msg_vcr;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        mActivity = this;
        mHttpPost = new HttpPost();
        Intent intent = getIntent();
        try {
            mQueryMsgType = intent.getStringExtra("type");
        } catch (Exception e) {
            e.printStackTrace();
        }

        initListView();
        initTitleName();
    }

    private void initListView() {
        mListView = (PullToRefreshListView) mActivity.findViewById(R.id.listview_message);

        PullToRefreshListView.OnRefreshListener listener = new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "yanlog onRefresh:" + isLoading);
                if (isLoading) {
                    //mListView.onRefreshComplete();
                } else {
                    mCurPageNum = -1;
                    new QueryMsgTask(true).execute();
                }
            }

            @Override
            public void onLoadMore() {
                Log.e(TAG, "yanlog onLoadMore:" + isLoading);
                if (isLoading) {
                    // mListView.onLoadMoreComplete();
                } else {
                    new QueryMsgTask(false).execute();
                }
            }
        };
        mListView.setOnRefreshListener(listener);
        mAdapter = new MsgListAdapter(mActivity, mDatas);
        mListView.setAdapter(mAdapter);
        new QueryMsgTask(true).execute();
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
        new ReadMsgTask().execute();
    }


    private class QueryMsgTask extends AsyncTask<String, Integer, String> {
        private boolean mIsReLoad = true;

        public QueryMsgTask(boolean mIsReLoad) {
            this.mIsReLoad = mIsReLoad;
        }

        @Override
        protected void onPreExecute() {
            isLoading = true;
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                PageableBean page = new PageableBean();
                page.setSize(BaseActivity.DEFAULT_PAGE_SIZE);
                page.setPage(mCurPageNum + 1 + "");
                MessageBeanPage resultPage = mHttpPost.getMessagePage("", "", "", mQueryMsgType, page);
                ArrayList<MessageBean> msgs = resultPage.getContent();
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
                    ArrayList<MsgData> temp = MsgUtils.toMsgData(msgs);
                    if (mIsReLoad) {
                        mDatas.clear();
                    }
                    if (msgs != null && msgs.size() > 0) {
                        mCurPageNum++;
                    }
                    mDatas.addAll(temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            isLoading = false;
            super.onPostExecute(s);
            Log.e(TAG, "defernotifyDatasetChanged");
            if (mDatas == null || mDatas.size() == 0) {
                ToastUtils.showShort("未获取到数据");
            }
            mListView.onLoadMoreComplete();
            mListView.onRefreshComplete();
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
