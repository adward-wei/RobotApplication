package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.os.Bundle;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
/**
 * @ClassName ContactusAcitivty
 * @date 5/25/2017
 * @author tanghongyu
 * @Description 联系我们
 * @modifier
 * @modify_time
 */
public class ContactusActivity extends BaseContactActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_contact_us);
        this.mContext = this;
        initView();
    }

    public void initView() {
        title= (TextView) findViewById(R.id.authorize_title);
        title.setText(getString(R.string.left_contact_us));


    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.right_out, 0);
	}


    @Override
    public void onResume() {
        super.onResume();
    }


}
