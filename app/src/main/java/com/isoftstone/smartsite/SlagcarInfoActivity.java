package com.isoftstone.smartsite;

import android.content.Context;
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
    private String [] titles =new String[]{"月视图","日视图"};

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.vp);
        btn_icon = (ImageButton) findViewById(R.id.btn_icon);
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
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_icon:
                break;
            default:
                break;
        }
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

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
