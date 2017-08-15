package com.ubtechinc.nets.phonerobotcommunite;

import android.support.annotation.NonNull;

import com.google.protobuf.GeneratedMessageLite;


/**
 * @author：tanghongyu
 * @date：4/11/2017 8:14 PM
 * @modifier：tanghongyu
 * @modify_date：4/11/2017 8:14 PM
 * [A brief description]
 * version
 */

public interface IRobotCommunication<T extends GeneratedMessageLite> {

    void init();

    void connect(IConnectionListener connectionCallback);

    void disconnect();

    public interface IConnectionListener {

        void connected();
        /**
         * 登录
         */
        void connectSuccess();
        void connectFailed(int code, String message);
        void connectionClosed();
        void connectionClosedOnError(int code);
        void reconnecting();
        void reconnectFailed();
        void reconnectSuccessful();
    }

    void sendData(@NonNull GeneratedMessageLite data, ICallback dataCallback);
}
