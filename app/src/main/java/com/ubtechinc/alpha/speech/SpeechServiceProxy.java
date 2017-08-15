package com.ubtechinc.alpha.speech;

import android.os.IBinder;
import android.os.RemoteException;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.affairdispatch.TtsAffairManager;
import com.ubtechinc.alpha.affairdispatch.constants.AffairPriority;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.serverlibutil.aidl.IPcmListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechAsrListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechGrammarInitListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechInterface;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechWakeUpListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;

import java.util.List;

/**
 * description: 语音服务接口类
 * * autour: bob.xu
 * date: 2017/6/24 15:30
 * update: 2017/6/24
 * version: 1.0 */
public class SpeechServiceProxy {
    private static SpeechServiceProxy instance;
    private ISpeechInterface impl;
    private IBinder binder;
    public static final String TAG = "SpeechServiceProxy";
    public static final SpeechServiceProxy getInstance() {
        if (instance == null) {
            synchronized (SpeechServiceProxy.class) {
                instance = new SpeechServiceProxy();
            }
        }
        return instance;
    }

    private SpeechServiceProxy() {
        impl = new SpeechServicesImpl();
        binder = new BinderStub();
    }

    public IBinder getBinderStub() {
        return binder;
    }

    public ISpeechInterface impl(){
        return impl;
    }

    /**
     * tts播报
     * @param text：要播报的语音
     * @return
     */
    public int speechStartTTS(String text,ITtsCallBackListener callback) {
        return speechStartTTSInnel(AlphaApplication.getContext().getPackageName(),text,AffairPriority.PRIORITY_NORMAL,callback);
    }

    /**
     * tts播报
     * @param text:要播报的语音
     * @param priority：在事务处理中的优先级
     * @return
     */
    public int speechStartTTS(String text, int priority,ITtsCallBackListener callback) {
        return speechStartTTSInnel(AlphaApplication.getContext().getPackageName(),text,priority,callback);
    }

    private int speechStartTTSInnel(String packageName, String text, int priority,ITtsCallBackListener callback) {
        LogUtils.d(TAG,"speechStartTTSInnel---text = "+text);
//        TtsAffair.Builder builder = new TtsAffair.Builder();
//        TtsAffair affair = builder.setPackageName(packageName)
//                .setText(text)
//                .setCallback(callback)
//                .setPriority(priority).createTtsAffair();
//        AffairConstruct.processAffair(affair);
        onRealPlay(packageName,text,callback);
        return 0;
    }

    /**仅供AffairExecute调用，其他地方如需要，请使用onPlay()*/
    public int onRealPlay(String packageName,String text,ITtsCallBackListener callback){
        LogUtils.d(TAG,"onRealPlay---text = "+text);
        try {
            impl.onPlayCallback(packageName,text,callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void speechStopTTS() {
        LogUtils.d(TAG,"speechStopTTS");
        try {
            impl.onStopPlay();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void switchWakeup(boolean enable){
        try {
            impl.switchWakeup(enable);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空待处理的tts
     */
    public void clearAllPendingTTS() {
        TtsAffairManager.getInstance().clearAllAffair();
    }

    public void startSpeechAsr(String packagename, final IRecognizerListener listener) {
        try {
            impl.startSpeechAsr(packagename, 1000, new ISpeechAsrListener() {
                @Override
                public void onBegin() throws RemoteException {
                    if (listener == null)return;
                    listener.onBegin();
                }

                @Override
                public void onEnd() throws RemoteException {
                    if (listener == null)return;
                    listener.onEnd();
                }

                @Override
                public void onResult(String text) throws RemoteException {
                    if (listener == null)return;
                    listener.onResult(text, true);
                }

                @Override
                public void onError(int code) throws RemoteException {
                    if (listener == null)return;
                    listener.onError(code);
                }

                @Override
                public IBinder asBinder() {
                    return null;
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**供SDK aidl接口调用*/
    public class BinderStub extends ISpeechInterface.Stub {

        @Override
        public int registerPcmListener(String packageName, IPcmListener callBack) throws RemoteException {
            return impl.registerPcmListener(packageName, callBack);
        }

        @Override
        public int unregisterPcmListener(String packageName) throws RemoteException {
            return impl.unregisterPcmListener(packageName);
        }

        @Override
        public int registerWakeUpCallbackListener(String packageName, ISpeechWakeUpListener callback) throws RemoteException {
            return impl.registerWakeUpCallbackListener(packageName,callback);
        }

        @Override
        public int unregisterWakeUpCallbackListener(String packageName) throws RemoteException {
            return impl.unregisterWakeUpCallbackListener(packageName);
        }

        @Override
        public int onPlayCallback(String packageName, String text, ITtsCallBackListener callback) throws RemoteException {
            return SpeechServiceProxy.this.speechStartTTSInnel(packageName, text, AffairPriority.PRIORITY_NORMAL,callback);
        }

        @Override
        public void onStopPlay() throws RemoteException {
            SpeechServiceProxy.this.speechStopTTS();
        }

        @Override
        public void setVoiceName(String strVoiceName) throws RemoteException {
            impl.setVoiceName(strVoiceName);
        }

        @Override
        public void setTtsSpeed(String speed) throws RemoteException {
            impl.setTtsSpeed(speed);
        }

        @Override
        public String getTtsSpeed() throws RemoteException {
            return impl.getTtsSpeed();
        }

        @Override
        public void setTtsVolume(String volume) throws RemoteException {
            impl.setTtsVolume(volume);
        }

        @Override
        public String getTtsVolume() throws RemoteException {
            return impl.getTtsVolume();
        }

        @Override
        public void startSpeechAsr(String packageName, int appId, ISpeechAsrListener callBack) throws RemoteException {
            impl.startSpeechAsr(packageName, appId, callBack);
        }

        @Override
        public void stopSpeechAsr() throws RemoteException {
            impl.stopSpeechAsr();
        }

        @Override
        public List getSpeechVoices() throws RemoteException {
            return impl.getSpeechVoices();
        }

        @Override
        public SpeechVoice getCurSpeechVoices() throws RemoteException {
            return impl.getCurSpeechVoices();
        }

        @Override
        public void initSpeechGrammar(String strGrammar, ISpeechGrammarInitListener listener) throws RemoteException {
            impl.initSpeechGrammar(strGrammar,listener);
        }

        @Override
        public void switchSpeechCore(String language) throws RemoteException {
            impl.switchSpeechCore(language);
        }

        @Override
        public void switchWakeup(boolean enable) throws RemoteException {
            impl.switchWakeup(enable);
        }

        @Override
        public void startLocalFunction(String function) throws RemoteException {
            impl.startLocalFunction(function);
        }

        @Override
        public boolean isSpeechGrammar() throws RemoteException {
            return impl.isSpeechGrammar();
        }

        @Override
        public boolean isSpeechIat() throws RemoteException {
            return impl.isSpeechIat();
        }

        @Override
        public void setSpeechMode(int mode) throws RemoteException {
            impl.setSpeechMode(mode);
        }
    }
}
