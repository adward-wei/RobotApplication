package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.ubtechinc.alpha2ctrlapp.base.BasePresenter;
import com.ubtechinc.alpha2ctrlapp.base.BaseView;

/**
 * @author：tanghongyu
 * @date：4/13/2017 10:22 AM
 * @modifier：tanghongyu
 * @modify_date：4/13/2017 10:22 AM
 * [A brief description]
 * version
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void refreshUI(String loginKey, String countryNum, String countryName, String imgUrl);
        void refreshRegisterByPhone();
        void refreshRegisterByEmail();
        void refreshCountry(String countryNum, String countryName);
        void showLoginUserNamePrompt(int promptId);
        void showLoginPwdPrompt(int promptId);
        void replaceFragment(String fragmentName, Bundle bundle);
        void showLoadingDialog();
        void dismissLoadingDialog();
        void setCountry(String countryNum, String countryName);
        void skipActivity(Intent intent);

    }

    interface Presenter extends BasePresenter {
        void doWeiXinLogin();
        void doFackbookLogin(Activity activity);
        void doTwitterLogin();
        void doQQLogin(Activity activity);
        void handleCountryRequest(Bundle bundle);
        boolean checkLoginParam(String userName, String pwd);
        void doLoginRequest(String userName, String pwd);
        void getWeiXinLoginInfo(SendAuth.Resp rsp);
        void changeLoginModel(boolean isPhone);
    }
}
