package com.isoftstone.smartsite.model.tripartite.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity;
import com.isoftstone.smartsite.model.tripartite.adapter.InspectReportAdapter;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;

import java.util.ArrayList;

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
    private ListView mListView = null;
    private ArrayList<ReportData> mDatas = new ArrayList<>();
    private BaseAdapter mAdapter = null;
    private HttpPost mHttpPost = null;
    private String mAccountName = "";
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
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mDatas.clear();
                    if (mAccountName == null || mAccountName.equals("")) {
                        mAccountName = mHttpPost.mLoginBean.getmName();
                    }
                    ArrayList<ReportData> sourceData = mActivity.getDatas();
                    for (ReportData temp : sourceData) {
                        mDatas.add(temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void init() {
        //初始化Listview
        mListView = (ListView) mActivity.findViewById(R.id.listview);
        mAdapter = new InspectReportAdapter(mActivity, mDatas);
        mListView.setAdapter(mAdapter);
    }

}
