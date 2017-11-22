package com.isoftstone.smartsite.model.dirtcar.Service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * Created by yanyongjun on 2017/11/22.
 */
open class RecognizeDirtCarService : Service(){
    override fun onBind(intent: Intent?): IBinder {
        return Binder()
    }



}