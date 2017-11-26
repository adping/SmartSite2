package com.isoftstone.smartsite.model.map.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.model.map.bean.MyValueFomatter;
import com.isoftstone.smartsite.model.map.bean.MyXFormatter;
import com.isoftstone.smartsite.utils.DensityUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zw on 2017/11/25.
 */

public class ConstructionSummaryActivity extends BaseActivity implements View.OnClickListener {

    private LineChart lineChart;
    private LineChart lineChart2;
    private LineChart lineChart3;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_construction_summary;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        ToastUtils.showShort("此界面数据为假！");
        initToolBar();
        initFirstBarChart();
        initSecondBarChart();
        initThridBarChart();
        initFourBarChart();
        initFiveBarChart();
    }

    private void initToolBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText("巡查概况");
        ImageButton imageButton = (ImageButton) findViewById(R.id.btn_icon);
        imageButton.setOnClickListener(this);
        imageButton.setImageResource(R.drawable.environmentlist);
    }

    private void initFirstBarChart() {
        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.hbc);
        ViewGroup.LayoutParams layoutParams = barChart.getLayoutParams();
        layoutParams.height = DensityUtils.dip2px(this,30 * 8);

        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放

        //设置显示边界
        barChart.setDrawBorders(false);

        //网格
        barChart.setDrawGridBackground(false);

        //不显示描述
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        barChart.setDescription(null);

        //X轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.parseColor("#dddddd"));


        //Y轴
        YAxis yLeftAxis = barChart.getAxisLeft();
        YAxis yRrightAxis = barChart.getAxisRight();
        yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        yRrightAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        yLeftAxis.setAxisMaximum(700);
        yLeftAxis.setAxisMinimum(0);
        yRrightAxis.setAxisMaximum(700);
        yRrightAxis.setAxisMinimum(0);
        yLeftAxis.setDrawLabels(false);
        yRrightAxis.setLabelCount(700/100);
        yLeftAxis.setGridColor(Color.parseColor("#ededed"));
        yRrightAxis.setGridColor(Color.parseColor("#ededed"));

        //保证Y轴从0开始，不然会上移一点
        yLeftAxis.setAxisMinimum(0f);
        yRrightAxis.setAxisMinimum(0f);

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        ArrayList<String> xVaulesName = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            xValues.add((float) i);
            xVaulesName.add("后勤部" + i);
        }
        xAxis.setLabelCount(xValues.size() - 1,false);
        MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
        xAxis.setValueFormatter(xFormatter);

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Float> yValue = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                yValue.add(i == 0 ? 600f : 300);
            }
            yValues.add(yValue);
        }

        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(Color.parseColor("#c6c6c6"));
        colours.add(Color.parseColor("#3464dd"));


        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("折线一");
        names.add("折线二");

        BarData barData = new BarData();
        barData.setBarWidth(0.5f);
        for (int i = 0; i < yValues.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yValues.get(i).size(); j++) {

                entries.add(new BarEntry(xValues.get(j), yValues.get(i).get(j)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, names.get(i));

            barDataSet.setColor(colours.get(i));
            barDataSet.setValueTextColor(Color.WHITE);
            barDataSet.setValueTextSize(10f);
            barDataSet.setValueFormatter(new MyValueFomatter());
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            barData.addDataSet(barDataSet);
        }


        int amount = xValues.size();

/*        float groupSpace = 0.12f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.12) / amount / 10 * 9); // x4 DataSet

        // (0.2 + 0.02) * 4 + 0.08 = 1.00 -> interval per "group"
        xAxis.setLabelCount(xValues.size() - 1, false);
        barData.setBarWidth(barWidth);


        barData.groupBars(0, groupSpace, barSpace);*/

        barChart.setDrawValueAboveBar(false);
        barChart.setData(barData);

    }

    private void initSecondBarChart(){
        BarChart barChart = (BarChart) findViewById(R.id.person_rank_chart);
        ViewGroup.LayoutParams layoutParams = barChart.getLayoutParams();



        //设置显示边界
        barChart.setDrawBorders(false);

        //网格
        barChart.setDrawGridBackground(false);

        //不显示描述
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        barChart.setDescription(null);

        //X轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.parseColor("#dddddd"));


        //Y轴
        YAxis yLeftAxis = barChart.getAxisLeft();
        YAxis yRrightAxis = barChart.getAxisRight();
        yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        yRrightAxis.setAxisLineColor(Color.TRANSPARENT);
        yLeftAxis.setAxisMaximum(50);
        yLeftAxis.setAxisMinimum(0);
        yRrightAxis.setAxisMaximum(50);
        yRrightAxis.setAxisMinimum(0);
        yRrightAxis.setDrawLabels(false);
        yLeftAxis.setLabelCount(50 / 10);
        yLeftAxis.setGridColor(Color.parseColor("#ededed"));
        yRrightAxis.setGridColor(Color.parseColor("#ededed"));

        //保证Y轴从0开始，不然会上移一点
        yLeftAxis.setAxisMinimum(0f);
        yRrightAxis.setAxisMinimum(0f);

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        ArrayList<String> xVaulesName = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            xValues.add((float) i);
            xVaulesName.add("人员" + i);
        }
        xAxis.setLabelCount(xValues.size() - 1,false);
        MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
        xAxis.setValueFormatter(xFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Float> yValue = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                yValue.add(40f);
            }
            yValues.add(yValue);
        }

        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(Color.parseColor("#3464dd"));
        colours.add(Color.parseColor("#c6c6c6"));

        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("折线一");
        names.add("折线二");

        BarData barData = new BarData();
        barData.setBarWidth(0.5f);
        for (int i = 0; i < yValues.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yValues.get(i).size(); j++) {
                entries.add(new BarEntry(xValues.get(j), yValues.get(i).get(j)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, names.get(i));

            barDataSet.setDrawValues(false);
            barDataSet.setColor(colours.get(i));
            barDataSet.setValueTextColor(Color.WHITE);
            barDataSet.setValueTextSize(10f);
            barDataSet.setValueFormatter(new MyValueFomatter());
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            barData.addDataSet(barDataSet);
        }


        int amount = xValues.size();

        float groupSpace = 0.3f; //柱状图组之间的间距
        float barSpace = (float) ((1 - groupSpace) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - groupSpace) / amount / 10 * 9); // x4 DataSet

