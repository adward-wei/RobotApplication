package com.ubtechinc.nets.im.util.parser;

/**
 * Created by Administrator on 2016/12/15.
 */
public class UbtMessageInfo {

    private String fromId;
    private String toId;
    private String company;
    private String project;
    private String client;
    private String clientVersion;


    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String versionName) {
        this.clientVersion = versionName;
    }

}
