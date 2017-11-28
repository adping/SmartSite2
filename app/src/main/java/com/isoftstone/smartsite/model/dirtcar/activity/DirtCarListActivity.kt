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
import com.isoftstone.smartsite.utils.ToastUtils

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
        var query = object:AsyncTask<Void,Void,Boolean>(){
            override fun onPreExecute() {
                this@DirtCarListActivity.showDlg("正在获取列表")
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Void?): Boolean? {
                var bean =PageableBean()
                bean.setPage("0")
                bean.setSize("100")
                var result = mHttpPost.getTrackList("",bean)
                if(result == null || result.size == 0){
                    return false
                }
                Log.e(TAG,"yanlog result:"+result)
                mList.clear()
                mList.addAll(result.content)
                return true
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                this@DirtCarListActivity.closeDlg()
                var temp = if(result == null) false else result
                if(temp) {
                    mAdapter.notifyDataSetChanged()
                }else{
                    ToastUtils.showLong("获取列表失败，请稍后重试")
                }
            }
        }
        query.execute()
    }

}