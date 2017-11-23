package com.isoftstone.smartsite.model.dirtcar.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.muckcar.EvidencePhotoBean;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.model.dirtcar.adapter.ManualPhotographyAdapter;
import com.isoftstone.smartsite.model.dirtcar.bean.ManualPhotographyBean;
import com.isoftstone.smartsite.model.dirtcar.imagecache.ImageLoader;
import com.isoftstone.smartsite.model.system.ui.ActionSheetDialog;
import com.isoftstone.smartsite.model.system.ui.PhoneInfoUtils;
import com.isoftstone.smartsite.model.system.ui.SystemFragment;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhangyinfu on 2017/11/16.
 */

public class ManualPhotographyActivity extends BaseActivity  implements View.OnClickListener {

	protected static final String TAG = "zzz_ManualPhotography";
	/** Called when the activity is first created. */
	private ListView mListView;
	private ManualPhotographyAdapter mAdapter;
	ArrayList<ManualPhotographyBean> mListDate = new ArrayList<ManualPhotographyBean>();
	private Context mContext;
	private HttpPost mHttpPost;
	private Bitmap mHeadBitmap;//裁剪后得图片

	/* 请求识别码 选择图库*/
	private static final int IMAGE_REQUEST_CODE = 1;
	/* 请求识别码 照相机*/
	private static final int CAMERA_REQUEST_CODE = 2;
	/* 请求识别码 裁剪*/
	private static final int RESULECODE = 3;

	private static final int  HANDLER_MANUAL_PHOTPGRAPHY_START = 1;
	private static  final int  HANDLER_MANUAL_PHOTPGRAPHY_END = 2;

