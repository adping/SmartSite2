package com.isoftstone.smartsite.model.map.ui;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.model.map.adapter.MapTaskDetailRecyclerViewAdapter;
import com.isoftstone.smartsite.utils.DensityUtils;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zw on 2017/11/19.
 */

public class MapTaskDetailActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener {

    private MapView mapView;
    private AMap aMap;
    private LatLng aotiLatLon = new LatLng(30.482348,114.514417);
    private LatLng startLatlon = new LatLng(30.490513,114.467518);
    private LatLng latLng2 = new LatLng(30.489206,114.474417);
    private LatLng endLatlon = new LatLng(30.483978,114.470968);
    private LatLng startLatlon1 = new LatLng(30.486654,114.482969);
    private RecyclerView rv;
    private Marker startMarker;
    private Polyline polyline;
    private Marker endMarker;
    private MapTaskDetailRecyclerViewAdapter recyclerViewAdapter;
    private Marker roundMarker;
    private TextView tv_task_name;
    private ImageView iv_status;
    private TextView tv_time;
    private TextView tv_address;
    private PopupWindow mPopWindow;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_task_detail;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initToorBar();
        initRecyclerView();
        initMapView(savedInstanceState);
        initPopWindow();
        addTrail();
    }

    private void initToorBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        ImageButton btn = (ImageButton) findViewById(R.id.btn_icon);
        btn.setImageResource(R.drawable.search);
        btn.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("任务详情");
    }

    private void initRecyclerView(){
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        recyclerViewAdapter = new MapTaskDetailRecyclerViewAdapter(this);
        recyclerViewAdapter.setItemClickListener(new MapTaskDetailRecyclerViewAdapter.onMapTaskItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rv.getChildAdapterPosition(view);
                recyclerViewAdapter.updateViews(position);
                test();
            }
        });
        rv.setAdapter(recyclerViewAdapter);
    }

    private void test(){
        removeTrail();

        if(mPopWindow.isShowing()){
            mPopWindow.dismiss();
        }

        //起点
        addStartMarker(startLatlon1);
        //经过描线
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(startLatlon1);
        latLngs.add(latLng2);
        latLngs.add(endLatlon);
        polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(20).color(Color.parseColor("#4f4de6")));
        //终点
        addEndMarker(endLatlon);
        addRoundMarker(endLatlon);

        showPopWindow();

        initLocation(startLatlon1);
    }

    private void initMapView(Bundle savedInstanceState){
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setOnMarkerClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    private void initPopWindow(){
        View popRootView = LayoutInflater.from(this).inflate(R.layout.layout_map_task_detail_popwindow,null);
        tv_task_name = (TextView) popRootView.findViewById(R.id.tv_task_name);
        iv_status = (ImageView) popRootView.findViewById(R.id.iv_status);
        tv_time = (TextView) popRootView.findViewById(R.id.tv_time);
        tv_address = (TextView) popRootView.findViewById(R.id.tv_address);

        mPopWindow = new PopupWindow(this);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(popRootView);
        mPopWindow.setOutsideTouchable(false);
        mPopWindow.setFocusable(true);
        mPopWindow.setTouchable(true);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void showPopWindow(){
        mPopWindow.showAtLocation(mapView, Gravity.BOTTOM,0, DensityUtils.dip2px(this,-8));
    }

    private void initLocation(LatLng latLng){
        CameraPosition mCameraPosition = new CameraPosition(latLng,13f,0,0);

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(mCameraPosition);
        if(aMap != null){
            aMap.animateCamera(update);
        }

    }

    private void addTrail(){
        removeTrail();

        if(mPopWindow.isShowing()){
            mPopWindow.dismiss();
        }

        //起点
        addStartMarker(startLatlon);
        //经过描线
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(startLatlon);
        latLngs.add(latLng2);
        latLngs.add(endLatlon);
        polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(20).color(Color.parseColor("#4f4de6")));
        //终点
        addEndMarker(endLatlon);

        addRoundMarker(endLatlon);

        initLocation(startLatlon);

    }

    private void removeTrail(){
        if(startMarker != null){
            startMarker.remove();
        }
        if(polyline != null){
            polyline.remove();
        }
        if(endMarker != null){
            endMarker.remove();
        }
        if(roundMarker != null){
            roundMarker.remove();
        }
    }

    private void addStartMarker(LatLng latLng){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.blueround));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        startMarker = aMap.addMarker(markerOption);
        startMarker.setAnchor(0.5f,0.5f);
    }

    private void addEndMarker(LatLng latLng){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        View centerView = LayoutInflater.from(this).inflate(R.layout.layout_map_corner_marker,null);
        CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);
        civ.setImageResource(R.drawable.test);
        markerOption.icon(BitmapDescriptorFactory.fromView(centerView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        endMarker = aMap.addMarker(markerOption);
        endMarker.setAnchor(0.5f,0.5f);
    }

    private void addRoundMarker(LatLng latLng){
        MarkerOptions markerOption1 = new MarkerOptions();

        markerOption1.position(latLng);

        markerOption1.visible(true);

        markerOption1.draggable(false);//设置Marker可拖动
        View centerView = LayoutInflater.from(this).inflate(R.layout.layout_map_task_backround,null);
        markerOption1.icon(BitmapDescriptorFactory.fromView(centerView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption1.setFlat(true);//设置marker平贴地图效果

        roundMarker = aMap.addMarker(markerOption1);
        roundMarker.setAnchor(0.5f,0.5f);
    }

    private boolean isFirstIn = true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isFirstIn){
            showPopWindow();
            isFirstIn = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_icon:

                break;
            case R.id.iv_dismiss:
                mPopWindow.dismiss();
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }
}
