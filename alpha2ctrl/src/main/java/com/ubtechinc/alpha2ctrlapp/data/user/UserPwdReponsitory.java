package com.ubtechinc.alpha2ctrlapp.data.user;


import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetRegisterCodeModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetResetPwdCodeModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.ResetPwdModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.VerifyResetPwdCodeModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @ClassName UserPwdReponsitory
 * @date 6/5/2017
 * @author tanghongyu
 * @Description 用户密码管理类
 * @modifier
 * @modify_time
 */
public class UserPwdReponsitory implements IUserPwdSource{

    volatile private static UserPwdReponsitory INSTANCE = null;
    private UserPwdReponsitory(){}

    public static UserPwdReponsitory get() {
        try {
            if(INSTANCE != null){//懒汉式

            }else{
                //创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (UserPwdReponsitory.class) {
                    if(INSTANCE == null){//二次检查
                        INSTANCE = new UserPwdReponsitory();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getVerifyCode(String account, int accountType, final @NonNull GetVerifyCodeCallback callback) {

        GetResetPwdCodeModule.Request request = new GetResetPwdCodeModule().new Request();
        request.setAccountType(accountType);
        request.setAccount(account);
        HttpProxy.get().doGet(request, new ResponseListener<GetRegisterCodeModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(GetRegisterCodeModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess();
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }

    @Override
    public void checkVerifyCode(String account, int accountType, String verifyCode, final @NonNull CheckVerifyCodeCallback callback) {
        VerifyResetPwdCodeModule.Request request = new VerifyResetPwdCodeModule().new Request();
        request.setVerificationCode(verifyCode);
        if(accountType == 0) {
            request.setUserEmail(account);
        }else {
            request.setUserPhone(account);
        }
        HttpProxy.get().doPost(request, new ResponseListener<VerifyResetPwdCodeModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(VerifyResetPwdCodeModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess(response);
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }

    @Override
    public void resetPwd(String userId, String pwd, String uuid,final  @NonNull ResetPwdCallback callback) {
        ResetPwdModule.Request request = new ResetPwdModule().new Request();
        request.setUserId(userId);
        request.setUserPassword(pwd);
        request.setUuid(uuid);
        HttpProxy.get().doPost(request, new ResponseListener<ResetPwdModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(ResetPwdModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess();
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }
}
