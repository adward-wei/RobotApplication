package com.ubtechinc.alpha2ctrlapp.entity.request;

/**
 * @author：liuhai
 * @date：2017/4/20 10:11
 * @modifier：ubt
 * @modify_date：2017/4/20 10:11
 * [A brief description] 中英互译请求类型
 * version
 */

public class GetTranlateRequest extends CommonRequest {
    private String userId;
    private String robotId;
    private String labelId;
    private int direction;
    private int pageSize;
    private int id;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
