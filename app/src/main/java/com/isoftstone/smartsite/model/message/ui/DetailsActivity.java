package com.isoftstone.smartsite.model.message.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;

/**
 * Created by yanyongjun on 2017/10/15.
 */

public class DetailsActivity extends BaseActivity {
    private TextView mDetails = null;
    private TextView mDate = null;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_msg_details;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
//        mDetails = (TextView) findViewById(R.id.lab_msg_details);
//        mDate = (TextView) findViewById(R.id.lab_msg_date);
//        Resources res = getResources();
//
//        Intent intent = getIntent();
//        int type = intent.getIntExtra(MsgFragment.FRAGMENT_TYPE, MsgFragment.FRAGMENT_TYPE_VCR);
//        String details = "";
//        String loc = "";
//        String date = "";
//        switch (type) {
//            case MsgFragment.FRAGMENT_TYPE_VCR: {
//                VCRData data = (VCRData) intent.getSerializableExtra(MsgFragment.FRAGMENT_DATA);
//
//                if (data.getType() == VCRData.TYPE_ERROR) {
//                    details = String.format(res.getString(R.string.vcr_error), data.getId());
//                } else if (data.getType() == VCRData.TYPE_NEED_REPAIR) {
//                    details = String.format(res.getString(R.string.vcr_need_repair), data.getId());
//                }
//                loc = String.format(res.getString(R.string.location), data.getLoc());
//                date = data.getStringDate();
//                break;
//            }
//            case MsgFragment.FRAGMENT_TYPE_ENVIRON: {
//                EnvironData data = (EnvironData) intent.getSerializableExtra(MsgFragment.FRAGMENT_DATA);
//                details = "";
//                if (data.getType() == EnvironData.TYPE_PM_EXTENDS) {
//                    details = String.format(res.getString(R.string.environ_pm_exceeded), data.getId());
//                } else if (data.getType() == VCRData.TYPE_NEED_REPAIR) {
//                    details = String.format(res.getString(R.string.environ_need_repair), data.getId());
//                }
//                loc = String.format(res.getString(R.string.location), data.getLoc());
//                date = data.getStringDate();
//                break;
//            }
//            case MsgFragment.FRAGMENT_TYPE_SYNERGY: {
//                ThreePartyData data = (ThreePartyData) intent.getSerializableExtra(MsgFragment.FRAGMENT_DATA);
//                details = "";
//                if (data.getType() == ThreePartyData.TYPE_RECEIVE_REPORT) {
//                    details = String.format(res.getString(R.string.receive_report), data.getName());
//                } else if (data.getType() == ThreePartyData.TYPE_SEND_REPORT) {
//                    details = res.getString(R.string.send_report);
//                }
//                loc = String.format(res.getString(R.string.location), data.getLoc());
//                date = data.getStringDate();
//                break;
//            }
//            default: {
//                break;
//            }
//
//        }
//        mDetails.setText(details + "\n" + loc);
//        mDate.setText(date);

    }
}
