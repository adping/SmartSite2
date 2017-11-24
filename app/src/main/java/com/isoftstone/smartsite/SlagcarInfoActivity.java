package com.isoftstone.smartsite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.model.dirtcar.activity.DirtCarListActivity;

import java.util.ArrayList;

/**
 * Created by 2013020220 on 2017/11/22.
 */

public class SlagcarInfoActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton btn_icon;
    private ImageButton btn_back;
    private TextView title;
    private ArrayList<Fragment> fragmentLists;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.vp);
        btn_icon = (ImageButton) findViewById(R.id.btn_icon);
        btn_icon.setImageResource(R.drawable.environmentlist);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("渣土车概览");
        btn_back.setOnClickListener(this);
        btn_icon.setOnClickListener(this);
        fragmentLists = new ArrayList<Fragment>();
        initTablayout();
        initViewPagerAndFragment();
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentLists);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_slagcar_info;
    }

    private void initViewPagerAndFragment() {
        fragmentLists.add(new MonthSlagcarInfoFragment());
        fragmentLists.add(new DaySlagcarInfoFragment());
    }

    private void initTablayout() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText("月视图"));
        tabLayout.addTab(tabLayout.newTab().setText("日视图"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_icon:
                enterDirctCar();
                break;
            default:
                break;
        }
    }

    private void enterDirctCar(){
        Intent intent = new Intent(this,DirtCarListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> list;

        public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> list) {
            super(fragmentManager);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }
    }
}
