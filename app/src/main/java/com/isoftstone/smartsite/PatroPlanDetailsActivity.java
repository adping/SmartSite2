package com.isoftstone.smartsite;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBean;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBeanPage;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBeanPage;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.isoftstone.smartsite.widgets.StartworkDialog;

import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 2013020220 on 2017/11/19.
 */

public class PatroPlanDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ListView listview;
    private ImageButton ibt_back;
    private TextView title;
    public static final int WORK_WAIT_FOR_DOING = 0;
    public static final int WORK_IS_DOING = 1;
    public static final int WORK_HAS_DONE = 2;
    public static final int TIME_TO_INITVIEW = 4;
    private ImageView add_plan;
    private ArrayList<PatrolTaskBean> patrolTaskBeanArrayList;
    private PatrolTaskBeanPage patrolTaskBeanPage;
    private StartworkDialog startworkDialog = null;

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {


            @Override
            public void run() {
                PageableBean pageableBean = new PageableBean();
                HttpPost httpPost = new HttpPost();
                patrolTaskBeanPage = httpPost.getPatrolTaskList(HttpPost.mLoginBean.getmUserBean().getLoginUser().getId(), "", "", "", "", pageableBean);
                patrolTaskBeanArrayList = patrolTaskBeanPage.getContent();
                handler.sendEmptyMessage(TIME_TO_INITVIEW);
            }
        }).start();

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_patrol_plan_details;
    }

    private void initViews() {
        View v = findViewById(R.id.title);
        ibt_back = (ImageButton) v.findViewById(R.id.btn_back);
        title = (TextView) v.findViewById(R.id.toolbar_title);
        add_plan = (ImageView) findViewById(R.id.btn_icon);
        add_plan.setImageResource(R.drawable.jiahao);
        title.setText("巡查任务");
        listview = (ListView) findViewById(R.id.patrol_detail_list);
        ibt_back.setOnClickListener(this);
        add_plan.setOnClickListener(this);
        listview.setAdapter(new MyBaseAdapter(this, patrolTaskBeanArrayList));
        listview.setOnItemClickListener(itemClickListener);
        startworkDialog = new StartworkDialog(this, listener);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //dianjijutitiaou
            ToastUtils.showShort("choice is :" + (i + 1));
            PatrolTaskBean selectPatrolTaskBean = patrolTaskBeanArrayList.get(i);
            switch (selectPatrolTaskBean.getTaskStatus()) {
                case WORK_WAIT_FOR_DOING:
                    startworkDialog.show();
                    break;
                case WORK_IS_DOING:
                    ToastUtils.showShort("ZAI  ZHI  XING ");
                    break;
                case WORK_HAS_DONE:
                    ToastUtils.showShort("YI WAN CHENG");
                    break;
                default:
                    break;
            }

        }
    };
    private StartworkDialog.OnStartworkLstener listener = new StartworkDialog.OnStartworkLstener() {
        @Override
        public void onStartwork() {
            ToastUtils.showShort("你点了开始执行");
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_icon:
                //TODO
                break;
            default:
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TIME_TO_INITVIEW) {
                initViews();
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
                holder.report = (TextView) convertView.findViewById(R.id.reporterName);
                holder.report_status = (ImageView) convertView.findViewById(R.id.report_status);
                holder.reportor = (TextView) convertView.findViewById(R.id.reporterName);
                holder.company_name = (TextView) convertView.findViewById(R.id.comparyName);
                holder.data = (TextView) convertView.findViewById(R.id.data);
                holder.work_status = (ImageView) findViewById(R.id.work_status);
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
            } else if (status == 1) {
                holder.report_status.setImageResource(R.drawable.zhixingzhong);
                holder.data.setText(patrolTaskBean.getTaskStart());
            } else if (status == 2) {
                holder.report_status.setImageResource(R.drawable.yiwancheng);
                holder.data.setText(patrolTaskBean.getTaskEnd());
            }
            holder.reportor.setText(patrolTaskBean.getCreator().name);
            holder.company_name.setText("湖北毅瑞公司");
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
