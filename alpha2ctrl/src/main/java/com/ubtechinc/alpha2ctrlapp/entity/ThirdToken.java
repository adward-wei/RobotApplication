package com.ubtechinc.alpha2ctrlapp.entity;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author：tanghongyu
 * @date：2016/8/30 16:37
 * @modifier：tanghongyu
 * @modify_date：2016/8/30 16:37
 * [A brief description]
 * version
 */
public class ThirdToken implements Serializable {
    //
    private String userId;
    //
    private String accessToken ;
    private List<RobotInfo> robotlist;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public List<RobotInfo> getRobotlist() {
        return robotlist;
    }

    public void setRobotlist(List<RobotInfo> robotlist) {
        this.robotlist = robotlist;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
