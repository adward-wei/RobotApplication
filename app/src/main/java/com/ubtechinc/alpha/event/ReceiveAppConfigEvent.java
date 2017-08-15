package com.ubtechinc.alpha.event;

/**
 * @desc : 接收到第三方app配置信息的事件
 * @author: wzt
 * @time : 2017/6/5
 * @modifier:
 * @modify_time:
 */

public class ReceiveAppConfigEvent {
    /**
     * 命令
     */
    public int cmd;
    /**
     * 命令对应的数据
     */
    public byte[] datas;
    /**
     * 第三方APP包名
     */
    public String packageName;
    /** 标签信息 **/
    public byte[] tags;
}
