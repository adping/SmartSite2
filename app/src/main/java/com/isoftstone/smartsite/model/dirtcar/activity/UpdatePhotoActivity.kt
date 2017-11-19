package com.isoftstone.smartsite.model.dirtcar.activity

import android.app.Activity
import android.content.ContentProvider
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import com.amap.api.mapcore.util.id
import com.github.mikephil.charting.utils.FileUtils
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.base.BaseActivity
import com.isoftstone.smartsite.http.HttpPost.mLoginBean
import com.isoftstone.smartsite.model.dirtcar.adapter.UpdatePhotoAdapter
import com.isoftstone.smartsite.utils.DateUtils
import com.isoftstone.smartsite.utils.FilesUtils
import java.io.File
import java.util.*
import android.graphics.Bitmap
import android.provider.OpenableColumns
import android.support.v4.app.NotificationCompat.getExtras


/**
 * 拍照上传界面
 * Created by yanyongjun on 2017/11/19.
 */
open class UpdatePhotoActivity : BaseActivity() {
    var mGridView: GridView? = null //照片列表
    var mGridViewAdapter: UpdatePhotoAdapter? = null
    var mUriList: ArrayList<Uri> = ArrayList<Uri>()
    var mStoragePath = Environment.getExternalStorageDirectory().toString() + "/isoftstone/Camera";
    var mUriImage: Uri? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_update_photo
    }

    override fun afterCreated(savedInstanceState: Bundle?) {
        initGridView()
    }

    fun onClick_save(v: View) {
        //TODO
    }

    fun initGridView() {
        mGridView = findViewById(R.id.grid_view) as GridView
        mGridViewAdapter = UpdatePhotoAdapter(this, mUriList)
        mGridView?.adapter = mGridViewAdapter

        mGridView?.setOnItemClickListener(AdapterView.OnItemClickListener(fun(parent, v, pos, id) {
            if (pos == mUriList.size) {
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                var fileName = DateUtils.format_file_name.format(Date()) + ".png"
                var path = File(mStoragePath)
                var file = File(path, fileName)
                mUriImage = FilesUtils.getUriForFile(this@UpdatePhotoActivity, file.path)
                i.putExtra(MediaStore.EXTRA_OUTPUT, mUriImage)
                val uri = mUriImage;
                if (uri != null) {
                    mUriImage = uri
                }
                startActivityForResult(i, 0)
            } else {
                val intent = FilesUtils.getImageFileIntent(this@UpdatePhotoActivity, mUriList.get(pos))
                startActivity(intent)
            }
        }))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e(TAG, "onActivityResult:" + requestCode)
        val uri = mUriImage
        if (uri != null && isTakePicOk(uri)) {
            mUriList.add(uri)
            mGridViewAdapter?.notifyDataSetChanged()
        } else {
            Log.e(TAG, "file size == null")
        }
        mUriImage = null

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun isTakePicOk(uri: Uri): Boolean {
        var cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            val sizeCol = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            val size = cursor.getInt(sizeCol)
            Log.e(TAG, "file size:" + size)
            if (size <= 0) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
