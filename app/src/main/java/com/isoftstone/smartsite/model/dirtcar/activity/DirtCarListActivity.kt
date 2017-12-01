package com.isoftstone.smartsite.model.dirtcar.activity

import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
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
    var mListView: ListView? = null
    var mList: ArrayList<BayonetGrabInfoBean> = ArrayList<BayonetGrabInfoBean>()
    var mAdapter: DirtCarAdapter = DirtCarAdapter(this, mList)
    var mHttpPost: HttpPost = HttpPost()

    var mIsUIInSearchMode = false
    var mIsDataInSearchMode = false

    override fun getLayoutRes(): Int {
        return R.layout.activity_dircarlist;
    }

    override fun afterCreated(savedInstanceState: Bundle?) {
        mListView = findViewById(R.id.listview) as ListView
        mListView?.adapter = mAdapter
        initSearchView()
    }

    override fun onResume() {
        super.onResume()
        if (!mIsUIInSearchMode) {
            queryData("")
        } else {
            val search_edit_text = findViewById(R.id.search_edit_text) as EditText
            queryData(search_edit_text.text.toString())
        }
    }

    fun initSearchView() {
        var toolbar_search = findViewById(R.id.toolbar_search)
        var toolbar_default = findViewById(R.id.toolbar_default)
        val cancel_search = findViewById(R.id.search_btn_icon_right) as TextView
        val search_edit_text = findViewById(R.id.search_edit_text) as EditText
        var btn_search = findViewById(R.id.btn_search)
        var search_btn_back = findViewById(R.id.search_btn_back)

        if (mIsUIInSearchMode) {
            enterSearchMode()
        } else {
            exitSearchMode()
        }


        btn_search.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                enterSearchMode()
            }
        })


        cancel_search.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (TextUtils.isEmpty(search_edit_text.text.toString())) {
                    exitSearchMode()
                } else {
                    queryData(search_edit_text.text.toString())
                }
            }
        })


        search_btn_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                exitSearchMode()
            }
        })


        search_edit_text.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (count == 0) {
                            cancel_search.setText("取消")
                        } else {
                            cancel_search.setText("搜索")
                        }
                    }
                })
    }

    fun enterSearchMode() {
        var toolbar_search = findViewById(R.id.toolbar_search)
        var toolbar_default = findViewById(R.id.toolbar_default)
        val cancel_search = findViewById(R.id.search_btn_icon_right) as TextView
        val search_edit_text = findViewById(R.id.search_edit_text) as EditText
        var btn_search = findViewById(R.id.btn_search)
        var search_btn_back = findViewById(R.id.search_btn_back)
        toolbar_default.visibility = View.INVISIBLE
        toolbar_search.visibility = View.VISIBLE
        mIsUIInSearchMode = true
        search_edit_text.setText("")
        cancel_search.setText("取消")
    }

    fun exitSearchMode() {
        var toolbar_search = findViewById(R.id.toolbar_search)
        var toolbar_default = findViewById(R.id.toolbar_default)
        val cancel_search = findViewById(R.id.search_btn_icon_right) as TextView
        val search_edit_text = findViewById(R.id.search_edit_text) as EditText
        var btn_search = findViewById(R.id.btn_search)
        var search_btn_back = findViewById(R.id.search_btn_back)
        toolbar_default.visibility = View.VISIBLE
        toolbar_search.visibility = View.INVISIBLE
        mIsUIInSearchMode = false
        if (mIsDataInSearchMode) {
            queryData("")
        }

    }

    //    fun onSearchBtnClick(v: View) {
//        mIsInSeachMode = true
//        var toolbar_search = findViewById(R.id.toolbar_search)
//        var toolbar_default = findViewById(R.id.toolbar_default)
//        toolbar_search.setVisibility(View.VISIBLE)
//        toolbar_default.setVisibility(View.INVISIBLE)
//    }
//    fun
    fun queryData(licence: String) {
        var query = object : AsyncTask<Void, Void, Boolean>() {
            override fun onPreExecute() {
                this@DirtCarListActivity.showDlg("正在获取列表")
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Void?): Boolean? {
                var bean = PageableBean()
                bean.setPage("0")
                bean.setSize("100")
                var result = mHttpPost.getTrackList(licence, bean)
                if (result == null || result.size == 0) {
                    return false
                }
                Log.e(TAG, "yanlog result:" + result)
                mList.clear()
                mList.addAll(result.content)

                if (TextUtils.isEmpty(licence)) {
                    mIsDataInSearchMode = false
                } else {
                    mIsDataInSearchMode = true
                }
                return true
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                this@DirtCarListActivity.closeDlg()
                var temp = if (result == null) false else result
                if (temp) {
                    mAdapter.notifyDataSetChanged()
                } else {
                    ToastUtils.showLong("没有获取到数据")
                }
            }
        }
        query.execute()
    }

}