package com.isoftstone.smartsite.model.inspectplan.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.model.inspectplan.data.InspectorData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-24.
 */

public class InspectorsAdapter extends BaseAdapter {

    private ArrayList<InspectorData> list = null;
    private Context mContext;

    public InspectorsAdapter() {
        super();
    }

    public InspectorsAdapter(Context mContext, ArrayList<InspectorData> list) {
        this.mContext = mContext;
        this.list = list;
        Log.i("ContactAdapter","list length is:" + list.size());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public InspectorData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final InspectorData contactDate = getItem(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.inspector_item, parent, false);
            holder = new ViewHolder();
            holder.textView_Sort = (TextView)convertView.findViewById(R.id.textView_sort);
            holder.textView_ContactName = (TextView)convertView.findViewById(R.id.textView_contactName);
            holder.imageView_ContactIcon = (ImageView)convertView.findViewById(R.id.imageView_contactIcon);
            holder.checkBox_ContactIsCheck = (CheckBox)convertView.findViewById(R.id.checkBox_contactIsCheck);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.textView_Sort.setText(contactDate.getSort());
        holder.textView_ContactName.setText(contactDate.getName());
        holder.textView_Sort.setVisibility(contactDate.getIsVisible());
        holder.checkBox_ContactIsCheck.setChecked(contactDate.getIsSelected());

        holder.checkBox_ContactIsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDate.setIsSelected(v.isSelected());
                Toast.makeText(mContext,"点击了多选框",Toast.LENGTH_SHORT).show();

            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView textView_Sort;
        public TextView textView_ContactName;
        public ImageView imageView_ContactIcon;
        public CheckBox checkBox_ContactIsCheck;
    }
}
