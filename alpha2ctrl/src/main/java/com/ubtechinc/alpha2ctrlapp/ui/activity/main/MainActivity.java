package com.ubtechinc.alpha2ctrlapp.ui.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.Tencent;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.third.FaceBookManager;
import com.ubtechinc.alpha2ctrlapp.third.TwitterManager;
import com.ubtechinc.alpha2ctrlapp.third.WeiXinManager;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.user.CompleteUserInfoFragment;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.user.FindPwdFragment;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.user.LoginFragement;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.user.RegistFragement;
import com.ubtechinc.alpha2ctrlapp.util.WriteBookUtils;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends BaseActivity {

	public final static String FORCE_OFFLINE = "FORCE_OFFLINE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_fragment);
        if (savedInstanceState == null) {
            addFragment(LoginFragement.class.getName(), getIntent().getExtras());
        }
        registerBroadcastReceiver();
        setBtnbackGone();
        printKeyHash(this);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(action.equals(WeiXinManager.ACTION_WEIXIN_API_CALLBACK)){
                SendAuth.Resp weixin_return_info = WeiXinManager.handleIntent(intent,
                        mContext);
                Logger.i("微信登录", "广播收到回调");
                if (weixin_return_info == null) {
                    ToastUtils.showShortToast(mContext.getResources()
                            .getString(R.string.ui_login_fail));
                } else {
                    LoginFragement l = (LoginFragement) getFragmentManager()
                            .findFragmentByTag(LoginFragement.class.getName());
    				/* 然后在碎片中调用重写的onActivityResult方法 */
					if (l != null)
						l.getWeixinLoginInfo(weixin_return_info);
				}
			}





		}

	};

	@Override
	public void onResume() {
		super.onResume();

	}

	/**
	 * 用于提示的广播
	 *
	 * @author zengdengyi
	 * @date 2015-1-13 下午1:36:59
	 */
	private void registerBroadcastReceiver() {
		// 注册 广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(WeiXinManager.ACTION_WEIXIN_API_CALLBACK);
		this.registerReceiver(mReceiver, filter);
	}

	private void unRegisterReceiver() {
		this.unregisterReceiver(mReceiver);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mReceiver != null)
			unRegisterReceiver();
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		Logger.i("yuyong", "requestCode:" + requestCode + ",resultCode:"
				+ resultCode);

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == WriteBookUtils.RESULT_CHOOSE_LOCAL_PITURE) {
			if (data != null) {
				Uri currImageURI = data.getData();
				if(currImageURI!=null){
					String retResult = WriteBookUtils.getPath(MainActivity.this,
							currImageURI);
					CompleteUserInfoFragment f = (CompleteUserInfoFragment) getFragmentManager()
							.findFragmentByTag(
									CompleteUserInfoFragment.class.getName());
				/* 然后在Fragment中调用重写的onActivityResult方法 */
					f.onActivityResult(requestCode, resultCode, data);
				}
			}

		}
		if (requestCode == Constants.GET_COUNTRY_REQUEST) {
			if (resultCode == RESULT_OK) {
				RegistFragement f = (RegistFragement) getFragmentManager()
						.findFragmentByTag(RegistFragement.class.getName());
				/* 然后在Fragment中调用重写的onActivityResult方法 */
				if (f != null)
					f.onActivityResult(requestCode, resultCode, data);

				LoginFragement l = (LoginFragement) getFragmentManager()
						.findFragmentByTag(LoginFragement.class.getName());

				/* 然后在Fragment中调用重写的onActivityResult方法 */
				if (l != null)
					l.onActivityResult(requestCode, resultCode, data);
				FindPwdFragment fw = (FindPwdFragment) getFragmentManager()
						.findFragmentByTag(FindPwdFragment.class.getName());
				/* 然后在Fragment中调用重写的onActivityResult方法 */
				if (fw != null)
					fw.onActivityResult(requestCode, resultCode, data);
				CompleteUserInfoFragment ci = (CompleteUserInfoFragment) getFragmentManager()
						.findFragmentByTag(CompleteUserInfoFragment.class.getName());
				/* 然后在Fragment中调用重写的onActivityResult方法 */

                if (ci != null)
                    ci.onActivityResult(requestCode, resultCode, data);
            }
        }

        if(requestCode==11101){

            Tencent.onActivityResultData(
                    requestCode,
                    resultCode,
                    data,
                    (LoginFragement) getFragmentManager().findFragmentByTag(
                            LoginFragement.class.getName()));
        }
        if(requestCode==64206){
            FaceBookManager.onActivityResult(requestCode, resultCode, data);
//			}
        }
        if (TwitterManager.requestCode_tw == requestCode) {
            TwitterManager.onActivityResult(requestCode, resultCode, data);
        }

    }
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Logger.i("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
				Logger.i( key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
			Logger.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
			Logger.e("No such an algorithm", e.toString());
        } catch (Exception e) {
			Logger.e("Exception", e.toString());
        }

        return key;
    }



}
