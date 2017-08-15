package com.ubtechinc.alpha2ctrlapp.third;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;


public class WarningActivity extends Activity {

    TextView tv_control_app, tv_control_user, tv_control_time;
    Button btn_sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        Intent intent = getIntent();

        findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_control_app = (TextView) findViewById(R.id.tv_control_app);
        tv_control_user = (TextView) findViewById(R.id.tv_control_user);
        tv_control_time = (TextView) findViewById(R.id.tv_control_time);
        if(intent != null) {
            tv_control_app.setText("控制应用：" + intent.getStringExtra(IntentConstants.DATA_ROBOT_CONTROL_APP));;
            tv_control_user.setText("控制人：" +intent.getStringExtra(IntentConstants.DATA_ROBOT_CONTROL_USER));;
            tv_control_time.setText("控制时间：" +intent.getStringExtra(IntentConstants.DATA_ROBOT_CONTROL_TIME));;
        }
    }
}
