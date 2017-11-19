package com.isoftstone.smartsite.model.dirtcar.activity

import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.GridView
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.base.BaseActivity
import com.isoftstone.smartsite.model.dirtcar.Data.SelectImage
import com.isoftstone.smartsite.model.dirtcar.adapter.SelectImageAdapter
import java.io.File
import java.util.*

/**
 * Created by yanyongjun on 2017/11/19.
 */
open class SelectImageActivity : BaseActivity() {
    var mGridView: GridView? = null
    var mAdapter: SelectImageAdapter? = null
    var mDataList = ArrayList<SelectImage>()
    var mRootDir = ArrayList<String>()

    init {
        mRootDir.add(Environment.getExternalStorageDirectory().toString() + "/isoftstone/Camera")
        var sd1 = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera")
        var sd2 = File("/storage/3864-6330/DCIM/Camera/")
        Log.e(TAG, "sync image dir sd2:" + sd2.toString())
        if (sd1.exists()) {
            mRootDir.add(sd1.toString())
        }
        if (sd2.exists()) {
            mRootDir.add(sd2.toString())
        }

        for (temp in mRootDir) {
            Log.e(TAG, "sync image dir:" + temp);
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_select_image
    }

    override fun afterCreated(savedInstanceState: Bundle?) {
        initGridView()
    }

    fun initGridView() {
        mGridView = findViewById(R.id.grid_view) as GridView
        mAdapter = SelectImageAdapter(this, mDataList)
        mGridView?.adapter = mAdapter
        mInitResTask.execute()
    }

    fun onClick_submit(v: View) {
        //TODO
    }

    var mInitResTask = object : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            mDataList.clear()
            for (path in mRootDir) {
                Log.e(TAG, "yanlog rootPath:" + path)
                var stack = Stack<File>()
                stack.push(File(path))
                while (!stack.isEmpty()) {
                    var curFile = stack.pop()
                    if (!curFile.exists()) {
                        continue
                    }
                    if (curFile.isFile && (curFile.path.endsWith(".jpg") || curFile.path.endsWith(".jpeg") || curFile.path.endsWith(".png") ||
                            curFile.path.endsWith(".JPG") || curFile.path.endsWith(".JPEG") || curFile.path.endsWith(".PNG"))) {
                        mDataList.add(SelectImage(curFile.canonicalPath, false))
                    } else if (curFile.isDirectory) {
                        var files = curFile.listFiles()
                        for (file in files) {
                            if (!(file.name.equals(".") || file.name.equals(".."))) {
                                stack.push(file)
                            }
                        }
                    }
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            mAdapter?.notifyDataSetChanged()
            super.onPostExecute(result)
        }
    }
}
