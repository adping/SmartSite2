package com.isoftstone.smartsite.model.inspectplan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.isoftstone.smartsite.R

/**
 * Created by yanyongjun on 2017/11/15.
 */
open class AddressAdapter(context: Context, list: ArrayList<String>) : BaseAdapter() {
    var mContext = context
    var mList = list
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = if (convertView != null) convertView else {
            LayoutInflater.from(mContext).inflate(R.layout.listview_add_inspect_plan_address_item, null)
        }
        return v
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return 1
    }
}