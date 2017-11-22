package com.isoftstone.smartsite.model.inspectplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBean;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBeanPage;
import com.isoftstone.smartsite.model.inspectplan.adapter.PatrolPlanAdapter;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/19.
 */

public class PatrolPlanActivity extends BaseActivity implements View.OnClickListener {

    private HttpPost mHttpPost = new HttpPost();
    private ImageButton mAddPatrolTask = null;   //新增巡查任务
    private TextView mTitleTextView = null;
    private LinearLayout weeks = null;
    private LinearLayout days = null;
    private TextView day_0 = null;
    private TextView day_1 = null;
    private TextView day_2 = null;
    private TextView day_3 = null;
    private TextView day_4 = null;
    private TextView day_5 = null;
    private TextView day_6 = null;
    private LocalDate today = null;
    private LocalDate Sunday = null;
    private LocalDate Monday = null;
    private LocalDate Tuesday = null;
    private LocalDate Wednesday = null;
    private LocalDate Thursday = null;
    private LocalDate Friday = null;
    private LocalDate Saturday = null;
    private final int HANDLER_GET_WEEK_START = 1;
    private final int HANDLER_GET_WEEK_END = 2;
    private final int HANDLER_GET_DAY_START = 3;
    private final int HANDLER_GET_DAY_END = 4;
    private PatrolTaskBeanPage patrolTaskBeanPage = null;
    private int selectindex = -1;
    private ArrayList<TextView> dayTextViewList = new ArrayList<TextView>();
    private ListView mListView = null;
    private ArrayList<PatrolTaskBean> mListData = null;
    private int planState = -1;
    private PatrolPlanBean patrolPlanBean = null;
    private ImageView imageview_tuihui;   //退回按钮
    private ImageView imageview_tongguo;  //通过按钮
    private LinearLayout tuihuitonguo_layout;  //退回通过layout
    private LinearLayout tijiaoshenpi_layout;  //提交审批layout
    private ImageView  imageview_tijiaoshenpi; //提交审批按钮
    private float touchX; //触摸点
    private long  planId;     //计划ID
    private String taskTimeStart; //开始时间
    private String taskTimeEnd;   //结束时间
    private long userId;   //用户id

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_GET_WEEK_START: {
                     showDlg("数据加载中");
                     new Thread(){
                         @Override
                         public void run() {
                            String user_id ="";
                            if(userId >= 0 ){
                                user_id = userId+"";
                            }
                            String plan_id = "";
                            if(planId >= 0){
                                plan_id = planId+"";
                            }
                            PageableBean pageableBean = new PageableBean();
                            mListData = mHttpPost.getPatrolTaskListAll(user_id, plan_id, "", "0", "", taskTimeStart, taskTimeEnd, pageableBean);
                             mHandler.sendEmptyMessage(HANDLER_GET_WEEK_END);
                         }
                     }.start();;
                }
                    break;
                case HANDLER_GET_WEEK_END:{
                    loadingData();
                    closeDlg();
                }
                    break;
                case HANDLER_GET_DAY_START: {
                    showDlg("数据加载中");
                    new Thread() {
                        @Override
                        public void run() {

                            PageableBean pageableBean = new PageableBean();
                            mListData = mHttpPost.getPatrolTaskListAll(HttpPost.mLoginBean.getmUserBean().getLoginUser().id + "", "", "", "0", "", "", "", pageableBean);
                            mHandler.sendEmptyMessage(HANDLER_GET_WEEK_END);
                        }
                    }.start();
                }
                break;
                case HANDLER_GET_DAY_END: {
                    loadingData();
                    closeDlg();
                }
                break;
            }
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_patrolplan;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        //获取巡查计划ID
        patrolPlanBean = (PatrolPlanBean)getIntent().getSerializableExtra("patrolplan");
        initToolbar();
        //显示星期
        weeks = (LinearLayout) findViewById(R.id.weeks);
        String[] strweeks = getResources().getStringArray(R.array.weeks);
        for (int i = 0; i < 7; i++) {
            TextView textView = (TextView) weeks.getChildAt(i);
            textView.setText(strweeks[i]);
            textView.setTextColor(getResources().getColor(R.color.single_text_color));
        }
        //显示日期
        days = (LinearLayout) findViewById(R.id.days);
        day_0 = (TextView) findViewById(R.id.day_0);
        day_0.setOnClickListener(this);
        day_1 = (TextView) findViewById(R.id.day_1);
        day_1.setOnClickListener(this);
        day_2 = (TextView) findViewById(R.id.day_2);
        day_2.setOnClickListener(this);
        day_3 = (TextView) findViewById(R.id.day_3);
        day_3.setOnClickListener(this);
        day_4 = (TextView) findViewById(R.id.day_4);
        day_4.setOnClickListener(this);
        day_5 = (TextView) findViewById(R.id.day_5);
        day_5.setOnClickListener(this);
        day_6 = (TextView) findViewById(R.id.day_6);
        day_6.setOnClickListener(this);
        dayTextViewList.add(day_0);
        dayTextViewList.add(day_1);
        dayTextViewList.add(day_2);
        dayTextViewList.add(day_3);
        dayTextViewList.add(day_4);
        dayTextViewList.add(day_5);
        dayTextViewList.add(day_6);

        days.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PatrolPlanActivity.super.onTouchEvent(event);
                switch (event.getAction()){
                    case  MotionEvent.ACTION_DOWN:{
                        touchX = event.getX();
                    }
                    return true;
                    case MotionEvent.ACTION_UP: {
                          float X = event.getX();
                        if (X - touchX > 80) {
                              //下一周
                              today = Saturday.plusDays(2);
                              loadingDate(today);
                            selectindex = -1;
                            updateWidget();
                              mHandler.sendEmptyMessage(HANDLER_GET_WEEK_START);
                          }

                        if (touchX - X > 80) {
                              //上一周
                              today = Sunday.minusDays(1);
                              loadingDate(today);
                            selectindex = -1;
                            updateWidget();
                              mHandler.sendEmptyMessage(HANDLER_GET_WEEK_START);
                          }
                    }
                    return  false;
                }
                return  false;
            }
        });

        mListView = (ListView) findViewById(R.id.listview);
        imageview_tuihui = (ImageView) findViewById(R.id.imageview_tuihui);   //退回按钮
        imageview_tuihui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //审批通过
    }
        });
        imageview_tongguo = (ImageView) findViewById(R.id.imageview_tongguo);  //通过按钮
        imageview_tongguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //审批拒绝
            }
        });
        tuihuitonguo_layout = (LinearLayout) findViewById(R.id.tuihuitonguo_layout);  //退回通过layout
        tijiaoshenpi_layout = (LinearLayout)findViewById(R.id.tijiaoshenpi_layout);  //提交审批
        imageview_tijiaoshenpi = (ImageView) findViewById(R.id.imageview_tijiaoshenpi);
        imageview_tijiaoshenpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交审批
            }
        });

        if(patrolPlanBean != null){
            planId = patrolPlanBean.getId();     //计划ID
            taskTimeStart = patrolPlanBean.getStart(); //开始时间
            taskTimeEnd = patrolPlanBean.getEndDate();   //结束时间
            userId = patrolPlanBean.getCreator().getId();   //用户id
            today = LocalDate.parse(taskTimeEnd);
            loadingDate(today);
        }else {
            today = LocalDate.now();
            loadingDate(today);
            planId = -1;     //计划ID
            taskTimeStart = Sunday.toString(); //开始时间
            taskTimeEnd = Saturday.toString();   //结束时间
            userId = HttpPost.mLoginBean.getmUserBean().getLoginUser().getId();   //用户id
        }
        mHandler.sendEmptyMessage(HANDLER_GET_WEEK_START);
    }


    private void initToolbar(){
        mTitleTextView = (TextView) findViewById(R.id.toolbar_title);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAddPatrolTask = (ImageButton) findViewById(R.id.btn_icon);
        mAddPatrolTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新增巡查任务
                //Toast.makeText(PatrolPlanActivity.this,"添加任务",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PatrolPlanActivity.this,AddInspectPlan.class);
                startActivity(intent);
            }
        });
    }

    /*
    加载时间
     */
    private void loadingDate(LocalDate date){
        //获取时间
        int dayOfWeek = date.getDayOfWeek(); //获取今天是周几
        Sunday = date.minusDays(dayOfWeek); //获取周日
        Monday = date.minusDays(dayOfWeek-1);
        Tuesday = date.minusDays(dayOfWeek-2);
        Wednesday = date.minusDays(dayOfWeek-3);
        Thursday = date.minusDays(dayOfWeek-4);
        Friday = date.minusDays(dayOfWeek-5);
        Saturday = date.minusDays(dayOfWeek-6);
        day_0.setText(Sunday.getDayOfMonth()+"");
        day_1.setText(Monday.getDayOfMonth()+"");
        day_2.setText(Tuesday.getDayOfMonth()+"");
        day_3.setText(Wednesday.getDayOfMonth()+"");
        day_4.setText(Thursday.getDayOfMonth()+"");
        day_5.setText(Friday.getDayOfMonth()+"");
        day_6.setText(Saturday.getDayOfMonth()+"");
        mTitleTextView.setText(Sunday.toString());
    }

    /*
    加载数据
     */
    private void loadingData(){
        //根据计划状态来加载背景  是否显示审批按钮
        //设置计划状态
        int state = 0;
        if(patrolPlanBean != null){
            state = patrolPlanBean.getStatus();
        }else {

    }
        switch (state){
            case 1://已创建，待提交
                break;
            case 2://已提交，待审批
                days.setBackgroundResource(R.drawable.jihua_daishenpishenpi_bg);
                break;
            case 3://已通过
                days.setBackgroundResource(R.drawable.jihua_shenpitongguo_bg);
                break;
            case 4://已打回
                days.setBackgroundResource(R.drawable.jihua_shenpituihui_bg);
                break;
        }
        if(mListData != null){
            PatrolPlanAdapter adapter = new PatrolPlanAdapter(this);
            adapter.setList(mListData);
            mListView.setAdapter(adapter);
        }

        //审批权限
        if(HttpPost.mLoginBean.getmUserBean().getmPermission().isM_CPPA()){
            tuihuitonguo_layout.setVisibility(View.VISIBLE);
            tijiaoshenpi_layout.setVisibility(View.GONE);
        }else {
            tuihuitonguo_layout.setVisibility(View.GONE);
            tijiaoshenpi_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int select = -1;
        switch (v.getId()) {
            case R.id.day_0: {
                select = 0;
            }
            break;
            case R.id.day_1: {
                select = 1;
            }
            break;
            case R.id.day_2: {
                select = 2;
            }
            break;
            case R.id.day_3: {
                select = 3;
            }
            break;
            case R.id.day_4: {
                select = 4;
            }
            break;
            case R.id.day_5: {
                select = 5;
            }
            break;
            case R.id.day_6: {
                select = 6;
            }
            break;
        }

        if (select == selectindex) {
            selectindex = -1;
        } else {
            selectindex = select;
        }
        updateWidget();
        //today = Saturday.plusDays(2);
        updateWidget();
        mHandler.sendEmptyMessage(HANDLER_GET_DAY_START);
    }

    private void updateWidget() {
        //修改按钮  和文字颜色
        for (TextView textView : dayTextViewList)
            textView.setBackgroundResource(R.drawable.rili_tian_zhengchang);
        if (selectindex != -1) {
            dayTextViewList.get(selectindex).setBackgroundResource(R.drawable.rili_tian_xuanzhong);
        }
    }


}
