package com.ubtechinc.alpha2ctrlapp.ui.activity.main;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.ActivitySupport;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.Injection;
import com.ubtechinc.alpha2ctrlapp.util.ApkTools;


/**
 * @author
 * @date 2016/6/22
 * @Description
 * @modify
 * @modify_time
 */
public class WelcomeActivity extends ActivitySupport implements WelcomeContract.View{
    private static final String TAG = "WelcomeActivity";
    private ImageView imgLogoLine;
    private Animation img_logo_line_anim;

    private TextView appVersionTv, dateTv;

    WelcomeContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(TextUtils.isEmpty(SPUtils.get().getString(Constants.APP_LAUNGUAGE))){
            SPUtils.get().put(Constants.APP_LAUNGUAGE, getString(R.string.language_system));
        }
        dateTv = (TextView) findViewById(R.id.date);
        appVersionTv = (TextView) findViewById(R.id.app_version);
        dateTv.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.welcome_setting_anim));
        dateTv.setText(TimeUtils.getCurrentTimeInString(TimeUtils.DEFAULT_DATE_FORMAT));
        imgLogoLine = (ImageView) findViewById(R.id.img_logo_line);
        img_logo_line_anim = AnimationUtils.loadAnimation(this,
                R.anim.turn_around_anim);
        imgLogoLine.setAnimation(img_logo_line_anim);

        mPresenter = new WelcomePresenter(mContext, this, Injection.provideMessageRepository(getApplication()) );
        mPresenter.initData();

        appVersionTv.setText("V" + ApkTools.getVersionName(mContext));
        mPresenter.doGo();
        getData();

    }

    private void getData() {
        Intent intent = getIntent();
        String scheme = intent.getScheme();
        Uri uri = intent.getData();
        Logger.d( "scheme:" + scheme);

        if (uri != null) {
            String host = uri.getHost();
            String dataString = intent.getDataString();
            String id = uri.getQueryParameter("d");
            String path = uri.getPath();
            String path1 = uri.getEncodedPath();
            String queryString = uri.getQuery();
            Logger.d( "host:" + host + "dataString:" + dataString+  "id:" + id +  "path:" + path+  "path1:" + path1+  "queryString:" + queryString);

        }
    }





    @Override
    public void onResume() {
        super.onResume();
        // 检测网络和版本
        //validateInternet();

    }



    @Override
    public void skipToGuide() {
        Logger.d( " skipToGuide !");
        Intent intent = new Intent();
        imgLogoLine.getAnimation().cancel();
        intent.setClass(WelcomeActivity.this, GuidePageActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }



    @Override
    public void skipToMainPage() {
        Intent intent = new Intent(WelcomeActivity.this,MainPageActivity.class);
        startActivity(intent);
    }

    @Override
    public void skipMainToLogin(int state) {
        Intent intent = new Intent();
        imgLogoLine.getAnimation().cancel();
        intent.setClass(WelcomeActivity.this, MainActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }
}