	private String mLicence = "";

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case HANDLER_MANUAL_PHOTPGRAPHY_START: {
					Thread thread = new Thread(){
						@Override
						public void run() {
							//mListDate =  mHttpPost.getDevices("1","","","");getEvidencePhotoList
							//showDlg("zzzzzzzzzzzzzzzz");
							try {
								PageableBean pageableBean = new PageableBean();
								ArrayList<EvidencePhotoBean> arrayList = mHttpPost.getEvidencePhotoList(mLicence, pageableBean).getContent();
								//Log.i("zzz","BBBBBBBBBBBBBBBBBBBBB     arrayList = " + arrayList);
								if (arrayList != null) {
									for (int i=0; i< arrayList.size(); i++) {
										String urlStr = arrayList.get(i).getSmallPhotoSrc();
										StringBuffer stringBuffer = new StringBuffer();
										if (urlStr != null) {
											String[] urlsStr = urlStr.split(",");
											for (int j=0; j<urlsStr.length; j++) {
												if (j == urlsStr.length -1) {
													stringBuffer.append(mHttpPost.getFileUrl(urlsStr[j]));
												} else {
													stringBuffer.append(mHttpPost.getFileUrl(urlsStr[j]) + ",");
												}
											}
										}
										Log.i("zzz","BBBBBBBB  arrayList.size() = " + arrayList.size() + "  & " + i + "  && " + arrayList.get(i).toString());
										Log.i("zzz","BBBBBBBB"  +  " mLicence = " +  mLicence  +   "    &&stringBuffer = " + stringBuffer.toString());
										BaseUserBean  baseUserBean = arrayList.get(i).getTakePhoroUser();
										ManualPhotographyBean manualPhotographyBean = null;
										if (baseUserBean != null) {
											manualPhotographyBean = new ManualPhotographyBean(arrayList.get(i).getLicence(),  mHttpPost.getFileUrl(arrayList.get(i).getTakePhoroUser().getImageData()), arrayList.get(i).getTakePhoroUser().getName(), arrayList.get(i).getTakePhotoTime(),  arrayList.get(i).getAddr(),  stringBuffer.toString(),  mHttpPost.getCompanyNameByid(Integer.parseInt(arrayList.get(i).getTakePhoroUser().getDepartmentId())));
										} else {
											manualPhotographyBean = new ManualPhotographyBean(arrayList.get(i).getLicence(),  null, null, arrayList.get(i).getTakePhotoTime(),  arrayList.get(i).getAddr(),  stringBuffer.toString(),  null);
										}
										mListDate.add(manualPhotographyBean);
									}

								}
							} catch (Exception e) {
								Log.e(TAG,"e : " + e.getMessage());
							}

							/**for (int i=0; i < 3; i++) {
								ManualPhotographyBean manualPhotographyBean = new ManualPhotographyBean("eA0000" + i, URLS[0], "李向双" + i , "2017-11-1" + i, "光谷五路和光谷六路交汇出", photoSrc, "湖北怡瑞有限公司" + i);
								mListDate.add(manualPhotographyBean);
							}*/
							mHandler.sendEmptyMessage(HANDLER_MANUAL_PHOTPGRAPHY_END);
						}
					};
					thread.start();
				}
				break;
				case HANDLER_MANUAL_PHOTPGRAPHY_END:{
					setListViewData();
				}
				break;
			}
		}
	};

	private void setListViewData() {
		setupViews();
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_manual_photography;
	}

	@Override
	protected void afterCreated(Bundle savedInstanceState) {
		mLicence = getIntent().getStringExtra("licence");
		initToolbar();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		queryDataTask updateTextTask = new queryDataTask(this);
		updateTextTask.execute();
	}

	private void initView() {
		mContext = getApplicationContext();
		mHttpPost = new HttpPost();
		mListView = (ListView) findViewById(R.id.main_lv_list);
		//mHandler.sendEmptyMessage(HANDLER_MANUAL_PHOTPGRAPHY_START);
	}

	private void setupViews() {
		mAdapter = new ManualPhotographyAdapter(this.getBaseContext(), mListDate, ManualPhotographyActivity.this);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mScrollListener);
	}

	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_FLING:
				mAdapter.setFlagBusy(true);
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				mAdapter.setFlagBusy(false);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				mAdapter.setFlagBusy(false);
				break;
			default:
				break;
			}
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};


	@Override
	protected void onDestroy() {

		if (mAdapter != null){
			ImageLoader imageLoader = mAdapter.getImageLoader();
			if (imageLoader != null) {
				imageLoader.clearCache();
			}
		}

		super.onDestroy();

		if (mHeadBitmap != null) {
			mHeadBitmap.recycle();
			mHeadBitmap = null;
			System.gc();
		}
	}

	private void initToolbar(){
		TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
		tv_title.setText(mLicence);

		findViewById(R.id.btn_back).setOnClickListener(ManualPhotographyActivity.this);

		ImageButton imageButton = (ImageButton) findViewById(R.id.btn_icon);
		imageButton.setImageDrawable(getResources().getDrawable(R.drawable.camera, null));
		imageButton.setOnClickListener(ManualPhotographyActivity.this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_back:
				ManualPhotographyActivity.this.finish();
				break;
			case R.id.btn_icon:
				new ActionSheetDialog(ManualPhotographyActivity.this)
						.builder(false)
						.setCancelable(true)
						.setCanceledOnTouchOutside(true)
						.addSheetItem(mContext.getText(R.string.camera).toString(),
								ActionSheetDialog.SheetItemColor.Blue,
								new ActionSheetDialog.OnSheetItemClickListener() {

									@Override
									public void onClick(int which) {
										//choseHeadImageFromGallery();
										Intent i = new Intent(ManualPhotographyActivity.this,UpdatePhotoActivity.class);
										i.putExtra("target_flag",1);
										i.putExtra("licence",mLicence);
										startActivity(i);
									}
								})
						.addSheetItem(mContext.getText(R.string.album).toString(),
								ActionSheetDialog.SheetItemColor.Blue,
								new ActionSheetDialog.OnSheetItemClickListener() {

									@Override
									public void onClick(int which) {
										//choseHeadImageFromCameraCapture();
										Intent i = new Intent(ManualPhotographyActivity.this,UpdatePhotoActivity.class);
										i.putExtra("target_flag",2);
										i.putExtra("licence",mLicence);
										startActivity(i);
									}
								}).show();

				//enterOtherActivity();
				break;
			default:
				break;
		}
	}

	// 从本地相册选取图片作为头像
	private void choseHeadImageFromGallery() {

		Intent intentFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// 设置文件类型
		intentFromGallery.setType("image/*");
		startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
	}

	// 启动手机相机拍摄照片作为头像
	private void choseHeadImageFromCameraCapture() {

		if (PhoneInfoUtils.hasSdcard()) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
					SystemFragment.HEAD_IMAGE_NAME)));
			startActivityForResult(intent, CAMERA_REQUEST_CODE);//采用ForResult打开
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
				// 如果是直接从相册获取
				case IMAGE_REQUEST_CODE:
					cropPhoto(intent.getData());//裁剪图片
					break;
				// 如果是调用相机拍照时
				case CAMERA_REQUEST_CODE:
					File temp = new File(Environment.getExternalStorageDirectory()
							+ "/" + SystemFragment.HEAD_IMAGE_NAME);
					cropPhoto(Uri.fromFile(temp));//裁剪图片
					break;
				case RESULECODE:
					if (intent != null) {
						Bundle extras = intent.getExtras();
						mHeadBitmap = extras.getParcelable("data");
					}
					break;
				default:
					break;

			}
			super.onActivityResult(requestCode, resultCode, intent);
		}
	}

	/**
	 * 裁剪图片
	 *
	 * @param uri 照片的url地址
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULECODE);
	}

	private void enterOtherActivity() {
		Intent intent = new Intent(mContext, CameraDetailsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	class queryDataTask extends AsyncTask<Void,Void,Boolean>{
		private Context context;
		queryDataTask(Context context) {
			this.context = context;
		}

		/**
		 * 运行在UI线程中，在调用doInBackground()之前执行
		 */
		@Override
		protected void onPreExecute() {
			//Toast.makeText(context,"开始执行",Toast.LENGTH_SHORT).show();
			showDlg("正在获取列表");
		}
		/**
		 * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				PageableBean pageableBean = new PageableBean();
				ArrayList<EvidencePhotoBean> arrayList = mHttpPost.getEvidencePhotoList(mLicence, pageableBean).getContent();
				//Log.i("zzz","BBBBBBBBBBBBBBBBBBBBB     arrayList = " + arrayList);
				if (arrayList == null ||  arrayList.size() == 0) {
					return  false;
				}
				if (arrayList != null) {
					for (int i=0; i< arrayList.size(); i++) {
						String urlStr = arrayList.get(i).getSmallPhotoSrc();
						StringBuffer stringBuffer = new StringBuffer();
						if (urlStr != null) {
							String[] urlsStr = urlStr.split(",");
							for (int j=0; j<urlsStr.length; j++) {
								if (j == urlsStr.length -1) {
									stringBuffer.append(mHttpPost.getFileUrl(urlsStr[j]));
								} else {
									stringBuffer.append(mHttpPost.getFileUrl(urlsStr[j]) + ",");
								}
							}
						}
						Log.i("zzz","BBBBBBBB  arrayList.size() = " + arrayList.size() + "  & " + i + "  && " + arrayList.get(i).toString());
						Log.i("zzz","BBBBBBBB"  +  " mLicence = " +  mLicence  +   "    &&stringBuffer = " + stringBuffer.toString());
						BaseUserBean  baseUserBean = arrayList.get(i).getTakePhoroUser();
						ManualPhotographyBean manualPhotographyBean = null;
						if (baseUserBean != null) {
							manualPhotographyBean = new ManualPhotographyBean(arrayList.get(i).getLicence(),  baseUserBean.getImageData()!=null ? mHttpPost.getFileUrl(baseUserBean.getImageData()) : null, arrayList.get(i).getTakePhoroUser().getName(), arrayList.get(i).getTakePhotoTime(),  arrayList.get(i).getAddr(),  stringBuffer.toString(),  mHttpPost.getCompanyNameByid(Integer.parseInt(arrayList.get(i).getTakePhoroUser().getDepartmentId())));
						} else {
							manualPhotographyBean = new ManualPhotographyBean(arrayList.get(i).getLicence(),  null, null, arrayList.get(i).getTakePhotoTime(),  arrayList.get(i).getAddr(),  stringBuffer.toString(),  null);
						}
						mListDate.add(manualPhotographyBean);
					}

				}
			} catch (Exception e) {
				Log.e(TAG,"e : " + e.getMessage());
				return false;
			}

			return true;
		}

		/**
		 * 运行在ui线程中，在doInBackground()执行完毕后执行
		 */
		@Override
		protected void onPostExecute(Boolean aBoolean) {
			super.onPostExecute(aBoolean);
			closeDlg();
			//Toast.makeText(context,"执行完毕",Toast.LENGTH_SHORT).show();
			if (aBoolean) {
				setListViewData();
			} else {
				ToastUtils.showLong("获取列表为空.");
			}
		}

		/**
		 * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}


	private static final String photoSrc = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg,https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861321&di=7c603a9f41935d8051e35cdbce4fe154&imgtype=0&src=http%3A%2F%2Fc11.eoemarket.com%2Fapp0%2F119%2F119986%2Fscreen%2F1985845.png,https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg,https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg,https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg";
	private static final String[] URLS = {
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg",
			"http://lh5.ggpht.com/_Z6tbBnE-swM/TB0CryLkiLI/AAAAAAAAVSo/n6B78hsDUz4/s144-c/_DSC3454.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861321&di=7c603a9f41935d8051e35cdbce4fe154&imgtype=0&src=http%3A%2F%2Fc11.eoemarket.com%2Fapp0%2F119%2F119986%2Fscreen%2F1985845.png",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg",
			"http://lh3.ggpht.com/_lLj6go_T1CQ/TCD8PW09KBI/AAAAAAAAQdc/AqmOJ7eg5ig/s144-c/Juvenile%20Gannet%20despute.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861320&di=424a50518b20bf79b5d6322c2a08ff0d&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa5c27d1ed21b0ef40f4513b3dfc451da80cb3ea9.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861320&di=424a50518b20bf79b5d6322c2a08ff0d&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa5c27d1ed21b0ef40f4513b3dfc451da80cb3ea9.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg",
			"http://lh4.ggpht.com/_TPlturzdSE8/TBv4ugH60PI/AAAAAAAAMsI/p2pqG85Ghhs/s144-c/_MG_3963.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg",
			"http://lh6.ggpht.com/_iFt5VZDjxkY/TB9rQyWnJ4I/AAAAAAAADpU/lP2iStizJz0/s144-c/DSCF1014.JPG",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861322&di=1384fe7d8c1fdba219ab0439cc45402b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F7aec54e736d12f2e3e656ddd4ac2d5628535682f.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861321&di=73764b4c20269a0b05c886bf7e6b06a5&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F877a91aajw1f9wfi75j7oj20fk078wfw.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861320&di=424a50518b20bf79b5d6322c2a08ff0d&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa5c27d1ed21b0ef40f4513b3dfc451da80cb3ea9.jpg",
			"http://lh6.ggpht.com/_a29lGRJwo0E/TBqOK_tUKmI/AAAAAAAAVbw/UloKpjsKP3c/s144-c/31012332.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861320&di=424a50518b20bf79b5d6322c2a08ff0d&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa5c27d1ed21b0ef40f4513b3dfc451da80cb3ea9.jpg",
			"http://lh3.ggpht.com/_iVnqmIBYi4Y/TCaOH6rRl1I/AAAAAAAA1qg/qeMerYQ6DYo/s144-c/Kwiat_100626_0016.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861320&di=424a50518b20bf79b5d6322c2a08ff0d&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa5c27d1ed21b0ef40f4513b3dfc451da80cb3ea9.jpg",
			"http://lh5.ggpht.com/_JTI0xxNrKFA/TBsKQ9uOGNI/AAAAAAAChQg/z8Exh32VVTA/s144-c/CRW_0015-composite.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861320&di=424a50518b20bf79b5d6322c2a08ff0d&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa5c27d1ed21b0ef40f4513b3dfc451da80cb3ea9.jpg",
			"http://lh4.ggpht.com/_L7i4Tra_XRY/TBtxjScXLqI/AAAAAAAAE5o/ue15HuP8eWw/s144-c/opera%20house%20II.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861320&di=424a50518b20bf79b5d6322c2a08ff0d&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa5c27d1ed21b0ef40f4513b3dfc451da80cb3ea9.jpg",
			"http://lh6.ggpht.com/_iGI-XCxGLew/S-iYQWBEG-I/AAAAAAAACB8/JuFti4elptE/s144-c/norvig-polar-bear.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861320&di=424a50518b20bf79b5d6322c2a08ff0d&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa5c27d1ed21b0ef40f4513b3dfc451da80cb3ea9.jpg",
			"http://lh4.ggpht.com/_loGyjar4MMI/S-InQvd_3hI/AAAAAAAADIw/dHvCFWfyHxQ/s144-c/Rainbokeh.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510765861320&di=424a50518b20bf79b5d6322c2a08ff0d&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa5c27d1ed21b0ef40f4513b3dfc451da80cb3ea9.jpg",
			"http://lh5.ggpht.com/_6_dLVKawGJA/SMwq86HlAqI/AAAAAAAAG5U/q1gDNkmE5hI/s144-c/mobius-glow.jpg",
			"http://lh3.ggpht.com/_QFsB_q7HFlo/TCItc19Jw3I/AAAAAAAAFs4/nPfiz5VGENk/s144-c/4551649039_852be0a952_o.jpg",
			"http://lh6.ggpht.com/_TQY-Nm7P7Jc/TBpjA0ks2MI/AAAAAAAABcI/J6ViH98_poM/s144-c/IMG_6517.jpg",
			"http://lh3.ggpht.com/_rfAz5DWHZYs/S9cLAeKuueI/AAAAAAAAeYU/E19G8DOlJRo/s144-c/DSC_4397_8_9_tonemapped2.jpg",
			"http://lh4.ggpht.com/_TQY-Nm7P7Jc/TBpi6rKfFII/AAAAAAAABbg/79FOc0Dbq0c/s144-c/david_lee_sakura.jpg",
			"http://lh3.ggpht.com/_TQY-Nm7P7Jc/TBpi8EJ4eDI/AAAAAAAABb0/AZ8Cw1GCaIs/s144-c/Hokkaido%20Swans.jpg",
			"http://lh3.ggpht.com/_1aZMSFkxSJI/TCIjB6od89I/AAAAAAAACHM/CLWrkH0ziII/s144-c/079.jpg",
			"http://lh5.ggpht.com/_loGyjar4MMI/S-InWuHkR9I/AAAAAAAADJE/wD-XdmF7yUQ/s144-c/Colorado%20River%20Sunset.jpg",
			"http://lh3.ggpht.com/_0YSlK3HfZDQ/TCExCG1Zc3I/AAAAAAAAX1w/9oCH47V6uIQ/s144-c/3138923889_a7fa89cf94_o.jpg",
			"http://lh6.ggpht.com/_K29ox9DWiaM/TAXe4Fi0xTI/AAAAAAAAVIY/zZA2Qqt2HG0/s144-c/IMG_7100.JPG",
			"http://lh6.ggpht.com/_0YSlK3HfZDQ/TCEx16nJqpI/AAAAAAAAX1c/R5Vkzb8l7yo/s144-c/4235400281_34d87a1e0a_o.jpg",
			"http://lh4.ggpht.com/_8zSk3OGcpP4/TBsOVXXnkTI/AAAAAAAAAEo/0AwEmuqvboo/s144-c/yosemite_forrest.jpg",
			"http://lh4.ggpht.com/_6_dLVKawGJA/SLZToqXXVrI/AAAAAAAAG5k/7fPSz_ldN9w/s144-c/coastal-1.jpg",
			"http://lh4.ggpht.com/_WW8gsdKXVXI/TBpVr9i6BxI/AAAAAAABhNg/KC8aAJ0wVyk/s144-c/IMG_6233_1_2-2.jpg",
			"http://lh3.ggpht.com/_loGyjar4MMI/S-InS0tJJSI/AAAAAAAADHU/E8GQJ_qII58/s144-c/Windmills.jpg",
			"http://lh4.ggpht.com/_loGyjar4MMI/S-InbXaME3I/AAAAAAAADHo/4gNYkbxemFM/s144-c/Frantic.jpg",
			"http://lh5.ggpht.com/_loGyjar4MMI/S-InKAviXzI/AAAAAAAADHA/NkyP5Gge8eQ/s144-c/Rice%20Fields.jpg",
			"http://lh3.ggpht.com/_loGyjar4MMI/S-InZA8YsZI/AAAAAAAADH8/csssVxalPcc/s144-c/Seahorse.jpg",
			"http://lh3.ggpht.com/_syQa1hJRWGY/TBwkCHcq6aI/AAAAAAABBEg/R5KU1WWq59E/s144-c/Antelope.JPG",
			"http://lh5.ggpht.com/_MoEPoevCLZc/S9fHzNgdKDI/AAAAAAAADwE/UAno6j5StAs/s144-c/c84_7083.jpg",
			"http://lh4.ggpht.com/_DJGvVWd7IEc/TBpRsGjdAyI/AAAAAAAAFNw/rdvyRDgUD8A/s144-c/Free.jpg",
			"http://lh6.ggpht.com/_iO97DXC99NY/TBwq3_kmp9I/AAAAAAABcz0/apq1ffo_MZo/s144-c/IMG_0682_cp.jpg",
			"http://lh4.ggpht.com/_7V85eCJY_fg/TBpXudG4_PI/AAAAAAAAPEE/8cHJ7G84TkM/s144-c/20100530_120257_0273-Edit-2.jpg" };
}
