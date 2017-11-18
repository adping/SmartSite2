package com.isoftstone.smartsite.model.dirtcar.activity;


import android.app.Activity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import org.apache.http.Header;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.model.dirtcar.adapter.ManualPhotographyAdapter;
import com.isoftstone.smartsite.model.dirtcar.imagecache.ImageLoader;

/**
 * Created by zhangyinfu on 2017/11/16.
 */

public class ManualPhotographyActivity extends Activity {

	protected static final String TAG = "ListViewPerformaceActivity";
	/** Called when the activity is first created. */
	private ListView mListview;
	private ManualPhotographyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_photography);
		setupViews();
	}
	
	

	@Override
	protected void onDestroy() {

		ImageLoader imageLoader = adapter.getImageLoader();
		if (imageLoader != null){
			imageLoader.clearCache();
		}
		
		super.onDestroy();
	}



	private void setupViews() {
		mListview = (ListView) findViewById(R.id.main_lv_list);
		adapter = new ManualPhotographyAdapter(500, this, URLS);
		mListview.setAdapter(adapter);
		mListview.setOnScrollListener(mScrollListener);
	}

	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_FLING:
				adapter.setFlagBusy(true);
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				adapter.setFlagBusy(false);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				adapter.setFlagBusy(false);
				break;
			default:
				break;
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};


	
	
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
