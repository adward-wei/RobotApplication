package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

/**
 * @ClassName NewActionInfo
 * @date 6/15/2017
 * @author tanghongyu
 * @Description 机器人动作
 * @modifier
 * @modify_time
 */
public class NewActionInfo {
    private String actionId;
    /**
     * 动作的名称
     */
    private String actionName;

    /**
     * 动作的类型
     */
    private int actionType;

    private String robotSerialNo;

    private int downloadState;

    public String getRobotSerialNo() {
        return robotSerialNo;
    }

    public void setRobotSerialNo(String serialNo) {
        this.robotSerialNo = serialNo;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "NewActionInfo{" +
                "actionId='" + actionId + '\'' +
                ", actionName='" + actionName + '\'' +
                ", actionType=" + actionType +
                '}';
    }
}
