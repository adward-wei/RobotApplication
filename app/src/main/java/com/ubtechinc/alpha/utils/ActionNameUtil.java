package com.ubtechinc.alpha.utils;


import android.content.Context;
import android.util.Log;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.download.ActionFileEntrity;
import com.ubtechinc.alpha.network.business.ActionDetail;
import com.ubtechinc.alpha.network.module.ActionDetailModule;
import com.ubtechinc.alpha.ops.action.Action;
import com.ubtechinc.alpha.provider.ActionInfoVisitor;

import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wzt
 * @date 2017/6/9
 * @Description 动作名称的后台查询、数据库操作类
 * @modifier
 * @modify_time
 */

public class ActionNameUtil {

    public static void insertActionInfo(String ids) {
        findActionNameInServer(ids, "EN");
        findActionNameInServer(ids, "CN");
    }

    private static void findActionNameInServer(final String ids, final String language) {
        ActionDetail.getInstance().requestAcionDetail(ids, new ResponseListener<ActionDetailModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(ActionDetailModule.Response response) {
                Action action = new Action();
                action.Id = response.data.result.actionOriginalId;
                action.cn_name = response.data.result.actionName;
                action.type = response.data.result.actionType;
                ActionInfoVisitor provider = ActionInfoVisitor.get();
                provider.saveOrUpdate(action);
            }

        });

/*
        ActionNameRequest actionNameRequest = new ActionNameRequest();
        actionNameRequest.setActionOriginalIds(ids);
        actionNameRequest.setSystemLanguage(language);

        UserAction action  = new UserAction(context, new ClientAuthorizeListener() {

            @Override
            public void onResult(int code, String info) {
                if(code == 1) {
                    LogUtil.d("", "!!! action name info=" + info);
                    ActionNameResponse actionNameResponse = (ActionNameResponse) JsonUtils.getInstance().jsonToBean(
                            info, ActionNameResponse.class);
                    if(actionNameResponse != null) {
                        //if("EN".equals(actionNameResponse.getModels().get(0).getSystemLanguage())) {
                        if("EN".equals(language)) {
                            for (ActionNameModel item : actionNameResponse.getModels()) {
                                if (ActionNameDao.isExist(context, ids + "")) {
                                    ActionNameDao.update(context, ids + "",
                                            ids + "", item.getActionSonType(),
                                            null, item.getActionName());
                                } else {
                                    ActionNameDao.insert(context, ids + "",
                                            item.getActionSonType(),
                                            null, item.getActionName());
                                }
                            }
                        //} else if("CN".equals(actionNameResponse.getModels().get(0).getSystemLanguage())) {
                        } else if("CN".equals(language)) {
                            for (ActionNameModel item : actionNameResponse.getModels()) {
                                if (ActionNameDao.isExist(context, ids + "")) {
                                    ActionNameDao.update(context, ids + "",
                                            ids + "", item.getActionSonType(),
                                            item.getActionName(), null);
                                } else {
                                    ActionNameDao.insert(context, ids + "",
                                            item.getActionSonType(),
                                            item.getActionName(), null);
                                }
                            }
                        }
                    } else {

                    }
                } else {

                }
            }});

        action.setParamerObj(actionNameRequest);
        action.doRequest(NetWorkConstant.REQUEST_ACTION_DETAIL, NetWorkConstant.ACTION_DETAIL);
*/
    }

    private static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }



}
