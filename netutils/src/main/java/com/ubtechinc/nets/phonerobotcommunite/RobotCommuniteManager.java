package com.ubtechinc.nets.phonerobotcommunite;

import android.support.annotation.NonNull;

//import com.google.common.base.Preconditions;
//import com.google.common.base.Preconditions;
import com.google.protobuf.GeneratedMessageLite;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.im.ImMsgDispathcer;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;


/**
 * @author：tanghongyu
 * @date：4/11/2017 8:16 PM
 * @modifier：tanghongyu
 * @modify_date：4/11/2017 8:16 PM
 * [机器人通信类]
 * version
 */

public class RobotCommuniteManager<T> {


    private RobotPhoneCommuniteProxy mConnection;

    //使用volatile关键字保其可见性
    volatile private static RobotCommuniteManager instance = null;

    private RobotCommuniteManager(){
    }

    public static RobotCommuniteManager getInstance() {
        if(instance == null){//懒汉式
            synchronized (RobotCommuniteManager.class) {
                if(instance == null){//二次检查
                    instance = new RobotCommuniteManager();
                }
            }
        }
        return instance;
    }

    public RobotCommuniteManager init() {
        mConnection = RobotPhoneCommuniteProxy.getInstance();
        mConnection.init();

        return this;
    }

    public void connect() {

    }


    public void sendData(int cmdId,String version,GeneratedMessageLite requestBody, String peer, @NonNull ICallback<AlphaMessageOuterClass.AlphaMessage> dataCallback) {

        mConnection.sendMessage(cmdId,version,requestBody,peer, dataCallback);
    }


    public void sendDataToRobot(int cmdId,String version, GeneratedMessageLite requestBody, String peer, @NonNull ICallback<T> dataCallback) {



        mConnection.sendMessage2Robot(cmdId,version,requestBody,peer, dataCallback);
    }


    public void setMsgDispathcer(ImMsgDispathcer msgDispatcher) {
        mConnection.setIMsgDispatcher(msgDispatcher);
    }
}
