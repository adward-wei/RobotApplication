package com.ubtechinc.alpha2ctrlapp.entity.business.app;

/**
 * @author：ubt
 * @date：2017/3/14 10:08
 * @modifier：ubt
 * @modify_date：2017/3/14 10:08
 * [A brief description]
 * version
 */

public class BasicCmdModel {
    private String title;
    private String content;
    private int ivId;

    public BasicCmdModel(String title, String content, int ivId) {
        this.title = title;
        this.content = content;
        this.ivId = ivId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIvId() {
        return ivId;
    }

    public void setIvId(int ivId) {
        this.ivId = ivId;
    }
}


