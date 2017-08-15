package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.io.Serializable;

/**
 * @author：liuhai
 * @date：2017/4/20 11:04
 * @modifier：ubt
 * @modify_date：2017/4/20 11:04
 * [A brief description]
 * version
 */

public class RecordResultInfo implements Serializable {

    private int id;

    private String createTime;

    private String robotMsg;

    private String labelColor;

    private String robotId;

    private String userMsg;

    private int userId;

    private String labelName;

    private int labelId;


    private String pushLinks;


    private String pushContent;

    private String msgLanguage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRobotMsg() {
        return robotMsg;
    }

    public void setRobotMsg(String robotMsg) {
        this.robotMsg = robotMsg;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public String getPushLinks() {
        return pushLinks;
    }

    public void setPushLinks(String pushLinks) {
        this.pushLinks = pushLinks;
    }



    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getMsgLanguage() {
        return msgLanguage;
    }

    public void setMsgLanguage(String msgLanguage) {
        this.msgLanguage = msgLanguage;
    }

    @Override
    public String toString() {
        return "RecordResultInfo{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", robotMsg='" + robotMsg + '\'' +
                ", labelColor='" + labelColor + '\'' +
                ", robotId='" + robotId + '\'' +
                ", userMsg='" + userMsg + '\'' +
                ", userId=" + userId +
                ", labelName='" + labelName + '\'' +
                ", labelId=" + labelId +
                ", pushLinks='" + pushLinks + '\'' +
                ", pushContent='" + pushContent + '\'' +
                ", msgLanguage='" + msgLanguage + '\'' +
                '}';
    }
}
