package com.ubtechinc.nets.im.business;

import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.event.ReceiveHeartBeatEvent;
import com.ubtechinc.alpha.im.IMsgHandleEngine;
import com.ubtechinc.alpha.im.ImMsgDispathcer;
import com.ubtechinc.nets.im.service.MsgHandleTask;
import com.ubtechinc.nets.phonerobotcommunite.IReceiveMsg;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/5/25.
 */

public class ReceiveMessageBussinesss implements IReceiveMsg{

    private ExecutorService receiveMsgThreadPool;

    private static ReceiveMessageBussinesss sInstance;
    private static String TAG = "ReceiveMessageBussinesss";
    public static ReceiveMessageBussinesss getInstance() {
        if (sInstance == null) {
            synchronized (ReceiveMessageBussinesss.class) {
                sInstance = new ReceiveMessageBussinesss();
            }
        }
        return sInstance;
    }

    private ReceiveMessageBussinesss() {
    }

    @Override
    public void init() {
        TIMManager.getInstance().addMessageListener(onRevieveMessageListenenr);
        receiveMsgThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void setIMsgDispatcher(ImMsgDispathcer msgDispathcer){
        IMsgHandleEngine.getInstance().setIMsgDispatcher(msgDispathcer);
    }

    private TIMMessageListener onRevieveMessageListenenr = new TIMMessageListener() {
        @Override
        public boolean onNewMessages(List<TIMMessage> list) {
            LogUtils.d("onNewMessages---");
            handleReceivedMessage(list);
            return true;
        }
    };

    /**
     * 处理接收到的IM消息
     * 从后台推送的消息是Json格式(IM给后台提供的接口不支持传byte[]),客户端与机器人互传的是byte[]格式，可转成Protobuffer
     * byte[] ---> String --->byte[]并不一定是等价的，会存在数据丢失
     * @param list
     */
    private void handleReceivedMessage(List<TIMMessage> list) {
        LogUtils.d(TAG,"handleReceivedMessage");
        for(TIMMessage timMessage :list){
            for(int i=0;i<timMessage.getElementCount();i++){

                String peer = timMessage.getConversation().getPeer();
                //发送已读回执
                TIMManager.getInstance().getConversation(TIMConversationType.C2C,peer).setReadMessage(timMessage);//set has readable!!

                TIMElem elem =timMessage.getElement(i);
                TIMElemType elemType= elem.getType();

                if(elemType == TIMElemType.Text){ //来自后台的IM消息和心跳包
                    final TIMTextElem textElem =(TIMTextElem)elem;
                    LogUtils.v("收到消息:"+textElem.getText());
                    String messageStr= textElem.getText();
                    if(messageStr.startsWith("alpha_heart_beat")) {
                        LogUtils.d("心跳包： "+messageStr);
                        //TODO:心跳包最好也改成Protobuffer格式
                        ReceiveHeartBeatEvent event = new ReceiveHeartBeatEvent();
                        event.message = messageStr;
                        NotificationCenter.defaultCenter().publish(event);
                    } else { //从后台
                        MsgHandleTask parserTask = new MsgHandleTask(messageStr,peer);
                        receiveMsgThreadPool.execute(parserTask);
                    }
                } else if (elemType == TIMElemType.Custom){
                    final TIMCustomElem customElem = (TIMCustomElem)elem;
                    byte[] data = customElem.getData();

                    LogUtils.v(TAG,"收到消息--type : custom,msg:"+data);
                    MsgHandleTask parserTask = new MsgHandleTask(data,peer);
                    receiveMsgThreadPool.execute(parserTask);
                }
            }
        }
    }
}
