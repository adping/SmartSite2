package com.isoftstone.smartsite.model.dirtcar.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.common.AppManager
import com.isoftstone.smartsite.http.HttpPost

/**
 * Created by yanyongjun on 2017/11/21.
 */
open class RecognizeDirtCarActivity : AppCompatActivity(){
//    override fun getLayoutRes(): Int {
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        return R.layout.activity_recognize_dirt_car
//    }
    var mHttpPost:HttpPost ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppManager.getAppManager().addToActivities(this)

        setContentView(R.layout.activity_recognize_dirt_car)


    }
}