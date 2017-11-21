package com.isoftstone.smartsite.model.map.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zw on 2017/11/21.
 */

public class MapSearchTaskPositionActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener {

    private MapView mapView;
    private AMap aMap;
    private EditText et;
    private LatLng currentLatLng;
    private Marker currentMarker;
    private List<LatLng> latLngs = new ArrayList<>();
    private List<String> latLngsName = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private LatLng aotiLatLon = new LatLng(30.482348,114.514417);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_search_task_position;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initToolBar();
        et = (EditText) findViewById(R.id.et);
        initMapView(savedInstanceState);
        initLocation(aotiLatLon);
    }

    private void initToolBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("任务地点");
        TextView tv_save = (TextView) findViewById(R.id.btn_icon_right);
        tv_save.setText("保存");
        tv_save.setOnClickListener(this);
    }

    private void initMapView(Bundle savedInstanceState){
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
    }

    private void initLocation(LatLng latLng){
        CameraPosition mCameraPosition = new CameraPosition(latLng,13f,0,0);

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(mCameraPosition);
        if(aMap != null){
            aMap.animateCamera(update);
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_icon_right:
                savePosition();
                break;
        }
    }

    private void savePosition(){
        if(currentMarker == null){
            ToastUtils.showShort("没有获取到坐标！");
            return;
        }

        if(latLngs.size() > 20){
            ToastUtils.showShort("最多只能输入20个地址！");
            return;
        }

        String name = et.getText().toString();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showShort("巡查点名称不能为空！");
            return;
        }
        for (int i = 0; i < latLngs.size(); i++) {
            LatLng latLng = latLngs.get(i);
            if(currentLatLng.latitude == latLng.latitude &&
                    currentLatLng.longitude == latLng.longitude){
                ToastUtils.showShort("该巡查点地址已存在！");
                return;
            }
            if(TextUtils.equals(name,latLngsName.get(i))){
                ToastUtils.showShort("该巡查点名称重复,请重新输入！");
                return;
            }
        }

        latLngs.add(currentLatLng);
        latLngsName.add(name);
        markers.add(currentMarker);
        currentMarker = null;
        ToastUtils.showShort("保存成功！");

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        currentLatLng = latLng;
        addMarker(latLng);
    }

    private void addMarker(LatLng latLng){

        if(currentMarker != null && !markers.contains(currentMarker)){
            currentMarker.remove();
        }

        MarkerOptions markerOption = new MarkerOptions();

        markerOption.position(latLng);

        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.xuanzedidian));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        currentMarker = aMap.addMarker(markerOption);
        currentMarker.setAnchor(0.5f,0.5f);
        LogUtils.e(TAG,"...");
    }
}
