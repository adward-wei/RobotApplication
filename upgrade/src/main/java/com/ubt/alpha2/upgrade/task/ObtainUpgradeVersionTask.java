package com.ubt.alpha2.upgrade.task;

import android.content.Context;

import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.AccessTokenInfo;
import com.ubt.alpha2.upgrade.bean.ModelIdBean;
import com.ubt.alpha2.upgrade.bean.ModuleVersionBean;
import com.ubt.alpha2.upgrade.bean.ReportDataBean;
import com.ubt.alpha2.upgrade.bean.UpgradeModuleManager;
import com.ubt.alpha2.upgrade.exception.UpgradeException;
import com.ubt.alpha2.upgrade.impl.ISyncTask;
import com.ubt.alpha2.upgrade.impl.IUpgradeVersionInterface;
import com.ubt.alpha2.upgrade.impl.IUpgradeVersionResult;
import com.ubt.alpha2.upgrade.net.HttpCommon;
import com.ubt.alpha2.upgrade.net.HttpManager;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubtech.utilcode.utils.Utils;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * Created by ubt on 2017/7/20.
 */

public class ObtainUpgradeVersionTask implements ISyncTask,IUpgradeVersionInterface {

    UpgradeModuleManager upgradeModuleManager;
    HttpManager httpManager;
    IUpgradeVersionResult result;

    public ObtainUpgradeVersionTask(IUpgradeVersionResult upgradeVersionResult){
        Context mContext = UpgradeApplication.getContext();
        upgradeModuleManager = UpgradeModuleManager.getInstance();
        httpManager = HttpManager.get(UpgradeApplication.getContext());
        Utils.init(mContext);
        httpManager.init(mContext, HttpCommon.BASE_URL);
        this.result = upgradeVersionResult;
    }


    @Override
    public void run() {
        try{
            getAccessToken();
            getModuleIdList();
            getVersionInfoList();
        }catch (Exception e){
            LogUtils.logException(e);
        }finally {
            if(result != null){
                result.onResult();
            }
        }
    }

    @Override
    public void getAccessToken() {
        LogUtils.d("  getAccessToken: "+upgradeModuleManager.getAccessTokenInfo());
        if(upgradeModuleManager.getAccessTokenInfo() == null) {
            httpManager.doGet(HttpCommon.AccessToken.URL,
                    HttpCommon.AccessToken.getAccessTokenParams(),
                    new HttpManager.HttpResponseListener<AccessTokenInfo>() {
                        @Override
                        public void onError(ThrowableWrapper e) {
                            LogUtils.logException(new Exception(e));
                            upgradeModuleManager.setAccessTokenInfo(null);
                            upgradeModuleManager.setModelIdBean(null);
                            ReportDataBean.getInstance().setAccessToken("null");
                            notifyForExecute();
                        }

                        @Override
                        public void onSuccess(AccessTokenInfo accessTokenInfo) {
                            upgradeModuleManager.setAccessTokenInfo(accessTokenInfo);
                            upgradeModuleManager.setModelIdBean(null);
                            ReportDataBean.getInstance().setAccessToken(accessTokenInfo.access_token);
                            notifyForExecute();
                        }
                    });
            LogUtils.e("wait for getAccessToken");
            waitForResponse();
        }
    }

    @Override
    public void getModuleIdList() {
        LogUtils.d("getModuleIdList: "+upgradeModuleManager.getModelIdBean());
        if(upgradeModuleManager.getModelIdBean() == null){
            httpManager.doGet(HttpCommon.RobotModuleInfo.URL,
                    HttpCommon.RobotModuleInfo.getRobotModuleInfoParams(upgradeModuleManager.getAccessToken()),
                    new HttpManager.HttpResponseListener<ModelIdBean>() {
                        @Override
                        public void onError(ThrowableWrapper e){
                            LogUtils.logException(new Exception(e));
                            upgradeModuleManager.setModelIdBean(null);
                            ReportDataBean.getInstance().setModuleId("null");
                            notifyForExecute();
                        }

                        @Override
                        public void onSuccess(ModelIdBean modelIdBean) {
                            upgradeModuleManager.setModelIdBean(modelIdBean);
                            notifyForExecute();
                        }
                    });
            LogUtils.e("wait for getModuleId");
            waitForResponse();
        }
    }

    @Override
    public void getVersionInfoList() {
        LogUtils.d("getVersionInfo: "+upgradeModuleManager.getModuleVersionBean());
        if(upgradeModuleManager.getModuleVersionBean() == null){
            String model = upgradeModuleManager.getModuleNameList();
            LogUtils.d("model: "+model);
            httpManager.doGet(HttpCommon.VersionInfo.URL,
                    HttpCommon.VersionInfo.getVersionInfoParams(upgradeModuleManager.getAccessToken(),
                            model, ApkUtils.getRobotId()), new HttpManager.HttpResponseListener<ModuleVersionBean>() {
                        @Override
                        public void onError(ThrowableWrapper e)  {
                            LogUtils.logException(new Exception(e));
                            LogUtils.d("onError: "+e);
                            upgradeModuleManager.setModuleVersionBean(null);
                            notifyForExecute();
                        }

                        @Override
                        public void onSuccess(ModuleVersionBean module) {
                            LogUtils.d("onSuccess: "+module);
                            upgradeModuleManager.setModuleVersionBean(module);
                            notifyForExecute();
                        }
                    });
            LogUtils.e("wait for getVersionInfo");
            waitForResponse();
        }
    }

    public void waitForResponse(){
        synchronized (this){
            try{
                wait();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void notifyForExecute(){
        synchronized (this){
            notify();
        }
    }
}
