package com.isoftstone.smartsite.model.inspectplan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;

/**
 * Created by gone on 2017/11/19.
 */

public class PatrolPlanActivity extends BaseActivity{

    private LinearLayout weeks = null;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_patrolplan;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        weeks = (LinearLayout) findViewById(R.id.weeks);
        String[] strweeks = getResources().getStringArray(R.array.weeks);
        for (int i = 0; i < 7; i++) {
            TextView textView = (TextView) weeks.getChildAt(i);
            textView.setText(strweeks[i]);
            textView.setTextColor(getResources().getColor(R.color.single_text_color));
        }
    }
    private void initToolbar(){
        TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.approval_pending_inspect_plans_title);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
