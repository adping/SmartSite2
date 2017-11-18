package com.isoftstone.smartsite.model.video;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.system.ui.PermissionsActivity;
import com.isoftstone.smartsite.model.system.ui.PermissionsChecker;
import com.isoftstone.smartsite.model.video.Adapter.PhotoGridAdapter;
import com.isoftstone.smartsite.model.video.bean.PhotoInfo;
import com.isoftstone.smartsite.model.video.bean.PhotoList;
import com.isoftstone.smartsite.model.video.utils.CheckImageLoaderConfiguration;
import com.isoftstone.smartsite.model.video.utils.UniversalImageLoadTool;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SnapPicturesActivity extends Activity {
	
	private GridView gridView;
	private PhotoGridAdapter photoAdapter;
	private List<PhotoInfo> list;

	@Override
	public void onStart() {
		super.onStart();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_snap_pictures);

		gridView = (GridView) findViewById(R.id.gv_photos);

		if(null == bundle) {
			bundle = this.getIntent().getExtras();
		}

		PhotoList photoGrid = (PhotoList) bundle.getSerializable("list");
		list = new ArrayList<PhotoInfo>();
		list.addAll(photoGrid.getList());
		photoAdapter=new PhotoGridAdapter(this,list);

		gridView.setAdapter(photoAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				//打开指定的一张照片
				String pictureFilepath = list.get(position).getPath_absolute();
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				//ToastUtils.showShort(pictureFilepath);
				Log.i("zyf", "zzz   : " + pictureFilepath );
				File file = new File(pictureFilepath);
				Uri imageUri ;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					//申请权限
					intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					//getUriForFile的第二个参数就是Manifest中的authorities
					imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.isoftstone.smartsite.fileprovider", file);
				} else {
					imageUri = Uri.fromFile(file);//Uri.parse(pictureFilepath)
				}
				intent.setDataAndType(imageUri, "image/*");
				startActivity(intent);

				//打开系统相册浏览照片  
				/**Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
				startActivity(intent2);*/
			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState==0){
					UniversalImageLoadTool.resume();
				}else{
					UniversalImageLoadTool.pause();
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
			}
		});
	}
}
