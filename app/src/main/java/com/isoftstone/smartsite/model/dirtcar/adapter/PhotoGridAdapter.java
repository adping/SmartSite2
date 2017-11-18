package com.isoftstone.smartsite.model.dirtcar.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.dirtcar.imagecache.ImageLoader;
import com.isoftstone.smartsite.model.video.bean.PhotoInfo;
import com.isoftstone.smartsite.model.video.imgaware.RotateImageViewAware;
import com.isoftstone.smartsite.model.video.utils.ThumbnailsUtil;
import com.isoftstone.smartsite.model.video.utils.UniversalImageLoadTool;

import java.util.ArrayList;
import java.util.List;

public class PhotoGridAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<String> mListData;
	private ViewHolder viewHolder;
	private int mWidth;
	private ImageLoader mImageLoader;
	private Context mContext;

	public PhotoGridAdapter(Context context, ArrayList<String> data, ImageLoader imageLoader){
		mContext = context;
		this.mListData = data;
		if (imageLoader != null) {
			mImageLoader = imageLoader;
		} else {
			mImageLoader = new ImageLoader(context);
		}

		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels/3;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public Object getItem(int position) {
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup paramViewGroup) {
		String url = mListData.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.photogrid_item, null);
			ImageView imageView=(ImageView)convertView.findViewById(R.id.iv_thumbnail);
			viewHolder.mImage = imageView;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		LayoutParams layoutParams = viewHolder.mImage.getLayoutParams();
		layoutParams.width = mWidth;
		layoutParams.height =  mContext.getResources().getDimensionPixelOffset(R.dimen.grid_view_item_hight);
		viewHolder.mImage.setLayoutParams(layoutParams);

		mImageLoader.DisplayImage(url, viewHolder.mImage, false);

		return convertView;
	}
	public class ViewHolder{
		public ImageView mImage;
	}
}
