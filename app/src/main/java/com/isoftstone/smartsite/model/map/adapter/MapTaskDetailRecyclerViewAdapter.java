package com.isoftstone.smartsite.model.map.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.utils.DensityUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zw on 2017/11/19.
 */

public class MapTaskDetailRecyclerViewAdapter extends RecyclerView.Adapter<MapTaskDetailRecyclerViewAdapter.MapTaskDetailViewHolder> implements View.OnClickListener {

    private Context mContext;
    private onMapTaskItemClickListener mItemClickListener;
    private int currentPosition = 0;

    public MapTaskDetailRecyclerViewAdapter(Context context){
        this.mContext = context;
    }

    public void setItemClickListener(onMapTaskItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void updateViews(int position){
        currentPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public MapTaskDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_map_rv_task_detail_item,parent,false);
        convertView.setOnClickListener(this);
        MapTaskDetailViewHolder viewHolder = new MapTaskDetailViewHolder(convertView);
        viewHolder.civ = (CircleImageView) convertView.findViewById(R.id.civ);
        viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MapTaskDetailViewHolder holder, int position) {

        if(position == currentPosition){
            holder.civ.setBorderColor(Color.parseColor("#4f6de6"));
        } else{
            holder.civ.setBorderColor(Color.TRANSPARENT);
        }
        holder.tv.setText("测试" + position);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onClick(View v) {
            mItemClickListener.onItemClick(v);
    }

    public class MapTaskDetailViewHolder extends RecyclerView.ViewHolder{

        CircleImageView civ;
        TextView tv;

        public MapTaskDetailViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface onMapTaskItemClickListener{

        public void onItemClick(View view);
    }
}
