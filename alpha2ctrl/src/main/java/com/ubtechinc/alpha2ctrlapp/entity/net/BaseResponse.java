package com.ubtechinc.alpha2ctrlapp.entity.net;

/**
 * @author：tanghongyu
 * @date：6/5/2017 4:10 PM
 * @modifier：tanghongyu
 * @modify_date：6/5/2017 4:10 PM
 * [A brief description]
 * version
 */

public class BaseResponse {
    private String msg;
    private int resultCode;
    private boolean success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
