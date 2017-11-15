package com.isoftstone.smartsite.model.tripartite.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.PatrolBean;
import com.isoftstone.smartsite.model.message.data.MsgData;
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity;
import com.isoftstone.smartsite.model.tripartite.adapter.CheckReportAdapter;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;
import com.isoftstone.smartsite.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by yanyongjun on 2017/10/16.
 * 验收报告Fragment
 */

public class CheckReportMainFragment extends BaseFragment {
    public static final String ITEM_TITLE = "lab_title";
    public static final String ITEM_NAME = "lab_name";
    public static final String ITEM_TIME = "lab_time";
    public static final String ITEM_COMPANY = "lab_company";
    public static final String ITEM_STATS = "lab_status";

    private TripartiteActivity mActivity = null;
    private ListView mListView = null;
    private ArrayList<ReportData> mDatas = new ArrayList<>();
    private HttpPost mHttpPost = new HttpPost();
    private String mAccountName = "";
    private BaseAdapter mAdapter = null;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_check_report;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        mActivity = (TripartiteActivity) getActivity();
        init();
    }

    public void onDataSetChanged() {

    }

    private void init() {
        //初始化Listview
        mListView = (ListView) mActivity.findViewById(R.id.listview_check_frag);
        mAdapter = new CheckReportAdapter(mActivity, mDatas);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        new QueryMsgTask().execute();
        super.onResume();
    }

    private class QueryMsgTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            ArrayList<PatrolBean> msgs = mHttpPost.getCheckReportList("");

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
            mDatas.clear();
            for (PatrolBean temp : msgs) {
                ReportData reportData = new ReportData(temp);
                Log.e(TAG, "reportData:" + reportData);
                mDatas.add(reportData);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mAdapter.notifyDataSetChanged();
        }
    }
}
