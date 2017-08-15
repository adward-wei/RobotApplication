package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

/**
 * @author：tanghongyu
 * @date：2016/10/10 19:11
 * @modifier：tanghongyu
 * @modify_date：2016/10/10 19:11
 * [A brief description]
 * version
 */

public class ControlRobotResponse {

    private String result;
    private String controlAppName;
    private String controlUserName;
    private String controlTime;
    private int code;
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getControlAppName() {
        return controlAppName;
    }

    public void setControlAppName(String controlAppName) {
        this.controlAppName = controlAppName;
    }

    public String getControlUserName() {
        return controlUserName;
    }

    public void setControlUserName(String controlUserName) {
        this.controlUserName = controlUserName;
    }

    public String getControlTime() {
        return controlTime;
    }

    public void setControlTime(String controlTime) {
        this.controlTime = controlTime;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
