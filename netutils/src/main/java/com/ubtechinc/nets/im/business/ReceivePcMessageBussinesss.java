package com.ubtechinc.nets.im.business;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.im.IMsgHandleEngine;
import com.ubtechinc.alpha.im.ImMsgDispathcer;
import com.ubtechinc.nets.im.service.MsgHandleTask;
import com.ubtechinc.nets.phonerobotcommunite.IReceiveMsg;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/5/25.
 */

public class ReceivePcMessageBussinesss implements IReceiveMsg {

    private ExecutorService receiveMsgThreadPool;

    private static ReceivePcMessageBussinesss sInstance;
    private static String TAG = "ReceiveMessageBussinesss";
    public static ReceivePcMessageBussinesss getInstance() {
        if (sInstance == null) {
            synchronized (ReceivePcMessageBussinesss.class) {
                sInstance = new ReceivePcMessageBussinesss();
            }
        }
        return sInstance;
    }

    private ReceivePcMessageBussinesss() {
    }

    @Override
    public void init() {
        receiveMsgThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void setIMsgDispatcher(ImMsgDispathcer msgDispathcer) {
        IMsgHandleEngine.getInstance().setIMsgDispatcher(msgDispathcer);
    }


    /**
     * 处理接收到的IM消息
     * 从后台推送的消息是Json格式(IM给后台提供的接口不支持传byte[]),客户端与机器人互传的是byte[]格式，可转成Protobuffer
     * byte[] ---> String --->byte[]并不一定是等价的，会存在数据丢失
     *
     * @param data
     */
    public void handleReceivedMessage(byte[] data) {
        LogUtils.d(TAG, "handleReceivedMessage");

        LogUtils.v(TAG, "收到消息--type : custom,msg:" + data);
        MsgHandleTask parserTask = new MsgHandleTask(data, null);
        receiveMsgThreadPool.execute(parserTask);

    }

}
