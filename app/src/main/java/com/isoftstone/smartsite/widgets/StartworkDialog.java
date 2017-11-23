package com.isoftstone.smartsite.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.isoftstone.smartsite.R;

import org.apache.http.protocol.RequestConnControl;

/**
 * Created by 2013020220 on 2017/11/20.
 */

public class StartworkDialog extends Dialog implements View.OnClickListener {

    private ImageView dialog_cancell;
    private TextView patrol_plan;
    private Button bt_start;
    private Context context;
    private OnStartworkLstener onStartworkLstener;

    public StartworkDialog(Context context, OnStartworkLstener onStartworkLstener) {
        super(context);
        this.context = context;
        this.onStartworkLstener = onStartworkLstener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_work_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        dialog_cancell = (ImageView) findViewById(R.id.dialog_cancll);
        bt_start = (Button) findViewById(R.id.bt_start);
        dialog_cancell.setOnClickListener(this);
        bt_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_cancll) {
            dismiss();
        } else if (v.getId() == R.id.bt_start) {
            onStartworkLstener.onStartwork();
            dismiss();
        }
    }

    public interface OnStartworkLstener {
        void onStartwork();
    }
}
