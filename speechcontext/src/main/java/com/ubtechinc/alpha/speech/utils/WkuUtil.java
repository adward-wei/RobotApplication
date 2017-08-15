package com.ubtechinc.alpha.speech.utils;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.callback.message.speech.WkuCbMessage;
import com.ubtechinc.alpha.event.WakeUpEvent;
import com.ubtechinc.alpha.speech.AbstractSpeech;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;

/**
 *  @author: Logic
 *  @email : logic.peng@ubtech.com
 *  @time  : 2017/4/6
 *  @desc  : 用于处理语音引擎唤醒回调
 */
public final class WkuUtil {
    private static final String TAG = WkuUtil.class.getSimpleName();
    private AbstractSpeech mSpeech;
    public volatile String mPackageName;

    public WkuUtil(AbstractSpeech speech) {
        this.mSpeech = speech;
    }

    public WkuUtil(AbstractSpeech speech, String packageName) {
        this.mSpeech = speech;
        this.mPackageName = packageName;
    }

    public IWakeuperListener getListener(){
        return mProxyListener;
    }

    private void handleCallback(String content, @WkuCbMessage.WkuCbState int state, int errcode, int angle){
        if (mPackageName == null) return;
        WkuCbMessage callback = new WkuCbMessage(mSpeech, mPackageName, state);
        callback.setContent(content);
        callback.setErrCode(errcode);
        callback.setAngle(angle);
        mSpeech.getDispatch().enqueue(callback);
    }

    private IWakeuperListener mProxyListener = new IWakeuperListener() {

        @Override
        public void onWakeup(final String  resultStr, final int soundAngle) {
            LogUtils.i( "唤醒语音引擎成功");
            WakeUpEvent event = new WakeUpEvent();
            event.angle = soundAngle;
            NotificationCenter.defaultCenter().publish(event);
            HandlerUtils.runUITask(new Runnable() {
                @Override
                public void run() {
                    //通知外部
                    handleCallback(resultStr, WkuCbMessage.CALL_BACK_STATE_WAKE_UP_SUCCESS, 0, soundAngle);

                }
            }, 1000);//停顿以1.5秒，等待wakeup事件被处理完
        }

        @Override
        public void onError(final int errCode) {
            LogUtils.I("唤醒语音引擎出错 错误码:%d: 原因%s", errCode, "");
            HandlerUtils.runUITask(new Runnable() {
                @Override
                public void run() {
                    //通知外部
                    handleCallback("", WkuCbMessage.CALL_BACK_STATE_WAKE_UP_FAIL, errCode, -1);
                }
            }, 100);
        }

        @Override
        public void onAudio(byte[] data, int dataLen, int param1, int param2) {
            //nothing do
        }
    };
}
