package com.isoftstone.smartsite.model.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.patroltask.PatrolPositionBean;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;

import java.util.List;

/**
 * Created by zw on 2017/11/19.
 */

public class MapTaskAdapter extends BaseAdapter{

    private Context mContext;
    private List<PatrolTaskBean> beans;


    public MapTaskAdapter(Context context, List<PatrolTaskBean> data){
        this.mContext = context;
        this.beans = data;
    }

    public void setDatas(List<PatrolTaskBean> data){
        this.beans = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
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
        tv.setText(beans.get(position).getTaskName());

        return convertView;
    }
}
