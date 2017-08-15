package com.ubtechinc.alpha2ctrlapp.entity.business.robot;


import com.ubtechinc.alpha2ctrlapp.entity.SearchRelationInfo;

import java.util.List;

/**
 * Created by nixiaoyan on 2016/12/15.
 * 机器人信息 以及授权用户类
 */

public class RobotAndAuthoriserModel {
    /**刷新授权用户的数据时间避免用户重复多次刷新**/
    private long refreshAuthoriserTime;
    /**机器人信息**/
    private RobotInfo robotInfo;
    /**授权用户信息**/
    private List<SearchRelationInfo> authoriInfo;

    public long getRefreshAuthoriserTime() {
        return refreshAuthoriserTime;
    }

    public void setRefreshAuthoriserTime(long refreshAuthoriserTime) {
        this.refreshAuthoriserTime = refreshAuthoriserTime;
    }

    public List<SearchRelationInfo> getAuthoriInfo() {
        return authoriInfo;
    }

    public void setAuthoriInfo(List<SearchRelationInfo> authoriInfo) {
        this.authoriInfo = authoriInfo;
    }

    public RobotInfo getRobotInfo() {
        return robotInfo;
    }

    public void setRobotInfo(RobotInfo robotInfo) {
        this.robotInfo = robotInfo;
    }
}
