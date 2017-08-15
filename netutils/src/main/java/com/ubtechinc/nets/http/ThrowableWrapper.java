package com.ubtechinc.nets.http;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/16
 * @modifier:
 * @modify_time:
 */

public class ThrowableWrapper extends Throwable {
    private int errorCode;
    private String message;
    public ThrowableWrapper(Throwable e, int errorCode) {
        super(e);
        this.errorCode = errorCode;
    }

    public ThrowableWrapper(String msg, int errorCode) {
        super(new Throwable(msg));
        this.message = msg;
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ThrowableWrapper{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                '}';
    }
}
