package com.ubtechinc.alpha.affairdispatch.affair;

import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.ubtechinc.alpha.affairdispatch.AffairCallback;
import com.ubtechinc.alpha.affairdispatch.BaseAffair;
import com.ubtechinc.alpha.affairdispatch.constants.AffairType;
import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;


/**
 * description: 播报TTS的事务实体
 * autour: bob.xu
 * date: 2017/6/30 16:29
 * update: 2017/6/30
 * version: 1.0
*/
public class TtsAffair extends BaseAffair {
    private String text;
    private String packageName;
    private ITtsCallBackListener callback;
    public static final String TAG = "TtsAffair";

    private TtsAffair(String text,String packageName,ITtsCallBackListener callback,int priority) {
        super();
        this.text = text;
        this.packageName = packageName;
        this.callback = callback;
        this.setPriority(priority);
        setType(AffairType.EVENT_TYPE_TTS);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ITtsCallBackListener getCallback() {
        return callback;
    }

    public void setCallback(ITtsCallBackListener callback) {
        this.callback = callback;
    }


    public static class Builder {

        private String text;
        private String packageName;
        private ITtsCallBackListener callback;
        private int priority;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder setCallback(ITtsCallBackListener callback) {
            this.callback = callback;
            return this;
        }

        public TtsAffair createTtsAffair() {
            return new TtsAffair(text, packageName, callback,priority);
        }

        public int getPriority() {
            return priority;
        }

        public Builder setPriority(int priority) {
            this.priority = priority;
            return this;
        }
    }

    @Override
    public void execute(final AffairCallback affairCallback) {
        SpeechServiceProxy.getInstance().onRealPlay(packageName,text,new ITtsCallBackListener() {

            @Override
            public void onBegin() throws RemoteException {

            }

            @Override
            public void onEnd() throws RemoteException {
                Log.d(TAG,"tts speak: '"+text+"'  --onEnd");
                if (callback != null) {
                    callback.onEnd();
                }

                if (affairCallback != null) {
                    affairCallback.onComplete();
                }
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        });
    }

    @Override
    public long calcuteExecuteTime() { //用于超时没收到回调的逻辑
        if(!TextUtils.isEmpty(text)) {
            long length = text.length();
            long time = 1000*length + 10*1000;

            return time;
        }
        return 0;
    }

    @Override
    public void stop() {
        super.stop();
        SpeechServiceProxy.getInstance().speechStopTTS();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(",")
                .append("text:").append(text).append(",")
                .append("packageName").append(packageName);
        return builder.toString();
    }
}
