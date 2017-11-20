package com.isoftstone.smartsite.model.inspectplan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBeanPage;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by gone on 2017/11/19.
 */

public class PatrolPlanActivity extends BaseActivity{

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
    private PatrolTaskBeanPage patrolTaskBeanPage = null;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_GET_WEEK_START: {
                     showDlg("数据加载中");
                     new Thread(){
                         @Override
                         public void run() {
                             //PageableBean pageableBean = new PageableBean();
                             //patrolTaskBeanPage = mHttpPost.getPatrolTaskList(HttpPost.mLoginBean.getmUserBean().getLoginUser().id,"","","","",pageableBean);
                             mHandler.sendEmptyMessage(HANDLER_GET_WEEK_END);
                         }
                     }.start();;
                }
                    break;
                case HANDLER_GET_WEEK_END:{

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
        initToolbar();
        weeks = (LinearLayout) findViewById(R.id.weeks);
        //显示星期
        String[] strweeks = getResources().getStringArray(R.array.weeks);
        for (int i = 0; i < 7; i++) {
            TextView textView = (TextView) weeks.getChildAt(i);
            textView.setText(strweeks[i]);
            textView.setTextColor(getResources().getColor(R.color.single_text_color));
        }
        //显示日期
        days = (LinearLayout) findViewById(R.id.days);
        day_0 = (TextView) findViewById(R.id.day_0);
        day_1 = (TextView) findViewById(R.id.day_1);
        day_2 = (TextView) findViewById(R.id.day_2);
        day_3 = (TextView) findViewById(R.id.day_3);
        day_4 = (TextView) findViewById(R.id.day_4);
        day_5 = (TextView) findViewById(R.id.day_5);
        day_6 = (TextView) findViewById(R.id.day_6);

        today = LocalDate.now();
        loadingDate(today);
        mHandler.sendEmptyMessage(HANDLER_GET_WEEK_START);
        days.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PatrolPlanActivity.super.onTouchEvent(event);
                switch (event.getAction()){
                    case  MotionEvent.ACTION_DOWN:{
                          float X = event.getX();
                          if(X > touchX){
                              //上一周
                              today = Sunday.minusDays(1);
                              loadingDate(today);
                              mHandler.sendEmptyMessage(HANDLER_GET_WEEK_START);
                          }

                          if(X < touchX){
                              //下一周
                              today = Saturday.plusDays(2);
                              loadingDate(today);
                              mHandler.sendEmptyMessage(HANDLER_GET_WEEK_START);
                          }
                    }
                    return  true;
                    case MotionEvent.ACTION_UP:{
                        touchX =  event.getX();
                    }
                    return  false;
                }
                return  false;
            }
        });
    }

    private float touchX;
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
                Toast.makeText(PatrolPlanActivity.this,"添加任务",Toast.LENGTH_SHORT).show();
            }
        });
    }

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
    }
}
