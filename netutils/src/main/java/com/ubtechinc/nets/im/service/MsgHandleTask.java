package com.ubtechinc.nets.im.service;


import com.ubtechinc.alpha.im.IMsgHandleEngine;

/**
 * Created by Liudongyang on 2017/4/12.
 */

public class MsgHandleTask implements Runnable {

    private byte[] messageContent;
    private String jsonStr;
    private String peer; //消息的发送方

    public MsgHandleTask(byte[] content,String peer){
        messageContent = content;
        this.peer = peer;
    }

    public MsgHandleTask(String jsonStr, String peer) {
        this.jsonStr = jsonStr;
        this.peer = peer;
    }

    @Override
    public void run() {
        if (messageContent != null) {
            IMsgHandleEngine.getInstance().handleProtoBuffMsg(messageContent, peer);
        } else if (jsonStr != null){
            IMsgHandleEngine.getInstance().handleJsonMsg(jsonStr,peer);
        }
    }



}
