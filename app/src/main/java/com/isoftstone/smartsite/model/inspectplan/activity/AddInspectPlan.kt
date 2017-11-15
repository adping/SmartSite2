package com.isoftstone.smartsite.model.inspectplan.activity

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.SimpleAdapter
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.base.BaseActivity
import com.isoftstone.smartsite.model.inspectplan.adapter.AddressAdapter
import com.isoftstone.smartsite.model.inspectplan.adapter.PeopleAdapter
import com.isoftstone.smartsite.model.tripartite.adapter.AttachGridViewAdatper
import com.isoftstone.smartsite.utils.ToastUtils

/**
 * Created by yanyongjun on 2017/11/15.
 */
open class AddInspectPlan : BaseActivity() {
    var mGridViewAddress: GridView? = null
    var mAddressList = ArrayList<String>()
    var mAdapterAddress: AddressAdapter? = null

    var mGridViewPeople: GridView? = null
    var mPeopleList = ArrayList<String>()
    var mAdapterPeople: PeopleAdapter? = null

    var mGridViewAttach: GridView? = null
    var mAttachList = java.util.ArrayList<Any>()
    var mAttachAdapter: AttachGridViewAdatper? = null


    override fun getLayoutRes(): Int {
        return R.layout.activity_add_inspect_plan;
    }

    override fun afterCreated(savedInstanceState: Bundle?) {
        mAdapterAddress = AddressAdapter(this, mAddressList)
        mAdapterPeople = PeopleAdapter(this, mPeopleList)
        mAttachAdapter = AttachGridViewAdatper(this, mAttachList)

        initAddressGridView()
        initPeopleGridView()
        initAttachGridView()
    }

    fun initAddressGridView() {
        mGridViewPeople = findViewById(R.id.grid_view_people) as GridView
        mGridViewPeople?.adapter = mAdapterPeople
    }

    fun initPeopleGridView() {
        mGridViewAddress = findViewById(R.id.grid_view_address) as GridView
        mGridViewAddress?.adapter = mAdapterAddress
    }

    fun initAttachGridView() {
        mGridViewAttach = findViewById(R.id.grid_view) as GridView
        mAttachList.add(R.drawable.attachment)
        mGridViewAttach?.adapter = mAttachAdapter
//        mGridViewAttach?.setOnItemClickListener(AdapterView.OnItemClickListener(fun onItemClick(parent:AdapterView<>, view: View,pos:Int,id:Long ){
//
//        }))
    }


//    public void initGridView() {
//        mAttachView = (GridView) getView().findViewById(R.id.grid_view);
//
//        mData = new ArrayList<Object>();
//        mData.add(R.drawable.attachment);
//        //mAttachAdapter = new SimpleAdapter(getActivity(), mData, R.layout.add_attach_grid_item, new String[]{"image"}, new int[]{R.id.image});
//        mAttachAdapter = new AttachGridViewAdatper(getActivity(), mData);
//        mAttachView.setAdapter(mAttachAdapter);
//
//        mAttachView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == mData.size() - 1) {
//                    //点击添加附件
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.setType("*/*");
//                    startActivityForResult(i, REQUEST_ACTIVITY_ATTACH);
//                } else {
//                    mFilesPath.remove(position);
//                    mData.remove(position);
//                    mAttachAdapter.notifyDataSetChanged();
//                    ToastUtils.showShort("附件删除成功");
//                }
//            }
//        });
//    }
}
