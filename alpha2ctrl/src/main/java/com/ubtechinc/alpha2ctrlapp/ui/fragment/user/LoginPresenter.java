package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.ILoginDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.LoginAndOutReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.MessageRepository;
import com.ubtechinc.alpha2ctrlapp.database.NoticeManager;
import com.ubtechinc.alpha2ctrlapp.entity.LoginInfo;
import com.ubtechinc.alpha2ctrlapp.entity.QQLoginInfo;
import com.ubtechinc.alpha2ctrlapp.entity.WeiXinLoginInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.UserInfo;
import com.ubtechinc.alpha2ctrlapp.entity.net.LoginModule;
import com.ubtechinc.alpha2ctrlapp.entity.request.GetWeixinLoginRequest;
import com.ubtechinc.alpha2ctrlapp.entity.response.ThirdLoginResponse;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.alpha2ctrlapp.third.FaceBookManager;
import com.ubtechinc.alpha2ctrlapp.third.ITwitterLoginListener;
import com.ubtechinc.alpha2ctrlapp.third.IWeiXinListener;
import com.ubtechinc.alpha2ctrlapp.third.TencentQQManager;
import com.ubtechinc.alpha2ctrlapp.third.TwitterManager;
import com.ubtechinc.alpha2ctrlapp.third.WeiXinManager;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.AcceptAuthorizeActivity;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import twitter4j.auth.AccessToken;

/**
 * @author：tanghongyu
 * @date：4/13/2017 10:23 AM
 * @modifier：tanghongyu
 * @modify_date：4/13/2017 10:23 AM
 * [A brief description]
 * version
 */

public class LoginPresenter implements LoginContract.Presenter {
    private static final String TAG = "LoginPresenter";
    //国家名
    private String countryName = "";
    //国家手机码前缀，中国+86
    private String countryNum = "-1";

    private QQLoginInfo mQQLoginInfo;
    private int thirdLoginType = 0;
    private WeiXinLoginInfo mWeiXinLoginInfo;
    //手机号注册
    private final int REGIST_BY_PHONE = 0;
    //email注册
    private final int REGIST_BY_EMAIL = 1;
    private int currentModel = REGIST_BY_PHONE;
    private Activity activity;
    private Alpha2Application mContext;
    private LoginContract.View mView;
    private MessageRepository mNoticeMessageRepository;
    private LoginAndOutReponsitory loginReponsitory;

    public LoginPresenter(@NonNull Activity mContext, @NonNull LoginContract.View view, @NonNull MessageRepository noticeMessageRepository, @NonNull LoginAndOutReponsitory loginReponsitory) {
        Preconditions.checkNotNull(mContext);
        Preconditions.checkNotNull(view);
        Preconditions.checkNotNull(noticeMessageRepository);
        Preconditions.checkNotNull(loginReponsitory);
        this.activity = mContext;
        this.mContext = (Alpha2Application) activity.getApplicationContext();
        this.mView = view;
        this.loginReponsitory = loginReponsitory;
        this.mNoticeMessageRepository = noticeMessageRepository;
        mView.setPresenter(this);
    }


    @Override
    public void initData() {
        countryNum = SPUtils.get().getString( PreferenceConstants.COUNTRY_NUM, "-1");
        countryName = SPUtils.get().getString( PreferenceConstants.COUNTRY_NAME, "");
        String userAccount = SPUtils.get().getString( PreferenceConstants.USER_ACCOUNT, "");
        if (StringUtils.isEmpty(countryNum) || countryNum.equals("-1") || StringUtils.isEmpty(countryName)) {
            //默认国家：中国
            countryNum = "86";
            countryName = "CN";
            countryNum = "+" + countryNum;
        }

        mView.setCountry(countryNum, countryName);

        String userUrl = SPUtils.get().getString( PreferenceConstants.USER_IMAGE);


        if (Tools.isEmail(userAccount)) {//本地缓存的用户名是否是手机号码
            currentModel = REGIST_BY_EMAIL;
            mView.refreshRegisterByEmail();
        } else {

            currentModel = REGIST_BY_PHONE;
            mView.refreshRegisterByPhone();

        }

        mView.refreshUI(userAccount, countryNum, countryName, userUrl);

    }

    @Override
    public void doWeiXinLogin() {
        WeiXinManager.doLogin(mContext, new IWeiXinListener() {
            @Override
            public void noteWeixinNotInstalled() {
                ToastUtils.showShortToast(  R.string.ui_weixin_not_install);
            }
        });
    }

