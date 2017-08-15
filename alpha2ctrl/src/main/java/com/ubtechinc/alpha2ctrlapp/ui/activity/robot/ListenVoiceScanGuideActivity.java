package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ubtechinc.alpha2ctrlapp.R;

/**
 * Created by ubt on 2017/2/7.
 */

public class ListenVoiceScanGuideActivity  extends Activity{
    private ImageView btn_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_voice_scan_guide);
        btn_close = (ImageView)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
