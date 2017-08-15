package com.ubtechinc.alpha.event;

/**
 * @desc : 接收到第三方app按键信息的事件
 * @author: wzt
 * @time : 2017/6/5
 * @modifier:
 * @modify_time:
 */

public class ReceiveAppButtonEvent {
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
}
