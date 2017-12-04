package com.isoftstone.smartsite.model.tripartite.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.common.widget.PullToRefreshListView;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.patrolreport.PatrolBean;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.model.message.data.MsgData;
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity;
import com.isoftstone.smartsite.model.tripartite.adapter.InspectReportAdapter;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by yanyongjun on 2017/10/16.
 * 巡查报告fragment
 */

public class InspectReportMainFragment extends BaseFragment {
    public static final String ITEM_TITLE = "lab_title";
    public static final String ITEM_NAME = "lab_name";
    public static final String ITEM_TIME = "lab_time";
    public static final String ITEM_COMPANY = "lab_company";
    public static final String ITEM_STATS = "lab_status";

    private TripartiteActivity mActivity = null;
    private PullToRefreshListView mListView = null;
    private ArrayList<ReportData> mDatas = new ArrayList<>();
    private BaseAdapter mAdapter = null;
    private HttpPost mHttpPost = null;
    private String mAccountName = "";
    //分页开始
    private int mCurPageNum = -1;
    public boolean isLoading = false;
/*    private Button mAdd = null;*/

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_inspect_report;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        mActivity = (TripartiteActivity) getActivity();
        mHttpPost = new HttpPost();
        init();
    }

    public void onDataSetChanged() {
        Log.e(TAG, "onDataSetChanged");
        if (!mHttpPost.mLoginBean.getmUserBean().getmPermission().isM_PATROL_REPORT()) {
            return;
        }
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    mDatas.clear();
//                    if (mAccountName == null || mAccountName.equals("")) {
//                        mAccountName = mHttpPost.mLoginBean.getmName();
//                    }
//                    ArrayList<ReportData> sourceData = mActivity.getDatas();
//                    for (ReportData temp : sourceData) {
//                        mDatas.add(temp);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                mAdapter.notifyDataSetChanged();
//            }
//        }.execute();
    }

    //分页代码
    private void init() {
        //初始化Listview
        mListView = (PullToRefreshListView) mActivity.findViewById(R.id.listview);
        PullToRefreshListView.OnRefreshListener listener = new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG,"yanlog onRefresh:"+isLoading);
                if(isLoading){
                    //mListView.onRefreshComplete();
                }else{
                    mCurPageNum = -1;
                    new QueryMsgTask(true).execute();
                }
            }

            @Override
            public void onLoadMore() {
                Log.e(TAG,"yanlog onLoadMore:"+isLoading);
                if(isLoading){
                   // mListView.onLoadMoreComplete();
                }else{
                    new QueryMsgTask(false).execute();
                }
            }
        };
        mListView.setOnRefreshListener(listener);
        mAdapter = new InspectReportAdapter(mActivity, mDatas);
        mListView.setAdapter(mAdapter);
        new QueryMsgTask(true).execute();
    }

    private class QueryMsgTask extends AsyncTask<String, Integer, String> {
        private boolean mIsReLoad = true;
        public QueryMsgTask(boolean isreLoad){
            mIsReLoad = isreLoad;
        }

        @Override
        protected void onPreExecute() {
            Log.e(TAG,"yanlog query begin:"+mCurPageNum);
            super.onPreExecute();
            isLoading = true;
        }

        @Override
        protected String doInBackground(String... params) {
            PageableBean page = new PageableBean();
            page.setSize(BaseActivity.DEFAULT_PAGE_SIZE);
            page.setPage(mCurPageNum + 1 + "");
            ArrayList<PatrolBean> msgs = mHttpPost.getPatrolReportList("", page);
            if (msgs == null) {
                return "";
            }
            Collections.sort(msgs, new Comparator<PatrolBean>() {
                @Override
                public int compare(PatrolBean o1, PatrolBean o2) {
                    try {
                        Date date1 = MsgData.format5.parse(o1.getDate());
                        Date date2 = MsgData.format5.parse(o2.getDate());
                        return date2.compareTo(date1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
            if(mIsReLoad){
                mDatas.clear();
            }
            if(msgs != null && msgs.size() > 0){
                mCurPageNum++;
            }
            for (PatrolBean temp : msgs) {
                ReportData reportData = new ReportData(temp);
                Log.e(TAG, "reportData:" + reportData);
                mDatas.add(reportData);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG,"yanlog queryEnd:"+mCurPageNum);
            isLoading = false;
            if (mDatas == null || mDatas.size() == 0) {
                ToastUtils.showShort("未获取到数据");
            }
            mListView.onLoadMoreComplete();
            mListView.onRefreshComplete();
            mAdapter.notifyDataSetChanged();
        }
    }
    //分页代码结束
}
