package com.isoftstone.smartsite.model.map.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.Text;
import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.UserBean;
import com.isoftstone.smartsite.http.patroltask.PatrolPositionBean;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;
import com.isoftstone.smartsite.http.patroluser.UserTrackBean;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.model.map.adapter.MapTaskDetailRecyclerViewAdapter;
import com.isoftstone.smartsite.utils.DensityUtils;
import com.isoftstone.smartsite.utils.ImageUtils;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER;

/**
 * Created by zw on 2017/11/19.
 */

public class ConstructionMontitoringMapActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener {


    private final int NO_DATA = 0x0001;
    private final int INIT_DATA = 0x0002;
    private final int UPDATE_USER_GUIJI = 0x0003;
    private final int NO_GUI_JI = 0x0004;

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
    private ImageView iv_task_status;
    private TextView tv_time;
    private TextView tv_address;
    private TextView tv_person;
    private PopupWindow mPopWindow;
    private ImageView iv_start_task;
    private View content_parent;
    private LoadingDailog loadingDailog;
    private HttpPost httpPost;
    private PatrolTaskBean patrolTaskBean;
    private ArrayList<BaseUserBean> userBeans;
    private ArrayList<PatrolPositionBean> patrolPositionBeans;

    private ArrayList<UserTrackBean> currentUserTrackBeans;

    private BaseUserBean currentUserBean;
//    private long loginUseId = HttpPost.mLoginBean.getmUserBean().getLoginUser().getId();
//upload/xuncha187.png
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NO_DATA:
                    loadingDailog.dismiss();
                    ToastUtils.showLong("没有获取到任务详情！");
                    break;
                case INIT_DATA:
                    loadingDailog.dismiss();
                    updateRecyclerView();
                    updateTaskPoints();
                    updateUserGuiji();
                case UPDATE_USER_GUIJI:
                    loadingDailog.dismiss();
                    addAndRemoveUserGuiJi();
                    break;
                case NO_GUI_JI:
                    ToastUtils.showLong("没有获取到轨迹！");
                    if(polyline != null){
                        polyline.remove();
                    }
                    break;
            }
        }
    };
    private Polyline polyline;
    private List<Marker> markers;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_construction_monitoring;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initToolBar();
        iv_status = (ImageView) findViewById(R.id.iv_status);
        httpPost = new HttpPost();
        markers = new ArrayList<>();
        initMapView(savedInstanceState);
        initLocation(aotiLatLon);
        initLoadingDialog();
        initRecyclerView();
        initPopWindow();
        initNowLocation();

        getData();

