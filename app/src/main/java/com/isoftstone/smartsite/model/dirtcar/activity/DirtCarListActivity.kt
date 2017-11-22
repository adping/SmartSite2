package com.isoftstone.smartsite.model.dirtcar.activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.base.BaseActivity
import com.isoftstone.smartsite.http.HttpPost
import com.isoftstone.smartsite.http.muckcar.BayonetGrabInfoBean
import com.isoftstone.smartsite.http.pageable.PageableBean
import com.isoftstone.smartsite.model.dirtcar.adapter.DirtCarAdapter

/**
 * Created by yanyongjun on 2017/11/14.
 */
class DirtCarListActivity : BaseActivity() {
    var mListView : ListView ?= null
    var mList : ArrayList<BayonetGrabInfoBean> = ArrayList<BayonetGrabInfoBean>()
    var mAdapter : DirtCarAdapter = DirtCarAdapter(this,mList)
    var mHttpPost: HttpPost = HttpPost()


    override fun getLayoutRes(): Int {
        return R.layout.activity_dircarlist;
    }

    override fun afterCreated(savedInstanceState: Bundle?) {
        mListView = findViewById(R.id.listview) as ListView
        mListView?.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()

        queryData()
    }

    fun onSearchBtnClick(v: View) {
        //TODO
    }
    fun queryData(){
        var query = object:AsyncTask<Void,Void,Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                var bean =PageableBean()
                bean.setPage("0")
                bean.setSize("100")
                var result = mHttpPost.getTrackList("",bean)
                Log.e(TAG,"yanlog result:"+result.toString())
                mList.clear()
                mList.addAll(result.content)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                mAdapter.notifyDataSetChanged()
            }
        }
        query.execute()
    }

}