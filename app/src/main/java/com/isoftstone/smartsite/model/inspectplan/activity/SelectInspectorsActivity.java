package com.isoftstone.smartsite.model.inspectplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.PatrolBean;
import com.isoftstone.smartsite.model.inspectplan.adapter.InspectorsAdapter;
import com.isoftstone.smartsite.model.inspectplan.adapter.InspectorsIconAdapter;
import com.isoftstone.smartsite.model.inspectplan.data.InspectorData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-24.
 */

public class SelectInspectorsActivity extends Activity {
    public ArrayList<InspectorData> list = null;
    public InspectorData contactDate;
    private ListView listView_Contact;
    private HorizontalScrollView listView_Icon;
    public InspectorsAdapter contactAdapter;
    public InspectorsIconAdapter iconAdapter;
    private LinearLayout linearLayout_inspector_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_inspectors);
        listView_Contact = (ListView) findViewById(R.id.listView_Contact) ;
        listView_Icon = (HorizontalScrollView) findViewById(R.id.listView_Icon) ;
        linearLayout_inspector_icon = (LinearLayout) findViewById(R.id.linear_inspector_icon);
        initDate();//本地填充巡查人员数据
        initSideBar();
        contactAdapter = new InspectorsAdapter(this, list);
        listView_Contact.setAdapter(contactAdapter);
        refreshHorizontalScrollView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void initDate() {
        list = new ArrayList<InspectorData>();
        for (int i = 0; i < 50; i++) {
            contactDate = new InspectorData();
            if (i < 10) {
                contactDate.setSort("A");
            } else if (i < 20) {
                contactDate.setSort("B");
            } else if (i < 30) {
                contactDate.setSort("C");
            } else if (i < 40) {
                contactDate.setSort("D");
            } else if (i < 50) {
                contactDate.setSort("F");
            }
            contactDate.setSelected(true);
            contactDate.setContactName("姓名" + String.valueOf(i));
            if (i % 10 == 0) {
                contactDate.setVisible(View.VISIBLE);
            } else {
                contactDate.setVisible(View.GONE);
            }
            list.add(contactDate);
        }
    }

    public void initSideBar() {
        WaveSideBar sideBar = (WaveSideBar) findViewById(R.id.side_bar);
        sideBar.setIndexItems("↑","☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#");
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                Log.d("WaveSideBar", index);
                // Do something here ....
                for (int i = 0; i < list.size(); i++){
                    if (list.get(i).getSort().equals(index)){
                        listView_Contact.smoothScrollToPosition(i);
                        contactAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        });
    }

    public void refreshHorizontalScrollView() {
        for (int i = 0; i < list.size(); i ++)
        {
            View inflate = View.inflate(this, R.layout.inspector_icon_item, null);
            ImageView inspector_icon = (ImageView) inflate.findViewById(R.id.imageView_icon);
            if ( list.get(i).getSelected() ) {
                linearLayout_inspector_icon.addView(inflate);
            }
        }
    }
}
