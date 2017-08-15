package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.io.Serializable;

/**
 * Created by nixiaoyan on 2016/9/26.
 */

public class NotifactionIQBean implements Serializable{
    private static final long serialVersionUID = 7610961516527463709L;
    private String id;

    private int hashValue;

    private String title;

    private String message;
    private String  from;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHashValue() {
        return hashValue;
    }

    public void setHashValue(int hashValue) {
        this.hashValue = hashValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
