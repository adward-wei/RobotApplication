package com.ubtechinc.alpha.speech;

import android.content.Context;
import android.os.Looper;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.speech.recoder.IPcmListener;
import com.ubtechinc.alpha.speech.recognizer.IGrammarListener;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;
import com.ubtechinc.alpha.speech.ttser.ITTSListener;
import com.ubtechinc.alpha.speech.utils.IRecUtil;
import com.ubtechinc.alpha.speech.utils.PcmUtil;
import com.ubtechinc.alpha.speech.utils.WkuUtil;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;

import java.util.concurrent.ConcurrentHashMap;

import hugo.weaving.DebugLog;

/**
 * @desc : 语音环境抽象父类：抽离公共代码
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/31
 * @modifier:
 * @modify_time:
 */

abstract public class SpeechContextBase extends AbstractSpeech implements SpeechContext {
    protected static final String TAG = "SpeechContext";
    protected final Context mContext;
    private final IRecUtil mRecUtil;
    private final WkuUtil mWkuUtil;
    private final PcmUtil mPcmUtil;
    protected final SpeechUtil mUtil;
    private ConcurrentHashMap<String, IPcmListener> mPcmMaps = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, IWakeuperListener> mWkuMps = new ConcurrentHashMap<>(10);

    protected SpeechContextBase(Context context, SpeechUtil util) {
        super();
        this.mContext = context;
        this.mUtil = util;
        mWkuUtil = new WkuUtil(this);
        mRecUtil = getRecUtil();
        mPcmUtil = new PcmUtil(this);
        this.state = SpeechState.IDLE;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @DebugLog
    @Override
    public void onCreate() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            mUtil.init();
            mUtil.initParam();
            mUtil.enterRecoding(mPcmUtil.getListener());
            enterWakeUp();
        } else {
            HandlerUtils.runUITask(new Runnable() {
                @Override
                public void run() {
                    mUtil.init();
                    mUtil.initParam();
                    mUtil.enterRecoding(mPcmUtil.getListener());
                    enterWakeUp();
                }
            });
        }
    }

    @DebugLog
    @Override
    public void onDestroy() {
        if (Looper.getMainLooper() == Looper.myLooper())
            mUtil.release();
        else {
            HandlerUtils.runUITask(new Runnable() {
                @Override
                public void run() {
                    mUtil.release();
                }
            });
        }
    }

    @DebugLog
    @Override
    public int registerPcmListener(String packageName, IPcmListener listener) {
        mPcmMaps.put(packageName, listener);
        mPcmUtil.mPackageName = packageName;
        return 0;
    }

    @DebugLog
    @Override
    public int unregisterPcmListener(String packageName) {
        if (packageName.equals(mPcmUtil.mPackageName)) {
            mPcmUtil.mPackageName = null;
        }
        mPcmMaps.remove(packageName);
        return 0;
    }

    @DebugLog
    @Override
    public int registerWakeUpCallbackListener(String packageName, IWakeuperListener callback) {
        mWkuMps.put(packageName, callback);
        mWkuUtil.mPackageName = packageName;
        return 0;
    }

    @DebugLog
    @Override
    public int unregisterWakeUpCallbackListener(String packageName) {
        if (packageName.equals(mPcmUtil.mPackageName)) {
            mWkuUtil.mPackageName = null;
        }
        mWkuMps.remove(packageName);
        return 0;
    }

    @DebugLog
    @Override
    public int onPlay(String packageName, final String text, final ITTSListener callback) {
        LogUtils.d(TAG, "playTTS withCallback---text = " + text);
        mUtil.startTts(text, callback);
        return 0;
    }

    @DebugLog
    @Override
    public void onStopPlay() {
        LogUtils.d(TAG, "stopPlayTTS...");
        mUtil.stopTts();
    }

    @DebugLog
    @Override
    public void setVoiceName(String voiceName) {
        mUtil.setVoiceName(voiceName);
    }

    @DebugLog
    @Override
    public void setTtsSpeed(String speed) {
        mUtil.setTtsSpeed(speed);
    }

    @DebugLog
    @Override
    public String getTtsSpeed() {
        return mUtil.getTtsSpeed();
    }

    @DebugLog
    @Override
    public void setTtsVolume(String volume) {
        mUtil.setTtsVolume(volume);
    }

    @Override
    public String getTtsVolume() {
        return mUtil.getTtsVolume();
    }

    @DebugLog
    @Override
    public void initSpeechGrammar(String strGrammar, final IGrammarListener listener) {
        mUtil.buildGrammar(strGrammar, listener);
    }

    @DebugLog
    @Override
    public void startSpeechAsr(String packageName, int appId, IRecognizerListener listener) {
        mRecUtil.mPackageName = packageName;
        mRecUtil.appid = appId;
        mRecUtil.setListener(listener);
        startSpeechAsr();
    }

    @DebugLog
    @Override
    public void startSpeechAsr() {
        LogUtils.i(TAG + "->startSpeechAsr...");
        enterWakeUp();
        enterRecognize();
    }

    @DebugLog
    @Override
    public void stopSpeechAsr() {
        LogUtils.i(TAG + "->stopSpeechAsr...");
        exitRecognize();
    }

    @DebugLog
    @Override
    public void switchWakeup(boolean enable) {
        LogUtils.I(TAG + "->switchWakeup...%s", enable);
        if (enable) {
            enterWakeUp();
        } else {
            exitWakeup();
        }
    }

    @DebugLog
    @Override
    public void setSpeechMode(SpeechMode mode) {
        this.mode = mode;
    }

    @DebugLog
    @Override
    public boolean isSpeechGrammar() {
        return this.mode == SpeechMode.GRAMMAR;
    }

    @DebugLog
    @Override
    public boolean isSpeechIat() {
        return this.mode == SpeechMode.IAT;
    }

    @DebugLog
    @Override
    public SpeechVoice getCurSpeechVoices() {
        String curName = mUtil.getCurVoiceName();
        for (SpeechVoice sv : getSpeechVoices()) {
            if (sv.getName().equals(curName)) {
                return sv;
            }
        }
        return null;
    }

    @DebugLog
    @Override
    public ConcurrentHashMap<String, IPcmListener> getPcmCallback() {
        return mPcmMaps;
    }

    @DebugLog
    @Override
    public ConcurrentHashMap<String, IWakeuperListener> getWkuCallback() {
        return mWkuMps;
    }

    private void enterWakeUp() {
        LogUtils.i(TAG + "->enterWakeUp...");
        if (state == SpeechState.ASR) return;
        this.state = SpeechState.WKU;
        mUtil.enterWakeUp(mWkuUtil.getListener());
    }

    private void exitWakeup() {
        LogUtils.i(TAG + "->exitWakeup...");
        this.state = SpeechState.IDLE;
        exitRecognize();
        mUtil.exitWakeUp();
    }

    private void enterRecognize() {
        LogUtils.i(TAG + "->enterRecognize...");
        this.state = SpeechState.ASR;
        mUtil.startRecognize(mRecUtil.getListener());
    }

    private void exitRecognize() {
        LogUtils.i(TAG + "->exitRecognize...");
        if (state == SpeechState.ASR) {
            this.state = SpeechState.WKU;
        }
        mUtil.stopRecognize();
    }
}
