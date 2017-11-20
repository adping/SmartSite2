package com.isoftstone.smartsite.model.inspectplan.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridView
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.base.BaseActivity
import com.isoftstone.smartsite.model.inspectplan.adapter.AddressAdapter
import com.isoftstone.smartsite.model.inspectplan.adapter.PeopleAdapter
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity
import com.isoftstone.smartsite.model.tripartite.adapter.AttachGridViewAdatper
import com.isoftstone.smartsite.utils.FilesUtils

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
        //mAttachAdapter = AttachGridViewAdatper(this, mAttachList)

        initAddressGridView()
        initPeopleGridView()
        //initAttachGridView()
    }

    fun initPeopleGridView() {
        mGridViewPeople = findViewById(R.id.grid_view_people) as GridView
        mGridViewPeople?.adapter = mAdapterPeople
    }

    fun initAddressGridView() {
        mGridViewAddress = findViewById(R.id.grid_view_address) as GridView
        mGridViewAddress?.adapter = mAdapterAddress
    }

    fun initAttachGridView() {
        mGridViewAttach = findViewById(R.id.grid_view) as GridView
        mAttachList.add(R.drawable.attachment)
        mGridViewAttach?.adapter = mAttachAdapter
        mGridViewAttach?.setOnItemClickListener({ parent, view, pos, id ->
            if (pos == mAttachList.size - 1) {
                var i = Intent(Intent.ACTION_GET_CONTENT)
                i.setType("*/*")
                startActivityForResult(i, 0)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri = data?.getData()
                Log.e(TAG, "yanlog uri:" + uri!!)
                if ("file".equals(uri.scheme, ignoreCase = true)) {//使用第三方应用打开
                    //Toast.makeText(getActivity(), uri.getPath() + "11111", Toast.LENGTH_SHORT).show();
                    addAttach(uri.path, uri.toString())
                    return
                }
                val path = FilesUtils.getPath(this, uri)
                //Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
                addAttach(path, uri.toString())
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    //add files
    fun addAttach(path: String, uri: String) {
        Log.e(TAG, "yanlog remove begin size:" + mAttachList.size);
        var formatPath = FilesUtils.getFormatString(path);
        Log.e(TAG, "yanlog remove begin size at0:" + mAttachList.get(0));
        mAttachList.removeAt(mAttachList.size - 1);
        //mFilesPath.add(path);
        if (TripartiteActivity.mImageList.contains(formatPath)) {
            mAttachList.add(uri);
        } else if (TripartiteActivity.mXlsList.contains(formatPath)) {
            mAttachList.add(TripartiteActivity.mAttach.get(".xls") ?: 0);
        } else if (TripartiteActivity.mDocList.contains(formatPath)) {
            mAttachList.add(TripartiteActivity.mAttach.get(".doc") ?: 0);
        } else if (TripartiteActivity.mPdfList.contains(formatPath)) {
            mAttachList.add(TripartiteActivity.mAttach.get(".pdf") ?: 0);
        } else if (TripartiteActivity.mPptList.contains(formatPath)) {
            mAttachList.add(TripartiteActivity.mAttach.get(".ppt") ?: 0);
        } else if (TripartiteActivity.mVideoList.contains(formatPath)) {
            mAttachList.add(TripartiteActivity.mAttach.get(".video") ?: 0);
        } else {
            mAttachList.add(TripartiteActivity.mAttach.get(".doc") ?: 0);
        }

        mAttachList.add(R.drawable.attachment);
        Log.e(TAG, "yanlog remove end size:" + mAttachList.size);
        Log.e(TAG, "yanlog mData at 0:" + mAttachList.get(0));
        mAttachAdapter?.notifyDataSetChanged();
    }

    fun onClick_submit(v:View){
        //TODO
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
