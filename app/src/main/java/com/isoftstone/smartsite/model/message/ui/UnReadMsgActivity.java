package com.isoftstone.smartsite.model.message.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.model.message.data.VCRData;

import java.util.List;

/**
 * Created by yanyongjun on 2017/10/23.
 */

public class UnReadMsgActivity extends BaseActivity {
    //listview中各item项的名称
    public static final String ITEM_DATE = "lab_time";
    public static final String ITEM_TITLE = "lab_title";
    public static final String ITEM_DETAILS = "lab_details";

    private Activity mActivity = null;
    private ListView mListView = null;
    private List<VCRData> mDatas = null;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_unread_msg;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        mActivity = this;
        init();
    }

    private void init() {
//        mListView = (ListView) mActivity.findViewById(R.id.listview);
//        SimpleAdapter adapter = new SimpleAdapter(mActivity, getData(), R.layout.listview_msg_item, new String[]{ITEM_DATE,ITEM_TITLE,ITEM_DETAILS,  },
//                new int[]{R.id.lab_time, R.id.lab_title,R.id.lab_details});
//        mListView.setAdapter(adapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mActivity, DetailsActivity.class);
//                intent.putExtra(MsgFragment.FRAGMENT_TYPE, MsgFragment.FRAGMENT_TYPE_VCR);
//                intent.putExtra(MsgFragment.FRAGMENT_DATA, mDatas.get(position));
//                mActivity.startActivity(intent);
//            }
//        });
//        mListView.setDividerHeight(40);//TODO
    }

    /**
     * 加载listview的数据源
     *
     * @return
     */
//    private List<Map<String, Object>> getData() {
//        //TODO
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        Resources res = mActivity.getResources();
//
//        List<VCRData> vcrDatas = readDataFromSDK();
//        String vcrError = res.getString(R.string.vcr_error);
//        String vcrNeedRepair = res.getString(R.string.vcr_need_repair);
//        for (VCRData data : vcrDatas) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            if (data.getType() == VCRData.TYPE_ERROR) {
//                map.put(ITEM_TITLE,"NXC-12检测到PM超标");
//                map.put(ITEM_DETAILS, String.format(vcrError, data.getId()));
//                map.put( ITEM_DATE, data.getStringDate());
//                list.add(map);
//            } else if (data.getType() == VCRData.TYPE_NEED_REPAIR) {
//                map.put(ITEM_TITLE,"NXC-12设备损坏");
//                map.put(ITEM_DETAILS, String.format(vcrNeedRepair, data.getId()));
//                map.put( ITEM_DATE, data.getStringDate());
//                list.add(map);
//            }
//        }
//        return list;
//    }
//
//    /**
//     * 目前是测试使用，后续需要从网络侧读取
//     *
//     * @return
//     */
//    private List<VCRData> readDataFromSDK() {
//        //TODO
//        mDatas = new ArrayList<>();
//
//        VCRData data = new VCRData();
//        data.setType(VCRData.TYPE_ERROR);
//        data.setId(13001);
//        mDatas.add(data);
//
//        data = new VCRData();
//        data.setType(VCRData.TYPE_NEED_REPAIR);
//        data.setId(13002);
//        mDatas.add(data);
//
//        data = new VCRData();
//        data.setType(VCRData.TYPE_NEED_REPAIR);
//        data.setId(13003);
//        mDatas.add(data);
//
//        data = new VCRData();
//        data.setType(VCRData.TYPE_ERROR);
//        data.setId(13004);
//        mDatas.add(data);
//
//        data = new VCRData();
//        data.setType(VCRData.TYPE_NEED_REPAIR);
//        data.setId(13005);
//        mDatas.add(data);
//
//        return mDatas;
//    }
}
