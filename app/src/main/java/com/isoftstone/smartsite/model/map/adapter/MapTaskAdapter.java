package com.isoftstone.smartsite.model.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.isoftstone.smartsite.R;

/**
 * Created by zw on 2017/11/19.
 */

public class MapTaskAdapter extends BaseAdapter{

    private Context mContext;


    public MapTaskAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 3;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_task_item,parent,false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_task_name);
        tv.setText("这是测试任务 : " + position);

        return convertView;
    }
}
