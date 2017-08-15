package com.ubt.alpha2.upgrade.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.FeedbackInfo;

/**
 * @author: slive
 * @description: 上传升级信息
 * @create: 2017/7/17
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class UpgradeFeedbackConfig {

    public static final String UMENG_ACTION_MD5_ERROR="action_md5_error";
    public static final String UMENG_ACTION_UNZIP_ERROR = "action_unzip_error";

    private SharedPreferences sharedPreferences;

    private UpgradeFeedbackConfig(){
        sharedPreferences = UpgradeApplication.getContext().getSharedPreferences("upgrade_feedback", Context.MODE_PRIVATE);
    }

    public static UpgradeFeedbackConfig getInstance(){
        return ConfigHolder._install;
    }

    private static class ConfigHolder{
       public static UpgradeFeedbackConfig _install = new UpgradeFeedbackConfig();
    }

    public void upgdateUpgradeStatus(String moduleName,int upgradeStatus){
        FeedbackInfo feedbackInfo = getFeedbackInfo(moduleName);
        if(feedbackInfo == null)
            return;
        saveFeedbackInfo(moduleName,feedbackInfo);
    }

    public void saveFeedbackInfo(String moduleName,FeedbackInfo feedbackInfo){
        Gson gson = new Gson();
        String feedbackInfoJson = gson.toJson(feedbackInfo);
        sharedPreferences.edit().putString(moduleName,feedbackInfoJson).apply();
    }

    public void saveFeedbackInfo(String moduleName,String feedbackInfoJson){
        sharedPreferences.edit().putString(moduleName,feedbackInfoJson).apply();
    }

    public FeedbackInfo getFeedbackInfo(String moduleName){
        String feedbackInfoJson =  sharedPreferences.getString(moduleName,"");
        if(TextUtils.isEmpty(feedbackInfoJson))
            return null;
        Gson gson = new Gson();
        FeedbackInfo feedbackInfo = gson.fromJson(feedbackInfoJson,FeedbackInfo.class);
        return feedbackInfo;
    }

    public void saveActionMd5ErrorReport(String actionIds){
        sharedPreferences.edit().putString(UMENG_ACTION_MD5_ERROR,actionIds).apply();
    }

    public String getActionErrorReport(String reason){
        return sharedPreferences.getString(reason,null);
    }

    public void saveActionErrorReport(String reason,String actionIds){
        sharedPreferences.edit().putString(reason,actionIds).apply();
    }

    public void saveActionUnzipErrorReport(String actionIds){
        sharedPreferences.edit().putString(UMENG_ACTION_UNZIP_ERROR,actionIds).apply();
    }

    public String getActionUnzipErrorReport(){
        return sharedPreferences.getString(UMENG_ACTION_UNZIP_ERROR,null);
    }

    public String getActionMd5ErrorReport(){
        return sharedPreferences.getString(UMENG_ACTION_MD5_ERROR,null);
    }

}
