package com.isoftstone.smartsite.model.inspectplan.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.GridLayout
import android.widget.GridView
import android.widget.TextView
import com.google.gson.Gson
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.base.BaseActivity
import com.isoftstone.smartsite.model.dirtcar.View.MyFlowLayout
import com.isoftstone.smartsite.model.inspectplan.adapter.AddressAdapter
import com.isoftstone.smartsite.model.inspectplan.adapter.PeopleAdapter
import com.isoftstone.smartsite.model.map.ui.MapSearchTaskPositionActivity
import com.isoftstone.smartsite.model.tripartite.activity.TripartiteActivity
import com.isoftstone.smartsite.model.tripartite.adapter.AttachGridViewAdatper
import com.isoftstone.smartsite.utils.DateUtils
import com.isoftstone.smartsite.utils.FilesUtils
import com.isoftstone.smartsite.widgets.CustomDatePicker
import java.text.SimpleDateFormat
import java.util.*

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

    var mEditName: EditText? = null

    private var mWaittingAdd: Drawable? = null
    private var mWattingChanged: Drawable? = null

    var labAddress: TextView? = null
    var labPeople: TextView? = null

    var labTimeLeft: TextView? = null
    var labBeginTimeRight: TextView? = null
    var labEndTimeRight: TextView? = null

    var edit_report_msg: EditText? = null
    var lab_address_choose_left: TextView? = null
    val FLAG_TARGET_ADDRESS = 0

    var grid_layout_address :GridLayout ?= null

    var flow_layout_address:MyFlowLayout ?= null

    override fun getLayoutRes(): Int {
        return R.layout.activity_add_inspect_plan;
    }

    override fun afterCreated(savedInstanceState: Bundle?) {

        mWaittingAdd = resources.getDrawable(R.drawable.addcolumn)
        mWaittingAdd?.setBounds(0, 0, mWaittingAdd?.getIntrinsicWidth()!!, mWaittingAdd?.getIntrinsicHeight()!!)
        mWattingChanged = resources.getDrawable(R.drawable.editcolumn)
        mWattingChanged?.setBounds(0, 0, mWattingChanged?.getIntrinsicWidth()!!, mWattingChanged?.getIntrinsicHeight()!!)

        mAdapterPeople = PeopleAdapter(this, mPeopleList)
        //mAttachAdapter = AttachGridViewAdatper(this, mAttachList)



        initEditName()
        initBeginTime()
        initEndTime()
        initMsg()
        initAddressGridView()
        initGridLayout()
        initPeopleGridView()
        //initAttachGridView()
    }

    fun initEditName() {
        mEditName = findViewById(R.id.edit_name) as EditText
        var labName = findViewById(R.id.lab_name) as TextView
        mEditName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length != 0) {
                    labName.setCompoundDrawables(mWattingChanged, null, null, null)
                } else {
                    labName.setCompoundDrawables(mWaittingAdd, null, null, null)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    fun initBeginTime() {
        labTimeLeft = findViewById(R.id.lab_inspect_report_time) as TextView
        labBeginTimeRight = findViewById(R.id.lab_begin_time) as TextView
        labBeginTimeRight?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showDatePickerDialog(labBeginTimeRight, labTimeLeft)
            }
        })
    }

    fun initEndTime() {
        labEndTimeRight = findViewById(R.id.lab_end_time) as TextView
        labEndTimeRight?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showDatePickerDialog(labEndTimeRight, labTimeLeft)
            }
        })
    }

    fun initMsg() {
        val lab_report_msg = findViewById(R.id.lab_report_msg) as TextView
        edit_report_msg = findViewById(R.id.edit_report_msg) as EditText
        edit_report_msg?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length != 0) {
                    lab_report_msg.setCompoundDrawables(mWattingChanged, null, null, null)
                } else {
                    lab_report_msg.setCompoundDrawables(mWaittingAdd, null, null, null)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    fun initPeopleGridView() {
        labPeople = findViewById(R.id.lab_people_left) as TextView
        mGridViewPeople = findViewById(R.id.grid_view_people) as GridView
        mGridViewPeople?.adapter = mAdapterPeople
    }

    fun initGridLayout(){
        //grid_layout_address = findViewById(R.id.grid_layout_address) as GridLayout
        flow_layout_address = findViewById(R.id.flow_layout_address) as MyFlowLayout
    }

    fun initAddressGridView() {
       // mGridViewAddress = findViewById(R.id.grid_view_address) as GridView

        mAdapterAddress = AddressAdapter(this, mAddressList,mWaittingAdd,labAddress)
        mGridViewAddress?.adapter = mAdapterAddress

        val lab_address_choose_right = findViewById(R.id.lab_address_choose_right) as TextView
        lab_address_choose_left = findViewById(R.id.lab_address_choose_left) as TextView

        lab_address_choose_right.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var i = Intent(this@AddInspectPlan, MapSearchTaskPositionActivity::class.java)
                startActivityForResult(i, FLAG_TARGET_ADDRESS)
            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FLAG_TARGET_ADDRESS) {
            var address = data?.getStringExtra("latLngsNameJson")
            Log.e(TAG, "yanlog return address:" + address)
            if (address != null) {
                var gson = Gson()
                var temp: ArrayList<String> = gson.fromJson<ArrayList<String>>(address, ArrayList::class.java)
                mAddressList.addAll(temp)
                //mAdapterAddress?.notifyDataSetChanged()
                if (mAddressList.size > 0) {
                    lab_address_choose_left?.setCompoundDrawables(mWattingChanged, null, null, null)
                }

                //for test
//                var str = temp[0]
//                var v = LayoutInflater.from(this@AddInspectPlan).inflate(R.layout.listview_add_inspect_plan_address_item, null)
//                var textView = v.findViewById(R.id.lab_address) as TextView
//                textView.setText(str)
//                var array = ArrayList<View>()
//                array.add(v)
//                val rowSpec = GridLayout.spec(mAddressList.size / 3, 1f)
//                val columnSpec = GridLayout.spec(mAddressList.size % 3, 1f)
//
//                val layoutParams = GridLayout.LayoutParams(rowSpec, columnSpec)
//                layoutParams.height = v.height
//                layoutParams.width = v.width
//                grid_layout_address?.addView(v,layoutParams)
                addAddressView()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    fun addAddressView(){
        flow_layout_address?.removeAllViews()
        for(str in mAddressList) {
            var v = LayoutInflater.from(this@AddInspectPlan).inflate(R.layout.listview_add_inspect_plan_address_item, null)
            var textView = v.findViewById(R.id.lab_address) as TextView
            textView.setText(str)
            flow_layout_address?.addView(v)
        }
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

    fun onClick_submit(v: View) {
        //TODO
    }

    fun showDatePickerDialog(editRight: TextView?, labLeft: TextView?) {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        val now = sdf.format(Date())

        val customDatePicker = CustomDatePicker(this, CustomDatePicker.ResultHandler { time ->
            // 回调接口，获得选中的时间
            try {
                editRight?.text = DateUtils.format_yyyy_MM_dd_HH_mm_ss.format(DateUtils.format_yyyy_MM_dd_HH_mm.parse(time))
                labLeft?.setCompoundDrawables(mWattingChanged, null, null, null)
                editRight?.setTextColor(resources.getColor(R.color.main_text_color))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, "1970-01-01 00:00", "2099-12-12 00:00") // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true) // 不显示时和分
        //customDatePicker.showYearMonth();
        customDatePicker.setIsLoop(false) // 不允许循环滚动
        //customDatePicker.show(dateText.getText().toString() + " " + timeText.getText().toString());
        customDatePicker.show(DateUtils.format_yyyy_MM_dd_HH_mm.format(Date()))
    }
}
