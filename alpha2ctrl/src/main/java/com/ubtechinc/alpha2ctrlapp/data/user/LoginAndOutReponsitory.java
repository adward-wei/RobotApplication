package com.ubtechinc.alpha2ctrlapp.data.user;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.orhanobut.logger.Logger;
import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.net.LoginModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.LoginoutModule;
import com.ubtechinc.alpha2ctrlapp.entity.request.GetWeixinLoginRequest;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @author：tanghongyu
 * @date：4/13/2017 5:22 PM
 * @modifier：tanghongyu
 * @modify_date：4/13/2017 5:22 PM
 * [A brief description]
 * version
 */

public class LoginAndOutReponsitory implements ILoginDataSource {
    private static final String TAG = "LoginAndOutReponsitory";
    private static LoginAndOutReponsitory INSTANCE = null;


    public LoginAndOutReponsitory() {
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link MessageRepository} instance
     */
    public static LoginAndOutReponsitory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoginAndOutReponsitory();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void loginOut(final @NonNull LoginOutCallback callback) {
        LoginoutModule.Request request = new LoginoutModule().new Request();
        HttpProxy.get().doPost(request, new ResponseListener<LoginoutModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(LoginoutModule.Response response) {


                if(ErrorParser.get().isSuccess(response.getResultCode())) {

                    callback.onSuccess();
                }else{
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }

            }
        });
    }

    @Override
    public void loginAndLoadData(final String userName, final String pwd,final  @NonNull LoginDataCallback callback) {
        Preconditions.checkNotNull(callback);
        LoginModule.LoginRequest loginRequest = new LoginModule().new LoginRequest();
        if(Tools.isEmail(userName)) {
            loginRequest.setUserEmail(userName);
        }else {
            loginRequest.setUserPhone(userName);
        }
        loginRequest.setUserPassword(pwd);
        HttpProxy.get().doPost(loginRequest, new ResponseListener<LoginModule.Response>() {

            @Override
            public void onError(ThrowableWrapper e)
            {
                callback.onDataNotAvailable(e);
                Logger.e(TAG, e);
            }

            @Override
            public void onSuccess(LoginModule.Response loginResponse) {

                if(ErrorParser.get().isSuccess(loginResponse.getResultCode())) {

                    callback.onLoadLoginData(loginResponse);
                }else{
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }


            }
        });
    }

    @Override
    public void doThirdLogin(String relationId, String relationType, @NonNull ThirdLoginCallback callback) {


    }

    @Override
    public void getWeiXinLoginInfo(GetWeixinLoginRequest request, @NonNull getWeiXinLoginInfoCallback callback) {

    }


}
