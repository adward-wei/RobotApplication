package com.ubt.alpha2.upgrade.action;

import android.support.annotation.Keep;
import android.text.TextUtils;

import com.ubt.alpha2.statistics.StatisticsWrapper;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.UpgradeFeedbackConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: slive
 * @description: 动作上报异常管理
 * @create: 2017/7/27
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
@Keep
public class ActionReportManager{

    public void addReportWithMd5Error(String actionIds){
        StatisticsWrapper.getInstance().onEvent(UpgradeApplication.getContext(),
                UpgradeFeedbackConfig.UMENG_ACTION_MD5_ERROR,actionIds);
        String oldReport = UpgradeFeedbackConfig.getInstance().getActionMd5ErrorReport();
        if(!TextUtils.isEmpty(oldReport))
            oldReport += ","+actionIds;
        else
            oldReport = actionIds;
        UpgradeFeedbackConfig.getInstance().saveActionMd5ErrorReport(oldReport);
    }

    public void addReportWithUnZipError(String actionIds){
        String oldReport = UpgradeFeedbackConfig.getInstance().getActionUnzipErrorReport();
        if(!TextUtils.isEmpty(oldReport))
            oldReport += ","+actionIds;
        else
            oldReport = actionIds;
        UpgradeFeedbackConfig.getInstance().saveActionUnzipErrorReport(oldReport);
        StatisticsWrapper.getInstance().onEvent(UpgradeApplication.getContext(),
                UpgradeFeedbackConfig.UMENG_ACTION_UNZIP_ERROR,actionIds);
    }


    public String toJsonStr(String actionIds,String reason){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("actionIds",actionIds);
            jsonObject.put("reason",reason);
            jsonObject.put("robotSeq", ApkUtils.getRobotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * @author: slive
     * @description: 动作异常数据上报
     * @create: 2017/7/27
     * @email: slive.shu@ubtrobot.com
     * @modified: slive
     */
    @Keep
    static class ActionReportBean {
        public List<String> actionIds;
        public String reason;
        public String robotSeq;

        public ActionReportBean(){
            actionIds = new ArrayList<>();
        }

        public String toJsonStr(){
            if(actionIds == null || actionIds.isEmpty())
                return null;
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<actionIds.size();i++){
                sb.append(actionIds.get(i));
                if(i<actionIds.size()-1)
                    sb.append(",");
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("actionIds",sb.toString());
                jsonObject.put("reason",reason);
                jsonObject.put("robotSeq",robotSeq);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

    /**
     * @author: slive
     * @description: 动作上报数据响应
     * @create: 2017/7/27
     * @email: slive.shu@ubtrobot.com
     * @modified: slive
     */
    @Keep
    static class ResponseBean{
        public Object data;
        public String msg;
        public String number;
        public boolean success;

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("data: "+data+",");
            sb.append("msg: "+msg+",");
            sb.append("number: "+number+",");
            sb.append("success: "+success);
            return sb.toString();
        }
    }
}


