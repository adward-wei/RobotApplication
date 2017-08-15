package com.ubtechinc.nets.im.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.protobuf.GeneratedMessageLite;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.im.ImMsgDispathcer;
import com.ubtechinc.nets.http.Utils;
import com.ubtechinc.nets.im.IMErrorUtil;
import com.ubtechinc.nets.im.business.ReceiveMessageBussinesss;
import com.ubtechinc.nets.im.business.SendMessageBusiness;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;
import com.ubtechinc.nets.phonerobotcommunite.IReceiveMsg;
import com.ubtechinc.nets.phonerobotcommunite.ISendMsg;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;



/**
 * Created by Administrator on 2017/5/24.
 */

public class RobotPhoneCommuniteProxy<T> {

    HashMap<Long,RequestBean> requestCache = new HashMap<>();
    AtomicLong requestSerial = new AtomicLong();

    private ISendMsg sendMsgEngine = SendMessageBusiness.getInstance();
    private IReceiveMsg receiveMsgEngine = ReceiveMessageBussinesss.getInstance();
    private static final String TAG = "RobotPhoneComProxy";

    private static RobotPhoneCommuniteProxy sInstance;

    public static RobotPhoneCommuniteProxy getInstance() {
        if (sInstance == null) {
            synchronized (RobotPhoneCommuniteProxy.class) {
                sInstance = new RobotPhoneCommuniteProxy();
            }
        }
        return sInstance;
    }

    private synchronized void sendData(int cmdId, String version,long responseSerail,  GeneratedMessageLite requestBody, String peer, Callback callback){
        long requestId = requestSerial.incrementAndGet();
        AlphaMessageOuterClass.AlphaMessage msgRequest = ProtoBufferDispose.buildAlphaMessage(cmdId,version,requestBody,requestId,responseSerail);
        LogUtils.d("sendData--cmdId = "+msgRequest.getHeader().getCommandId()+", sendSerail = "+msgRequest.getHeader().getSendSerial()
                +", responseSerail = "+msgRequest.getHeader().getResponseSerial());

        byte[] bytes = ProtoBufferDispose.getPackMData(msgRequest);
        requestCache.put(requestId,new RequestBean(cmdId,callback));

        sendMsgEngine.sendMsg(requestId,peer, bytes, callback);

    }

    public void init() {
        sendMsgEngine.init();
        receiveMsgEngine.init();
    }

    public void setIMsgDispatcher(ImMsgDispathcer msgDispathcer){
        receiveMsgEngine.setIMsgDispatcher(msgDispathcer);
    }

    /**发送回复消息*/
    public void sendResponseMessage(int cmdId, String version,long responseSerial,  GeneratedMessageLite requestBody, String peer, final ICallback dataCallback) {
        sendData(cmdId,version,responseSerial,requestBody,peer, new Callback() {

            @Override
            public void onSendSuccess() {
                //消息发送成功，等待回调
                LogUtils.d(TAG,"onSendSuccess---");
            }

            @Override
            public void onSendError(long requestId,int errorCode) {
                if (dataCallback != null) {
                    dataCallback.onError(IMErrorUtil.handleException(errorCode));
                    requestCache.remove(requestId);
                }

            }

            @Override
            public void onReturnMessage(long requestId,AlphaMessageOuterClass.AlphaMessage response) {

                if (dataCallback != null) {
                    dataCallback.onSuccess(response);
                    requestCache.remove(requestId);
                }
            }

        });
    }

    public void sendMessage(int cmdId, String version, @NonNull GeneratedMessageLite requestBody, String peer, final ICallback<AlphaMessageOuterClass.AlphaMessage> dataCallback) {
        sendData(cmdId,version,0,requestBody,peer, new Callback() {

            @Override
            public void onSendSuccess() {
                //消息发送成功，等待回调
                Log.d(TAG,"onSendSuccess---");
            }

            @Override
            public void onSendError(long requestId,int errorCode) {
                if (dataCallback != null) {
                    dataCallback.onError(IMErrorUtil.handleException(errorCode));
                    requestCache.remove(requestId);
                }
            }

            @Override
            public void onReturnMessage(long requestId,AlphaMessageOuterClass.AlphaMessage response) {
                if (dataCallback != null) {
                    dataCallback.onSuccess(response);
                    requestCache.remove(requestId);
                }
            }

        });
    }

    public void sendMessage2Robot(int cmdId, String version, final @NonNull GeneratedMessageLite requestBody, String peer, final ICallback<T> dataCallback) {
        sendData(cmdId,version,0,requestBody,peer, new Callback() {

            @Override
            public void onSendSuccess() {
                //消息发送成功，等待回调
                Log.d(TAG,"onSendSuccess---");
            }

            @Override
            public void onSendError(long requestId,int errorCode) {
                if (dataCallback != null) {
                    dataCallback.onError(IMErrorUtil.handleException(errorCode));
                    requestCache.remove(requestId);
                }
            }

            @Override
            public void onReturnMessage(long requestId,AlphaMessageOuterClass.AlphaMessage response) {
                if (dataCallback != null) {
                    if (response != null && response.getBodyData() != null) {



                        Type[] types =  ((ParameterizedType)dataCallback.getClass().getGenericInterfaces()[0]).getActualTypeArguments();
                        if (Utils.hasUnresolvableType(types[0])){
                            return;
                        }
                        Class < T >  entityClass  =  (Class < T > ) ((ParameterizedType) dataCallback.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[ 0 ];
                        GeneratedMessageLite generatedMessageLite = ProtoBufferDispose.unPackData(entityClass, response.getBodyData().toByteArray());
                        if(generatedMessageLite != null) {
                            dataCallback.onSuccess((T)generatedMessageLite);

                        }else {
                            dataCallback.onError(IMErrorUtil.handleException(IMErrorUtil.ERROR.NULL_DATA));
                        }
                        }

                    requestCache.remove(requestId);
                }
            }

        });
    }

    public void dispatchResponse(long responseId, AlphaMessageOuterClass.AlphaMessage cmdBody) {
        RequestBean bean = requestCache.get(responseId);
        if (bean != null && bean.callback != null) {
            bean.callback.onReturnMessage(responseId,cmdBody);
        }
        requestCache.remove(responseId);
    }


    static class RequestBean {
       int cmdId;
       Callback callback;
       RequestBean(int cmdId,Callback callback) {
           this.cmdId = cmdId;
           this.callback = callback;
       }
   }


  public interface Callback {
      void onSendSuccess(); //消息发送成功
      void onSendError(long requestId,int errorCode);
      void onReturnMessage(long requestId,AlphaMessageOuterClass.AlphaMessage response); //收到返回的数据
  }
}