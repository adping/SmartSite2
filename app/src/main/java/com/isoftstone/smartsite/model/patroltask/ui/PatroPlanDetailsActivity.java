package com.isoftstone.smartsite.model.patroltask.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.common.widget.PullToRefreshListView;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBeanPage;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.model.inspectplan.activity.AddInspectPlan;
import com.isoftstone.smartsite.model.map.ui.ConstructionMontitoringMapActivity;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.widgets.StartworkDialog;

import java.util.ArrayList;

/**
 * Created by 2013020220 on 2017/11/19.
 */

public class PatroPlanDetailsActivity extends BaseActivity implements View.OnClickListener {

    private int mCurPageNum = 0;  //当前页
    private int mFlag; //0 首次进入    1 加载更多    2刷新
    private HttpPost mHttpPost = new HttpPost();
    private PullToRefreshListView listview;
    private ImageButton ibt_back;
    private TextView title;
    public static final int WORK_WAIT_FOR_DOING = 0;
    public static final int WORK_IS_DOING = 1;
    public static final int WORK_HAS_DONE = 2;
    public static final int TIME_TO_INITVIEW = 4;
    public static final int START_TASK_TO_ACTIVITY = 5;
    public static final int GET_TASK_DATA = 6;
    private ImageButton add_plan;
    private ArrayList<PatrolTaskBean> patrolTaskBeanArrayList = new ArrayList<PatrolTaskBean>();
    private PatrolTaskBeanPage patrolTaskBeanPage;
    private StartworkDialog startworkDialog = null;
    private MyBaseAdapter adapter = null;
    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFlag = 0;
        mCurPageNum = 0;
        handler.sendEmptyMessage(GET_TASK_DATA);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_patrol_plan_details;
    }

    private void initViews() {
        View v = findViewById(R.id.title);
        ibt_back = (ImageButton) v.findViewById(R.id.btn_back);
        title = (TextView) v.findViewById(R.id.toolbar_title);
        add_plan = (ImageButton) v.findViewById(R.id.btn_icon);
        add_plan.setImageResource(R.drawable.addreport);
        title.setText("巡查任务");
        listview = (PullToRefreshListView) findViewById(R.id.patrol_detail_list);
        ibt_back.setOnClickListener(this);
        add_plan.setOnClickListener(this);
        listview.setOnItemClickListener(itemClickListener);
        listview.setOnRefreshListener(listviewlistener);
        adapter = new MyBaseAdapter(this, patrolTaskBeanArrayList);
        listview.setAdapter(adapter);
        startworkDialog = new StartworkDialog(this, listener);
    }

    private void setData() {
        if(mFlag == 0){
            patrolTaskBeanArrayList.clear();
        }else if(mFlag == 1){
            listview.onLoadMoreComplete();
        }else if(mFlag == 2){
            patrolTaskBeanArrayList.clear();
            listview.onRefreshComplete();
        }
        if(patrolTaskBeanPage != null){
            ArrayList<PatrolTaskBean> content = patrolTaskBeanPage.getContent();
            if(content!=null){
               for (int i = 0 ; i < content.size() ;i ++){
                   patrolTaskBeanArrayList.add(content.get(i));
               }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private PatrolTaskBean selectPatrolTaskBean;
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectPatrolTaskBean = patrolTaskBeanArrayList.get(i-1);
            switch (selectPatrolTaskBean.getTaskStatus()) {
                case WORK_WAIT_FOR_DOING:
                    startworkDialog.setTaskName(selectPatrolTaskBean.getTaskName());
                    startworkDialog.show();
                    break;
                case WORK_IS_DOING:
                case WORK_HAS_DONE:
                    Bundle bundle = new Bundle();
                    bundle.putLong("taskId", selectPatrolTaskBean.getTaskId());
                    LogUtils.e(TAG,"zw taskId1 : " + selectPatrolTaskBean.getTaskId());
                    openActivity(ConstructionMontitoringMapActivity.class, bundle);
                    break;
                default:
                    break;
            }

        }
    };
    private StartworkDialog.OnStartworkLstener listener = new StartworkDialog.OnStartworkLstener() {
        @Override
        public void onStartwork() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new HttpPost().updateTaskStart(selectPatrolTaskBean.getTaskId(), selectPatrolTaskBean.getTaskName());
                    handler.sendEmptyMessage(START_TASK_TO_ACTIVITY);
                }
            }).start();

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_icon:
                Intent intent = new Intent(PatroPlanDetailsActivity.this, AddInspectPlan.class);
                intent.putExtra("taskType", 1);
                startActivity(intent);
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    PullToRefreshListView.OnRefreshListener listviewlistener = new PullToRefreshListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if(patrolTaskBeanPage.isFirst()){
                mFlag = 2;
                handler.sendEmptyMessage(GET_TASK_DATA);
            }else {
                listview.onRefreshComplete();
            }
        }

        @Override
        public void onLoadMore() {
            if(!patrolTaskBeanPage.isLast()){
                mFlag = 1;
                handler.sendEmptyMessage(GET_TASK_DATA);
            }else{
                listview.onLoadMoreComplete();
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_TASK_DATA: {
                    if(mFlag == 0){
                        showDlg("数据加载中，请稍等");
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PageableBean pageableBean = new PageableBean();
                            pageableBean.setSize(BaseActivity.DEFAULT_PAGE_SIZE);
                            if(mFlag == 0){
                                mCurPageNum = 0;
                            }else if(mFlag == 1){
                                mCurPageNum = mCurPageNum +1 ;
                            }else if(mFlag == 2){
                                mCurPageNum = 0;
                            }
                            pageableBean.setPage(mCurPageNum+"");
                            patrolTaskBeanPage = mHttpPost.getPatrolTaskList(HttpPost.mLoginBean.getmUserBean().getLoginUser().getId(), "", "", "", "", pageableBean);
                            handler.sendEmptyMessage(TIME_TO_INITVIEW);
                        }
                    }).start();
                }
                break;
                case TIME_TO_INITVIEW:{
                    setData();
                    if(mFlag == 0){
                        closeDlg();
                    }
                }
                break;
                case START_TASK_TO_ACTIVITY:{
                    Bundle bundle = new Bundle();
                    bundle.putLong("taskId", selectPatrolTaskBean.getTaskId());
                    openActivity(ConstructionMontitoringMapActivity.class, bundle);
                }
                break;
            }
        }
    };

    public class MyBaseAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<PatrolTaskBean> patrolPlanBeanLists;

        public MyBaseAdapter(Context context, ArrayList<PatrolTaskBean> patrolPlanBeanLists) {
            this.context = context;
            this.patrolPlanBeanLists = patrolPlanBeanLists;
        }

        @Override
        public int getCount() {
            return patrolPlanBeanLists == null ? 0 : patrolPlanBeanLists.size();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.patrol_plan_details, null);
                holder.report = (TextView) convertView.findViewById(R.id.reportNmme);
                holder.report_status = (ImageView) convertView.findViewById(R.id.report_status);
                holder.reportor = (TextView) convertView.findViewById(R.id.reporterName);
                holder.company_name = (TextView) convertView.findViewById(R.id.comparyName);
                holder.data = (TextView) convertView.findViewById(R.id.data);
                holder.work_status = (ImageView) convertView.findViewById(R.id.work_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PatrolTaskBean patrolTaskBean = patrolPlanBeanLists.get(i);
            holder.report.setText(patrolTaskBean.getTaskName());
            int status = patrolTaskBean.getTaskStatus();
            if (status == 0) {
                holder.report_status.setImageResource(R.drawable.daizhixing);
                holder.data.setText(patrolTaskBean.getTaskTimeStart());
                holder.work_status.setImageResource(R.drawable.kaishizhixing);
            } else if (status == 1) {
                holder.report_status.setImageResource(R.drawable.zhixingzhong);
                holder.data.setText(patrolTaskBean.getTaskStart());
                holder.work_status.setImageResource(R.drawable.jinrurenwu);
            } else if (status == 2) {
                holder.report_status.setImageResource(R.drawable.yiwancheng);
                holder.data.setText(patrolTaskBean.getTaskEnd());
                holder.work_status.setImageResource(R.drawable.chakanbaogao);
            }
            holder.reportor.setText(patrolTaskBean.getCreator().name);
            BaseUserBean userBean = patrolTaskBean.getCreator();
            if (userBean != null) {
                String departmentId = userBean.getDepartmentId();
                if (departmentId != null && !departmentId.equals("")) {
                    holder.company_name.setText(new HttpPost().getCompanyNameByid(Integer.parseInt(departmentId)));
                } else {
                    holder.company_name.setText("公司ID未空");
                }
            }
            return convertView;
        }

        @Override
        public Object getItem(int i) {
            return patrolPlanBeanLists == null ? null : patrolPlanBeanLists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }

    static class ViewHolder {
        public TextView report;//哪里的巡查报告
        public ImageView report_status;//状态
        public TextView reportor;//人名
        public TextView company_name;//公司名字
        public TextView data;//时间
        public ImageView work_status;//开始执行，查看报告
    }
}
