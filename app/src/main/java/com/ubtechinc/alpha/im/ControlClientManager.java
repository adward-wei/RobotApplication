package com.ubtechinc.alpha.im;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.tencent.TIMValueCallBack;
import com.ubtechinc.nets.im.business.SendMessageBusiness;
import com.ubtechinc.nets.im.util.parser.UbtMessageConstants;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/2.
 */

public class ControlClientManager {
    private static ControlClientManager sInstance;
    private static final boolean USE_HEART_BEAT = false;
    final ArrayList<String> controlClientList = new ArrayList<>();
    int i = 0;
    public static ControlClientManager getInstance() {
        if (sInstance == null) {
            synchronized (ControlClientManager.class) {
                if (sInstance == null) {
                    sInstance = new ControlClientManager();
                }
            }
        }
        return sInstance;
    }

    //最新方案，不需要发新跳包，当Robot下线时IM服务器会向ubt服务器回调状态变化，
    // 后台再通过Im向客户端发送Robot状态变化的msg。

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UbtMessageConstants.SENDHEART_MESSAGE_ID:
                    //TODO:后续添上相关参数
                    synchronized (controlClientList) {
                        for(String client :controlClientList) {
                            sendHeartMessage(client);
                        }
                    }
                    break;
            }
        }
    };


    /**支持多客户端同时控制 Robot*/
    public void addClient(String userId) {
        synchronized (controlClientList) {
            controlClientList.add(userId);
        }
        if(USE_HEART_BEAT) {
            startToHeartMessage();
        }
    }

    public void removeClient(String userId) {
        synchronized (controlClientList) {
            controlClientList.remove(userId);
        }
        if (USE_HEART_BEAT) {
            if (controlClientList.size() == 0) {
                stopToHeartMessage();
            }
        }
    }

    public boolean isLegalPeer(int cmdId,String peer) {
        if (cmdId == IMCmdId.IM_CONNECT_ROBOT_REQUEST) {
            return true;
        }
        synchronized (controlClientList) {
            return controlClientList.contains(peer);
        }
    }

    /**
     * 主服务给客户端发送心跳
     */
    public void startToHeartMessage(){
        handler.sendEmptyMessageDelayed(UbtMessageConstants.SENDHEART_MESSAGE_ID, 10);
    }

    public void stopToHeartMessage() {
        handler.removeMessages(UbtMessageConstants.SENDHEART_MESSAGE_ID);
    }

    /**
     * 训话发送心跳消息
     */
    private void sendHeartMessage(String peer){

        SendMessageBusiness.getInstance().sendHeartMessageByIM(peer, "alpha_heart_beat_"+(i++), new TIMValueCallBack() {
            @Override
            public void onError(int i, String s) {
                //TODO: 与手机客户端连接断了，需要做处理，如果连续几次心跳包发送失败，则认为是断掉了
                //TODO:需要加上相关逻辑
            }

            @Override
            public void onSuccess(Object o) {

            }
        });
        handler.sendEmptyMessageDelayed(UbtMessageConstants.SENDHEART_MESSAGE_ID, UbtMessageConstants.HEART_MESSAGE_TIME);
    }
}
