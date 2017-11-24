package com.isoftstone.smartsite.model.inspectplan.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.DevicesBean;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBean;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBeanPage;
import com.isoftstone.smartsite.model.inspectplan.adapter.ApprovalPendingInspectPlansAdapter;
import com.isoftstone.smartsite.model.inspectplan.bean.InspectPlanBean;
import com.isoftstone.smartsite.model.system.ui.OpinionFeedbackActivity;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zhang on 2017/11/18.
 */

public class ApprovalPendingInspectPlansActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "zzz_InspectPlans";
    private ListView mListView = null;
    private ArrayList<InspectPlanBean> mListData = new ArrayList<InspectPlanBean>();
    private HttpPost mHttpPost;

    private static final int  HANDLER_APPROVAL_PENDING_INSPECT_PLANS_START = 1;
    private static  final int  HANDLER_APPROVAL_PENDING_INSPECT_PLANS_END = 2;

    /* 查询请求识别码 查询成功*/
    private static final int QUERY_RESULTS_SUCCESSFUL_CODE = 1;
    /* 查询请求识别码 查询失败*/
    private static final int QUERY_RESULTS_FAILED_CODE = 2;
    /* 查询请求识别码 查询异常*/
    private static final int QUERY_RESULTS_EXCEPTION_CODE = 3;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_APPROVAL_PENDING_INSPECT_PLANS_START: {
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            //listData =  mHttpPost.getDevices("1","","","");getPlanPaging
                            try {
                                PatrolPlanBean patrolPlanBean = new PatrolPlanBean();
                                PageableBean pageableBean = new PageableBean();
                                PatrolPlanBeanPage patrolPlanBeanPage = mHttpPost.getPlanPaging(patrolPlanBean,pageableBean);
                                ArrayList<PatrolPlanBean> arrayList = patrolPlanBeanPage.getContent();
                                Log.i("zzz","AAAAAAAAAAAAAAAAAAAAA     arrayList = " + arrayList);
                                if (arrayList != null) {
                                    for (int i=0; i< arrayList.size(); i++) {
                                        Log.i("zzz","arrayList.size() = " + arrayList.size() + "  & " + i + "  && " + arrayList.get(i).toString());
                                        InspectPlanBean inspectPlanBean = new InspectPlanBean();
                                        inspectPlanBean.setUserId(arrayList.get(i).getId());
                                        inspectPlanBean.setTaskName(arrayList.get(i).getTitle());
                                        inspectPlanBean.setTaskTimeStart(arrayList.get(i).getStart());
                                        inspectPlanBean.setTaskTimeEnd(arrayList.get(i).getEndDate());
                                        inspectPlanBean.setUserName(arrayList.get(i).getCreator().getName());
                                        inspectPlanBean.setUserCompany(mHttpPost.getCompanyNameByid(Integer.parseInt(arrayList.get(i).getCreator().getDepartmentId())));
                                        inspectPlanBean.setTaskStatus(arrayList.get(i).getStatus());
                                        inspectPlanBean.setBaseUserBean(arrayList.get(i).getCreator());
                                        //inspectPlanBean.setAddress(arrayList.get(i));
                                        mListData.add(inspectPlanBean);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG,"e : " + e.getMessage());
                            }

                            /**for (int i = 0; i < 3; i++) {
                                InspectPlanBean inspectPlanBean = new InspectPlanBean();
                                inspectPlanBean.setUserId(i);
                                inspectPlanBean.setTaskName("东湖高新巡查报告");
                                inspectPlanBean.setTaskTimeStart("2017-11-20");
                                inspectPlanBean.setTaskTimeEnd("2017-11-20");
                                inspectPlanBean.setUserName("李程辉" + i);
                                inspectPlanBean.setUserCompany("湖北怡瑞有限公司" + i);
                                inspectPlanBean.setTaskStatus(i);
                                inspectPlanBean.setAddress("东湖高新地i点" + i);

                                mListData.add(inspectPlanBean);
                            }*/
                            mHandler.sendEmptyMessage(HANDLER_APPROVAL_PENDING_INSPECT_PLANS_END);
                        }
                    };
                    thread.start();
                }
                break;
                case HANDLER_APPROVAL_PENDING_INSPECT_PLANS_END:{
                    setListViewData();
                }
                break;
            }
        }
    };

    private void setListViewData() {
        ApprovalPendingInspectPlansAdapter adapter = new ApprovalPendingInspectPlansAdapter(ApprovalPendingInspectPlansActivity.this, mListData);
        mListView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_approval_pending_inspect_plans;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initToolbar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        queryDataTask updateTextTask = new queryDataTask(this);
        updateTextTask.execute();
    }

    private void initView() {
        mHttpPost = new HttpPost();
        mListView = (ListView) findViewById(R.id.list_view);
        //mHandler.sendEmptyMessage(HANDLER_APPROVAL_PENDING_INSPECT_PLANS_START);
    }

    private void initToolbar(){
        TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.approval_pending_inspect_plans_title);

        findViewById(R.id.btn_back).setOnClickListener(ApprovalPendingInspectPlansActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                ApprovalPendingInspectPlansActivity.this.finish();
                break;
            default:
                break;
        }
    }

    class queryDataTask extends AsyncTask<Void,Void,Integer> {
        private Context context;
        queryDataTask(Context context) {
            this.context = context;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            //Toast.makeText(context,"开始执行",Toast.LENGTH_SHORT).show();
            if (mListView != null) {
                mListView.setAdapter(null);
            }
            showDlg("正在获取列表");
        }
        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {

            if(mListData != null) {
                mListData.clear();
            }

            try {
                PatrolPlanBean patrolPlanBean = new PatrolPlanBean();
                PageableBean pageableBean = new PageableBean();
                PatrolPlanBeanPage patrolPlanBeanPage = mHttpPost.getPlanPaging(patrolPlanBean,pageableBean);
                ArrayList<PatrolPlanBean> arrayList = patrolPlanBeanPage.getContent();
                Log.i("zzz","AAAAAAAAAAAAAAAAAAAAA     arrayList = " + arrayList);
                if (arrayList == null || arrayList.size() == 0) {
                    return QUERY_RESULTS_FAILED_CODE;
                }

                for (int i=0; i< arrayList.size(); i++) {
                    Log.i("zzz","arrayList.size() = " + arrayList.size() + "  & " + i + "  && " + arrayList.get(i).toString());
                    InspectPlanBean inspectPlanBean = new InspectPlanBean();
                    inspectPlanBean.setUserId(arrayList.get(i).getId());
                    inspectPlanBean.setTaskName(arrayList.get(i).getTitle());
                    inspectPlanBean.setTaskTimeStart(arrayList.get(i).getStart());
                    inspectPlanBean.setTaskTimeEnd(arrayList.get(i).getEndDate());
                    inspectPlanBean.setUserName(arrayList.get(i).getCreator().getName());
                    inspectPlanBean.setUserCompany(mHttpPost.getCompanyNameByid(Integer.parseInt(arrayList.get(i).getCreator().getDepartmentId())));
                    inspectPlanBean.setTaskStatus(arrayList.get(i).getStatus());
                    inspectPlanBean.setBaseUserBean(arrayList.get(i).getCreator());
                    //inspectPlanBean.setAddress(arrayList.get(i));
                    mListData.add(inspectPlanBean);
                }

            } catch (Exception e) {
                Log.e(TAG,"e : " + e.getMessage());
                return QUERY_RESULTS_EXCEPTION_CODE;
            }

            return QUERY_RESULTS_SUCCESSFUL_CODE;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer resultsCode) {
            super.onPostExecute(resultsCode);
            closeDlg();
            //Toast.makeText(context,"执行完毕",Toast.LENGTH_SHORT).show();
            if (resultsCode == QUERY_RESULTS_SUCCESSFUL_CODE) {
                setListViewData();
            } else if (resultsCode == QUERY_RESULTS_FAILED_CODE){
                ToastUtils.showLong("获取列表为空。");
            } else if (resultsCode == QUERY_RESULTS_EXCEPTION_CODE) {
                ToastUtils.showLong("获取列表失败，请稍后重试");
            }
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
