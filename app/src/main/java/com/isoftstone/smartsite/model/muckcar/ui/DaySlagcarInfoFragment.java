package com.isoftstone.smartsite.model.muckcar.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.http.muckcar.ArchMonthFlowBean;
import com.isoftstone.smartsite.http.muckcar.CarInfoBean;
import com.isoftstone.smartsite.http.muckcar.McFlowBean;
import com.isoftstone.smartsite.model.main.ui.AirMonitoringActivity;
import com.isoftstone.smartsite.utils.DateUtils;
import com.isoftstone.smartsite.widgets.CustomDatePicker;

import org.joda.time.LocalDate;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.FormBody;

/**
 * Created by 2013020220 on 2017/11/23.
 */

public class DaySlagcarInfoFragment extends BaseFragment {
    public static final int TIME_INIT_TEXTVIEW_LIST = 0;
    private int mDayOrMonthFlag = 1; //1月  0日
    private LinearLayout layout_1;
    private LinearLayout layout_2;
    private ArrayList<CarInfoBean> mCarInfoList;
    private String[] archName;
    private Spinner spinner_address = null;
    private TextView date_liuliangpaiming = null;
    private TextView date_louduanbaojinglv = null;
    private TextView date_liuliangduibi = null;
    private long liuliangduibi_id = 0;
    private ArchMonthFlowBean mArchMonthFlowBean;
    private LineChart liuliangduibi_linechart;
    private LineChart baojinglv_linechart;
    private TextView baojinglv_address;
    private LinearLayout list_trextviews;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_INIT_TEXTVIEW_LIST:
                    initTextViews();
                    break;
                    default:
                        break;
            }

        }
    };
    private Spinner address_baojinglv;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(null);
        }
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        list_trextviews = (LinearLayout) rootView.findViewById(R.id.list_textview);//动态tv数据
        layout_1 = (LinearLayout) rootView.findViewById(R.id.liuliangduibi_detail);
        layout_2 = (LinearLayout) rootView.findViewById(R.id.warning_detail);
        spinner_address = (Spinner) layout_1.findViewById(R.id.spinner_address);//bao jing lv
        address_baojinglv = (Spinner) layout_2.findViewById(R.id.spinner_address_baojinglv);
        address_baojinglv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                liuliangduibi_id = mCarInfoList.get(position).getArch().getId();
                ((SlagcarInfoActivity) getActivity()).getLiuliangduibiData(mDayOrMonthFlag);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //流量对比
        date_liuliangduibi = (TextView) layout_1.findViewById(R.id.date_liuliangduibi);
        date_liuliangduibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                if (mDayOrMonthFlag == 1) {
                    customDatePicker2.showYearMonth();
                } else if (mDayOrMonthFlag == 0) {
                    customDatePicker2.showSpecificTime(false); // 不显示时和分
                }
                customDatePicker2.show(now);
            }
        });
        //流量排行
        date_liuliangpaiming = (TextView) rootView.findViewById(R.id.date_liuliangpaiming);
        date_liuliangpaiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                if (mDayOrMonthFlag == 1) {
                    customDatePicker1.showYearMonth();
                } else if (mDayOrMonthFlag == 0) {
                    customDatePicker1.showSpecificTime(false); // 不显示时和分
                }
                customDatePicker1.show(now);
            }
        });
        date_louduanbaojinglv = (TextView) rootView.findViewById(R.id.date_louduanbaojinglv);
        date_louduanbaojinglv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                if (mDayOrMonthFlag == 1) {
                    customDatePicker3.showYearMonth();
                } else if (mDayOrMonthFlag == 0) {
                    customDatePicker3.showSpecificTime(false); // 不显示时和分
                }
                customDatePicker3.show(now);
            }
        });
        baojinglv_address = (TextView) rootView.findViewById(R.id.load_name_3);
        baojinglv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        liuliangduibi_linechart = (LineChart) layout_1.findViewById(R.id.chart_liuliangduibi);
        baojinglv_linechart = (LineChart) rootView.findViewById(R.id.chart_baojinglv);
        if (mDayOrMonthFlag == 1) {
            date_liuliangduibi.setText(DateUtils.getNewTime_2());
            date_liuliangpaiming.setText(DateUtils.getNewTime_2());
        } else if (mDayOrMonthFlag == 0) {
            date_liuliangduibi.setText(DateUtils.getNewTime_1());
            date_liuliangpaiming.setText(DateUtils.getNewTime_1());
        }
        ((SlagcarInfoActivity) getActivity()).getLiuliangpaimingData(mDayOrMonthFlag);
        initDatePicker();

    }

    private CustomDatePicker customDatePicker1, customDatePicker2, customDatePicker3;

    //初始化多个textview
    private void initTextViews() {
        list_trextviews.removeAllViews();
        if (mCarInfoList != null && mCarInfoList.size() != 0) {
            int tv_num = mCarInfoList.size();
            CarInfoBean mCarInfoBean;
            for (int i = 0; i < tv_num; i++) {
                mCarInfoBean = mCarInfoList.get(i);
                MyTextView myTextView = new MyTextView(mContext, mCarInfoBean.getArch().getName(), mCarInfoBean.getIsAlarmMc(), mCarInfoBean.getNoAlarmMc());
                list_trextviews.addView(myTextView);
            }
        }
    }

    private void initDatePicker() {
        customDatePicker1 = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间


            }
        }, "2010-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (mDayOrMonthFlag == 1) {
                    date_liuliangduibi.setText(time.substring(0, 7));
                } else if (mDayOrMonthFlag == 0) {
                    date_liuliangduibi.setText(time.substring(0, 10));
                }
                ((SlagcarInfoActivity) getActivity()).getLiuliangduibiData(mDayOrMonthFlag);
            }
        }, "2010-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.setIsLoop(false); // 不允许循环滚动

        customDatePicker3 = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间

            }
        }, "2010-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker3.setIsLoop(false); // 不允许循环滚动

    }

    public void setDayOrMonthFlag(int dayOrMonthFlag) {
        mDayOrMonthFlag = dayOrMonthFlag;
    }

    public int getDayOrMonthFlag() {
        return mDayOrMonthFlag;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_slagcar_info;
    }

    public void setCarInfoList(ArrayList<CarInfoBean> carInfoList) {
        handler.sendEmptyMessage(TIME_INIT_TEXTVIEW_LIST);
        mCarInfoList = carInfoList;
        archName = null;
        if (mCarInfoList != null) {
            archName = new String[mCarInfoList.size()];
            for (int i = 0; i < mCarInfoList.size(); i++) {
                CarInfoBean carInfoBean = mCarInfoList.get(i);
                archName[i] = carInfoBean.getArch().getName();
            }

        }
        if (archName != null) {
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, archName);
            spinner_address.setAdapter(adapter);
        }

    }

    public String getLiuliangpaimingTime() {
        return date_liuliangpaiming.getText().toString();
    }

    public String getLiuliangduibiTime() {
        return date_liuliangduibi.getText().toString();
    }

    public long getLiuliangduibi_id() {
        return liuliangduibi_id;
    }

    public void setLiuliangduibi(ArchMonthFlowBean archMonthFlowBean) {
        mArchMonthFlowBean = archMonthFlowBean;
        //跟新数据
        int max = 0;
        if (mArchMonthFlowBean != null) {
            liuliangduibi_linechart.setDrawGridBackground(false);
            liuliangduibi_linechart.getDescription().setEnabled(false);
            liuliangduibi_linechart.setTouchEnabled(true);
            liuliangduibi_linechart.setDragEnabled(false);  //是否可以缩放
            liuliangduibi_linechart.setScaleEnabled(false);  //是否可以缩放
            liuliangduibi_linechart.setPinchZoom(true);
            liuliangduibi_linechart.setExtraOffsets(10, 0, 10, 20);

            XAxis xAxis = liuliangduibi_linechart.getXAxis();
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setAxisMaximum(31);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            YAxis leftAxis = liuliangduibi_linechart.getAxisLeft();
            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
            leftAxis.enableGridDashedLine(10f, 0f, 0f);
            leftAxis.setDrawZeroLine(false);
            leftAxis.setDrawLimitLinesBehindData(false);


            liuliangduibi_linechart.getAxisRight().setEnabled(false);


            liuliangduibi_linechart.animateX(2500);

            // get the legend (only possible after setting data)
            Legend l = liuliangduibi_linechart.getLegend();
            l.setForm(Legend.LegendForm.LINE);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setEnabled(false);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            ArrayList<McFlowBean> countFlow = mArchMonthFlowBean.getCountFlow();
            if (countFlow != null && countFlow.size() > 0) {

                ArrayList<Entry> values = new ArrayList<Entry>();
                for (int i = 0; i < countFlow.size(); i++) {
                    McFlowBean mcFlowBean = countFlow.get(i);
                    if (mDayOrMonthFlag == 1) {
                        LocalDate date = new LocalDate(mcFlowBean.getDataTimeDay());
                        int index = date.getDayOfMonth();
                        String value = mcFlowBean.getFlow() + "";
                        if (max < mcFlowBean.getFlow()) {
                            max = Integer.parseInt(value);
                        }
                        Entry entry = new Entry(index, Float.parseFloat(value));
                        values.add(entry);
                    } else if (mDayOrMonthFlag == 0) {
                        int index = Integer.parseInt(mcFlowBean.getDataTimeDay());
                        String value = mcFlowBean.getFlow() + "";
                        if (max < mcFlowBean.getFlow()) {
                            max = Integer.parseInt(value);
                        }
                        Entry entry = new Entry(index, Float.parseFloat(value));
                        values.add(entry);
                    }

                }

                LineDataSet set1 = new LineDataSet(values, "DataSet 1");
                set1.setDrawIcons(false);
                // set the line to be drawn like this "- - - - - -"
                set1.setColor(Color.parseColor("#4879df"));
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 10f}, 0f));
                set1.setFormSize(15.f);
                set1.setDrawCircles(false);
                set1.setDrawFilled(true);
                set1.setHighlightEnabled(false);
                set1.setDrawValues(false);
                set1.setFillAlpha(255);
                set1.setFillColor(Color.parseColor("#4879df"));

                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                    //set1.setFillDrawable(drawable);
                } else {
                    set1.setFillColor(Color.BLACK);
                }
                dataSets.add(set1); // add the datasets
            }

            ArrayList<McFlowBean> isAlarms = mArchMonthFlowBean.getIsAlarms();
            if (isAlarms != null && isAlarms.size() > 0) {
                ArrayList<Entry> values_2 = new ArrayList<Entry>();
                for (int i = 0; i < isAlarms.size(); i++) {
                    McFlowBean mcFlowBean = isAlarms.get(i);
                    if (mDayOrMonthFlag == 1) {
                        LocalDate date = new LocalDate(mcFlowBean.getDataTimeDay());
                        int day = date.getDayOfMonth();
                        String value = mcFlowBean.getFlow() + "";
                        Entry entry = new Entry(day, Float.parseFloat(value));
                        values_2.add(entry);
                    } else if (mDayOrMonthFlag == 0) {
                        int index = Integer.parseInt(mcFlowBean.getDataTimeDay());
                        String value = mcFlowBean.getFlow() + "";
                        Entry entry = new Entry(index, Float.parseFloat(value));
                        values_2.add(entry);
                    }

                }


                LineDataSet set2 = new LineDataSet(values_2, "DataSet 2");
                set2.setDrawIcons(false);
                // set the line to be drawn like this "- - - - - -"
                set2.enableDashedLine(10f, 0f, 0f);//设置连线样式
                set2.setColor(Color.RED);
                set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 10f}, 0f));
                set2.setCircleColor(Color.parseColor("#599fff"));
                //set2.setCircleRadius(4f);//设置焦点圆心的大小
                set2.setCircleColorHole(Color.WHITE);
                set2.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
                set2.setHighlightEnabled(false);//是否禁用点击高亮线
                set2.setDrawFilled(true);//设置禁用范围背景填充
                set2.setFillAlpha(255);
                set2.setFillColor(Color.RED);
                set2.setDrawCircles(false);
                set2.setDrawValues(false);

                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                    //set1.setFillDrawable(drawable);
                } else {
                    set2.setFillColor(Color.BLACK);
                }

                dataSets.add(set2); // add the datasets
            }

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            //设置最大高度
            leftAxis.setAxisMaximum(max);
            // set data
            liuliangduibi_linechart.setData(data);

        }
    }
}
