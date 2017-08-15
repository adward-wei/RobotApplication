package com.ubtechinc.alpha2ctrlapp.base;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ubtech.utilcode.utils.SPUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.util.AndroidToLan;
import com.umeng.analytics.MobclickAgent;

import java.util.Locale;


/**
 * @author tanghongyu
 * @ClassName ActivitySupport
 * @date 4/6/2017
 * @Description 工具支持类 继承换肤类
 * @modifier
 * @modify_time
 */
public abstract class ActivitySupport extends AppCompatActivity {
    private static final String TAG = "ActivitySupport";
    protected Context mContext = null;
    protected NotificationManager notificationManager;
    private String mCurrentSetLanguage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = Alpha2Application.getAlpha2();
        //初始化Preference
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        changeLanByLocal();
        Alpha2Application.getInstance().registerActivity(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
//		changeLanguage();
        MobclickAgent.onResume(this);
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        Log.d(TAG, "onPause()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Alpha2Application.getInstance().unregisterActivity(this);
    }
    public void changeLanByLocal() {

        mCurrentSetLanguage = SPUtils.get().getString(Constants.APP_LAUNGUAGE, "");
        //应用内配置语言
        Resources resources = getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        if (TextUtils.isEmpty(mCurrentSetLanguage)) {
            config.locale = Locale.getDefault();//系统默认语言
            Constants.SYSTEM_LAN = AndroidToLan.getStandardLocale(resources.getConfiguration().locale.getLanguage());
            mCurrentSetLanguage = getString(R.string.language_system);
        } else {

            if (mCurrentSetLanguage.equals(BusinessConstants.APP_LANGUAGE_CN)) {
                config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文
                Constants.SYSTEM_LAN = AndroidToLan.getStandardLocale(mCurrentSetLanguage);
            }  else if(mCurrentSetLanguage.equals(getString(R.string.language_system))) {
                config.locale = Locale.getDefault();//系统默认语言
                Constants.SYSTEM_LAN = AndroidToLan.getStandardLocale(resources.getConfiguration().locale.getLanguage());
            } else{
                config.locale = Locale.ENGLISH;            //英文
                Constants.SYSTEM_LAN = AndroidToLan.getStandardLocale(mCurrentSetLanguage);
            }





        }

        resources.updateConfiguration(config, dm);

    }




}
