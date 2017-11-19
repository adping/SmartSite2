package com.isoftstone.smartsite.model.inspectplan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.DevicesBean;
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

    private ListView mListView = null;
    private ArrayList<InspectPlanBean> mListData = new ArrayList<InspectPlanBean>();

    private static final int  HANDLER_APPROVAL_PENDING_INSPECT_PLANS_START = 1;
    private static  final int  HANDLER_APPROVAL_PENDING_INSPECT_PLANS_END = 2;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_APPROVAL_PENDING_INSPECT_PLANS_START: {
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            //listData =  mHttpPost.getDevices("1","","","");
                            for (int i = 0; i < 3; i++) {
                                InspectPlanBean inspectPlanBean = new InspectPlanBean();
                                inspectPlanBean.setUserId(i);
                                inspectPlanBean.setTaskName("东湖高新巡查报告");
                                inspectPlanBean.setTaskTimeStart(new Date());
                                inspectPlanBean.setTaskTimeEnd(new Date());
                                inspectPlanBean.setUserName("李程辉" + i);
                                inspectPlanBean.setUserCompany("湖北怡瑞有限公司" + i);
                                inspectPlanBean.setTaskStatus(i);
                                inspectPlanBean.setAddress("东湖高新地i点" + i);

                                mListData.add(inspectPlanBean);
                            }
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
        if( mListData != null ){
            ApprovalPendingInspectPlansAdapter adapter = new ApprovalPendingInspectPlansAdapter(ApprovalPendingInspectPlansActivity.this, mListData);
            mListView.setAdapter(adapter);
        }
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

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mHandler.sendEmptyMessage(HANDLER_APPROVAL_PENDING_INSPECT_PLANS_START);
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

}
