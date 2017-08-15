package com.ubt.alpha2.upgrade.action;


import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: slive
 * @description: 对比动作文件算法(1、对比本地数据库和后台返回的数据;
 *                                 2、本地数据库多出的记录全部del;
 *                                 3、后台多出的，全部insert到本地数据库;
 *                                 4、相同的，保留本地数据)
 * @create: 2017/7/26
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class CompareActionFile {
    List<ActionUpgradeBean.ActionFileBean> localActionFiles = new ArrayList<>();
    List<ActionUpgradeBean.ActionFileBean> serverActionFiles = new ArrayList<>();
    List<String> clientActionIds = new ArrayList<>();

    public CompareActionFile(List<ActionUpgradeBean.ActionFileBean> local, List<ActionUpgradeBean.ActionFileBean> serverActionFiles) {
        this.localActionFiles = local;
        this.serverActionFiles = serverActionFiles;
    }

    public ActionFileResultInfo getDifferent() {
        List<String> delActionIds = new ArrayList<>();
        Map<String, ActionUpgradeBean.ActionFileBean> map = new HashMap<>();
        //记录本地数据总数
        int count = localActionFiles.size();
        for(int i=0;i<count;i++){
            clientActionIds.add(localActionFiles.get(i).actionId);
        }
        //将server添加到map中
        for (ActionUpgradeBean.ActionFileBean actionFile : serverActionFiles){
            map.put(actionFile.actionId, actionFile);
        }

        //对比本地数据 相同id的，保留，不同的全部添加到 delActionIds中，剩下的下载并添加到数据库
        for (String s : clientActionIds){
            if (map.get(s) != null ) {
                map.remove(s);
                continue;
            }
            delActionIds.add(s);
        }
        for(int i=0;i<delActionIds.size();i++){
            LogUtils.d(" delActionId.id .. "+i+"=" +delActionIds.get(i));
        }
        List<ActionUpgradeBean.ActionFileBean> addActions = new ArrayList<>();
        for (Map.Entry<String, ActionUpgradeBean.ActionFileBean> entry : map.entrySet()) {
            LogUtils.d("entry.getValue: "+entry.getValue());
            addActions.add(entry.getValue());
        }
        for(int i=0;i<addActions.size();i++){
            LogUtils.d(" addActions.id .. "+i+"=" +addActions.get(i).actionId);
        }
        ActionFileResultInfo result = new ActionFileResultInfo();
        result.setAddActions(addActions);
        result.setDelActionIds(delActionIds);
        return result;
    }
}