//        float avgWidth = 1/amount;
//        barWidth = avgWidth/4;
//        barSpace = avgWidth/2 * 0.2f;
//        groupSpace = avgWidth/2 * 0.8f;
        barWidth = 0.25f;
        barSpace = 0.1f;

        // (0.2 + 0.02) * 4 + 0.08 = 1.00 -> interval per "group"
        barData.setBarWidth(barWidth);


        barData.groupBars(0, groupSpace, barSpace);

        barChart.setDrawValueAboveBar(false);

        barChart.setData(barData);

        xAxis.setAxisMaximum(xValues.size());
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(xValues.size() - 1,false);
        xAxis.setCenterAxisLabels(true);
        barChart.invalidate();

        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放

    }

    private void initThridBarChart(){
        lineChart = (LineChart) findViewById(R.id.company_total_line_chart);

        //设置显示边界
        lineChart.setDrawBorders(false);

        //网格
        lineChart.setDrawGridBackground(false);

        //不显示描述
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        lineChart.setDescription(null);

        //X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.parseColor("#dddddd"));

        //Y轴
        YAxis yLeftAxis = lineChart.getAxisLeft();
        YAxis yRrightAxis = lineChart.getAxisRight();
        yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        yRrightAxis.setAxisLineColor(Color.TRANSPARENT);
        yLeftAxis.setAxisMaximum(140);
        yLeftAxis.setAxisMinimum(0);
        yRrightAxis.setAxisMaximum(140);
        yRrightAxis.setAxisMinimum(0);
        yRrightAxis.setDrawLabels(false);
        yLeftAxis.setLabelCount(140 / 20);
        yLeftAxis.setGridColor(Color.parseColor("#ededed"));
        yRrightAxis.setGridColor(Color.parseColor("#ededed"));

        //保证Y轴从0开始，不然会上移一点
        yLeftAxis.setAxisMinimum(0f);
        yRrightAxis.setAxisMinimum(0f);

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        ArrayList<String> xVaulesName = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            xVaulesName.add((i < 10 ? "0" : "") + i);
            xValues.add((float) i);
        }
        xAxis.setLabelCount(xValues.size(),false);
        MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
        xAxis.setValueFormatter(xFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //设置y轴的数据()
        List<Entry> yValues1 = new ArrayList<>();
        List<Entry> yValues2 = new ArrayList<>();
        int shujucount = 11;
        for (int i = 0;i < 31; i++){
            if(i % 2 == 0){
                yValues1.add(new Entry(i,120f));
                yValues2.add(new Entry(i,60f));
            }else {
                yValues1.add(new Entry(i,100f));
                yValues2.add(new Entry(i,50f));
            }
        }


        LineDataSet set1 = null;
        LineDataSet set2 = null;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
            set1.setValues(yValues1);
            set2.setValues(yValues2);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {

            // create a dataset and give it a type
            set1 = new LineDataSet(yValues1, "DataSet");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.parseColor("#c6c6c6"));
            set1.setDrawCircles(false);
            set1.setLineWidth(1f);
            set1.setCircleRadius(5f);
            set1.setFillAlpha(255);
            set1.setDrawFilled(true);

            set1.setFillColor(Color.parseColor("#c6c6c6"));
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawCircleHole(false);
//            set.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return lineChart.getAxisLeft().getAxisMaximum();
//                }
//            });

            // create a dataset and give it a type
            set2 = new LineDataSet(yValues2, "DataSet");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.parseColor("#3464dd"));
            set2.setDrawCircles(false);
            set2.setLineWidth(1f);
            set2.setCircleRadius(5f);
            set2.setFillAlpha(255);
            set2.setDrawFilled(true);
            set2.setFillColor(Color.parseColor("#3464dd"));
            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set2.setDrawCircleHole(false);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setDrawValues(false);

            // set data
            lineChart.setData(data);
        }

        xAxis.setAxisMaximum(xValues.size());
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(xValues.size() % 2 == 0 ? xValues.size()/2 : (int)(xValues.size()/2) + 1,false);
        lineChart.invalidate();

        lineChart.setTouchEnabled(false); // 设置是否可以触摸
        lineChart.setDragEnabled(false);// 是否可以拖拽
        lineChart.setScaleEnabled(false);// 是否可以缩放
        lineChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放

    }

    private void initFourBarChart(){
        lineChart2 = (LineChart) findViewById(R.id.linechart4);

        //设置显示边界
        lineChart2.setDrawBorders(false);

        //网格
        lineChart2.setDrawGridBackground(false);

        //不显示描述
        Legend legend = lineChart2.getLegend();
        legend.setEnabled(false);
        lineChart2.setDescription(null);

        //X轴
        XAxis xAxis = lineChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.parseColor("#ededed"));
        xAxis.setAxisLineColor(Color.parseColor("#dddddd"));

        //Y轴
        YAxis yLeftAxis = lineChart2.getAxisLeft();
        YAxis yRrightAxis = lineChart2.getAxisRight();
        yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        yRrightAxis.setAxisLineColor(Color.TRANSPARENT);
        yLeftAxis.setAxisMaximum(140);
        yLeftAxis.setAxisMinimum(0);
        yRrightAxis.setAxisMaximum(140);
        yRrightAxis.setAxisMinimum(0);
        yRrightAxis.setDrawLabels(false);
        yLeftAxis.setLabelCount(140 / 20);
        yLeftAxis.setGridColor(Color.parseColor("#ededed"));
        yRrightAxis.setGridColor(Color.parseColor("#ededed"));

        //保证Y轴从0开始，不然会上移一点
        yLeftAxis.setAxisMinimum(0f);
        yRrightAxis.setAxisMinimum(0f);

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        ArrayList<String> xVaulesName = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            xVaulesName.add((i < 10 ? "0" : "") + i);
            xValues.add((float) i);
        }
        xAxis.setLabelCount(xValues.size(),false);
        MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
        xAxis.setValueFormatter(xFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //设置y轴的数据()
        List<Entry> yValues1 = new ArrayList<>();
        List<Entry> yValues2 = new ArrayList<>();
        int shujucount = 11;
        for (int i = 0;i < 31; i++){
            if(i % 2 == 0){
                yValues1.add(new Entry(i,120f));
                yValues2.add(new Entry(i,60f));
            }else {
                yValues1.add(new Entry(i,100f));
                yValues2.add(new Entry(i,50f));
            }
        }


        LineDataSet set1 = null;
        LineDataSet set2 = null;
        if (lineChart2.getData() != null &&
                lineChart2.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart2.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) lineChart2.getData().getDataSetByIndex(1);
            set1.setValues(yValues1);
            set2.setValues(yValues2);
            lineChart2.getData().notifyDataChanged();
            lineChart2.notifyDataSetChanged();
        } else {

            // create a dataset and give it a type
            set1 = new LineDataSet(yValues1, "DataSet");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            //orange
            set1.setColor(Color.parseColor("#ff9e5d"));
            set1.setDrawCircles(true);
            set1.setLineWidth(2f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.parseColor("#ff9e5d"));
            set1.setFillAlpha(255);
            set1.setDrawFilled(false);

            set1.setFillColor(Color.parseColor("#c6c6c6"));
            set1.setMode(LineDataSet.Mode.LINEAR);
            set1.setDrawCircleHole(true);
            set1.setCircleHoleRadius(2f);
            set1.setCircleColorHole(Color.WHITE);
//            set.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return lineChart.getAxisLeft().getAxisMaximum();
//                }
//            });

            // create a dataset and give it a type
            set2 = new LineDataSet(yValues2, "DataSet");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.parseColor("#61a4ff"));
            set2.setDrawCircles(true);
            set2.setCircleColor(Color.parseColor("#61a4ff"));
            set2.setLineWidth(2f);
            set2.setCircleRadius(4f);
            set2.setFillAlpha(255);
            set2.setDrawFilled(false);
            set2.setFillColor(Color.parseColor("#3464dd"));
            set2.setMode(LineDataSet.Mode.LINEAR);
            set2.setDrawCircleHole(true);
            set2.setCircleHoleRadius(2f);
            set2.setCircleColorHole(Color.WHITE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setDrawValues(false);

            // set data
            lineChart2.setData(data);
        }

        xAxis.setAxisMaximum(xValues.size());
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(xValues.size() % 2 == 0 ? xValues.size()/2 : (int)(xValues.size()/2) + 1,false);
        lineChart2.invalidate();

        lineChart2.setTouchEnabled(false); // 设置是否可以触摸
        lineChart2.setDragEnabled(false);// 是否可以拖拽
        lineChart2.setScaleEnabled(false);// 是否可以缩放
        lineChart2.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
    }

    private void initFiveBarChart(){
        lineChart3 = (LineChart) findViewById(R.id.linechart5);

        //设置显示边界
        lineChart3.setDrawBorders(false);

        //网格
        lineChart3.setDrawGridBackground(false);

        //不显示描述
        Legend legend = lineChart3.getLegend();
        legend.setEnabled(false);
        lineChart3.setDescription(null);

        //X轴
        XAxis xAxis = lineChart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.parseColor("#ededed"));
        xAxis.setAxisLineColor(Color.parseColor("#dddddd"));

        //Y轴
        YAxis yLeftAxis = lineChart3.getAxisLeft();
        YAxis yRrightAxis = lineChart3.getAxisRight();
        yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        yRrightAxis.setAxisLineColor(Color.TRANSPARENT);
        yLeftAxis.setAxisMaximum(140);
        yLeftAxis.setAxisMinimum(0);
        yRrightAxis.setAxisMaximum(140);
        yRrightAxis.setAxisMinimum(0);
        yRrightAxis.setDrawLabels(false);
        yLeftAxis.setLabelCount(140 / 20);
        yLeftAxis.setGridColor(Color.parseColor("#ededed"));
        yRrightAxis.setGridColor(Color.parseColor("#ededed"));

        //保证Y轴从0开始，不然会上移一点
        yLeftAxis.setAxisMinimum(0f);
        yRrightAxis.setAxisMinimum(0f);

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        ArrayList<String> xVaulesName = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            xVaulesName.add((i < 10 ? "0" : "") + i);
            xValues.add((float) i);
        }
        xAxis.setLabelCount(xValues.size(),false);
        MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
        xAxis.setValueFormatter(xFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //设置y轴的数据()
        List<Entry> yValues1 = new ArrayList<>();
        List<Entry> yValues2 = new ArrayList<>();
        int shujucount = 11;
        for (int i = 0;i < 31; i++){
            if(i % 2 == 0){
                yValues1.add(new Entry(i,120f));
                yValues2.add(new Entry(i,60f));
            }else {
                yValues1.add(new Entry(i,100f));
                yValues2.add(new Entry(i,50f));
            }
        }


        LineDataSet set1 = null;
        LineDataSet set2 = null;
        if (lineChart3.getData() != null &&
                lineChart3.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart3.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) lineChart3.getData().getDataSetByIndex(1);
            set1.setValues(yValues1);
            set2.setValues(yValues2);
            lineChart3.getData().notifyDataChanged();
            lineChart3.notifyDataSetChanged();
        } else {

            // create a dataset and give it a type
            set1 = new LineDataSet(yValues1, "DataSet");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            //orange
            set1.setColor(Color.parseColor("#ff9e5d"));
            set1.setDrawCircles(true);
            set1.setLineWidth(2f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.parseColor("#ff9e5d"));
            set1.setFillAlpha(255);
            set1.setDrawFilled(false);

            set1.setFillColor(Color.parseColor("#c6c6c6"));
            set1.setMode(LineDataSet.Mode.LINEAR);
            set1.setDrawCircleHole(true);
            set1.setCircleHoleRadius(2f);
            set1.setCircleColorHole(Color.WHITE);
//            set.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return lineChart.getAxisLeft().getAxisMaximum();
//                }
//            });

            // create a dataset and give it a type
            set2 = new LineDataSet(yValues2, "DataSet");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.parseColor("#61a4ff"));
            set2.setDrawCircles(true);
            set2.setCircleColor(Color.parseColor("#61a4ff"));
            set2.setLineWidth(2f);
            set2.setCircleRadius(4f);
            set2.setFillAlpha(255);
            set2.setDrawFilled(false);
            set2.setFillColor(Color.parseColor("#3464dd"));
            set2.setMode(LineDataSet.Mode.LINEAR);
            set2.setDrawCircleHole(true);
            set2.setCircleHoleRadius(2f);
            set2.setCircleColorHole(Color.WHITE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setDrawValues(false);

            // set data
            lineChart3.setData(data);
        }

        xAxis.setAxisMaximum(xValues.size());
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(xValues.size() % 2 == 0 ? xValues.size()/2 : (int)(xValues.size()/2) + 1,false);
        lineChart3.invalidate();

        lineChart3.setTouchEnabled(false); // 设置是否可以触摸
        lineChart3.setDragEnabled(false);// 是否可以拖拽
        lineChart3.setScaleEnabled(false);// 是否可以缩放
        lineChart3.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
        }
    }
}
