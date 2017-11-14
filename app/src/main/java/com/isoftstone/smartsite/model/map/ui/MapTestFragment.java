package com.isoftstone.smartsite.model.map.ui;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;

import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.MyLocationStyle;


//import com.amap.api.maps2d.AMap;
//import com.amap.api.maps2d.CameraUpdateFactory;
//import com.amap.api.maps2d.MapView;
//import com.amap.api.maps2d.SupportMapFragment;
//import com.amap.api.maps2d.model.MyLocationStyle;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.http.DeviceBean;
import com.isoftstone.smartsite.http.DevicesBean;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.utils.LogUtils;

import java.util.ArrayList;


/**
 * Created by zw on 2017/10/15.
 */

public class MapTestFragment extends BaseFragment {




    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_map_test;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final HttpPost mHttpPost = new HttpPost();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<DevicesBean> list = mHttpPost.getDevices("","","","");
                LogUtils.d(TAG,list);
            }
        }).start();






    }

    private void initView(Bundle savedInstanceState) {



    }


}
