package com.ubtechinc.alpha2ctrlapp.data.user;


import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetRegisterCodeModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.RegisterModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @ClassName UserPwdReponsitory
 * @date 6/5/2017
 * @author tanghongyu
 * @Description 注册相关类
 * @modifier
 * @modify_time
 */
public class RegisterReponsitory implements IRegisterDataSource{

    volatile private static RegisterReponsitory INSTANCE = null;
    private RegisterReponsitory(){}

    public static RegisterReponsitory get() {
        try {
            if(INSTANCE != null){//懒汉式

            }else{
                //创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (RegisterReponsitory.class) {
                    if(INSTANCE == null){//二次检查
                        INSTANCE = new RegisterReponsitory();
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
    public void getRegisterCode(String account, String registerType, final @NonNull GetRegisterCodeCallback callback) {
        GetRegisterCodeModule.Request getCodeRequest = new GetRegisterCodeModule().new Request();
        getCodeRequest.setAccount(account);
        getCodeRequest.setRegisterType(registerType);
        HttpProxy.get().doGet(getCodeRequest, new ResponseListener<GetRegisterCodeModule.Response>() {
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
    public void doRegister(String userAccount, String pwd, int accountType,String verifyCode, final  @NonNull RegisterCallback callback) {
        RegisterModule.Request registerRequest = new RegisterModule().new Request();
        if(accountType == BusinessConstants.ACCOUNT_TYPE_PHONE) {
            registerRequest.setUserPhone(userAccount);
        }else {
            registerRequest.setUserEmail(userAccount);
        }
        registerRequest.setUserPassword(pwd);
        registerRequest.setVerificationCode(verifyCode);
        HttpProxy.get().doPost(registerRequest, new ResponseListener<RegisterModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(RegisterModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess(response);
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }
}
