package com.isoftstone.smartsite.model.main.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.MessageBean;
import com.isoftstone.smartsite.http.MessageListBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/16.
 */

public class InstantMessageAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private ArrayList<MessageBean> mData = new ArrayList<MessageBean>();
    private Context mContext = null;

    public InstantMessageAdapter(Context context){
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<MessageBean> list){
        mData = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mData.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.instantmsg_adapter, null);
            holder.map = (TextView)convertView.findViewById(R.id.textView_map);
            holder.info = (TextView)convertView.findViewById(R.id.textView_info);
            holder.time = (TextView)convertView.findViewById(R.id.textView_time);
            holder.type = (ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else {

            holder = (ViewHolder)convertView.getTag();
        }
        MessageBean messageBean = mData.get(position);
        holder.info.setText(messageBean.getContent());
        holder.map.setText(messageBean.getTitle());
        int length = messageBean.getUpdateTime().length();
        if(length > 5){
            holder.time.setText(messageBean.getUpdateTime().substring(length-5));
        }else {
            holder.time.setText(messageBean.getUpdateTime());
        }

        MessageBean.InfoType  infoType =  messageBean.getInfoType();
        if (infoType != null) {
            if(infoType.getInfoTypeCode().startsWith("1")){
                holder.type.setBackground(mContext.getDrawable(R.drawable.main_huanjing_icon));
            }else if(infoType.getInfoTypeCode().startsWith("2")){
                holder.type.setBackground(mContext.getDrawable(R.drawable.main_shiping_icon));
            }else if(infoType.getInfoTypeCode().startsWith("3")){
                holder.type.setBackground(mContext.getDrawable(R.drawable.thirdmessage));
            }
        } else {
            //throw an exception....
        }

        return convertView;
    }

    public final class ViewHolder{
        public TextView map;
        public TextView info;
        public TextView time;
        public ImageView type;
    }
}
