package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;

import java.io.File;
import java.io.FileOutputStream;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class ScanDeviceEncodeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView alpha_no;
    public static String serialNo = "";
    private TextView btn_save;
    public final static String PIC_RECEIVE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/ubtech/image/";
    public final static String PIC_RECEIVE_TEMP = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ubtech/temp/image/";
    private Bitmap qRBitMap;
    public static boolean isHistroy = true;
    public static String qRText = "";
    private ImageView ivCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device_encode);
        alpha_no = (TextView) findViewById(R.id.alplah_no);
        btn_save = (TextView) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        ivCode = (ImageView) findViewById(R.id.image_view);
        serialNo = getIntent().getStringExtra("ALPHA_No");
        alpha_no.setText("ID  " + serialNo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createQRCode();
    }

    private void createQRCode() {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode("robotSeq=" + serialNo, BGAQRCodeUtil.dp2px(ScanDeviceEncodeActivity.this, 150), Color.BLACK);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    ivCode.setImageBitmap(bitmap);
                    qRBitMap = bitmap;
                } else {
                     ToastUtils.showShortToast(getString(R.string.encode_code_fail));
                }
            }
        }.execute();
    }


    public void onBack(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                saveQrImage();
                break;
        }
    }

    public void saveQrImage() {
        if (qRBitMap != null) {
            makeDirs(PIC_RECEIVE_PATH);
            File file = new File(PIC_RECEIVE_PATH, "core_image.png");
            // 从资源文件中选择一张图片作为将要写入的源文件
            try {
                FileOutputStream out = new FileOutputStream(file);
                qRBitMap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            if (file.exists()) {
//                Toast.makeText(this, getString(R.string.save_path) + PIC_RECEIVE_PATH, Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this, R.string.encode_code_fail, Toast.LENGTH_SHORT).show();
//            }

            // this.finish();
        }

    }

    public void saveTempQr(String url) {
        String filename = String.valueOf(url.hashCode());
        makeDirs(PIC_RECEIVE_TEMP);
        File f = new File(PIC_RECEIVE_TEMP, filename);
        try {
            FileOutputStream out = new FileOutputStream(f);
            qRBitMap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            ScanDeviceEncodeActivity.this.finish();
        } catch (Throwable ex) {
            ex.printStackTrace();
            ScanDeviceEncodeActivity.this.finish();
        }

    }


    public boolean makeDirs(String filePath) {
        File folder = new File(filePath);
        return (folder.exists() && folder.isDirectory()) ? true : folder
                .mkdirs();
    }


}