//        initMarkers();
//        addDoneRound(aotiLatLon);
//        addNotDoneRound(latLng2);

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
                if(patrolTaskBean != null){
                    userBeans = patrolTaskBean.getUsers();
                    patrolPositionBeans = patrolTaskBean.getPatrolPositions();
                    mHandler.sendEmptyMessage(INIT_DATA);
                } else {
                    mHandler.sendEmptyMessage(NO_DATA);
                }
            }
        }).start();
    }

    private void updateRecyclerView(){
        recyclerViewAdapter.setDatas(userBeans);
        if(currentUserBean == null){
            currentUserBean = userBeans.get(0);
        }
    }

    private void updateTaskPoints(){
        if(patrolPositionBeans == null) return;

        for (int i = 0; i < patrolPositionBeans.size(); i++) {
            PatrolPositionBean bean = patrolPositionBeans.get(i);
            LatLng latLng = new LatLng(bean.getLatitude(),bean.getLongitude());
            //0未巡查  1已巡查
            if(bean.getStatus() == 0){
                addNotDoneRound(latLng);
            } else if(bean.getStatus() == 1){
                addDoneRound(latLng);
                addMarker(bean,latLng);
            }

        }
    }

    private void addMarker(final PatrolPositionBean bean, LatLng latLng){
        BaseUserBean baseUserBean = bean.getUser();

        final MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        final String url = HttpPost.URL + "/" + baseUserBean.imageData;


//        markerOption.icon(BitmapDescriptorFactory.fromBitmap(setGeniusIcon(url)));

        BitmapTypeRequest<String> bitmapTypeRequest = Glide.with(getApplicationContext()).load(url)
                .asBitmap();

        SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                View centerView = LayoutInflater.from(ConstructionMontitoringMapActivity.this).inflate(R.layout.layout_marker_with_icon,null);
                CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);
                civ.setImageBitmap(resource);
                markerOption.icon(BitmapDescriptorFactory.fromView(centerView));
                Marker marker = aMap.addMarker(markerOption);
                marker.setAnchor(0.5f,1f);
                marker.setObject(bean);
                markers.add(marker);
            }
        };

        bitmapTypeRequest.into(simpleTarget);

    }

    private void updateUserGuiji(){
        if(currentUserBean == null) return;

        final UserTrackBean bean = new UserTrackBean();
        bean.setTaskId(patrolTaskBean.getTaskId());
        long userId = currentUserBean.getId();
        int intUserId = (int) userId;
        bean.setUserId(intUserId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentUserTrackBeans = httpPost.findByUserIdAndTaskId(bean);
                if(currentUserBean == null || currentUserTrackBeans.size() == 0){
                    mHandler.sendEmptyMessage(NO_GUI_JI);
                }else {
                    mHandler.sendEmptyMessage(UPDATE_USER_GUIJI);
                }
            }
        }).start();

    }

    private void addAndRemoveUserGuiJi(){
        if(currentUserTrackBeans == null) return;

        if(polyline != null){
            polyline.remove();
        }

        List<LatLng> latLngs = new ArrayList<LatLng>();
        for (int i = 0; i < currentUserTrackBeans.size(); i++) {
            UserTrackBean bean = currentUserTrackBeans.get(i);
            LatLng latLng = new LatLng(bean.getLatitude(),bean.getLongitude());
            latLngs.add(latLng);
        }
        polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(20).color(Color.parseColor("#4f4de6")));
    }

    private void initRecyclerView(){
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        recyclerViewAdapter = new MapTaskDetailRecyclerViewAdapter(this,userBeans);
        recyclerViewAdapter.setItemClickListener(new MapTaskDetailRecyclerViewAdapter.onMapTaskItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rv.getChildAdapterPosition(view);
                currentUserBean = userBeans.get(position);
                recyclerViewAdapter.updateViews(position);
                updateUserGuiji();
            }
        });
        rv.setAdapter(recyclerViewAdapter);
    }

    private void initPopWindow(){
        View popRootView = LayoutInflater.from(this).inflate(R.layout.layout_construction_monitoring_popwindw,null);
        popRootView.findViewById(R.id.iv_dismiss).setOnClickListener(this);
        tv_task_name = (TextView) popRootView.findViewById(R.id.tv_task_name);
        iv_task_status = (ImageView) popRootView.findViewById(R.id.iv_status);
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
        myLocationStyle.interval(3000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                updateNowLocation(location);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;

            case R.id.iv_start_task:

                break;
            case R.id.iv_dismiss:
                mPopWindow.dismiss();
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getObject() != null){
            PatrolPositionBean bean = (PatrolPositionBean) marker.getObject();
            tv_task_name.setText(bean.getPosition());
            if(bean.getStatus() == 0){
                iv_start_task.setVisibility(View.VISIBLE);
                content_parent.setVisibility(View.GONE);
                iv_task_status.setImageResource(R.drawable.weiwancheng);
                tv_time.setText("");
            } else if(bean.getStatus() == 1){
                iv_start_task.setVisibility(View.GONE);
                content_parent.setVisibility(View.VISIBLE);
                iv_task_status.setImageResource(R.drawable.yiwancheng);
                if(bean.getExecutionTime().length() >=10){
                    tv_time.setText(bean.getExecutionTime().substring(0,10));
                }else {
                    tv_time.setText(bean.getExecutionTime());
                }
                tv_person.setText(bean.getUser().name);
                tv_address.setText(bean.getUser().address);
            }
        }
        mPopWindow.showAtLocation(mapView, Gravity.BOTTOM,0, DensityUtils.dip2px(this,-8));
        return true;
    }


    //30秒间隔上传一次用户的坐标
    private void updateNowLocation(Location location){
        final double lat = location.getLatitude();
        final double lon = location.getLongitude();
        LogUtils.e("zw","lat : " + lat);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                httpPost.userTrack(loginUseId,patrolTaskBean.getTaskId(),lon,lat);
            }
        }).start();*/
    }

 /*   public Bitmap setGeniusIcon(String url) {
        Bitmap bitmap = null;
        View centerView = LayoutInflater.from(this).inflate(R.layout.layout_marker_with_icon,null);
        CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);
        ImageUtils.loadImage(civ,url);
        bitmap = convertViewToBitmap(centerView);
        return bitmap;
    }

    public Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }*/

}
