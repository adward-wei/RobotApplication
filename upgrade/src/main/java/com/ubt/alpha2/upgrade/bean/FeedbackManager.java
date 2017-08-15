package com.ubt.alpha2.upgrade.bean;

import com.google.gson.Gson;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.net.HttpCommon;
import com.ubt.alpha2.upgrade.net.HttpManager;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.UpgradeFeedbackConfig;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * Created by ubt on 2017/7/17.
 */

public class FeedbackManager {

    public FeedbackManager(){

    }

    public void uploadFeedback(final FeedbackInfo feedbackInfo){
        Gson gson = new Gson();
        String feedbackInfoJson = gson.toJson(feedbackInfo);
        HttpManager.get(UpgradeApplication.getContext()).doPostWithJson(HttpCommon.UpgradeFeedBack.URL+"?access_token="+feedbackInfo.access_token,
                feedbackInfoJson, new ResponseListener<FeedbackResult>() {
                    @Override
                    public void onError(ThrowableWrapper e) {
                        LogUtils.e("feedbackResult error "+e.toString());
                    }

                    @Override
                    public void onSuccess(FeedbackResult feedbackResult) {
                        LogUtils.e("feedbackResult: "+feedbackResult);
                        UpgradeFeedbackConfig.getInstance().saveFeedbackInfo(feedbackInfo.module_name,"");
                    }
                });
    }

    private static class FeedbackResult{
        String code;
        String data;

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("code: "+code);
            sb.append(",");
            sb.append("data: "+data);
            return sb.toString();
        }
    }
}
