package com.ubtechinc.alpha.callback.message.speech;

import android.support.annotation.IntDef;

import com.ubtechinc.alpha.callback.DispatchMessage;
import com.ubtechinc.alpha.speech.AbstractSpeech;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.ConcurrentHashMap;

/**
 *   @author: Logic
 *   @email : logic.peng@ubtech.com
 *   @time  : 2017/4/6
 *   @desc  : 唤醒回调处理
 */
public class WkuCbMessage implements DispatchMessage.Callback {

    public static final int CALL_BACK_STATE_WAKE_UP_SUCCESS = 1;
    public static final int CALL_BACK_STATE_WAKE_UP_FAIL = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {CALL_BACK_STATE_WAKE_UP_SUCCESS, CALL_BACK_STATE_WAKE_UP_FAIL})
    public @interface WkuCbState{}

    private final AbstractSpeech mSpeech;
    private final String mPackageName;
    private final @WkuCbState int state;
    private int errCode;
    private String content;
    private int angle;

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public WkuCbMessage(AbstractSpeech speech, String packageName, @WkuCbState int state) {
        this.mSpeech = speech;
        this.mPackageName = packageName;
        this.state = state;
    }

    @Override
    public void handleMessage() {
        ConcurrentHashMap<String, IWakeuperListener> maps = mSpeech.getWkuCallback();
        if (maps == null || maps.size() == 0) {
            return;
        }
        if (mPackageName == null) return;
        IWakeuperListener listener = maps.get(mPackageName);
        if(listener == null) return;
        switch (state){
            case CALL_BACK_STATE_WAKE_UP_SUCCESS:
                listener.onWakeup(content, angle);
                break;
            case CALL_BACK_STATE_WAKE_UP_FAIL:
                listener.onError(errCode);
                break;
            default:
                break;
        }
    }
}
