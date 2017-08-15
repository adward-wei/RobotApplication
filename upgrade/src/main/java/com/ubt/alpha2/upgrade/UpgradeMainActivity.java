package com.ubt.alpha2.upgrade;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ubt.alpha2.upgrade.bean.ReportDataBean;
import com.ubt.alpha2.upgrade.bean.VersionConfigs;
import com.ubt.alpha2.upgrade.net.HttpManager;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.VersionUtils;
import com.ubtechinc.alpha.sdk.speech.SpeechRobotApi;
import com.ubtechinc.alpha.sdk.sys.SysApi;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechASRListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechTtsListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechWakeUpListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpgradeMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String FILE_PATH = "/sdcard/test/upgrade-debug.apk";
    TextView tvUpgrade;
    TextView tvOpen;
    TextView tvClose;
    TextView tvFinish;
    TextView tvStart;
    TextView tvPower;
    TextView tvStart2;
    TextView tvTTSTest,test_provider;
    TextView tvInstall;
    TextView tvHttp;
    TextView tvRoProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_main);
        initViews();
        bindListener();
    }

    private void initViews(){
        tvUpgrade = (TextView)findViewById(R.id.upgrade);
        tvOpen = (TextView)findViewById(R.id.open);
        tvClose =(TextView)findViewById(R.id.close);
        tvFinish = (TextView) findViewById(R.id.finish);
        tvStart = (TextView)findViewById(R.id.start);
        tvPower = (TextView)findViewById(R.id.power);
        tvStart2 = (TextView)findViewById(R.id.start2);
        tvTTSTest = (TextView)findViewById(R.id.tts);
        test_provider = (TextView)findViewById(R.id.test_provider);
        tvInstall = (TextView)findViewById(R.id.install);
        tvHttp = (TextView)findViewById(R.id.http);
//        tvHttp.setVisibility(View.GONE);
//        test_provider.setVisibility(View.GONE);
        tvInstall.setVisibility(View.GONE);
        tvStart2.setVisibility(View.GONE);
        tvStart.setVisibility(View.GONE);
        tvUpgrade.setVisibility(View.GONE);
        tvClose.setVisibility(View.GONE);
        tvOpen.setVisibility(View.GONE);
//        tvFinish.setVisibility(View.GONE);
        tvPower.setVisibility(View.VISIBLE);
        tvRoProduct = (TextView)findViewById(R.id.product);
    }

    private void bindListener(){
        tvUpgrade.setOnClickListener(this);
        tvOpen.setOnClickListener(this);
        tvClose.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        tvPower.setOnClickListener(this);
        tvStart2.setOnClickListener(this);
        tvTTSTest.setOnClickListener(this);
        test_provider.setOnClickListener(this);
        tvInstall.setOnClickListener(this);
        tvHttp.setOnClickListener(this);
        tvRoProduct.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.upgrade:
                invokeUpgradeTest();
                break;
            case R.id.open:
                invokeOpen();
                break;
            case R.id.finish:
                //invokeFinish();
                ReportDataBean.reportDownloadOk("123","selfmodule","http://www.baidu.com",false);
                ReportDataBean.reportDownloadOk("123","selfmodule","http://www.google.com",true);
                break;
            case R.id.close:
                invokeClose();
                break;
            case R.id.start:
                invokeSendStartUpgardCommand();
                break;
            case R.id.power:
                invokePower();
                break;
            case R.id.start2:
                //invokeSendHeadUpgradeCommand();
                readLoaclFile();
                break;
            case R.id.tts:
                invokeTTSTest();
                break;
            case R.id.test_provider:
                testProvider();
                break;
            case R.id.install:
                install(FILE_PATH);
                break;
            case R.id.http:
                final SpeechRobotApi speechRobotApi = SpeechRobotApi.get().initializ(this);
                speechRobotApi.registerWakeUpListener(new SpeechWakeUpListener() {
                    @Override
                    public void onSuccess() {
                        LogUtils.d("registerWakeUpListener onSuccess");
                    }

                    @Override
                    public void onError(int errCode, String errDes) {
                        LogUtils.d("registerWakeUpListener onError");
                    }
                });
                break;
            case R.id.product:
                LogUtils.d(" DEVICE: "+ Build.DEVICE+"  PRODUCT: "+Build.PRODUCT+"  MODEL: "  + Build.MODEL);
                break;
        }
    }

    private void invokeOpen(){
    }

    private void invokeSendStartUpgardCommand(){
//        if(embeddedBatteryUpgradeTask != null)
//            embeddedBatteryUpgradeTask.sendTestUpgradeStart();
    }


    private void readLoaclFile(){
        File file = new File("/sdcard/ubt/embedded/ALPHA2L-CHEST-A-V317-170424.bin");
        LogUtils.d("file.exisits: "+file.exists());
        int count = 0;
        if(!file.exists())
            return;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[128];
            int len = 0;
            while((len = fileInputStream.read(buffer,0,128))!=-1){
                count += len;
                LogUtils.d("count: "+count);
            }
            LogUtils.d("end: "+count);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void invokeClose(){
    }

    private void invokeUpgradeTest(){
    }
    private void invokeFinish(){

    }

    private void invokePower(){
        SysApi.get().initializ(this);
        String localVersionName = ApkUtils.getAndroidSystemVersion();
        String batteryVersion = SysApi.get().getBatteryVersion();
        String chestVersion = SysApi.get().getChestVersion();
        File file = new File(FilePathUtils.LOCAL_CONFIG_PATH);
        String mainserviceVersion = null;
        if(file.exists()) {
            VersionConfigs versionConfigs = VersionUtils.getVersionConfigs(FilePathUtils.LOCAL_CONFIG_PATH);
            if (versionConfigs != null)
                mainserviceVersion = versionConfigs.version;
        }
        LogUtils.d("主服务版本： "+mainserviceVersion);
        LogUtils.d("电池版本: "+batteryVersion+"  胸版版本: "+chestVersion);
        LogUtils.d("android系统:"+localVersionName);
        LogUtils.d("电量: "+SysApi.get().getPowerValue()+"  充电状态: "+SysApi.get().isPowerCharging());
    }

    private void invokeTTSTest(){
        SpeechRobotApi speechRobotApi = SpeechRobotApi.get().initializ(this);
        speechRobotApi.speechStartTTS("升级文件已下载，是否立即安装", new SpeechTtsListener() {
            @Override
            public void onEnd() {
                LogUtils.d("tts end");
                startASRTest();
            }
        });
    }

    private void startASRTest(){
        final SpeechRobotApi speechRobotApi = SpeechRobotApi.get().initializ(this);
        speechRobotApi.switchWakeup(true);
        speechRobotApi.switchSpeechCore("zh_cn");
        speechRobotApi.startSpeechASR(9527, new SpeechASRListener() {
            @Override
            public void onBegin() {
                LogUtils.d("onBegin");
            }

            @Override
            public void onEnd() {
                LogUtils.d("onEnd");
            }

            @Override
            public void onResult(String text) {
                LogUtils.d("onResult: "+text);
            }

            @Override
            public void onError(int code) {
                LogUtils.d("onError: "+code);
            }
        });
    }

    private void testProvider(){
        ApkUtils.reflectReleaseProvider();
    }

    public String install(String apkAbsolutePath ) {

        String[] args = { "pm", "install", "-r", apkAbsolutePath };
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            LogUtils.d("install");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            LogUtils.d("processBuilder.start()");
            process = processBuilder.start();
            errIs = process.getErrorStream();
            LogUtils.d("read");
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            LogUtils.d("write");
            baos.write('/' + 'n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            LogUtils.d("baos.toByteArray()");
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//			if (process != null) {
//				process.destroy();
//			}
        }

        return result;
    }

    private void invokeHttp(){
        HttpManager httpManager = HttpManager.get(this);
//        httpManager.doGet();
    }
}
