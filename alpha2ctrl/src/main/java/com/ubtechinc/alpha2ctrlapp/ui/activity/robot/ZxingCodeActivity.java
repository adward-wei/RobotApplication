package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;


public class ZxingCodeActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final int REQUEST_CODE = 1000;

    private QRCodeView mQRCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_code);

        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        LoadingDialog.getInstance(this).show();
    }

    public void onBack(View v) {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();

        mHandler.sendEmptyMessageDelayed(1010, 700);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1010:
                    LoadingDialog.dissMiss();
                    mQRCodeView.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };


    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Logger.i("zxing", "result:" + result);
        //    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        Intent data = new Intent();
        data.putExtra(Constants.Result_text, result);
        //setResult(BindingRequestCode, data);
        //finish();
        data.setAction("com.ubt.qrcode.bind");
        startActivityForResult(data, REQUEST_CODE);
        vibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtils.showShortToast( getString(R.string.permission_request_camera));
        finish();
    }

}
