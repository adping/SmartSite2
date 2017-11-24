package com.isoftstone.smartsite;

import android.os.Bundle;

import com.isoftstone.smartsite.base.BaseFragment;

/**
 * Created by 2013020220 on 2017/11/23.
 */

public class MonthSlagcarInfoFragment extends BaseFragment {
    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_slagcar_info;
    }

    private void initView(){

    }
}