    @Override
    public void doFackbookLogin(Activity activity) {

        FaceBookManager.doLogin(activity, new FaceBookManager.IFaceBookLoginListener() {
            @Override
            public void onLoginComplete(JSONObject object) {
                Logger.i( "facebook 返回的登录消息"+object.toString());
                try {
                    doThirdLogin(object.getString("id"), 4);
                    mView.showLoadingDialog();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void doTwitterLogin() {
        TwitterManager.doLogin(activity, new ITwitterLoginListener() {
            @Override
            public void OnTwitterLoginComplete(AccessToken accessToken) {
                Logger.i( "twitter 返回的登录消息"+accessToken.toString());
                doThirdLogin(String.valueOf(accessToken.getUserId()), 5);
                mView.showLoadingDialog();
            }
        });
    }



    @Override
    public void doQQLogin(Activity activity) {
        TencentQQManager.doLogin(activity, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                String result = ((JSONObject) o).toString();
                Logger.d( "QQLogin  onComplete:" + result);
                mQQLoginInfo = JsonUtil.getObject(result,
                        QQLoginInfo.class);
                // 本地登录
               SPUtils.get().getString(Constants.QQ_TOKEN, mQQLoginInfo.getAccess_token());
                doThirdLogin(mQQLoginInfo.openid, 1);
                mView.showLoadingDialog();
            }

            @Override
            public void onError(UiError uiError) {
                LogUtils.w("QQLogin onError : " + uiError.errorMessage);
            }

            @Override
            public void onCancel() {
                LogUtils.w("QQLogin onCancel  " );
            }
        });
    }

    @Override
    public void handleCountryRequest(Bundle bundle) {
        countryName = bundle.getString("countryName");
        countryNum = bundle.getString("countryNumber").substring(1);
        mView.refreshCountry(countryNum, countryName);
         SPUtils.get().put( PreferenceConstants.COUNTRY_NAME, countryName);

         SPUtils.get().put( PreferenceConstants.COUNTRY_NUM, countryNum);
    }

    @Override
    public boolean checkLoginParam(String userName, String pwd) {
        if (currentModel == REGIST_BY_PHONE) {
            if (TextUtils.isEmpty(userName)) {

                mView.showLoginUserNamePrompt(R.string.ui_phone_empty_note);

                return false;
            } else if (!Tools.isNumeric(userName)) {
                Toast.makeText(mContext, R.string.ui_input_correct_phone,
                        Toast.LENGTH_SHORT).show();
                mView.showLoginUserNamePrompt(R.string.ui_input_correct_phone);

                return false;
            } else {
                if (TextUtils.isEmpty(countryNum) || countryName.equals("-1")) {
                    Toast.makeText(mContext, R.string.ui_sel_contory,
                            Toast.LENGTH_SHORT).show();
                    mView.showLoginUserNamePrompt(R.string.ui_sel_contory);
                    return false;
                }
            }
        } else if (currentModel == REGIST_BY_EMAIL) {
            if (TextUtils.isEmpty(userName)) {
                mView.showLoginUserNamePrompt(R.string.ui_email_empty_note);
                return false;
            } else if (!Tools.isEmail(userName)) {

                mView.showLoginUserNamePrompt(R.string.ui_input_correct_mial);
                return false;
            }
        }
        if (TextUtils.isEmpty(pwd)) {
            mView.showLoginPwdPrompt(R.string.ui_pass_empty_note);
            return false;
        }
        if (pwd.length() < 6) {
            mView.showLoginPwdPrompt(R.string.login_passwd_short);
            return false;
        }
//        tipsTV.setText("");
        return true;
    }
    @Override
    public void doLoginRequest( String userName,final String pwd) {

        SPUtils.get().put(PreferenceConstants.USER_ACCOUNT,
                userName);
        if(currentModel==REGIST_BY_PHONE){
            userName = countryNum + userName;
            if(userName.contains("+")) {
                userName =  userName.substring(1);
            }
        }


        this.loginReponsitory.loginAndLoadData( userName, pwd, new ILoginDataSource.LoginDataCallback() {


            @Override
            public void onLoadLoginData(LoginModule.Response loginResponses) {

                UserInfo result = loginResponses.getData().getResult().get(0);
                SPUtils.get().put(PreferenceConstants.USER_ID, result.getUserId());
                Alpha2Application.getAlpha2().setUserId(result.getUserId());
                Phone2RobotMsgMgr.getInstance().init();
                SPUtils.get().put(
                        PreferenceConstants.USER_PASSWORD,
                        pwd);
                SPUtils.get().put(PreferenceConstants.USER_NAME, result.getUserName());
                SPUtils.get().put(PreferenceConstants.USER_IMAGE, result.getUserImage());
                SPUtils.get().put(PreferenceConstants.USER_PHONE, result.getUserPhone());
                SPUtils.get().put(PreferenceConstants.USER_EMAIL, result.getUserEmail());
                SPUtils.get().put(PreferenceConstants.COUNTRY_NUM, result.getCountryCode());
                SPUtils.get().put(PreferenceConstants.USER_GENDER, result.getUserGender());
                SPUtils.get().put(PreferenceConstants.APP_RUN_TIMES, 2);
                loginSuccess();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
                ToastUtils.showShortToast(e.getMessage());
            }
        });

    }

    @Override
    public void getWeiXinLoginInfo(SendAuth.Resp rsp) {
        GetWeixinLoginRequest request = new GetWeixinLoginRequest();
        request.setAppid(WeiXinManager.WEIXIN_APP_ID);
        request.setSecret(WeiXinManager.WEIXIN_APP_SECRET);
        request.setCode(rsp.code);
        request.setGrant_type(WeiXinManager.GRANTTYPE);
        loginReponsitory.getWeiXinLoginInfo(request, new ILoginDataSource.getWeiXinLoginInfoCallback() {

            @Override
            public void onLoadLoginData(List<WeiXinLoginInfo> weiXinLoginInfos) {

                 SPUtils.get().put(Constants.WEIXIN_TOKEN, weiXinLoginInfos.get(0).getAccess_token());
                doThirdLogin(weiXinLoginInfos.get(0).getOpenid(), 2);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

       mView.showLoadingDialog();

    }

    @Override
    public void changeLoginModel(boolean isPhone) {
        currentModel = isPhone ? REGIST_BY_PHONE : REGIST_BY_EMAIL;
    }


    private void doThirdLogin(String id, int type) {
        thirdLoginType = type;
        loginReponsitory.doThirdLogin(id, type + "", new ILoginDataSource.ThirdLoginCallback() {
            @Override
            public void onLoadLoginData(ThirdLoginResponse thirdLoginResponses) {
                LoginInfo info = thirdLoginResponses.getModels().get(0);
                if(info!=null){
                    if(!StringUtils.isEmpty(info.getUserName())  && !StringUtils.isEmpty(info.getCountryCode())&&!info.getUserName().equals("-1")){
                        SPUtils.get().put(PreferenceConstants.USER_ID,info.getUserId());
                        mContext.setUserId(info.getUserId());
                        SPUtils.get().put(PreferenceConstants.TOKEN,info.getToken());
                        SPUtils.get().put(PreferenceConstants.USER_NAME,info.getUserName());
                        SPUtils.get().put(PreferenceConstants.COUNTRY_NUM,info.getCountryCode());
                        SPUtils.get().put(PreferenceConstants.USER_IMAGE,info.getUserImage());
                        SPUtils.get().put(PreferenceConstants.USER_PASSWORD, info.getUserRelationId().substring(0,6));
                    }else{
                        // 没有注册
                        mView.dismissLoadingDialog();
                        SPUtils.get().put(PreferenceConstants.USER_ID,info.getUserId());
                        mContext.setUserId(info.getUserId());
                        SPUtils.get().put(PreferenceConstants.TOKEN,info.getToken());
                        SPUtils.get().put(PreferenceConstants.USER_PASSWORD, info.getUserRelationId().substring(0,6));
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isThird", true);
                        bundle.putInt("type", thirdLoginType);
                        if(thirdLoginType==1)
                            bundle.putString("thrid_info",JsonUtil.object2Json(mQQLoginInfo));
                        else if(thirdLoginType==2)
                            bundle.putSerializable("thrid_info", (Serializable) mWeiXinLoginInfo);
//                        mView.replaceFragment(CompleteUserInfoFragment.class.getName(),bundle);
                    }
                }else{
                    mView.dismissLoadingDialog();
                }

                Constants.THIRD_LOGIN_TYPE = thirdLoginType;
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }

        });
    }

    private void loginSuccess() {
        Constants.hasLoginOut = false;
        SPUtils.get().put(PreferenceConstants.LOGIN_TIME, Tools.getCurrentTime());
        String countryNum = SPUtils.get().getString(PreferenceConstants.COUNTRY_NUM, "");
        if (RegistFragement.needLogin
                || StringUtils.isEmpty(countryNum)
                || countryNum.equals("-1")) {
            RegistFragement.needLogin = false;
            Bundle bundle = new Bundle();
            LoadingDialog.dissMiss();

            if (RegistFragement.currentModel == BusinessConstants.ACCOUNT_TYPE_PHONE) {
                bundle.putBoolean(IntentConstants.DATA_IS_PHONE_REGISTER, true);
            } else {
                bundle.putBoolean(IntentConstants.DATA_IS_PHONE_REGISTER, false);
            }
            mView.replaceFragment(CompleteUserInfoFragment.class.getName(), bundle);
        } else {
            //
            NoticeManager noticeManager = NoticeManager
                    .getInstance();

            String userId = SPUtils.get().getString(PreferenceConstants.USER_ID);
            //获取本地未读的授权消息（只取授权）
            List<NoticeMessage> authorizeList = noticeManager
                    .getUnReadAuthorizeNoticeList(userId);
            for (int i = 0; i < authorizeList.size(); i++) {
                if (authorizeList.get(i) == null
                        || authorizeList.get(i).getToId() == null
                        || !authorizeList.get(i).getToId()
                        .contains(userId)) {
                    if (authorizeList.get(i) != null)
                        noticeManager.delById(userId,authorizeList.get(i)
                                .getNoticeId());
                    authorizeList.remove(i);
                }
            }
            if (ListUtils.isEmpty(authorizeList)) {
                Intent intent = new Intent(mContext,MainPageActivity.class);
                mView.skipActivity(intent);
                Alpha2Application.getInstance().removeActivity();
            } else {
                Intent intent = new Intent(mContext,
                        AcceptAuthorizeActivity.class);
                intent.putExtra("noticelist", (Serializable) authorizeList);
                intent.putExtra(IntentConstants.IS_OFFLINE_MESSAGE, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               mView.skipActivity(intent);
            }
        }
    }

}
