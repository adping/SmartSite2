package com.isoftstone.smartsite.model.dirtcar.activity

import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.base.BaseActivity
import com.isoftstone.smartsite.model.dirtcar.Data.DirtcarBean
import com.isoftstone.smartsite.model.dirtcar.adapter.DirtCarAdapter

/**
 * Created by yanyongjun on 2017/11/14.
 */
class DirtCarListActivity : BaseActivity() {
    var mListView : ListView ?= null
    var mList : ArrayList<DirtcarBean> = ArrayList<DirtcarBean>()
    var mAdapter : DirtCarAdapter = DirtCarAdapter(this,mList)

    override fun getLayoutRes(): Int {
        return R.layout.activity_dircarlist;
    }

    override fun afterCreated(savedInstanceState: Bundle?) {
        mListView = findViewById(R.id.listview) as ListView
        mListView?.adapter = mAdapter
    }

    fun onSearchBtnClick(v: View) {
        //TODO
    }
}