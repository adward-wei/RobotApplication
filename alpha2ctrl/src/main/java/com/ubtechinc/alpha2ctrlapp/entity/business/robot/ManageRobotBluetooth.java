package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

/**
 * [A brief description]
 *
 * @author：zengdengyi
 * @date：2016/7/5 18:57
 * @modifier：zengdengyi
 * @modify_date：2016/7/5 18:57
 * [A brief description]
 * version
 */
public class ManageRobotBluetooth {

    /**
     * 1.扫描
     * 2.连接状态
     * 3.中断扫描
     * 4.连接设备
     * 5.断开连接
     *
     * 1.扫描
     *  1.1 isSendByClient = true，开始扫描
     *  1.2 isSendByClient = false，code = 1，返回扫描列表，扫一批发一批
     *  1.3 isSendByClient = false，code = 2，扫描结束
     *  1.3 isSendByClient = false，code = 3，扫描开始
     * 2.已配对设备列表
     *  2.1 isSendByClient = true，获取配对过的设备列表
     *  2.2 isSendByClient = false，返回配对过的设备列表
     * 3.中断扫描
     *  3.1 isSendByClient = true，中断扫描
     *  3.2 isSendByClient = false，中断扫描的结果,code = 1成功，code = 0失败
     * 4.连接设备
     *  4.1 isSendByClient = true，连接设备
     *  4.2 isSendByClient = false，连接设备的结果,code = 1成功，code = 0失败 code =3 收到消息
     * 5.断开连接
     *  5.1 isSendByClient = true，断开连接
     *  5.2 isSendByClient = false，断开连接的结果,code = 1成功，code = 0失败 code =3 收到消息
     *6.已连接设备
     *  6.1 isSendByClient = true，获取已连接设备
     *  6.2 isSendByClient = false，返回已连接设备,code = 1成功，code = 0失败
     */

    private int orderCmd;
    private boolean isSendByClient;
    private int code;
    private String content;//orderCmd =2时，这里包含设备名、address ,字段为空就没有连接设备

    public int getOrderCmd() {
        return orderCmd;
    }

    public void setOrderCmd(int orderCmd) {
        this.orderCmd = orderCmd;
    }

    public boolean isSendByClient() {
        return isSendByClient;
    }

    public void setSendByClient(boolean sendByClient) {
        isSendByClient = sendByClient;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
