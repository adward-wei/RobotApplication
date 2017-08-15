package com.ubtechinc.nets.im.util.parser;

/**
 * @author：adward
 * @date：12/14/2016 2:41 PM
 * @modify_date：12/14/2016 2:41 PM
 * [A brief description]
 */

public class UbtMessage {
    private String type;
    private String procotoVersion;
    private UbtMessageContent content;
    private UbtMessageInfo info;

    public String getProcotoVersion() {
        return procotoVersion;
    }

    public void setProcotoVersion(String procotoVersion) {
        this.procotoVersion = procotoVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UbtMessageContent getContent() {
        return content;
    }

    public void setContent(UbtMessageContent content) {
        this.content = content;
    }

    public UbtMessageInfo getInfo() {
        return info;
    }

    public void setInfo(UbtMessageInfo info) {
        this.info = info;
    }
}
