package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

/**
 * @author：liuhai
 * @date：2017/4/20 16:49
 * @modifier：ubt
 * @modify_date：2017/4/20 16:49
 * [A brief description]
 * version
 */

public class ReplaySpeechRcord {
    //内容
    private String content;
    //内容链接
    private String contentLinks;
    //标签id 1聊天 2百科 3算数 4时间 5提醒 6拍照 7天气 8跳舞 9动作 10汇率 11笑话 12音乐 13故事 14背诗 15运势 16翻译
    private int labelId;
    //消息语言（“EN”、“CN” ）
    private String msgLanguage;

    private String recordId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentLinks() {
        return contentLinks;
    }

    public void setContentLinks(String contentLinks) {
        this.contentLinks = contentLinks;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public String getMsgLanguage() {
        return msgLanguage;
    }

    public void setMsgLanguage(String msgLanguage) {
        this.msgLanguage = msgLanguage;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}