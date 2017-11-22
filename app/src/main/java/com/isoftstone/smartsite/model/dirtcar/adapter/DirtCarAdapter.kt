package com.isoftstone.smartsite.model.dirtcar.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.model.dirtcar.Data.DirtcarBean
import com.isoftstone.smartsite.model.dirtcar.activity.ManualPhotographyActivity

/**
 * Created by yanyongjun on 2017/11/14.
 */
open class DirtCarAdapter(context: Context, datas: ArrayList<DirtcarBean>) : BaseAdapter() {
    var mDatas: ArrayList<DirtcarBean> = ArrayList<DirtcarBean>()
    var mContext: Context = context

    init {
        mDatas.addAll(datas);
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v  = if (convertView != null) convertView else {
            LayoutInflater.from(mContext).inflate(R.layout.listview_dirtcar_item, null)
        }
        var vCamera = v.findViewById(R.id.linear_camera)
        vCamera.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                var i =  Intent(mContext,ManualPhotographyActivity::class.java)
                mContext.startActivity(i)
            }
        })
        return v
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0;
    }

    override fun getCount(): Int {
        return 10
        //TODO
    }
}