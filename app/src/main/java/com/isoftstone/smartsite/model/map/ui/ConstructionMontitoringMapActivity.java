package com.isoftstone.smartsite.model.map.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Text;
import com.android.tu.loadingdialog.LoadingDailog;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;
import com.isoftstone.smartsite.model.map.adapter.MapTaskDetailRecyclerViewAdapter;
import com.isoftstone.smartsite.utils.LogUtils;

import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER;

/**
 * Created by zw on 2017/11/19.
 */

public class ConstructionMontitoringMapActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener {

    private MapView mapView;
    private AMap aMap;
    private ImageView iv_status;
    private MapTaskDetailRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView rv;

    private LatLng aotiLatLon = new LatLng(30.482348,114.514417);
    private LatLng startLatlon = new LatLng(30.490513,114.467518);
    private LatLng latLng2 = new LatLng(30.489206,114.474417);
    private LatLng endLatlon = new LatLng(30.483978,114.470968);
    private LatLng startLatlon1 = new LatLng(30.486654,114.482969);
    private TextView tv_task_name;
    private TextView tv_task_status;
    private TextView tv_time;
    private TextView tv_address;
    private TextView tv_person;
    private PopupWindow mPopWindow;
    private ImageView iv_start_task;
    private View content_parent;
    private LoadingDailog loadingDailog;
    private HttpPost httpPost;
    private PatrolTaskBean patrolTaskBean;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_construction_monitoring;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initToolBar();
        iv_status = (ImageView) findViewById(R.id.iv_status);
        httpPost = new HttpPost();
        initMapView(savedInstanceState);
        initLocation(aotiLatLon);
        initLoadingDialog();

        getData();

        initRecyclerView();
        initPopWindow();
        initMarkers();
        addDoneRound(aotiLatLon);
        addNotDoneRound(latLng2);
        initNowLocation();
    }

    private void initToolBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        ImageButton btn = (ImageButton) findViewById(R.id.btn_icon);
        btn.setImageResource(R.drawable.search);
        btn.setOnClickListener(this);
        btn.setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("任务详情");
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

    private void initLocation(LatLng latLng){
        CameraPosition mCameraPosition = new CameraPosition(latLng,13f,0,0);

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(mCameraPosition);
        if(aMap != null){
            aMap.animateCamera(update);
        }

    }

    private void initLoadingDialog(){
        loadingDailog = new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(true).create();
    }

    private void getData(){
        loadingDailog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                patrolTaskBean= httpPost.patrolTaskFindOne(64);
                LogUtils.e(TAG,patrolTaskBean.toString());
            }
        }).start();
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
            }
        });
        rv.setAdapter(recyclerViewAdapter);
    }

    private void initPopWindow(){
        View popRootView = LayoutInflater.from(this).inflate(R.layout.layout_construction_monitoring_popwindw,null);
        tv_task_name = (TextView) popRootView.findViewById(R.id.tv_task_name);
        tv_task_status = (TextView) popRootView.findViewById(R.id.iv_status);
        tv_time = (TextView) popRootView.findViewById(R.id.tv_time);
        tv_person = (TextView) popRootView.findViewById(R.id.tv_person);
        tv_address = (TextView) popRootView.findViewById(R.id.tv_address);
        iv_start_task = (ImageView) popRootView.findViewById(R.id.iv_start_task);
        iv_start_task.setVisibility(View.GONE);
        iv_start_task.setOnClickListener(this);
        content_parent = popRootView.findViewById(R.id.content_parent);

        mPopWindow = new PopupWindow(this);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(popRootView);
        mPopWindow.setOutsideTouchable(false);
        mPopWindow.setFocusable(true);
        mPopWindow.setTouchable(true);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                /*if(roundMarker != null){
                    roundMarker.remove();
                }*/
            }
        });
    }

    private void initMarkers(){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(aotiLatLon);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        View centerView = LayoutInflater.from(this).inflate(R.layout.layout_marker_with_icon,null);
        markerOption.icon(BitmapDescriptorFactory.fromView(centerView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        Marker startMarker = aMap.addMarker(markerOption);
        startMarker.setAnchor(0.5f,1f);
    }

    private void addDoneRound(LatLng latLng){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.yiwanchengdianwei));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        Marker startMarker = aMap.addMarker(markerOption);
        startMarker.setAnchor(0.5f,0.5f);
    }

    private void addNotDoneRound(LatLng latLng){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.weiwanchengdianwei));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        Marker startMarker = aMap.addMarker(markerOption);
        startMarker.setAnchor(0.5f,0.5f);
    }

    private void initNowLocation(){
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;

            case R.id.btn_icon:

                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }
}
