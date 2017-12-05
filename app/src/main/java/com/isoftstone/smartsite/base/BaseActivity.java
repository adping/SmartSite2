package com.isoftstone.smartsite.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.common.AppManager;
import com.isoftstone.smartsite.http.patrolreport.PatrolBean;
import com.isoftstone.smartsite.utils.StatusViewUtils;

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();
    private Dialog mLoginingDlg; // 显示正在登录的Dialog
    private TextView dlg_textview = null;
    public static final String DEFAULT_PAGE_SIZE = "20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addToActivities(this);

        setContentView(getLayoutRes());

        StatusViewUtils.initStatusBar(this);

        initLoginingDlg();

        afterCreated(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        StatusViewUtils.removeStatusBar(this);
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 返回布局UI的ID
     * @return
     */
    protected abstract int getLayoutRes();

    /**
     * 布局加载完毕后的逻辑操作
     * @param savedInstanceState
     */
    protected abstract void afterCreated(Bundle savedInstanceState);

    /**
     * 跳转Activity
      * @param activity
     * @param bundle
     */
    public void openActivity(Class<?> activity,Bundle bundle){
        Intent intent = new Intent(this,activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 点击返回按钮
     */
    public void onBackBtnClick(View v){
        finish();;
    }

    /**
     * 获取巡查报告的数据，非三方协同界面不用关注
     * @return
     */
    public PatrolBean getReportData(){
        return null;
    }

    private void initLoginingDlg() {

        mLoginingDlg = new Dialog(this, R.style.loginingDlg);
        mLoginingDlg.setContentView(R.layout.logining_dlg);
        dlg_textview = (TextView) mLoginingDlg.findViewById(R.id.dlg_textview);
        mLoginingDlg.setCanceledOnTouchOutside(false); // 设置点击Dialog外部任意区域关闭Dialog
    }

    /* 显示正在登录对话框 */
    public void showDlg(String text) {
        if (mLoginingDlg != null){
            dlg_textview.setText(text);
            mLoginingDlg.show();
        }
    }

    /* 关闭正在登录对话框 */
    public void closeDlg() {
        if (mLoginingDlg != null && mLoginingDlg.isShowing())
            mLoginingDlg.dismiss();
    }
}
