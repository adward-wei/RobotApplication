package com.performance.ubt.sdkTest.ui;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.performance.ubt.sdkTest.R;
import com.ubtechinc.alpha.sdk.speech.SpeechRobotApi;
import com.ubtechinc.alpha.serverlibutil.interfaces.PcmListener;

public class SpeechFragment extends BaseFragement implements View.OnClickListener {

    private static final String TAG = "SpeechTest";

    @Override
    protected void initView() {
        //初始化view
        initButton();
        SpeechRobotApi.get().initializ(mContext);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_speech;
    }

    @Override
    protected void getDataFromServer() {
    }

    /**
     * 初始化button
     */
    private void initButton() {
        Button registerPcmListenerButton = mView.findViewById(R.id.registerPcmListener);
        registerPcmListenerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.registerPcmListener:
                registerPcmListener();
                break;
            default:
                break;
        }

    }

    private void registerPcmListener() {
        int ret = SpeechRobotApi.get().registerPcmListener(new PcmListener() {
            @Override
            public void onPcmData(byte[] data, int dataLen) {
                Log.i(TAG, "dataLen is " + dataLen);
            }
        });
        Log.i(TAG, "ret is " + ret);
    }
}
