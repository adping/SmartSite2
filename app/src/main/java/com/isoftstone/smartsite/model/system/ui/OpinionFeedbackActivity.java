/*
 * Copyright (c) 2013, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : IMOS
 * Module Name : 
 * Date Created: 2017-10-19
 * Creator     : zhangyinfu
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 * 
 *------------------------------------------------------------------------------
 */
package com.isoftstone.smartsite.model.system.ui;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.utils.ToastUtils;

/**
 * 意见反馈界面
 * created by zhangyinfu 2017-11-1
 */
public class OpinionFeedbackActivity extends Activity implements View.OnClickListener{

    private TextView mSubmitView;
    private EditText mFeedbackView;
    private TextView mNumTextShow;
    private static final int MAX_LENGTH = 150;//最大输入字符数150 
    private int Rest_Length = MAX_LENGTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_feedback);

        initView();
        initToolbar();
    }

    private void initView() {
        mSubmitView = (TextView) findViewById(R.id.submit_view);
        mFeedbackView = (EditText) findViewById(R.id.feedback_text);
        mNumTextShow = (TextView) findViewById(R.id.feedback_text_show_num);
        mNumTextShow.setText(Html.fromHtml("您还可以输入:"+"<font color=\"red\">"+MAX_LENGTH+"/"+ MAX_LENGTH + "</font>"));

        mSubmitView.setOnClickListener(this);

        mFeedbackView.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mNumTextShow.setText(Html.fromHtml("您还可以输入:"+"<font color=\"red\">"+Rest_Length+"/"+ MAX_LENGTH + "</font>"));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Rest_Length > 0){
                    Rest_Length = MAX_LENGTH - mFeedbackView.getText().length();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mNumTextShow.setText(Html.fromHtml("您还可以输入:"+"<font color=\"red\">"+Rest_Length+"/"+ MAX_LENGTH + "</font>"));
            }
        });
    }

    private void initToolbar(){
        TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.opinion_feedback);

        findViewById(R.id.btn_back).setOnClickListener(OpinionFeedbackActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                OpinionFeedbackActivity.this.finish();
                break;

            case R.id.submit_view:
                if (mFeedbackView.getText().length() <= 0) {
                    ToastUtils.showShort("请输入您的意见反馈信息，谢谢！");
                    return;
                }
                ToastUtils.showShort("您的意见反馈已收集，感谢您的反馈！");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                OpinionFeedbackActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
