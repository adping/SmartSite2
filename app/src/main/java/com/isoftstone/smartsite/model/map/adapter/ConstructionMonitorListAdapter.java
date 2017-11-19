package com.isoftstone.smartsite.model.map.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.tripartite.view.MyListView;

/**
 * Created by zw on 2017/11/19.
 */

public class ConstructionMonitorListAdapter extends BaseAdapter{

    private Context mContext;

    public ConstructionMonitorListAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_construction_monitor_list_item,parent,false);
            MyListView myListView = (MyListView) convertView.findViewById(R.id.mlv);
            myListView.setDivider(new ColorDrawable(Color.parseColor("#eeeeee")));
            myListView.setDividerHeight(2);
            myListView.setAdapter(new MapTaskAdapter(mContext));
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }

        return convertView;
    }

}
