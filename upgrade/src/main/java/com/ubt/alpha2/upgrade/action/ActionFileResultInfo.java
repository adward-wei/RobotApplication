package com.ubt.alpha2.upgrade.action;

import java.util.List;

/**
 * @author: slive
 * @description: 
 * @create: 2017/7/25
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class ActionFileResultInfo {
    List<ActionUpgradeBean.ActionFileBean> addActions;
    List<String> delActionIds;

    public ActionFileResultInfo() {
    }

    public List<ActionUpgradeBean.ActionFileBean> getAddActions() {
        return addActions;
    }

    public void setAddActions(List<ActionUpgradeBean.ActionFileBean> addActions) {
        this.addActions = addActions;
    }

    public List<String> getDelActionIds() {
        return delActionIds;
    }

    public void setDelActionIds(List<String> delActionIds) {
        this.delActionIds = delActionIds;
    }
}
