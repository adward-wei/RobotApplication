package com.ubt.alpha2.upgrade.exception;

/**
 * @author: slive
 * @description: 集中处理更新过程中的异常
 * @create: 2017/6/27
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class UpgradeException extends Exception {
    private String errorMessage;
    private int errorCode;


    public UpgradeException() {}

    public UpgradeException(String detailMessage) {
        super(detailMessage);
        this.errorMessage = detailMessage;
    }

    public UpgradeException(int errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.errorMessage = detailMessage;
    }

    public UpgradeException(int errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public UpgradeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorMessage = detailMessage;
    }

    public UpgradeException(int errorCode, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorCode = errorCode;
        this.errorMessage = detailMessage;
    }

    public UpgradeException(Throwable throwable) {
        super(throwable);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
